package net.ihaha.sunny.base.core.repository

import com.orhanobut.logger.Logger.e
import net.ihaha.sunny.base.core.repository.network.*
import net.ihaha.sunny.base.core.repository.network.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import net.ihaha.sunny.base.core.repository.network.ErrorHandling.Companion.NETWORK_ERROR
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val eventState: EventState
) {
    suspend fun getResult(): DataState<ViewState> {
        return when (response) {
            is ApiResult.GenericError -> {
                Timber.d(response.errorMessage)
                DataState.error(
                    response = Response(
                        message = "${eventState.errorInfo()}\n\nError: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    eventState = eventState
                )
            }
            is ApiResult.NetworkError -> {
                Timber.d( "NetworkError :$NETWORK_ERROR")
                DataState.error(
                    response = Response(
                        message = "$NETWORK_ERROR",
                        uiComponentType = UIComponentType.Network,
                        messageType = MessageType.Error
                    ),
                    eventState = eventState
                )
            }
            is ApiResult.Login -> {
                return DataState.data(
                    response = Response(
                        eventState.errorInfo(),
                        UIComponentType.Dialog,
                        MessageType.Login
                    ),
                    eventState = eventState
                )
            }
            is ApiResult.ResendOTP -> {
                return DataState.data(
                    response = Response(
                        eventState.errorInfo(),
                        UIComponentType.Dialog,
                        MessageType.Resend
                    ),
                    eventState = eventState
                )
            }
            is ApiResult.VerityOTP -> {
                return DataState.data(
                    response = Response(
                        eventState.errorInfo(),
                        UIComponentType.Dialog,
                        MessageType.Verify
                    ),
                    eventState = eventState
                )
            }
            is ApiResult.Success -> {
                if (response.value == null) {
                    DataState.error(
                        response = Response(
                            message = "${eventState.errorInfo()}\n\nError: Data is null.",
                            uiComponentType = UIComponentType.None,
                            messageType = MessageType.Error
                        ),
                        eventState = eventState
                    )
                } else {
                    handleSuccess(response = response.value)
                }
            }
        }
    }

    abstract suspend fun handleSuccess(response: Data): DataState<ViewState>

    private fun parseErrorMsg(rawJson: String?): String {
        try {
            if (!rawJson.isNullOrBlank()) {
                if (rawJson.contains(ERROR_CHECK_NETWORK_CONNECTION)) {
                    return NETWORK_ERROR
                }
                return JSONObject(rawJson).get("response") as String
            }
        } catch (e: JSONException) {
            e(e, "ERROR", rawJson)
        }
        return ""
    }
}