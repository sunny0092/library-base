package net.ihaha.sunny.base.presentation.listener
import android.view.View

interface OnItemPositionClickListener {
    fun <T>onItemClick(view: View, position: Int, data: T)
}