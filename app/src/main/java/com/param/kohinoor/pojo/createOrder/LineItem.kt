package com.param.kohinoor.pojo.createOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LineItem(
    @Json(name = "product_id")
    val productId: Int?,
    @Json(name = "quantity")
    val quantity: Int?,
    @Json(name = "variation_id")
    var variationId: Int? = null
)