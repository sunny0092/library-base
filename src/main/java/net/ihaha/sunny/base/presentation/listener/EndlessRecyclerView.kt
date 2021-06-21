package net.ihaha.sunny.base.presentation.listener

import androidx.recyclerview.widget.RecyclerView


/**
 * Date: 04/05/2021.
 * @author SANG.
 * @version 1.0.0.
 */
interface EndlessRecyclerView {

    fun resetState()
    fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)

}