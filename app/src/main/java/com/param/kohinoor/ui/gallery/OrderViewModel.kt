package com.param.kohinoor.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.param.exercise.utils.NetworkHelper
import com.param.exercise.utils.ResourceState
import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.order.ResponseOrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val mainRepository: OrderRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _newsSource = MutableStateFlow<ResourceState<List<ResponseOrderItem>>>(
        ResourceState.Loading
    )
    val orders: StateFlow<ResourceState<List<ResponseOrderItem>>>
        get() = _newsSource

    fun getOrders() {
        viewModelScope.launch {
            _newsSource.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getOrders().let {
                    _newsSource.value = it

                }
            } else _newsSource.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    private val _createOrders = MutableStateFlow<ResourceState<ResponseOrderItem>>(
        ResourceState.Loading
    )
    val createOrders: StateFlow<ResourceState<ResponseOrderItem>>
        get() = _createOrders

    fun createOrders(request: RequestCreateOrder) {
        viewModelScope.launch {
            _createOrders.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.createOrder(request).let {
                    _createOrders.value = it

                }
            } else _createOrders.value = ResourceState.Error(Exception("No Internet"))
        }
    }
}