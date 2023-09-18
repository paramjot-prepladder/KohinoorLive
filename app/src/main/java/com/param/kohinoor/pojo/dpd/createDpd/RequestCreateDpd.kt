package com.param.kohinoor.pojo.dpd.createDpd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestCreateDpd(
    @Json(name = "parcel_content")
    val parcelContent: String?,
    @Json(name = "disable_order_completion")
    val disableOrderCompletion: Boolean?
) {
}