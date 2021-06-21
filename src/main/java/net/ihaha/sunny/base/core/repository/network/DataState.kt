package net.ihaha.sunny.base.core.repository.network

data class DataState<T>(
    var stateMessage: StateMessage? = null,
    var data: T? = null,
    var eventState: EventState? = null
) {

    companion object {
        fun <T> error(
            response: Response,
            eventState: EventState?
        ): DataState<T> {
            return DataState(
                stateMessage = StateMessage(
                    response
                ),
                data = null,
                eventState = eventState
            )
        }

        fun <T> data(
            response: Response?,
            data: T? = null,
            eventState: EventState?
        ): DataState<T> {
            return DataState(
                stateMessage = response?.let {
                    StateMessage(
                        it
                    )
                },
                data = data,
                eventState = eventState
            )
        }
    }
}