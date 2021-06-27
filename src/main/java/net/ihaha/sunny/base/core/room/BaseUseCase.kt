package net.ihaha.sunny.base.core.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import net.ihaha.sunny.base.core.repository.network.DataState


/**
 * Author: Sunny
 * Version: 1.0.0
 * Date: 19/11/2020
 */

abstract class BaseUseCase<EventState, ViewState, in Params> {
    abstract suspend fun run(eventState: EventState, params: Params): Flow<DataState<ViewState>>
    suspend operator fun invoke(eventState: EventState, params: Params): Flow<DataState<ViewState>> {
        return Mutex().withLock {
            withContext(Dispatchers.IO) { run(eventState, params) }
        }
    }
}

abstract class BaseUseCaseNotParam<EventState, ViewState> {
    abstract suspend fun run(eventState: EventState): Flow<DataState<ViewState>>
    suspend operator fun invoke(eventState: EventState): Flow<DataState<ViewState>> {
        return Mutex().withLock {
            withContext(Dispatchers.IO) { run(eventState) }
        }
    }
}