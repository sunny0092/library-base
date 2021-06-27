package net.ihaha.sunny.base.core.repository

import net.ihaha.sunny.base.core.repository.network.*

abstract class CacheResponseHandler<ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val eventState: EventState?
) {
    suspend fun getResult(): DataState<ViewState> {
        return when (response) {
            is CacheResult.GenericError -> {
                DataState.error(
                    responseType = ResponseType(
                        message = "${eventState?.errorInfo()}\n\nError: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Error
                    ),
                    eventState = eventState
                )
            }
            is CacheResult.Success -> {
                if (response.value == null) {
                    DataState.error(
                        responseType = ResponseType(
                            message = "${eventState?.errorInfo()}\n\nError: Data is NULL.",
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
}