//package net.ihaha.sunny.base.presentation.viewpager
//
//import androidx.databinding.ViewDataBinding
//import androidx.recyclerview.widget.RecyclerView
//import androidx.viewpager2.widget.ViewPager2
//import com.google.android.material.tabs.TabLayout
//import kotlinx.android.synthetic.main.layout_tab_viewpager_custom.*
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//import com.delichill.shipper.R
//import net.ihaha.sunny.base.extention.color
//import net.ihaha.sunny.base.extention.drawable
//import net.ihaha.sunny.base.presentation.*
//import net.ihaha.sunny.base.extention.unsafeLazy
//
//@FlowPreview
//@ExperimentalCoroutinesApi
//abstract class BaseTabViewPagerFragment<T : ViewDataBinding, VM : BaseViewModel<*>> :
//    BaseBindingFragment<T, VM>(),
//    TabSelectListener {
//
//    //region Variant
//    abstract val pageTitles: Array<String>
//    abstract val pages: List<BaseBindingFragment<*, *>>
//    var selectedTab: TabLayout.Tab? = null
//    var tabLayoutMediator: TabLayoutMediator? = null
//    var selectedPosition: Int = 0
//
//    protected open fun selectTab(tab: TabLayout.Tab) {
//        selectedTab = tab
//    }
//
//    protected open fun unSelectTab(tab: TabLayout.Tab?) = Unit
//    protected open fun setCustomTabView(tab: TabLayout.Tab, position: Int) = Unit
//
//    protected val tabBackground by unsafeLazy { drawable(R.drawable.bg_tab_selected) }
//    protected val tabUnBackground by unsafeLazy { drawable(R.drawable.bg_tab_unselected) }
//    protected val selectedTabColor by unsafeLazy { color(R.color.colorSecondary) }
//    protected val unselectedTabColor by unsafeLazy { color(R.color.colorText2) }
//
//    open val tabViewLayout: tabLayout as TabLayout
//    open val viewPagerLayout: viewPager as ViewPager2
//
//    protected var historyPosition: Int = 0
//
//    //endregion
//
//    //region Override
//    protected open fun setupViewpager() {
//        viewPagerLayout?.adapter = createViewPagerAdapter()
//        viewPagerLayout?.isSaveEnabled = false
//        viewPagerLayout?.setCurrentItem(selectedPosition, false)
//        viewPagerLayout?.offscreenPageLimit = pages.size
//        tabLayoutMediator = TabLayoutMediator(tabViewLayout, viewPagerLayout) { tab, position ->
//            setCustomTabView(tab, position)
//        }.apply { attach() }
//        tabViewLayout.addOnTabSelectedListener(this)
//    }
//
//    protected open fun createViewPagerAdapter(): RecyclerView.Adapter<*> {
//        val pagerAdapter = BaseFragmentStateAdapter(childFragmentManager, lifecycle)
//        pagerAdapter.setData(pages.toMutableList())
//        return pagerAdapter
//    }
//
//    override fun onTabSelected(tab: TabLayout.Tab) {
//        unSelectTab(selectedTab)
//        selectTab(tab)
//        selectedPosition = tab.position
//        selectedTab = tab
//    }
//
//    override fun onTabUnselected(tab: TabLayout.Tab) {
//        unSelectTab(selectedTab)
//        selectedTab = tab
//    }
//
//    override fun onDestroyView() {
//        selectedTab = null
//        tabLayoutMediator?.detach()
//        tabLayoutMediator = null
//        super.onDestroyView()
//    }
//    //endregion
//
//    //region Method
//
//    companion object {
//        private const val KEY_POSITION = "SELECTED_POSITION"
//        private const val DEFAULT_POSITION = 0
//    }
//
//    //endregion
//}
