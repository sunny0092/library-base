package net.ihaha.sunny.base.presentation.listener

import com.delichill.shipper.core.data.util.network.Response
import com.delichill.shipper.core.data.util.network.StateMessageCallback
import com.delichill.shipper.listener.OnListenerNavigation


interface UICommunicationListener {
    fun onResponseReceived(
        response: Response,
        onListenerNavigation: OnListenerNavigation? = null,
        stateMessageCallback: StateMessageCallback
    )
    fun displayProgressBar(isLoading: Boolean)
}
