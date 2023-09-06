package com.param.kohinoor.pojo.gallery


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseGallery(
    @Json(name = "data")
    val `data`: List<String>,
    @Json(name = "msg")
    val msg: String?,
    @Json(name = "success")
    val success: Boolean?
)