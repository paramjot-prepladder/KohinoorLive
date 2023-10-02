package com.param.kohinoor.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.param.exercise.utils.NetworkHelper
import com.param.exercise.utils.ResourceState
import com.param.kohinoor.pojo.RequestAddBrand
import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.dpd.ResponseGetDpd
import com.param.kohinoor.pojo.dpd.createDpd.RequestCreateDpd
import com.param.kohinoor.pojo.order.RequestDeleteOrder
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
        ResourceState.Idle
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

    fun getOrder(orderId: Int) {
        viewModelScope.launch {
            _createOrders.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getOrder(orderId).let {
                    _createOrders.value = it

                }
            } else _createOrders.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    private val _getDpd = MutableStateFlow<ResourceState<ResponseGetDpd>>(
        ResourceState.Idle
    )
    val getDpd: StateFlow<ResourceState<ResponseGetDpd>>
        get() = _getDpd

    fun getDpd(request: Int) {
        viewModelScope.launch {
            _getDpd.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getDpd(request).let {
                    _getDpd.value = it

                }
            } else _getDpd.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    fun createDpd(request: RequestCreateDpd) {
        viewModelScope.launch {
            _getDpd.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.createDpd(request).let {
                    _getDpd.value = it

                }
            } else _getDpd.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    private val _createOrders = MutableStateFlow<ResourceState<ResponseOrderItem>>(
        ResourceState.Idle
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

    fun updateOrder(id: Int, request: RequestCreateOrder) {
        viewModelScope.launch {
            _createOrders.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.updateOrder(id, request).let {
                    _createOrders.value = it

                }
            } else _createOrders.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    fun deleteOrder(request: RequestDeleteOrder) {
        viewModelScope.launch {
            _createOrders.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.deleteOrder(request).let {
                    _createOrders.value = it

                }
            } else _createOrders.value = ResourceState.Error(Exception("No Internet"))
        }
    }
}