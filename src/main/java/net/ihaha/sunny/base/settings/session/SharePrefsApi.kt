package net.ihaha.sunny.base.settings.session

/**
 * Created by SangNguyen on 9/29/2019.
 * Version 1.0
 */

interface SharePrefsApi {

    fun <T> get(key: String, clazz: Class<T>): T?

    fun <T> put(key: String, data: T)

    fun delete(key: String)

    fun clear()
}
