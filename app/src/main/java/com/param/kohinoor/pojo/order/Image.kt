package com.param.kohinoor.pojo.order


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "id")
    val id: String?,
    @Json(name = "src")
    val src: String?
): Parcelable