package net.ihaha.sunny.base.settings.session

import android.app.backup.BackupManager
import android.content.Context
import android.content.SharedPreferences
//import com.squareup.moshi.JsonAdapter
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.adapter


/**
 * Created by SangNguyen on 9/29/2019.
 * Version 1.0
 */

class SharePrefsImpl(context: Context) : SharePrefsApi, SharedPreferences.OnSharedPreferenceChangeListener {
    var sharePreferences: SharedPreferences? = null
    var mBackupManager: BackupManager = BackupManager(context)
    var mFileName = "preferences"

    init {
        sharePreferences = context.getSharedPreferences(mFileName, Context.MODE_PRIVATE)
        sharePreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        mBackupManager.dataChanged()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String, clazz: Class<T>): T? {
        when (clazz) {
            String::class.java -> return sharePreferences?.getString(key, "") as T
            Boolean::class.java -> return sharePreferences?.getBoolean(key, true) as T
            Float::class.java -> return sharePreferences?.getFloat(key, 0.0F) as T
            Int::class.java -> return sharePreferences?.getInt(key, 0) as T
            Long::class.java -> return sharePreferences?.getLong(key, 0L) as T
        }
        return null
    }

    override fun <T> put(key: String, data: T) {
        val editor = sharePreferences?.edit()
        when (data) {
            is String -> editor?.putString(key, data)
            is Boolean -> editor?.putBoolean(key, data)
            is Float -> editor?.putFloat(key, data)
            is Int -> editor?.putInt(key, data)
            is Long -> editor?.putLong(key, data)
        }
        editor?.apply()
    }

    override fun delete(key: String) {
        val editor = sharePreferences?.edit()
        editor?.remove(key)
        editor?.apply()
    }

    override fun clear() {
        sharePreferences?.edit()?.clear()?.apply()
    }


//    inline fun <reified T> saveObject(key: String, data: T) {
//        val moshi = Moshi.Builder().build()
//        val jsonAdapter = moshi.adapter(T::class.java)
//        val json = jsonAdapter.toJson(data)
//        sharePreferences?.edit()?.putString(key, moshi.toString())?.apply()
//    }
//
//    fun <T> getObject(key: String, clazz: Class<T>, defaultValue: Any): T? {
//        var json = sharePreferences?.getString(key, "")
//        if (json != null && json.isEmpty()) {
//            saveObject(key, defaultValue)
//            json = sharePreferences?.getString(key, null)
//        }
//        val moshi = Moshi.Builder().build()
//        val jsonAdapter: JsonAdapter<T> = moshi.adapter<T>(clazz::class.java)
//        return jsonAdapter.fromJson(json)
//    }
}


