package net.ihaha.sunny.base.core.room

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseObject<T>(
    @Json(name = "object")
    val `data`: T?,
    @Json(name = "message")
    val msg: String? = null,
    @Json(name = "code")
    val code: String? = null,
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "type")
    val type: String? = null
)
