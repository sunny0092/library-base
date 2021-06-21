package net.ihaha.sunny.base.presentation.listener

import net.ihaha.sunny.base.core.repository.network.DataState

interface DataStateChangeListener {
    fun onDataStateChangeListener(dataState: DataState<*>?)
}
