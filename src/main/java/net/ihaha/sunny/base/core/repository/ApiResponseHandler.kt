package net.ihaha.sunny.base.core.repository

import net.ihaha.sunny.base.R
import net.ihaha.sunny.base.core.repository.network.*
import net.ihaha.sunny.base.presentation.appContext

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val eventState: EventState
) {
    suspend fun getResult(): DataState<ViewState> {
        return when (response) {
            is ApiResult.GenericError -> {
                val message = "${eventState.errorInfo()}\n\n ${response.errorMessage}"
                message.error(eventState, UIComponentType.Dialog, MessageType.Error)
            }
            is ApiResult.NetworkError -> {
                val message = "${appContext.getString(R.string.error_no_internet_connection)}"
                message.error(eventState, UIComponentType.Network, MessageType.Error)
            }
            is ApiResult.Login -> {
                val message = appContext.getString(R.string.error_login_another)
                message.error(eventState, UIComponentType.Dialog, MessageType.Login)
            }
            is ApiResult.ResendOTP -> {
                val message = eventState.errorInfo()
                message.error(eventState, UIComponentType.Dialog, MessageType.Resend)
            }
            is ApiResult.VerityOTP -> {
                val message = appContext.getString(R.string.error_login_veriry)
                message.error(eventState, UIComponentType.Dialog, MessageType.Verify)
            }
            is ApiResult.Success -> {
                handleSuccess(response = response.value)
            }
        }
    }

    abstract suspend fun handleSuccess(response: Data?): DataState<ViewState>
}