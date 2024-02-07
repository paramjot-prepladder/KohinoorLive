package com.param.kohinoor.pojo.order


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class LineItem(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "image")
    val image: Image?,
    @Json(name = "images")
    val images: List<com.param.kohinoor.pojo.singleProduct.Image?>?,
//    @Json(name = "meta_data")
//    val metaData: List<Any?>?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "short_description")
    val shortDescription: String?,
//    @Json(name = "parent_name")
//    val parentName: Any?,
    @Json(name = "price")
    val price: String?,
    @Json(name = "regular_price")
    val regularPrice: String?,
    @Json(name = "sale_price")
    var salePrice: String? = null,
    @Json(name = "status")
    var status: String? = null,
    @Json(name = "product_id")
    val productId: Int?,
    @Json(name = "quantity")
    var quantity: Int?,
    @Json(name = "sku")
    val sku: String?,
    @Json(name = "subtotal")
    val subtotal: String?,
    @Json(name = "subtotal_tax")
    val subtotalTax: String?,
    @Json(name = "tax_class")
    val taxClass: String?,
//    @Json(name = "taxes")
//    val taxes: List<Any?>?,
    @Json(name = "total")
    val total: String?,
    @Json(name = "total_tax")
    val totalTax: String?,
    @Json(name = "variation_id")
    val variationId: Int?,
    var isSelected: Boolean = false
) : Parcelable