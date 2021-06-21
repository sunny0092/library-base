package net.ihaha.sunny.base.core.room

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginationModel(
    @Json(name = "current_page")
    var currentPage: Int? = 1,
    @Json(name = "next_page")
    val nextPage: String? = "",
    @Json(name = "per_page")
    val perPage: Int? = 10,
    @Json(name = "previous_page")
    val previousPage: String? = "",
    @Json(name = "total_count")
    val totalCount: Int? = 0,
    @Json(name = "total_page")
    val totalPage: Int? = 0
)
