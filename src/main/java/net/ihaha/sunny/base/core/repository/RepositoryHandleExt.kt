package net.ihaha.sunny.base.core.repository

import net.ihaha.sunny.base.core.repository.network.DataState
import net.ihaha.sunny.base.core.repository.network.EventState
import net.ihaha.sunny.base.core.room.BaseData
import net.ihaha.sunny.base.core.room.BaseModel
import net.ihaha.sunny.base.core.room.BaseObject
import retrofit2.Response


/**
 * Date: 23/06/2021.
 * @author SANG.
 * @version 1.0.0.
 */

fun <ViewState> Response<BaseModel>?.responseStatus(
    eventState: EventState,
    onCallBack: (data: BaseModel?) -> DataState<ViewState>?
): DataState<ViewState>? {
    return when (this?.isSuccessful) {
        true -> {
            when {
                this.code().checkStatusSuccess() -> onCallBack.invoke(this.body())
                this.code().checkStatusVerify(this.body()?.msg) -> eventState.verify(this.body()?.msg)
                else -> {
                    if (this.body() != null) eventState.serverError(this, this.body()?.msg)
                    else eventState.serverErrorMessage()
                }
            }
        }
        else -> {
            if (this?.code()?.checkLogin() == true)
                eventState.login()
            else {
                val body = this?.body()
                eventState.serverError(this, body?.msg)
            }
        }
    }
}

fun <ViewState, T> Response<BaseData<T>>?.responseDataNoWarning(
    eventState: EventState,
    onCallBack: (data: MutableList<T>?) -> DataState<ViewState>?
): DataState<ViewState>? {
    return when (this?.isSuccessful) {
        true -> {
            if (this.body() != null) {
                val body = this.body()
                when {
                    body?.data?.isNotEmpty() == true -> body.data.toMutableList().let { onCallBack.invoke(it) }
                    this.code().checkStatusVerify(this.body()?.msg) -> eventState.verify(this.body()?.msg)
                    else -> eventState.serverNone()
                }
            } else eventState.serverErrorMessage()
        }
        else -> {
            if (this?.code()?.checkLogin() == true)
                eventState.login()
            else
                eventState.serverNone()
        }
    }
}

fun <ViewState, T> Response<BaseData<T>>?.responseDataWarning(
    eventState: EventState,
    onCallBack: (data: MutableList<T>?) -> DataState<ViewState>?
): DataState<ViewState>? {
    return when (this?.isSuccessful) {
        true -> {
            if (this.body() != null) {
                val body = this.body()
                when {
                    body?.data?.isNotEmpty() == true -> body.data.toMutableList()
                        .let { onCallBack.invoke(it) }
                    this.code().checkStatusVerify(this.body()?.msg) -> eventState.verify(this.body()?.msg)
                    else -> eventState.serverErrorData(this)
                }
            } else eventState.serverErrorMessage()
        }
        else -> {
            if (this?.code()?.checkLogin() == true) {
                eventState.login()
            } else {
                val body = this?.body()
                eventState.serverError(this, body?.msg)
            }
        }
    }
}

fun <ViewState, T> Response<BaseObject<T>>?.responseObjectNoWarning(
    eventState: EventState,
    onCallBack: (data: T?) -> DataState<ViewState>?
): DataState<ViewState>? {
    return when {
        this?.isSuccessful == true -> {
            if (this.body() != null) {
                val body = this.body()
                when {
                    body?.data != null -> body.data.let { onCallBack.invoke(it) }
                    this.code().checkStatusVerify(this.body()?.msg) -> eventState.verify(this.body()?.msg)
                    else -> eventState.serverNone()
                }
            } else eventState.serverErrorMessage()
        }
        this?.code()?.checkLogin() == true -> {
            eventState.login()
        }
        else -> eventState.serverNone()
    }
}

fun <ViewState, T> Response<BaseObject<T>>?.responseObject(
    eventState: EventState,
    onCallBack: (data: T?) -> DataState<ViewState>?
): DataState<ViewState>? {
    return when (this?.isSuccessful) {
        true -> {
            if (this.body() != null) {
                val body = this.body()
                when {
                    body?.data != null -> body.data.let { onCallBack.invoke(it) }
                    this.code().checkStatusVerify(this.body()?.msg) -> eventState.verify(this.body()?.msg)
                    else -> eventState.serverErrorObject(this)
                }
            } else eventState.serverErrorMessage()
        }
        else -> {
            if (this?.code()?.checkLogin() == true) {
                eventState.login()
            } else {
                val body = this?.body()
                eventState.serverError(this, body?.msg)
            }
        }
    }
}


fun <U, T> EventState.serverErrorData(response: Response<BaseData<T>>?): DataState<U>? {
    val body = response?.body()
    return when {
        body?.msg?.isNotEmpty() == true -> serverWarning(body?.msg)
        else -> serverNone()
    }
}

fun <U, T> EventState.serverErrorObject(response: Response<BaseObject<T>>?): DataState<U>? {
    val body = response?.body()
    return when {
        body?.msg?.isNotEmpty() == true -> serverWarning(body?.msg)
        else -> serverNone()
    }
}
