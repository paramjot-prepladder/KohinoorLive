package com.param.kohinoor.pojo.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestDeleteOrder(
    @Json(name = "item_id")
    val itemId: Int?,
    @Json(name = "order_id")
    val orderId: Int?
)