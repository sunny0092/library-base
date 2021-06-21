package net.ihaha.sunny.base.extention


/**
 * Author: Sunny
 * Version: 1.0.0
 * Date: 23/11/2020
 */

import com.orhanobut.logger.Logger
import com.squareup.moshi.*
import java.lang.reflect.Type


inline fun <reified T> T?.toJsonObject(): T? =
    ModelUtil.toJsonObject(this, T::class.java)

inline fun <reified T> T?.toJsonObjectList(): List<T>? =
    ModelUtil.toJsonObjectList(this, T::class.java)

inline fun <reified T> String?.fromJson(): T? =
    this?.let { ModelUtil.fromJson(this, T::class.java) }

inline fun <reified T> T?.toJson(): String =
    ModelUtil.toJson(this, T::class.java)

inline fun <reified T> Moshi.fromJson(json: String?): T? =
    json?.let { ModelUtil.fromJson(json, T::class.java) }

inline fun <reified T> Moshi.toJson(t: T?): String =
    ModelUtil.toJson(t, T::class.java)

inline fun <reified T> List<T>.listToJson(): String =
    ModelUtil.listToJson(this, T::class.java)

inline fun <reified T> String.jsonToList(): List<T>? = ModelUtil.jsonToList(this, T::class.java)

object ModelUtil {

    internal var moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(MoshiTypeAdapters())
        .add(NullToZeroLongAdapter())
        .build()

    inline fun <reified S, reified T> copyModel(source: S): T? {
        return fromJson(
            toJson(
                any = source,
                classOfT = S::class.java
            ), T::class.java
        )
    }

    fun <T> toJsonObject(any: T?, classOfT: Class<T>): T? {
        val json = moshi.adapter(classOfT).toJson(any)
        return fromJson(json, classOfT)
    }

    fun <T> toJsonObjectList(any: T?, classOfT: Class<T>): List<T>? {
        val json = moshi.adapter(classOfT).toJson(any)
        return jsonToList(json, classOfT)
    }

    fun <T> toJson(any: T?, classOfT: Class<T>): String {
        return moshi.adapter(classOfT).toJson(any)
    }

    fun <T> fromJson(json: String, classOfT: Class<T>): T? {
        return moshi.adapter(classOfT).lenient().fromJson(json)
    }

    fun <T> fromJson(json: String, typeOfT: Type): T? {
        return moshi.adapter<T>(typeOfT).fromJson(json)
    }

    fun <T> listToJson(list: List<T>?, classOfT: Class<T>): String {
        return try {
            val type = Types.newParameterizedType(List::class.java, classOfT)
            val adapter: JsonAdapter<List<T>> = moshi.adapter(type)
            return adapter.toJson(list)
        } catch (e: Exception) {
            Logger.e(e, "ERROR")
            ""
        } catch (e: JsonDataException) {
            Logger.e(e, "ERROR")
            ""
        }
    }

    fun <T> jsonToList(json: String, classOfT: Class<T>): List<T>? {
        return try {
            val type = Types.newParameterizedType(List::class.java, classOfT)
            val adapter = moshi.adapter<List<T>>(type)
            adapter.fromJson(json)
        } catch (e: Exception) {
            Logger.e(e, "ERROR", json)
            null
        } catch (e: JsonDataException) {
            Logger.e(e, "ERROR", json)
            null
        }
    }
}