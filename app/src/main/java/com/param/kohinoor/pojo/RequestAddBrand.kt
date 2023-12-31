package com.param.kohinoor.pojo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestAddBrand(
    @Json(name = "parcel_content")
    val parcelContent: String?,
    @Json(name = "disable_order_completion")
    val disableOrderCompletion: String?
)