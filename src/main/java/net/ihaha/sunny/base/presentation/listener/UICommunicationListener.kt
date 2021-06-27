package net.ihaha.sunny.base.presentation.listener

import net.ihaha.sunny.base.core.repository.network.ResponseType
import net.ihaha.sunny.base.core.repository.network.StateMessageCallback


interface UICommunicationListener {
    fun onResponseReceived(
        responseType: ResponseType,
        onListenerNavigation: OnListenerNavigation? = null,
        stateMessageCallback: StateMessageCallback
    )
    fun displayProgressBar(isLoading: Boolean)
}
