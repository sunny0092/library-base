package net.ihaha.sunny.base.lifecycle

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

/**
 * Created by mkwon on 02/04/2019
 */
class LifeCycleObserverUtils(private val lifecycle: Lifecycle) : LifecycleObserver {

    var className: String? = null

    fun init(activity: Activity) {
        lifecycle.addObserver(this)
        className = activity::class.java.simpleName
    }

    fun init(fragment: Fragment) {
        lifecycle.addObserver(this)
        className = fragment::class.java.simpleName
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() = logInfo("onCreate() $className")

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() = logInfo("onStart() $className")

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        logInfo("onResume() $className")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() = logInfo("onPause() $className")

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() = logInfo("onStop() $className")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        logInfo("onDestroy() $className")
        lifecycle.removeObserver(this)
    }

    private fun logInfo(lifecycleName: String) {
        Timber.d(lifecycleName)
    }

}