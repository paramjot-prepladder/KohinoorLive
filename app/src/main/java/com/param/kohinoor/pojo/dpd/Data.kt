package com.param.kohinoor.pojo.dpd


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "date")
    val date: String?,
    @Json(name = "home_url")
    val homeUrl: String?,
    @Json(name = "pdf_url")
    val pdfUrl: String?,
    @Json(name = "predict")
    val predict: String?,
    @Json(name = "tracking_url")
    val trackingUrl: String?
)