package com.param.kohinoor

import com.param.kohinoor.pojo.brand.ResponseBrand
import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.gallery.ResponseGallery
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.ResponseProductListingItem
import com.param.kohinoor.pojo.order.ResponseOrderItem
import com.param.kohinoor.pojo.product.createRequest.RequestCreateProduct
import com.param.kohinoor.pojo.singleProduct.ResponseSingleProduct
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("wc/v3/products")
    suspend fun getProducts(@Query("per_page") perPage: Int): Response<List<LineItem>>

    @GET("wc/v3/products/categories")
    suspend fun getCategories(@Query("per_page") perPage: Int): Response<List<LineItem>>

    @GET("koh-app/wc/v3/category/brands/view")
    suspend fun getBrand(): Response<ResponseBrand>

    @GET("wc/v3/products")
    suspend fun getSingleProducts(@Query("search") id: String): Response<List<LineItem>>

    @GET("wc/v3/orders")
    suspend fun getOrders(@Query("per_page") perPage: Int): Response<List<ResponseOrderItem>>

    @POST("wc/v3/orders")
    suspend fun createOrders(@Body request: RequestCreateOrder): Response<ResponseOrderItem>

    @POST("wc/v3/products")
    suspend fun createProducts(@Body request: RequestCreateProduct): Response<LineItem>

    @Multipart
    @POST("koh-app/wc/v3/product/gallery")
    suspend fun addImage(
//        @Part("product_id") product_id: RequestBody?,
        @Part files: MultipartBody.Part
    ): Response<ResponseGallery>
}