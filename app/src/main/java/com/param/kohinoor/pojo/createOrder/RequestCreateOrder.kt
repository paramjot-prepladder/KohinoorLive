package com.param.kohinoor.pojo.createOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestCreateOrder(
    @Json(name = "billing")
    var billing: Billing? = null,
    @Json(name = "line_items")
    var lineItems: List<LineItem>? = null,
    @Json(name = "payment_method")
    var paymentMethod: String? = null,
    @Json(name = "payment_method_title")
    var paymentMethodTitle: String? = null,
    @Json(name = "set_paid")
    var setPaid: Boolean? = null,
    @Json(name = "shipping")
    var shipping: Billing? = null,
    @Json(name = "shipping_lines")
    var shippingLines: List<ShippingLine>? = null,
    @Json(name = "status")
    var status: String? = null
)