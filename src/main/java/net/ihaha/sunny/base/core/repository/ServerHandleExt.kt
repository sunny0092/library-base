package net.ihaha.sunny.base.core.repository

import net.ihaha.sunny.base.R
import net.ihaha.sunny.base.core.repository.network.*
import net.ihaha.sunny.base.presentation.appContext
import retrofit2.Response


/**
 * Date: 23/06/2021.
 * @author SANG.
 * @version 1.0.0.
 */

fun <T> EventState.serverSuccess(data: T? = null): DataState<T> {
    return this.success(data)
}

fun <ViewState> EventState.serverNone(): DataState<ViewState> {
    return "".error(this, UIComponentType.None, MessageType.None)
}

fun <ViewState, T> EventState.serverError(response: Response<T>?, message: String?): DataState<ViewState> {
    return "${response?.code()} ${response?.message()} \n $message".error(this, UIComponentType.Dialog, MessageType.Error)
}

fun <ViewState> EventState.serverErrorOrder(message: String?): DataState<ViewState>? {
    return message?.error(this, UIComponentType.Booking, MessageType.Warning)
}

fun <ViewState> EventState.serverWarning(message: String?): DataState<ViewState>? {
    return message?.error(this, UIComponentType.Dialog, MessageType.Warning)
}

fun <ViewState> EventState.serverErrorMessage(): DataState<ViewState>? {
    val message = appContext.resources?.getString(R.string.error_no_data_from_server)
    return message?.error(this, UIComponentType.Dialog, MessageType.Warning)
}

fun <ViewState> EventState.verify(message: String?): DataState<ViewState>? {
    return message?.error(this, UIComponentType.Dialog, MessageType.Verify)
}

fun <T> EventState.login(): DataState<T>? {
    val message = appContext.resources?.getString(R.string.error_login_another)
    return message?.error(this, UIComponentType.Dialog, MessageType.Login)
}

fun <T> EventState.success(data: T? = null): DataState<T> {
    return DataState.data(data = data, responseType = null, eventState = this)
}

fun <ViewState> String.error(eventState: EventState, typeUI: UIComponentType, type: MessageType): DataState<ViewState> {
    return DataState.error(
        responseType = ResponseType(this, typeUI, type),
        eventState = eventState
    )
}

fun Int.checkLogin(): Boolean {
    return this == 401
}

fun Int.checkStatusSuccess(): Boolean {
    return this == 200 || this == 204
}

fun Int.checkStatusVerify(message: String?): Boolean {
    return this == 202 && message?.contains("OTP") == true
}

