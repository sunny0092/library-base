package net.ihaha.sunny.base.presentation.listener

interface BindAdapter<T, K> {
    fun setItems(items: MutableList<T>?)
    fun setOnItemClickListener(onItemClickListener: K)
}
