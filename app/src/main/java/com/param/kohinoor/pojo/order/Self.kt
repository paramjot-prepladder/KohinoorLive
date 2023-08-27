package com.param.kohinoor.pojo.order


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Self(
    @Json(name = "href")
    val href: String?
): Parcelable