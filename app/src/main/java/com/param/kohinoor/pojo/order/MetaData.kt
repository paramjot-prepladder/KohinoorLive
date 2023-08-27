package com.param.kohinoor.pojo.order


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MetaData(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "key")
    val key: String?
): Parcelable