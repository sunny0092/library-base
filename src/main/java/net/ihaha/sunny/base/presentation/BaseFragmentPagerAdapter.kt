package net.ihaha.sunny.base.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


/**
 * Date: 07/12/2020.
 * @author SANG.
 * @version 1.0.0.
 */

@FlowPreview
@ExperimentalCoroutinesApi
open class BaseFragmentPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    fun addFragments(vararg fragments: Fragment) {
        fragments.forEach { this.fragments += it }
    }

    private val fragments = mutableListOf<Fragment>()

    override fun getCount() = fragments.size

    override fun getItem(position: Int) = fragments[position]

}



