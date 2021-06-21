package net.ihaha.sunny.base.core.repository

import com.orhanobut.logger.Logger.d
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import net.ihaha.sunny.base.core.repository.network.*
import net.ihaha.sunny.base.utils.ServiceConstants.Companion.CACHE_ERROR_TIMEOUT
import net.ihaha.sunny.base.utils.ServiceConstants.Companion.CACHE_TIMEOUT
import net.ihaha.sunny.base.utils.ServiceConstants.Companion.NETWORK_ERROR_TIMEOUT
import net.ihaha.sunny.base.utils.ServiceConstants.Companion.NETWORK_TIMEOUT
import net.ihaha.sunny.base.utils.ServiceConstants.Companion.UNKNOWN_ERROR
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */
private val TAG: String = "AppDebug"

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT) {
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            Timber.d( "safeApiCall throwable: $throwable.message")
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    if(code == 401){
                        ApiResult.Login
                    }
                    else {
                        d( "safeApiCall response:", throwable.response())
                        d( "safeApiCall message:", throwable.message())
                        val errorResponse = throwable.message
                        ApiResult.GenericError(
                            code,
                            errorResponse
                        )
                    }
                }
                else -> {
                    Timber.d( "UNKNOWN_ERROR :${throwable.message}")
                    ApiResult.GenericError(
                        null,
                        UNKNOWN_ERROR
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT) {
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is TimeoutCancellationException -> {
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.GenericError(UNKNOWN_ERROR)
                }
            }
        }
    }
}


fun <ViewState> buildError(
    message: String,
    uiComponentType: UIComponentType,
    eventState: EventState?
): DataState<ViewState> {
    return DataState.error(
        response = Response(
            message = "${eventState?.errorInfo()}\n\nError: $message",
            uiComponentType = uiComponentType,
            messageType = MessageType.Error
        ),
        eventState = eventState
    )

}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.message ?: throwable.code().toString()
    } catch (exception: Exception) {
        UNKNOWN_ERROR
    }
}