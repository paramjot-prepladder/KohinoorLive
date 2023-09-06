package com.param.kohinoor.pojo.brand


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Products(
    @Json(name = "tax_categories_list")
    val taxCategoriesList: List<TaxCategories>?
)