package net.ihaha.sunny.base.settings.language

import android.content.Context
import com.delichill.shipper.utils.prefs.SharePrefsManager

object LanguageUtils {
    fun setLanguage(context: Context, language: String) {
        val sharePrefsManager = SharePrefsManager(sharePreferences = net.ihaha.sunny.base.settings.session.SharePrefsImpl(
            context
        )
        )
        sharePrefsManager.setLanguage(language)
    }

    fun getLanguage(context: Context) : String?{
        val sharePrefsManager = SharePrefsManager(sharePreferences = net.ihaha.sunny.base.settings.session.SharePrefsImpl(
            context
        )
        )
        return sharePrefsManager.getLanguage()
    }

}