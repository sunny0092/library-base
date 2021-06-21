package net.ihaha.sunny.base.presentation.listener
import android.view.View

interface OnItemGroupClickListener {
    fun <T>onItemClick(view: View, data: T)
    fun onMoreClick(position: Int)
}