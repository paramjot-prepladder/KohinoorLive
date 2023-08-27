package com.param.kohinoor.pojo.createOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestCreateOrder(
    @Json(name = "billing")
    val billing: Billing?,
    @Json(name = "line_items")
    val lineItems: List<LineItem>?,
    @Json(name = "payment_method")
    val paymentMethod: String?,
    @Json(name = "payment_method_title")
    val paymentMethodTitle: String?,
    @Json(name = "set_paid")
    val setPaid: Boolean?,
    @Json(name = "shipping")
    val shipping: Billing?,
    @Json(name = "shipping_lines")
    val shippingLines: List<ShippingLine>?
)