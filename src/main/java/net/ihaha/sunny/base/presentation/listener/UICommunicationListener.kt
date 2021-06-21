package net.ihaha.sunny.base.presentation.listener

import net.ihaha.sunny.base.core.repository.network.Response
import net.ihaha.sunny.base.core.repository.network.StateMessageCallback


interface UICommunicationListener {
    fun onResponseReceived(
        response: Response,
        onListenerNavigation: OnListenerNavigation? = null,
        stateMessageCallback: StateMessageCallback
    )
    fun displayProgressBar(isLoading: Boolean)
}
