package com.param.kohinoor.ui.home

import android.app.Application
import com.param.exercise.utils.ResourceState
import com.param.kohinoor.ApiInterface
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.ResponseProductListingItem
import com.param.kohinoor.pojo.product.createRequest.RequestCreateProduct
import com.param.kohinoor.pojo.singleProduct.ResponseSingleProduct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    val application: Application,
    private val apiInterface: ApiInterface
)  {
    suspend fun getProducts(): ResourceState<List<LineItem>> {
        return try {

            val response = apiInterface.getProducts(100)

            val result = response.body()
            if (response.isSuccessful && result != null) {
                ResourceState.Success(result)
            } else {
                ResourceState.Error(java.lang.Exception(response.message()))
            }
        } catch (e: Exception) {
            ResourceState.Error(java.lang.Exception(e.message ?: "An Error Occurred"))
        }
    }
    suspend fun getCategories(): ResourceState<List<LineItem>> {
        return try {

            val response = apiInterface.getCategories(100)

            val result = response.body()
            if (response.isSuccessful && result != null) {
                ResourceState.Success(result)
            } else {
                ResourceState.Error(java.lang.Exception(response.message()))
            }
        } catch (e: Exception) {
            ResourceState.Error(java.lang.Exception(e.message ?: "An Error Occurred"))
        }
    }
    suspend fun getSingleProducts(id:String): ResourceState<List<LineItem>> {
        return try {

            val response = apiInterface.getSingleProducts(id)

            val result = response.body()
            if (response.isSuccessful && result != null) {
                ResourceState.Success(result)
            } else {
                ResourceState.Error(java.lang.Exception(response.message()))
            }
        } catch (e: Exception) {
            ResourceState.Error(java.lang.Exception(e.message ?: "An Error Occurred"))
        }
    }
    suspend fun createProduct(request: RequestCreateProduct): ResourceState<LineItem> {
        return try {

            val response = apiInterface.createProducts(request)

            val result = response.body()
            if (response.isSuccessful && result != null) {
                ResourceState.Success(result)
            } else {
                ResourceState.Error(java.lang.Exception(response.message()))
            }
        } catch (e: Exception) {
            ResourceState.Error(java.lang.Exception(e.message ?: "An Error Occurred"))
        }
    }
}