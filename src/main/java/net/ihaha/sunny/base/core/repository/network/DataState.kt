package net.ihaha.sunny.base.core.repository.network

data class DataState<T>(
    var stateMessage: StateMessage? = null,
    var data: T? = null,
    var eventState: EventState? = null
) {
    companion object {
        fun <T> error(
            responseType: ResponseType,
            eventState: EventState?
        ): DataState<T> {
            return DataState(
                stateMessage = StateMessage(responseType), data = null, eventState = eventState
            )
        }

        fun <T> data(responseType: ResponseType?, data: T? = null, eventState: EventState?): DataState<T> {
            return DataState(
                stateMessage = responseType?.let { StateMessage(it) },
                data = data,
                eventState = eventState
            )
        }
    }
}