package com.param.kohinoor

import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.ResponseProductListingItem
import com.param.kohinoor.pojo.order.ResponseOrderItem
import com.param.kohinoor.pojo.product.createRequest.RequestCreateProduct
import com.param.kohinoor.pojo.singleProduct.ResponseSingleProduct
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("products")
    suspend fun getProducts(@Query("per_page") perPage: Int): Response<List<LineItem>>

    @GET("products/categories")
    suspend fun getCategories(@Query("per_page") perPage: Int): Response<List<LineItem>>

    @GET("products")
    suspend fun getSingleProducts(@Query("search") id: String): Response<List<LineItem>>

    @GET("orders")
    suspend fun getOrders(@Query("per_page") perPage: Int): Response<List<ResponseOrderItem>>

    @POST("orders")
    suspend fun createOrders(@Body request: RequestCreateOrder): Response<ResponseOrderItem>

    @POST("products")
    suspend fun createProducts(@Body request: RequestCreateProduct): Response<LineItem>

}