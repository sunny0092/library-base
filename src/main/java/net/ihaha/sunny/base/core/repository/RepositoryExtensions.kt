package net.ihaha.sunny.base.core.repository

import com.orhanobut.logger.Logger.d
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import net.ihaha.sunny.base.R
import net.ihaha.sunny.base.core.repository.network.ApiResult
import net.ihaha.sunny.base.extention.string
import net.ihaha.sunny.base.presentation.appContext
import net.ihaha.sunny.base.utils.ServiceConstants.Companion.NETWORK_TIMEOUT
import retrofit2.HttpException
import java.io.IOException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            d("safeApiCall message:", throwable.message)
            when (throwable) {
                is TimeoutCancellationException -> {
                    val message = appContext.string(R.string.error_network_time_out)
                    ApiResult.GenericError(408, message)
                }
                is IOException -> ApiResult.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    if (code == 401) ApiResult.Login
                    else ApiResult.GenericError(code,  throwable.message)
                }
                else -> ApiResult.GenericError(null, throwable.message)
            }
        }
    }
}
