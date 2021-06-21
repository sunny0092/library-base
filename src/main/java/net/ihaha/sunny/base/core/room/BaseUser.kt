package net.ihaha.sunny.base.core.room

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Date: 25/10/2020.
 * @author SANG.
 * @version 1.0.0.
 */
@JsonClass(generateAdapter = true)
data class BaseUser(
    @Json(name = "object")
    val `data`: Any?,
    @Json(name = "message")
    val msg: String? = null,
    @Json(name = "code")
    val code: Int? = null,
    @Json(name = "success")
    val success: Boolean = false,
    @Json(name = "type")
    val type: String? = null
)