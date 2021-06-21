package net.ihaha.sunny.base.core.repository.network


data class StateMessage(val response: Response)
data class Response(
    val message: String? = "",
    val uiComponentType: UIComponentType,
    val messageType: MessageType
)

sealed class UIComponentType {
    object Toast : UIComponentType()
    object Dialog : UIComponentType()
    object Network : UIComponentType()
    object Message : UIComponentType()
    object Booking : UIComponentType()
    object None : UIComponentType()
}

sealed class MessageType {
    object Success : MessageType()
    object Login : MessageType()
    object Verify : MessageType()
    object Resend : MessageType()
    object Error : MessageType()
    object Warning : MessageType()
    object Info : MessageType()
    object None : MessageType()
}

interface StateMessageCallback {
    fun removeMessageFromStack()
}
