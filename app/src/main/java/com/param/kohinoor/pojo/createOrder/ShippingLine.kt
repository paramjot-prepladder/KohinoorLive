package com.param.kohinoor.pojo.createOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShippingLine(
    @Json(name = "method_id")
    val methodId: String?,
    @Json(name = "method_title")
    val methodTitle: String?,
    @Json(name = "total")
    val total: String?
)