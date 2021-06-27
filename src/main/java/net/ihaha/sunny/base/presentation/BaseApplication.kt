package net.ihaha.sunny.base.presentation

import android.content.Context
import androidx.multidex.MultiDexApplication

val appContext: Context by lazy { BaseApplication.instance }
open class BaseApplication : MultiDexApplication() {
    companion object {
        lateinit var instance: Context
    }
}