package net.ihaha.sunny.base.helpers

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.ihaha.sunny.base.extention.default
import net.ihaha.sunny.base.extention.set

class LiveDataTimer {

    internal var count = 90000
    private var timer: CountDownTimer? = null
    internal val status =
        MutableLiveData<LiveDataTimerStatus>().default(LiveDataTimerStatus.Default)

    private fun startTimer(){
        timer = object: CountDownTimer(90000, 1000){
            override fun onTick(counter: Long) {
                status.set(LiveDataTimerStatus.OnTick(count.toLong()))
                count - 1000
            }
            override fun onFinish() {
                status.set(LiveDataTimerStatus.OnFinish)
            }
        }.start()
    }

    fun getTimerStatus(): LiveData<LiveDataTimerStatus> = status

    fun start(){
        status.set(LiveDataTimerStatus.Default)
        timer = null
        count = 60
        startTimer()
    }

    fun stop(){
        status.set(LiveDataTimerStatus.Default)
        timer = null
    }


    sealed class LiveDataTimerStatus {
        class OnTick(val count: Long) : LiveDataTimerStatus()
        object OnFinish : LiveDataTimerStatus()
        object Default : LiveDataTimerStatus()
    }

}