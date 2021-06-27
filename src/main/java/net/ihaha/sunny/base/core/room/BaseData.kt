package net.ihaha.sunny.base.core.room

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseData<T>(
    @Json(name = "data")
    val `data`: List<T>?,
    @Json(name = "message")
    val msg: String? = null,
    @Json(name = "code")
    val code: String? = null,
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "type")
    val type: String? = null,
    @Json(name = "pagination")
    val pagination: PaginationModel? = null
)
