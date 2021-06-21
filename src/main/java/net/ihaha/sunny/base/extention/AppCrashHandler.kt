package net.ihaha.sunny.base.extention

import android.content.Context
import android.content.Intent
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess


/**
 * Date: 17/05/2021.
 * @author SANG.
 * @version 1.0.0.
 */
class AppCrashHandler<T>(private val applicationContext: Context, private val cls : Class<T>) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        val crashInfo = buildString {
            val writer = StringWriter()
            e.printStackTrace(PrintWriter(writer))
            append(writer.toString().trimIndent())
        }
        val intent = Intent(applicationContext, cls)
        intent.putExtra(Intent.EXTRA_TEXT, crashInfo)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            applicationContext.startActivity(intent)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        Timber.tag("CRASH").e(e)
        exitProcess(1)
    }
}