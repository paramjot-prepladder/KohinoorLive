package com.param.kohinoor.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.param.exercise.utils.NetworkHelper
import com.param.exercise.utils.ResourceState
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.ResponseProductListingItem
import com.param.kohinoor.pojo.product.createRequest.RequestCreateProduct
import com.param.kohinoor.pojo.singleProduct.ResponseSingleProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val mainRepository: ProductRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _newsSource =
        MutableStateFlow<ResourceState<List<LineItem>>>(ResourceState.Idle)
    val newsSource: StateFlow<ResourceState<List<LineItem>>>
        get() = _newsSource


    fun getProducts() {
        viewModelScope.launch {
            _newsSource.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getProducts().let {
                    _newsSource.value = it

                }
            } else _newsSource.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            _newsSource.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getCategories().let {
                    _newsSource.value = it

                }
            } else _newsSource.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    private val _singleProduct =
        MutableStateFlow<ResourceState<List<LineItem>>>(ResourceState.Loading)
    val singleProduct: StateFlow<ResourceState<List<LineItem>>>
        get() = _singleProduct

    fun getSingleProducts(id: String) {
        viewModelScope.launch {
            _singleProduct.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getSingleProducts(id).let {
                    _singleProduct.value = it

                }
            } else _singleProduct.value = ResourceState.Error(Exception("No Internet"))
        }
    }
    private val _createProduct =
        MutableStateFlow<ResourceState<LineItem>>(ResourceState.Idle)
    val createProduct: StateFlow<ResourceState<LineItem>>
        get() = _createProduct

    fun createProduct(id: RequestCreateProduct) {
        viewModelScope.launch {
            _createProduct.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.createProduct(id).let {
                    _createProduct.value = it

                }
            } else _createProduct.value = ResourceState.Error(Exception("No Internet"))
        }
    }
}