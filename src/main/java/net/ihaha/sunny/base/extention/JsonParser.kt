package net.ihaha.sunny.base.extention

import androidx.annotation.Nullable
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.BufferedSource
import java.util.*
import kotlin.collections.LinkedHashMap


/**
 * Author: Sunny
 * Version: 1.0.0
 * Date: 24/11/2020
 */

object JsonParser {

    internal var moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(MoshiTypeAdapters())
        .add(NullToZeroLongAdapter())
        .build()

    fun toJson(o: Any?): String? {
        try {
            val adapter = moshi.adapter(Any::class.java).lenient().nullSafe()
            return adapter.toJson(o)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> fromJson(json: BufferedSource, cls: Class<T>): T? {
        try {
            val adapter = moshi.adapter(cls).lenient().nullSafe()
            return adapter.fromJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <T> fromJson(jsonString: String, cls: Class<T>): T? {
        try {
            val adapter = moshi.adapter(cls).lenient().nullSafe()
            return adapter.fromJson(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun fromJsonValue(`object`: Any?): Map<String, Any>? {
        try {
            val adapter: JsonAdapter<Map<String, Any>> = moshi.adapter(
                Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))

            return adapter.fromJsonValue(`object`)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return null
    }
}

class MoshiTypeAdapters {
    @ToJson
    fun linkedListToJson(list: LinkedList<Any>): List<Any> = list

    @FromJson
    fun linkedListFromJson(list: List<Any>): LinkedList<Any> = LinkedList(list)

    @ToJson
    fun linkedHashMapToJson(map: LinkedHashMap<String, Any>): Map<String, Any> = map

    @FromJson
    fun linkedHashMapFromJson(map: Map<String, Any>): LinkedHashMap<String, Any> = LinkedHashMap(map)
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NullToZeroLong
class NullToZeroLongAdapter {

    @ToJson
    fun toJson(@NullToZeroLong value: Long): Long { return value }

    @FromJson
    @NullToZeroLong
    fun fromJson(@Nullable data: Long?): Long { return data ?: 0 }
}