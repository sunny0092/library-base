package net.ihaha.sunny.base.extention

import com.google.android.material.tabs.TabLayout


/**
 * Date: 09/12/2020.
 * @author SANG.
 * @version 1.0.0.
 */
fun TabLayout.addOnTabSelectedListener(
    bodyReselected: ((tabReselected: TabLayout.Tab?) -> Unit)? = null,
    bodyUnselected: ((tabUnselected: TabLayout.Tab?) -> Unit)? = null,
    bodySelected: ((tabSelected: TabLayout.Tab?) -> Unit)? = null
) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            if (bodyReselected != null) {
                bodyReselected(tab)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            if (bodyUnselected != null) {
                bodyUnselected(tab)
            }
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (bodySelected != null) {
                bodySelected(tab)
            }
        }
    })
}

fun TabLayout.setOnTabSelectedListener(
    bodySelected: (tabSelected: TabLayout.Tab?) -> Unit
) = addOnTabSelectedListener(null, null, bodySelected)


fun TabLayout.setOnTabSelectedAndReselectedListener(
    bodyReselected: (tabReselected: TabLayout.Tab?) -> Unit,
    bodySelected: (tabSelected: TabLayout.Tab?) -> Unit
) = addOnTabSelectedListener(bodyReselected, null, bodySelected)


fun TabLayout.setOnTabSelectedAndUnselectedListener(
    bodyUnselected: (tabUnselected: TabLayout.Tab?) -> Unit,
    bodySelected: (tabSelected: TabLayout.Tab?) -> Unit
) = addOnTabSelectedListener(null, bodyUnselected, bodySelected)