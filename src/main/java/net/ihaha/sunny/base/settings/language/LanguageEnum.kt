package net.ihaha.sunny.base.settings.language

import android.content.Context
import android.graphics.drawable.Drawable
import java.util.*


/**
 * Date: 24/12/2020.
 * @author SANG.
 * @version 1.0.0.
 */
enum class LanguageEnum {
    ENGLISH {
        override fun getLocale(): Locale = Locale.ENGLISH
        override fun getTitle() = Locale.ENGLISH.displayLanguage.capitalize()
        override fun getIcon(context: Context): Drawable? {
            return null
        }
    },

    VIETNAM {
        override fun getLocale(): Locale = Locale("vi")
        override fun getTitle() = Locale("vi").displayLanguage.capitalize()
        override fun getIcon(context: Context): Drawable? {
            return null
        }
    };

    abstract fun getLocale(): Locale
    abstract fun getTitle(): String
    abstract fun getIcon(context: Context): Drawable?
}