package net.ihaha.sunny.base.core.repository.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
abstract class DataChannelManager<ViewState> {
    private val TAG: String = "AppDebug"
    private val _activeEventStates: HashSet<String> = HashSet()
    private val _numActiveJobs: MutableLiveData<Int> = MutableLiveData()
    private val dataChannel: ConflatedBroadcastChannel<DataState<ViewState>> = ConflatedBroadcastChannel()
    private var channelScope: CoroutineScope? = null

    val messageStack = MessageStack()

    val numActiveJobs: LiveData<Int>
        get() = _numActiveJobs

    init {
        dataChannel
            .asFlow()
            .onEach { dataState ->
                dataState.data?.let { data ->
                    handleNewData(data)
                    removeEventState(dataState.eventState)
                }
                dataState.stateMessage?.let { stateMessage ->
                    handleNewStateMessage(stateMessage)
                    removeEventState(dataState.eventState)
                }
            }
            .launchIn(CoroutineScope(Main))
    }

    fun setupChannel() {
        cancelJobs()
        setupNewChannelScope(CoroutineScope(IO))
    }

    abstract fun handleNewData(data: ViewState)

    private fun offerToDataChannel(dataState: DataState<ViewState>) {
        dataChannel.let {
            if (!it.isClosedForSend) {
                it.offer(dataState)
            }
        }
    }

    fun launchJob(
        eventState: EventState,
        jobFunction: Flow<DataState<ViewState>>
    ) {
        if (!isEventStateActive(eventState) && messageStack.size == 0) {
            addEventState(eventState)
            jobFunction
                .onEach { dataState ->
                    offerToDataChannel(dataState)
                }
                .launchIn(getChannelScope())
        }
    }

    private fun handleNewStateMessage(stateMessage: StateMessage) {
        appendStateMessage(stateMessage)
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun clearStateMessage(index: Int = 0) {
        messageStack.removeAt(index)
    }

    private fun clearActiveEventStateCounter() {
        _activeEventStates.clear()
        syncNumActiveStateEvents()
    }

    private fun addEventState(eventState: EventState) {
        _activeEventStates.add(eventState.toString())
        syncNumActiveStateEvents()
    }

    private fun removeEventState(eventState: EventState?) {
        _activeEventStates.remove(eventState.toString())
        syncNumActiveStateEvents()
    }

    fun isJobAlreadyActive(eventState: EventState): Boolean {
        return isEventStateActive(eventState)
    }

    private fun isEventStateActive(eventState: EventState): Boolean {
        return _activeEventStates.contains(eventState.toString())
    }

    fun getChannelScope(): CoroutineScope {
        return channelScope ?: setupNewChannelScope(CoroutineScope(IO))
    }

    private fun setupNewChannelScope(coroutineScope: CoroutineScope): CoroutineScope {
        channelScope = coroutineScope
        return channelScope as CoroutineScope
    }

    fun cancelJobs() {
        if (channelScope != null) {
            if (channelScope?.isActive == true) {
                channelScope?.cancel()
            }
            channelScope = null
        }
        clearActiveEventStateCounter()
    }

    private fun syncNumActiveStateEvents() {
        _numActiveJobs.value = _activeEventStates.size
    }
}