package net.ihaha.sunny.base.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


/**
 * Date: 07/12/2020.
 * @author SANG.
 * @version 1.0.0.
 */

@FlowPreview
@ExperimentalCoroutinesApi
class BaseFragmentStateAdapter  (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(
        fragmentManager,
        lifecycle
    ) {

    private val fragmentList: MutableList<BaseBindingFragment<*, *>> = mutableListOf()

    open fun setData(fragments: MutableList<BaseBindingFragment<*, *>>? = null) {
        fragments?.let {
            fragmentList.clear()
            fragmentList.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = fragmentList[position]
        fragment.arguments = Bundle().apply {
            putInt("position", position + 1)
        }
        return fragment
    }
    override fun getItemCount(): Int = fragmentList.size

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


}

//@FlowPreview
//@ExperimentalCoroutinesApi
//class BaseFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
//
//    private var fragmentList: List<BaseBindingFragment<*,*>> = listOf()
//    private var mutableItems = mutableListOf<Int>()
//
//    open fun setData(fragments: MutableList<BaseBindingFragment<*,*>>? = null) {
//        fragments?.let {
//            fragmentList = it.toMutableList()
//        }
//        notifyDataSetChanged()
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return fragmentList[position]
//    }
//
//    /**
//     * Method override to support notify* methods
//     *
//     * @see FragmentStateAdapter.getItemId
//     */
//
//    override fun getItemId(position: Int): Long {
//        if (position < 0) return NO_ID
//        return fragmentList[position].id.toLong()
//    }
//
//    override fun containsItem(itemId: Long): Boolean {
//        val containsFlags = mutableListOf<Boolean>()
//        fragmentList.forEachIndexed { index, fragment ->
//            containsFlags.add(index, fragment.id == itemId.toInt())
//        }
//        return containsFlags.contains(true)
//    }
//
//    /**
//     * @see FragmentStateAdapter.getItemCount
//     */
//    override fun getItemCount(): Int = fragmentList.size
//
//
//}



