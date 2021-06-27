//package net.ihaha.sunny.base.core.repository
//
//import com.orhanobut.logger.Logger.d
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.FlowPreview
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import net.ihaha.sunny.base.core.repository.network.ApiResult
//import net.ihaha.sunny.base.core.repository.network.DataState
//import net.ihaha.sunny.base.core.repository.network.ErrorHandling.Companion.NETWORK_ERROR
//import net.ihaha.sunny.base.core.repository.network.ErrorHandling.Companion.UNKNOWN_ERROR
//import net.ihaha.sunny.base.core.repository.network.EventState
//import net.ihaha.sunny.base.core.repository.network.UIComponentType
//import net.ihaha.sunny.base.utils.ServiceConstants.Companion.LOGIN
//
//@FlowPreview
//abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
//constructor(
//    private val dispatcher: CoroutineDispatcher,
//    private val eventState: EventState,
//    private val apiCall: suspend () -> NetworkObj?,
//    private val cacheCall: suspend () -> CacheObj?
//) {
//    val result: Flow<DataState<ViewState>> = flow {
//        // ****** STEP 1: VIEW CACHE ******
//        emit(returnCache(markJobComplete = false))
//        // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
//        //        Log.d(TAG, "NetworkBoundResource, apiResult: $apiResult")
//        when (val apiResult = safeApiCall(dispatcher) { apiCall.invoke() }) {
//            is ApiResult.GenericError -> {
//                d("NetworkBoundResource, apiResult, Generic error", apiResult.errorMessage)
//                d("NetworkBoundResource, apiResult, Generic error, state event",eventState.errorInfo())
//                emit(
//                    buildError<ViewState>(
//                        apiResult.errorMessage?.let { it } ?: UNKNOWN_ERROR,
//                        UIComponentType.Dialog,
//                        eventState
//                    )
//                )
//            }
//            is ApiResult.NetworkError -> {
//                d("NetworkBoundResource, apiResult, Network error", apiResult)
//                emit(
//                    buildError<ViewState>(
//                        NETWORK_ERROR,
//                        UIComponentType.None,
//                        eventState
//                    )
//                )
//            }
//            is ApiResult.Login -> {
//                emit(
//                    buildError<ViewState>(
//                        LOGIN,
//                        UIComponentType.Dialog,
//                        eventState
//                    )
//                )
//            }
//            is ApiResult.ResendOTP -> {
//                emit(
//                    buildError<ViewState>(
//                        LOGIN,
//                        UIComponentType.Dialog,
//                        eventState
//                    )
//                )
//            }
//            is ApiResult.VerityOTP -> {
//                emit(
//                    buildError<ViewState>(
//                        LOGIN,
//                        UIComponentType.Dialog,
//                        eventState
//                    )
//                )
//            }
//            is ApiResult.Success -> {
//                if (apiResult.value == null) {
//                    emit(
//                        buildError<ViewState>(
//                            UNKNOWN_ERROR,
//                            UIComponentType.Dialog,
//                            eventState
//                        )
//                    )
//                } else {
//                    updateCache(apiResult.value as NetworkObj)
//                }
//            }
//        }
//        // ****** STEP 3: VIEW CACHE and MARK JOB COMPLETED ******
//        emit(returnCache(markJobComplete = true))
//    }
//
//    private suspend fun returnCache(markJobComplete: Boolean): DataState<ViewState> {
//        val cacheResult = safeCacheCall(dispatcher) { cacheCall.invoke() }
//        var jobCompleteMarker: EventState? = null
//        if (markJobComplete) {
//            jobCompleteMarker = eventState
//        }
//        return object : CacheResponseHandler<ViewState, CacheObj>(
//            response = cacheResult,
//            eventState = jobCompleteMarker
//        ) {
//            override suspend fun handleSuccess(response: CacheObj): DataState<ViewState> {
//                return handleCacheSuccess(response)
//            }
//        }.getResult()
//    }
//
//    abstract suspend fun updateCache(networkObject: NetworkObj)
//    abstract fun handleCacheSuccess(resultObj: CacheObj): DataState<ViewState> // make sure to return null for stateEvent
//}

//fun <ViewState> buildError(
//    message: String,
//    uiComponentType: UIComponentType,
//    eventState: EventState?
//): DataState<ViewState> {
//    return DataState.error(
//        responseType = ResponseType(
//            message = "${eventState?.errorInfo()}\n\nError: $message",
//            uiComponentType = uiComponentType,
//            messageType = MessageType.Error
//        ),
//        eventState = eventState
//    )
//
//}