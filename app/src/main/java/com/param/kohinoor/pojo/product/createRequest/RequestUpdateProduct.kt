package com.param.kohinoor.pojo.product.createRequest


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestUpdateProduct(
    @Json(name = "regular_price")
    var regularPrice: String? = null,
    @Json(name = "stock_quantity")
    var stockQuantity: String? = null
)