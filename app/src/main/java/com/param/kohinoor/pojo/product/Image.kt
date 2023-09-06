package com.param.kohinoor.pojo.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "alt")
    var alt: String? = null,
    @Json(name = "date_created")
    var dateCreated: String? = null,
    @Json(name = "date_created_gmt")
    var dateCreatedGmt: String? = null,
    @Json(name = "date_modified")
    var dateModified: String? = null,
    @Json(name = "date_modified_gmt")
    var dateModifiedGmt: String? = null,
    @Json(name = "id")
    var id: Int? = 0,
    @Json(name = "name")
    var name: String? = null,
    @Json(name = "src")
    val src: String?
)