package net.ihaha.sunny.base.settings.language

import android.content.Context
import net.ihaha.sunny.base.settings.session.SharePrefsImpl
import net.ihaha.sunny.base.settings.session.SharePrefsManager

object LanguageUtils {
    fun setLanguage(context: Context, language: String) {
        val sharePrefsManager = SharePrefsManager(sharePreferences = SharePrefsImpl(
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