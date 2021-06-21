package net.ihaha.sunny.base.presentation.listener

import com.delichill.shipper.core.data.util.network.DataState


interface DataStateChangeListener {
    fun onDataStateChangeListener(dataState: DataState<*>?)
}
