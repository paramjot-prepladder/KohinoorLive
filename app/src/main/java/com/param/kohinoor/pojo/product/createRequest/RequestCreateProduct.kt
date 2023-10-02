package com.param.kohinoor.pojo.product.createRequest


import com.param.kohinoor.pojo.product.Image
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestCreateProduct(
    @Json(name = "categories")
    val categories: List<Category?>?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "images")
    val images: List<Image?>?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "regular_price")
    val regularPrice: String?,
    @Json(name = "sale_price")
    val salePrice: String?,
    @Json(name = "short_description")
    val shortDescription: String?,
    @Json(name = "tax_class")
    val taxClass: String?,
    @Json(name = "sku")
    val sku: String?,
    @Json(name = "type")
    val type: String? = "simple"
)