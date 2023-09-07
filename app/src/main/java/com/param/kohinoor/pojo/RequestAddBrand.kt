package com.param.kohinoor.pojo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestAddBrand(
    @Json(name = "product_brand_name")
    val productBrandName: String?,
    @Json(name = "product_id")
    val productId: String?
)