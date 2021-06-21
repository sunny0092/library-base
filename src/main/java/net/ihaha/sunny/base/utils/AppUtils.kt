package net.ihaha.sunny.base.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue

/**
 * Created by SangNguyen on 10/30/2019.
 * Version 1.0
 */

object AppUtils {

    fun dpToPx(context: Context, dp: Float): Int {
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            )
        )
    }

    fun openAppOnPlayStore(context: Context) {
        val packageName = context.packageName
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    fun openMailOnDevice(context: Context, error: (String) -> Unit) {
        val mailTo = "mailto:vsang25@gmail.com" + "?subject=" + Uri.encode("context.getString(R.string.title_mail_subject)")
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(mailTo)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            error.invoke("context.getString(R.string.msg_mail_error)")
        }
    }
}