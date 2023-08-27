package com.param.kohinoor.ui.gallery

import android.app.Application
import com.param.exercise.utils.ResourceState
import com.param.kohinoor.ApiInterface
import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.order.ResponseOrderItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    val application: Application,
    private val apiInterface: ApiInterface
) {
    suspend fun getOrders(): ResourceState<List<ResponseOrderItem>> {
        return try {

            val response = apiInterface.getOrders(100)

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
    suspend fun createOrder(request: RequestCreateOrder): ResourceState<ResponseOrderItem> {
        return try {

            val response = apiInterface.createOrders(request)

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