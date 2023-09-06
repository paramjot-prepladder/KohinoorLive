package com.param.kohinoor.pojo.brand


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "products")
    val products: Products?
)