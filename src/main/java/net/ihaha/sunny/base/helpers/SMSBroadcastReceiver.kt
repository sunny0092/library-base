package net.ihaha.sunny.base.helpers
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Matcher
import java.util.regex.Pattern

class SMSBroadcastReceiver: BroadcastReceiver() {

    private var onCodeReceive: ((String) -> Unit)? = null
    private var onTimeOutExpired: (() -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent) {
        if(SmsRetriever.SMS_RETRIEVED_ACTION == intent.action){
            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
            when (smsRetrieverStatus.statusCode){
                CommonStatusCodes.SUCCESS -> {
                    Toast.makeText(context, extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String, Toast.LENGTH_LONG).show()
                    val sms = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    val pattern: Pattern = Pattern.compile("(\\d{6})")
                    val matcher: Matcher = pattern.matcher(sms)
                    var number = ""
                    if (matcher.find()) {
                        number = matcher.group(0) ?: "000000"
                    }
                    onCodeReceive?.invoke(number.trim())
                }
                CommonStatusCodes.TIMEOUT -> onTimeOutExpired?.invoke()
            }
        }
    }

    fun addOnCodeReceiveListener(onCodeReceive: (String) -> Unit){
        this.onCodeReceive = onCodeReceive
    }

   fun addOnTimeOutExpiredListener(onTimeOutExpired: () -> Unit){
       this.onTimeOutExpired = onTimeOutExpired
   }

}