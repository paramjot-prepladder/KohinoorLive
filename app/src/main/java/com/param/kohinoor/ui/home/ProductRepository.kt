package com.param.kohinoor.ui.home

import android.app.Application
import com.param.exercise.utils.ResourceState
import com.param.kohinoor.ApiInterface
import com.param.kohinoor.pojo.RequestAddBrand
import com.param.kohinoor.pojo.brand.ResponseBrand
import com.param.kohinoor.pojo.brand.TaxCategories
import com.param.kohinoor.pojo.gallery.ResponseGallery
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.ResponseProductListingItem
import com.param.kohinoor.pojo.product.createRequest.RequestCreateProduct
import com.param.kohinoor.pojo.product.createRequest.RequestUpdateProduct
import com.param.kohinoor.pojo.singleProduct.ResponseSingleProduct
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    val application: Application,
    private val apiInterface: ApiInterface
) {
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

    suspend fun getBrand(): ResourceState<ResponseBrand> {
        return try {

            val response = apiInterface.getBrand()

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

    suspend fun addBrand(request: RequestAddBrand): ResourceState<ResponseBrand> {
        return try {

            val response = apiInterface.addBrand(request)

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

    suspend fun getSingleProducts(id: String): ResourceState<List<LineItem>> {
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

    suspend fun updateSingleProducts(
        id: Int,
        body: RequestUpdateProduct
    ): ResourceState<LineItem> {
        return try {

            val response = apiInterface.updateSingleProducts(id, body)

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

    suspend fun getTaxClass(): ResourceState<List<TaxCategories>> {
        return try {

            val response = apiInterface.getTaxClass()

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

    suspend fun addImage(
//        productId: String,
        parts: MultipartBody.Part
    ): ResourceState<ResponseGallery> {
        return try {

            val response =
                apiInterface.addImage(/*productId.toRequestBody(MultipartBody.FORM),*/ parts)

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