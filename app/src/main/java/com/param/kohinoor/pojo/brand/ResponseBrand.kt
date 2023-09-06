package com.param.kohinoor.pojo.brand


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseBrand(
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "msg")
    val msg: String?,
    @Json(name = "success")
    val success: Boolean?
)