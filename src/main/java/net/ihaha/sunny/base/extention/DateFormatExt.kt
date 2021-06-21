package net.ihaha.sunny.base.extention

import net.ihaha.sunny.base.utils.TimeConstants.PARAMS_INPUT_4
import net.ihaha.sunny.base.utils.TimeConstants.PARAMS_INPUT_8
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * convert string to date
 * if string is blank or format is blank then return null
 * if string cannot be parsed then return null
 * else return date
 */
fun String.toDate(
    format: String, locale: Locale = Locale.getDefault(),
): Date? {
    if (this.isBlank() || format.isBlank()) return null
    return try {
        SimpleDateFormat(format, locale).parse(this)
    } catch (e: Exception) {
//        e.safeLog()
        null
    }
}

fun String.toCalendar(
    format: String,
    locale: Locale = Locale.getDefault()
): Calendar? = try {
    val cal = Calendar.getInstance()
    cal.time = SimpleDateFormat(format, locale).parse(this)
    cal
} catch (e: Exception) {
    null
}


/**
 * convert string to time long milliseconds
 * use function string to date
 */
fun String.toTimeLong(
    format: String, locale: Locale = Locale.getDefault(),
): Long? = toDate(format, locale)?.time

/**
 * convert time long milliseconds to string with predefined format
 * if format is blank return null
 * if format is not java date time format then catch Exception and return null
 * else return formatted string
 */
fun Long.toTimeString(
    format: String, locale: Locale = Locale.getDefault(),
): String? {
    if (format.isBlank()) return null
    return try {
        SimpleDateFormat(format, locale).format(Date(this))
    } catch (e: Exception) {
//        e.safeLog()
        null
    }
}

/**
 * change time string format from oldFormat to newFormat
 * if string or oldFormat or newFormat is blank then return null
 * if oldFormat/newFormat is illegal then catch exception and return null
 * else return string
 */
fun String.changeTimeFormat(
    oldFormat: String, newFormat: String, locale: Locale = Locale.getDefault(),
): String? {
    if (this.isBlank() || oldFormat.isBlank() || newFormat.isBlank()) return null
    return try {
        val simpleDateFormat = SimpleDateFormat(oldFormat, locale)
        val date = simpleDateFormat.parse(this)
        simpleDateFormat.applyPattern(newFormat)
        if (date != null) simpleDateFormat.format(date)
        else null
    } catch (e: Exception) {
//        e.safeLog()
        null
    }
}

/**
 * convert date to time string
 * if format is wrong or illegal then catch exception and return null
 * else return string
 */
fun Date.toTimeString(format: String, locale: Locale = Locale.getDefault()): String? {
    return if (format.isBlank()) null
    else try {
        SimpleDateFormat(format, locale).format(this)
    } catch (e: Exception) {
//        e.safeLog()
        null
    }
}

fun String.countdownTime(onCallBack: (isRunning: Boolean, timber: Long) -> Unit) {
    var timeMilliSecondCountDown = 0L
    val timeMilliSecondServer = this?.toDate(PARAMS_INPUT_8) ?: Calendar.getInstance().time
    val timeMilliSecondNow = Calendar.getInstance().time
    if (timeMilliSecondServer > timeMilliSecondNow) {
        timeMilliSecondCountDown = (timeMilliSecondServer.time - timeMilliSecondNow.time) / 1000
        Timber.d("Notification-Log: Time from server - CountDown: $timeMilliSecondCountDown")
        onCallBack.invoke(true, timeMilliSecondCountDown)
    }else{
        onCallBack.invoke(false, timeMilliSecondCountDown)
    }
}

/**
 * get current date time
 */
fun getCurrentDateTime(): Date = Calendar.getInstance().time

/**
 * convert date to calendar
 */
fun Date.toCalendar(): Calendar {
    return Calendar.getInstance().let {
        it.time = this
        it
    }
}

/**
 * get previous month of this date
 */
fun Date.getPreviousMonth(): Date {
    return Calendar.getInstance().let {
        it.time = this
        it.add(Calendar.MONTH, -1)
        it.time
    }
}

/**
 * get next month of this date
 */
fun Date.getNextMonth(): Date {
    return Calendar.getInstance().let {
        it.time = this
        it.add(Calendar.MONTH, 1)
        it.time
    }
}

/**
 * get previous day of this date
 */
fun Date.getPreviousDay(): Date {
    return Calendar.getInstance().let {
        it.time = this
        it.add(Calendar.DAY_OF_MONTH, -1)
        it.time
    }
}

/**
 * get next day of this date
 */
fun Date.getNextDay(): Date {
    return Calendar.getInstance().let {
        it.time = this
        it.add(Calendar.DAY_OF_MONTH, 1)
        it.time
    }
}

fun formatTimeToYourFormat(milliseconds: Long, format: String?): String? {
    val date = Date(milliseconds)
    val dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.format(date)
}

fun formatSecondTimeToYourFormat(second: Long, format: String?): String? {
    val date = Date(TimeUnit.SECONDS.toMillis(second))
    val dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.format(date)
}

fun formatTimeToYourFormatTimeZoneDefault(
    milliseconds: Long,
    format: String?,
    isHaveTimeZone: Boolean,
): String? {
    val date = Date(milliseconds)
    val dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
    if (isHaveTimeZone) {
        dateFormat.timeZone = TimeZone.getDefault()
    }
    return dateFormat.format(date)
}

fun addTimeWithCurrentLocationAndFormat(currentMilliseconds: Long, timeFormat: String?): String? {
    val gmtOfCurrentTimeZone = TimeZone.getDefault().rawOffset.toLong() //milliseconds.
    return formatTimeToYourFormat(currentMilliseconds + gmtOfCurrentTimeZone, timeFormat)
}

fun checkTimeWithCurrentTime(startTime: Long, endTime: Long): Int {
    var startTime = startTime
    var endTime = endTime
    val gmtOfCurrentTimeZone = TimeZone.getDefault().rawOffset.toLong() //milliseconds.
    val currentTime = System.currentTimeMillis() + gmtOfCurrentTimeZone
    startTime = TimeUnit.SECONDS.toMillis(startTime) + gmtOfCurrentTimeZone
    endTime = TimeUnit.SECONDS.toMillis(endTime) + gmtOfCurrentTimeZone
    if (currentTime < startTime) {
        return 1 //PROGRAM NOT ARISE.
    } else if (currentTime >= endTime) {
        return 2 //END PROGRAM.
    } else if (currentTime >= startTime && currentTime < endTime) {
        return 3 //PROGRAM LIVE.
    }
    return 0 //UNKNOWN.
}

fun nextDate(currentMilliseconds: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentMilliseconds
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    return calendar.timeInMillis
}

fun previousDate(currentMilliseconds: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentMilliseconds
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    return calendar.timeInMillis
}

@JvmName("addTimeWithCurrentLocationAndFormat1")
fun addTimeWithCurrentLocationAndFormat(currentMilliseconds: Long, timeFormat: String): String? {
    val gmtOfCurrentTimeZone = TimeZone.getDefault().rawOffset
    return formatTimeToYourFormat(currentMilliseconds + gmtOfCurrentTimeZone, timeFormat)
}

fun getRealTimeFromTime(timeBySeconds: Long): String? {
    val gmtOfCurrentTimeZone = TimeZone.getDefault().rawOffset.toLong() //milliseconds.
    val currentTime = System.currentTimeMillis() + gmtOfCurrentTimeZone
    val timeWithTimeZone = TimeUnit.SECONDS.toMillis(timeBySeconds) + gmtOfCurrentTimeZone
    return if (currentTime < timeWithTimeZone) addTimeWithCurrentLocationAndFormat(
        timeWithTimeZone,
        PARAMS_INPUT_4
    ) else {
        val timeAfterCalculation = currentTime - timeWithTimeZone
        val hour = TimeUnit.MILLISECONDS.toHours(timeAfterCalculation)
        val minute =
            TimeUnit.MILLISECONDS.toMinutes(timeAfterCalculation) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(timeAfterCalculation)
            )
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(timeAfterCalculation) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(timeAfterCalculation)
            )
        if (hour >= 24) {
            addTimeWithCurrentLocationAndFormat(timeWithTimeZone, PARAMS_INPUT_4)
        } else {
            if (hour > 0) {
                return "$hour giờ trước"
            }
            if (minute > 0) {
                "$minute phút trước"
            } else {
                "$seconds giây trước"
            }
        }
    }
}

fun getRealDayTimeFromTime(timeBySeconds: Long): String? {
    var timeBySeconds = timeBySeconds
    timeBySeconds = System.currentTimeMillis() - timeBySeconds
    val days = TimeUnit.MILLISECONDS.toDays(timeBySeconds)
    val hour = TimeUnit.MILLISECONDS.toHours(timeBySeconds) - TimeUnit.DAYS.toHours(
        TimeUnit.MILLISECONDS.toDays(timeBySeconds)
    )
    val minute = TimeUnit.MILLISECONDS.toMinutes(timeBySeconds) - TimeUnit.HOURS.toMinutes(
        TimeUnit.MILLISECONDS.toHours(timeBySeconds)
    )
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeBySeconds) - TimeUnit.MINUTES.toSeconds(
        TimeUnit.MILLISECONDS.toMinutes(timeBySeconds)
    )
    return if (hour >= 24) {
        "$days ngày trước"
    } else {
        if (hour > 0) {
            return "$hour giờ trước"
        }
        if (minute > 0) {
            "$minute phút trước"
        } else {
            "$seconds giây trước"
        }
    }
}

const val inputFormat = "HH:mm"
private var date: Date? = null
private var dateCompareOne: Date? = null
private var dateCompareTwo: Date? = null

var inputParser = SimpleDateFormat(inputFormat, Locale.US)

fun compareTimeRanger(compareStringOne: String, compareStringTwo: String): Boolean {
    val now = Calendar.getInstance()
    val hour = now[Calendar.HOUR]
    val minute = now[Calendar.MINUTE]
    date = parseDate("$hour:$minute")
    dateCompareOne = parseDate(compareStringOne)
    dateCompareTwo = parseDate(compareStringTwo)
    return dateCompareOne?.before(dateCompareTwo) == true
}

private fun parseDate(date: String): Date? {
    return try {
        inputParser.parse(date)
    } catch (e: ParseException) {
        Date(0)
    }
}

private const val DAY = 60000 * 60 * 24


fun getCompareDate(dateOne: Date?, dateTwo: Date?, monthName: String): String? {
    if (dateOne != null && dateTwo != null) {
        val calOne = Calendar.getInstance()
        val calTwo = Calendar.getInstance()
        calOne.time = dateOne
        calTwo.time = dateTwo
        if (calOne[Calendar.YEAR] == calTwo[Calendar.YEAR] && calOne[Calendar.MONTH] == calTwo[Calendar.MONTH]) {
            return null
        } else {
            return "$monthName ${calOne[Calendar.MONTH] + 1}/${calOne[Calendar.YEAR]}"
        }
    } else {
        return null
    }
}
