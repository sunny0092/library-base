package net.ihaha.sunny.base.presentation.listener
import android.view.View

interface OnItemCartListener {
    fun onItemDelete(view: View, productId: String)
    fun onItemPlus(view: View, productId: String)
    fun onItemMinus(view: View, productId: String)
    fun <T>onItemClick(view: View, data: T)
}