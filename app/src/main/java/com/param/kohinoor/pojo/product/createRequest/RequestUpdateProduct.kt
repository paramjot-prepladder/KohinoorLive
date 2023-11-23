package com.param.kohinoor.pojo.product.createRequest


import com.param.kohinoor.pojo.product.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestUpdateProduct(
    @Json(name = "regular_price")
    var regularPrice: String? = null,
    @Json(name = "stock_quantity")
    var stockQuantity: String? = null,
    @Json(name = "categories")
    var categories: List<Category?>? = null,
    @Json(name = "description")
    var description: String? = null,
    @Json(name = "images")
    var images: List<Image?>? = null,
    @Json(name = "name")
    var name: String? = null,
    @Json(name = "status")
    var status: String? = null,
    @Json(name = "sale_price")
    var salePrice: String? = null,
    @Json(name = "short_description")
    var shortDescription: String? = null,
    @Json(name = "tax_class")
    var taxClass: String? = null,
    @Json(name = "sku")
    var sku: String? = null,
    @Json(name = "stock_status")
    var stockStatus: String? = null,
    @Json(name = "type")
    val type: String? = "simple"
)