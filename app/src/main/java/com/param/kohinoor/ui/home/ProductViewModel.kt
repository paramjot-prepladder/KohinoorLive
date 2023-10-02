package com.param.kohinoor.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.param.exercise.utils.NetworkHelper
import com.param.exercise.utils.ResourceState
import com.param.kohinoor.pojo.RequestAddBrand
import com.param.kohinoor.pojo.brand.ResponseBrand
import com.param.kohinoor.pojo.brand.TaxCategories
import com.param.kohinoor.pojo.gallery.ResponseGallery
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.ResponseProductListingItem
import com.param.kohinoor.pojo.product.createRequest.RequestCreateProduct
import com.param.kohinoor.pojo.product.createRequest.RequestUpdateProduct
import com.param.kohinoor.pojo.singleProduct.ResponseSingleProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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

    private val _getBrand =
        MutableStateFlow<ResourceState<ResponseBrand>>(ResourceState.Idle)
    val getBrand: StateFlow<ResourceState<ResponseBrand>>
        get() = _getBrand

    fun getBrand() {
        viewModelScope.launch {
            _getBrand.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getBrand().let {
                    _getBrand.value = it

                }
            } else _getBrand.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    private val _addBrand =
        MutableStateFlow<ResourceState<ResponseBrand>>(ResourceState.Idle)
    val addBrand: StateFlow<ResourceState<ResponseBrand>>
        get() = _addBrand

    fun addBrand(request: RequestAddBrand) {
        viewModelScope.launch {
            _addBrand.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.addBrand(request).let {
                    _addBrand.value = it

                }
            } else _addBrand.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    private val _singleProduct =
        MutableStateFlow<ResourceState<List<LineItem>>>(ResourceState.Idle)
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

    fun getSingleProduct(id: Int) {
        viewModelScope.launch {
            _singleProduct.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getSingleProduct(id).let {
                    _singleProduct.value = it

                }
            } else _singleProduct.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    private val _updateProduct =
        MutableStateFlow<ResourceState<LineItem>>(ResourceState.Idle)
    val updateProduct: StateFlow<ResourceState<LineItem>>
        get() = _updateProduct

    fun updateSingleProducts(id: Int, update: RequestUpdateProduct) {
        viewModelScope.launch {
            _updateProduct.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.updateSingleProducts(id, update).let {
                    _updateProduct.value = it

                }
            } else _updateProduct.value = ResourceState.Error(Exception("No Internet"))
        }
    }

    private val _getTaxClass =
        MutableStateFlow<ResourceState<List<TaxCategories>>>(ResourceState.Idle)
    val getTaxClass: StateFlow<ResourceState<List<TaxCategories>>>
        get() = _getTaxClass

    fun getTaxClass() {
        viewModelScope.launch {
            _getTaxClass.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getTaxClass().let {
                    _getTaxClass.value = it

                }
            } else _getTaxClass.value = ResourceState.Error(Exception("No Internet"))
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

    private val _addImage =
        MutableStateFlow<ResourceState<ResponseGallery>>(ResourceState.Idle)
    val addImage: StateFlow<ResourceState<ResponseGallery>>
        get() = _addImage

    fun addImage(
//        productId: String,
        parts: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _addImage.value = ResourceState.Loading
            if (networkHelper.isNetworkConnected()) {
                mainRepository.addImage(/*productId,*/ parts).let {
                    _addImage.value = it

                }
            } else _addImage.value = ResourceState.Error(Exception("No Internet"))
        }
    }
}