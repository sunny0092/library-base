package net.ihaha.sunny.base.settings.services

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager


/**
 * Date: 25/10/2020.
 * @author SANG.
 * @version 1.0.0.
 */
class DeviceService {
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context?) : String{
        val telephonyManager = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            telephonyManager.deviceId
        }
    }
}