/*
 * Copyright 2020 Jose Maria Payá Castillo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ihaha.sunny.base.settings.theme

import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate

/**
 * Utils implementation for application theme configuration.
 * @see ThemeUtils
 */
class ThemeUtilsImpl(private val context: Context?) : ThemeUtils {

    /**
     * @see ThemeUtils.isDarkTheme
     */
    override fun isDarkTheme() = context?.resources?.configuration?.uiMode!! and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    /**
     * @see ThemeUtils.isLightTheme
     */
    override fun isLightTheme() = !isDarkTheme()

    /**
     * @see ThemeUtils.setNightMode
     */
    override fun setNightMode(forceNight: Boolean, delay: Long) {
        Handler().postDelayed({
            AppCompatDelegate.setDefaultNightMode(
                if (forceNight) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }, delay)
    }
}
