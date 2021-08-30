package net.ihaha.sunny.base.extention

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.EditText
import timber.log.Timber
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

private const val newPhoneNumberPattern = "(0[|3|5|7|8|9])+([0-9]{8})"
private const val textAndNumberPattern = "[^A-Za-z0-9]"
private const val pattern = "yyyy/MM/dd hh:mm"
private const val passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})"
private const val DATE_PATTERN =
    "(0?[1-9]|1[12][0-9]|3[01]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)"

fun String?.invalidPhoneNumber(): Boolean = this?.trim()?.let {
    newPhoneNumberPattern.toRegex().matches(it)
} ?: false

fun String?.invalidTextCharacterNotSpecial(): Boolean = this?.trim()?.let {
    textAndNumberPattern.toRegex().matches(it)
} ?: false

fun String?.invalidDate(): Boolean = this?.trim()?.let {
    DATE_PATTERN.toRegex().matches(it)
} ?: false

fun String?.invalidEmail(): Boolean =
    this?.trim()?.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() } ?: false

fun String?.invalidEmailToCallingUserAccounts(): Boolean =
    this?.trim()?.let { EMAIL_ADDRESS.matcher(it).matches() } ?: false

fun String?.invalidHardPassword(): Boolean =
    this?.trim()?.let { passwordPattern.toRegex().matches(it) } ?: false

fun String?.invalidNormalPassword(): Boolean = this?.isNotEmpty() ?: false

fun String?.invalidPasswordIncorrect(): Boolean =
    (this?.isNotEmpty()!! && this.length >= 6) ?: false

fun String?.invalidString(): Boolean = this?.isNotEmpty() ?: false


val EMAIL_ADDRESS = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+\\**]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
// endregion

fun String.generateName(): String {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy/HH:mm:ss")
        val localDateTime = LocalDateTime.now()
        return dateFormat.format(localDateTime)
    }
    return randomString()
}


fun randomString(): String {
    val array = ByteArray(7) // length is bounded by 7
    Random().nextBytes(array)
    return String(array, Charset.forName("UTF-8"))
}

//fun String.changeMonth(): String = if (this.length == 1) "0$this" else this

// "달, 일"을 가져와 1~9월이면 01~09 월로, 10~12 월이면 10~12월로 변경(일도 마찬가지)
fun String.plusZero(monthOfYear: Int, dayOfMonth: Int): String {
    var month = (monthOfYear + 1).toString()
    month = if (month.length == 1) "0$month" else month

    var day = dayOfMonth.toString()
    day = if (day.length == 1) "0$day" else day

    return "$this-$month-$day"
}

fun String.toSpanned(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(
    this,
    Html.FROM_HTML_MODE_LEGACY
) else @Suppress("DEPRECATION") Html.fromHtml(this)

//fun Long?.formatPrice(): String? {
//    val price = "-----"
//    try {
//        if (this != null && this != -1L) {
//            val format = DecimalFormat("#,###đ")
//            format.currency = Currency.getInstance(Locale.US);
//            return String.format("%s", format.format(this));
//        }
//        return price
//    } catch (e: Exception) {
//        return price
//        Timber.e(e)
//    }
//}

fun Double?.formatPrice(): String? {
    val price = "-----"
    try {
        if (this != null) {
            val format = DecimalFormat("#,###đ")
            format.roundingMode = RoundingMode.FLOOR
            return String.format("%s", format.format(this));
        }
        return price
    } catch (e: Exception) {
        return price
        Timber.e(e)
    }
}


var current = ""
val ddmmyyyy = "ddmmyyyy"
val cal = Calendar.getInstance()

fun validateBirthday(char: CharSequence?, view: EditText) {

    if (char.toString() != current) {
        var clean = char.toString().replace("[^\\d.]|\\.".toRegex(), "")
        val cleanCurrent = current.replace("[^\\d.]|\\.", "")

        val cleanLength = clean.length
        var select = cleanLength
        var index = 2
        while (index <= cleanLength && index < 6) {
            select++
            index += 2
        }
        //Fix for pressing delete next to a forward slash
        if (clean == cleanCurrent) select--

        if (clean.length < 8) {
            clean += ddmmyyyy.substring(clean.length)
        } else {
            var day = Integer.parseInt(clean.substring(0, 2))
            var mon = Integer.parseInt(clean.substring(2, 4))
            var year = Integer.parseInt(clean.substring(4, 8))

            mon = if (mon < 1) 1 else if (mon > 12) mon else mon
            cal.set(Calendar.MONTH, mon - 1)
            year = if (year < 1900) 1970 else if (year > 2100) year else year
            cal.set(Calendar.YEAR, year)
            day = if (day > cal.getActualMaximum(Calendar.DATE)) day else day
            clean = String.format("%02d%02d%02d", day, mon, year)
        }

        clean = String.format(
            "%s/%s/%s", clean.substring(0, 2),
            clean.substring(2, 4),
            clean.substring(4, 8)
        )

        select = if (select < 0) 0 else select
        current = clean
        view.setText(current)
        view.setSelection(if (select < current.count()) select else current.count())
    }
}
