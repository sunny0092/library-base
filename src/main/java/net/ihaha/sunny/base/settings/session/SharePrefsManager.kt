package net.ihaha.sunny.base.settings.session


class SharePrefsManager(private val sharePreferences: SharePrefsImpl?) {

    fun setTheme(theme: String) = sharePreferences?.put(SharedPrefKeys.Theme.THEME, theme)

    fun getTheme() : String? = sharePreferences?.get(SharedPrefKeys.Theme.THEME, String::class.java)

    fun setLanguage(language: String) = sharePreferences?.put(SharedPrefKeys.Language.LANGUAGE, language)

    fun getLanguage() : String? = sharePreferences?.get(SharedPrefKeys.Language.LANGUAGE, String::class.java)

    fun getToken() : String? = sharePreferences?.get(SharedPrefKeys.Account.TOKEN, String::class.java)

    fun setPhone(phone: String) =  sharePreferences?.put(SharedPrefKeys.Account.PHONE, phone)

    fun getPhone() = sharePreferences?.get(SharedPrefKeys.Account.PHONE, String::class.java)

    fun setPassword(password: String) =  sharePreferences?.put(SharedPrefKeys.Account.PASSWORD, password)

    fun getPassword() = sharePreferences?.get(SharedPrefKeys.Account.PASSWORD, String::class.java)

    fun setRemember(isCheck: Boolean) = sharePreferences?.put(SharedPrefKeys.Account.CHECK_BOX_REMEMBER, isCheck)

    fun getRemember() = sharePreferences?.get(SharedPrefKeys.Account.CHECK_BOX_REMEMBER, Boolean::class.java)

    fun getFirebaseToken() : String? = sharePreferences?.get(SharedPrefKeys.Account.FIREBASE_TOKEN, String::class.java)

    fun setFirebaseToken(token: String) =  sharePreferences?.put(SharedPrefKeys.Account.FIREBASE_TOKEN, token)

    fun getNotificationBadge() = sharePreferences?.get(SharedPrefKeys.Account.NOTIFICATION_BADGE, Int::class.java)

    fun setNotificationBadge(number: Int) = sharePreferences?.put(SharedPrefKeys.Account.NOTIFICATION_BADGE, number)

    fun getSmsOtp() = sharePreferences?.get(SharedPrefKeys.Account.OTP, String::class.java)

    fun setSmsOtp(otp: String) =  sharePreferences?.put(SharedPrefKeys.Account.OTP, otp)

    fun setTimeTracking(time: Int) = sharePreferences?.put(SharedPrefKeys.Account.TIME_TRACKING, time)

    fun getTimeTracking() = sharePreferences?.get(SharedPrefKeys.Account.TIME_TRACKING, Int::class.java)

    fun setLatitudeTracking(time: Double) = sharePreferences?.put(SharedPrefKeys.Account.LATITUDE_TRACKING, time)

    fun setLongitudeTracking(time: Double) = sharePreferences?.put(SharedPrefKeys.Account.LONGITUDE_TRACKING, time)

    fun getLatitudeTracking() = sharePreferences?.get(SharedPrefKeys.Account.LATITUDE_TRACKING, Double::class.java)

    fun getLongitudeTracking() = sharePreferences?.get(SharedPrefKeys.Account.LONGITUDE_TRACKING, Double::class.java)

    fun setBookingId(booking: String) = sharePreferences?.put(SharedPrefKeys.Account.BOOKING_STATUS_ID, booking)

    fun getBookingId() = sharePreferences?.get(SharedPrefKeys.Account.BOOKING_STATUS_ID, String::class.java)

    fun setIsRunning(isRunning: Boolean = true) = sharePreferences?.put(SharedPrefKeys.Account.IS_RUNNING, isRunning)

    fun getIsRunning() = sharePreferences?.get(SharedPrefKeys.Account.IS_RUNNING, Boolean::class.java)

    fun setCheckMoneyRunning(isRunning: Boolean = true) = sharePreferences?.put(SharedPrefKeys.Account.MONEY_RUNNING, isRunning)

    fun getCheckMoneyRunning() = sharePreferences?.get(SharedPrefKeys.Account.MONEY_RUNNING, Boolean::class.java)

    fun setBookingCountdown(isRunning: Boolean = true) = sharePreferences?.put(SharedPrefKeys.Account.MONEY_RUNNING, isRunning)

    fun getBookingCountDown() = sharePreferences?.get(SharedPrefKeys.Account.MONEY_RUNNING, Boolean::class.java)

}