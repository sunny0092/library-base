package net.ihaha.sunny.base.presentation.listener
import android.view.View

interface OnItemClickListener {
    fun <T>onItemClick(view: View, data: T)
}