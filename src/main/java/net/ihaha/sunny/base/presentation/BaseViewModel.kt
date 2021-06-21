package net.ihaha.sunny.base.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import net.ihaha.sunny.base.core.repository.network.DataChannelManager
import net.ihaha.sunny.base.core.repository.network.DataState
import net.ihaha.sunny.base.core.repository.network.EventState
import net.ihaha.sunny.base.core.repository.network.StateMessage
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<ViewState> : ViewModel() {
    val TAG: String = "AppDebug"
    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    val dataChannelManager: DataChannelManager<ViewState> =
        object : DataChannelManager<ViewState>() {
            override fun handleNewData(data: ViewState) {
                this@BaseViewModel.handleNewData(data)
            }
        }
    val viewState: LiveData<ViewState>
        get() = _viewState
    val numActiveJobs: LiveData<Int> = dataChannelManager.numActiveJobs
    val stateMessage: LiveData<StateMessage?>
        get() = dataChannelManager.messageStack.stateMessage

    // FOR DEBUGGING
    fun getMessageStackSize(): Int {
        return dataChannelManager.messageStack.size
    }

    fun setupChannel() = dataChannelManager.setupChannel()
    abstract fun handleNewData(data: ViewState)
    abstract fun setEventState(eventState: EventState)
    fun launchJob(
        eventState: EventState,
        jobFunction: Flow<DataState<ViewState>>
    ) {
        dataChannelManager.launchJob(eventState, jobFunction)
    }

    fun areAnyJobsActive(): Boolean {
        return dataChannelManager.numActiveJobs.value?.let {
            it > 0
        } ?: false
    }

    fun isJobAlreadyActive(eventState: EventState): Boolean {
        Timber.d("isJobAlreadyActive?: ${dataChannelManager.isJobAlreadyActive(eventState)} ")
        return dataChannelManager.isJobAlreadyActive(eventState)
    }

    fun getCurrentViewStateOrNew(): ViewState {
        return viewState.value?.let<ViewState, ViewState> { it } ?: initNewViewState()
    }

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun clearStateMessage(index: Int = 0) {
        dataChannelManager.clearStateMessage(index)
    }

    open fun cancelActiveJobs() {
        if (areAnyJobsActive()) {
            Log.d(TAG, "cancel active jobs: ${dataChannelManager.numActiveJobs.value ?: 0}")
            dataChannelManager.cancelJobs()
        }
    }

    abstract fun initNewViewState(): ViewState
}
