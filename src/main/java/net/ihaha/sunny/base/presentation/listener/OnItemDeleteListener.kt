package net.ihaha.sunny.base.presentation.listener
import android.view.View

interface OnItemDeleteListener {
    fun <T>onItemDelete(view: View, index: Int, data: T)
}