package net.ihaha.sunny.base.utils

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.delichill.shipper.ui.account.fragment.AccountFragment
import com.delichill.shipper.ui.auth.fragment.SignInFragment
import com.delichill.shipper.ui.home.fragment.HistoryDeliverFragment
import com.delichill.shipper.ui.home.fragment.HistoryTopUpFragment
import com.delichill.shipper.ui.home.fragment.HistoryWithdrawFragment
import com.delichill.shipper.ui.home.fragment.HomeFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*

/**
 * A custom navigator that keeps the state of toplevel navigation fragments when navigating to other tabs.
 * Based on the solution described here: https://github.com/STAR-ZERO/navigation-keep-fragment-sample
 */
private const val TAG = "DELICHILL-NAVIGATION"


@ExperimentalCoroutinesApi
@FlowPreview
@Navigator.Name("fragment")
class KeepStateNavigator(
    private val mContext: Context,
    private val mFragmentManager: FragmentManager,
    private val mContainerId: Int
) :
    FragmentNavigator(mContext, mFragmentManager, mContainerId) {
    private var lastTopLevelFragment: Fragment? = null
    private val mBackStack = ArrayDeque<Int>()
    private var mDestination: Destination? = null

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        mDestination = destination
        val cls = Class.forName(destination.className)
        if (mFragmentManager.isStateSaved) {
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        val frag = mFragmentManager.fragmentFactory.instantiate(mContext.classLoader, className)
        frag.arguments = args
        val tag = destination.id.toString()
        val ft = mFragmentManager.beginTransaction()
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
        if (HomeFragment::class.java.isAssignableFrom(cls)
            || HistoryDeliverFragment::class.java.isAssignableFrom(cls)
            || HistoryTopUpFragment::class.java.isAssignableFrom(cls)
            || HistoryWithdrawFragment::class.java.isAssignableFrom(cls)
            || SignInFragment::class.java.isAssignableFrom(cls)
            || AccountFragment::class.java.isAssignableFrom(cls)
        ) {
            (lastTopLevelFragment ?: mFragmentManager.primaryNavigationFragment)?.let {
                ft.detach(it)
            }

            while (mFragmentManager.backStackEntryCount >= 1) {
                mFragmentManager.popBackStackImmediate()
            }

            var fragment = mFragmentManager.findFragmentByTag(tag)
            if (fragment == null) {
                val className = destination.className
                fragment =
                    mFragmentManager.fragmentFactory.instantiate(mContext.classLoader, className)
                ft.add(mContainerId, fragment, tag)
            } else {
                ft.attach(fragment)
            }
            lastTopLevelFragment = fragment

            mBackStack.clear()
            mBackStack.add(destination.id)

            ft.setPrimaryNavigationFragment(fragment)
            ft.setReorderingAllowed(true)
            ft.commit()

            return destination
        } else {
            ft.add(mContainerId, frag)
            ft.setPrimaryNavigationFragment(frag)

            @IdRes val destId = destination.id
            val initialNavigation = mBackStack.isEmpty()
            if (!initialNavigation && mBackStack.size < mFragmentManager.fragments.size) {
                ft.hide(mFragmentManager.fragments[mBackStack.size - 1])
            }
            // TODO Build first class singleTop behavior for fragments
            val isSingleTopAdd = (navOptions != null && !initialNavigation
                    && navOptions.shouldLaunchSingleTop()
                    && mBackStack.peekLast() == destId)

            val isAdded = when {
                initialNavigation -> {
                    if (mFragmentManager.fragments.isNotEmpty()) {
                        mFragmentManager.fragments.forEach { ft.remove(it) }
                    }
                    true
                }
                isSingleTopAdd -> {
                    // Single Top means we only want one instance on the back stack
                    if (mBackStack.size > 1) {
                        mFragmentManager.popBackStack(
                            generateBackStackName(mBackStack.size, mBackStack.peekLast() ?: 0),
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        val hideFragment =
                            mFragmentManager.fragments[mFragmentManager.fragments.size - 2]
                        if (hideFragment != null) ft.hide(hideFragment)

                        ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
                    } else {
                        ft.remove(mFragmentManager.fragments[0])
                    }
                    false
                }
                else -> {
                    ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
                    true
                }
            }

            if (navigatorExtras is Extras) {
                for ((key, value) in navigatorExtras.sharedElements) {
                    ft.addSharedElement(key!!, value!!)
                }
            }
            val currentFragment: Fragment? = mFragmentManager.primaryNavigationFragment
            if (isAdded && currentFragment != null) {
                ft.hide(currentFragment)
            }

            ft.setReorderingAllowed(true)
            ft.commit()
            return if (isAdded) {
                mBackStack.add(destId)
                destination
            } else {
                null
            }
        }
    }

    override fun popBackStack(): Boolean {
        if (mBackStack.isEmpty()) {
            return false
        }
        if (mFragmentManager.isStateSaved) {
            return false
        }
        mFragmentManager.popBackStack(
            generateBackStackName(mBackStack.size, mBackStack.peekLast() ?: 0),
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        mBackStack.removeLast()
        return true
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }
}

//class KeepStateNavigator(
//    private val context: Context,
//    private val manager: FragmentManager, // Should pass childFragmentManager.
//    private val containerId: Int
//) : FragmentNavigator(context, manager, containerId) {
//    private var backStack: ArrayDeque<Int>
//    private var lastTopLevelFragment: Fragment? = null
//
//    init {
//        val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
//        field.isAccessible = true
//        @Suppress("UNCHECKED_CAST")
//        backStack = field.get(this) as ArrayDeque<Int>
//    }
//
//    override fun navigate(
//        destination: Destination,
//        args: Bundle?,
//        navOptions: NavOptions?,
//        navigatorExtras: Navigator.Extras?
//    ): NavDestination? {
////        onListenerNavigationDestination?.onNavigation(destination, args)
//        val cls = Class.forName(destination.className)
//        if (!HomeFragment::class.java.isAssignableFrom(cls)
//            && !HistoryDeliverFragment::class.java.isAssignableFrom(cls)
//            && !HistoryTopUpFragment::class.java.isAssignableFrom(cls)
//            && !HistoryWithdrawFragment::class.java.isAssignableFrom(cls)
//            && !AccountFragment::class.java.isAssignableFrom(cls)
//        ) {
//            return super.navigate(destination, args, navOptions, navigatorExtras)
//        }
//
//        if (manager.isStateSaved) {
//            return null
//        }
//
//        val tag = destination.id.toString()
//        val transaction = manager.beginTransaction()
//
//        (lastTopLevelFragment ?: manager.primaryNavigationFragment)?.let {
//            transaction.detach(it)
//        }
//
//        while (manager.backStackEntryCount >= 1) {
//            manager.popBackStackImmediate()
//        }
//
//        var fragment = manager.findFragmentByTag(tag)
//        if (fragment == null) {
//            val className = destination.className
//            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
//            transaction.add(containerId, fragment, tag)
//        } else {
//            transaction.attach(fragment)
//        }
//        lastTopLevelFragment = fragment
//
//        backStack.clear()
//        backStack.add(destination.id)
//
//        transaction.setPrimaryNavigationFragment(fragment)
//        transaction.setReorderingAllowed(true)
//        transaction.commit()
//
//        return destination
//    }
//
//    override fun popBackStack(): Boolean {
//        if (backStack.isEmpty()) {
//            return false
//        }
//        if (manager.isStateSaved) {
//            return false
//        }
//        manager.popBackStack(
//            generateBackStackName(backStack.size, backStack.peekLast() ?: 0),
//            FragmentManager.POP_BACK_STACK_INCLUSIVE
//        )
//        backStack.removeLast()
//        return true
//    }
//
//    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
//        return "$backStackIndex-$destId"
//    }
//}