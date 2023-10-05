package com.param.kohinoor.ui.productDetails

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.param.exercise.utils.ResourceState
import com.param.exercise.utils.gone
import com.param.exercise.utils.hide
import com.param.exercise.utils.loadImg
import com.param.exercise.utils.show
import com.param.kohinoor.R
import com.param.kohinoor.databinding.FragmentProductDetailBinding
import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.createRequest.RequestUpdateProduct
import com.param.kohinoor.ui.bottomsheet.UpdateStatusBottomSheet
import com.param.kohinoor.ui.home.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val args: ProductDetailFragmentArgs by navArgs()
    private val viewModel: ProductViewModel by viewModels()
    var binding: FragmentProductDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding?.root
//        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    fun updateProductData(data: LineItem?) {
        binding?.firstName?.setValue("Name", "Name ${data?.name}")
        binding?.status?.setValue("Status", "Status ${data?.status}")
        binding?.address1?.setValue("Description", "Description " + data?.description ?: "")
        binding?.city?.setValue("Regular Price", "Regular Price " + data?.regularPrice.toString())
        binding?.salePrice?.setValue("Sale Price", "Sale Price " + data?.salePrice.toString())
        binding?.brandSpinner?.setValue("Brand", "Brand " + data?.salePrice.toString())
        binding?.taxSlab?.setValue("Tax", "Tax " + data?.taxClass.toString())
        binding?.address2?.setValue("Short Desc", "Short Desc " + data?.shortDescription ?: "")
        binding?.productImage?.loadImg(data?.images?.get(0)?.src)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = args.productDetail
        viewModel.updateSingleProducts(data?.id ?: 0, RequestUpdateProduct(status = data?.status))
        binding?.taxSlab?.gone()
        binding?.brandSpinner?.gone()
        updateProductData(data)
        binding?.status?.setOnClickListener {
            UpdateStatusBottomSheet(true) { ls ->
                viewModel.updateSingleProducts(data?.id ?: 0, RequestUpdateProduct(status = ls))
            }.show(parentFragmentManager, "statusSheet")
        }

        binding?.firstName?.setOnClickListener {
            UpdateProductDetailBottomSheet("Name") {
                viewModel.updateSingleProducts(data?.id ?: 0, RequestUpdateProduct(name = it))
            }.show(parentFragmentManager, "updateSheet")
        }
        binding?.address1?.setOnClickListener {
            UpdateProductDetailBottomSheet("Description") {
                viewModel.updateSingleProducts(
                    data?.id ?: 0,
                    RequestUpdateProduct(description = it)
                )

            }.show(parentFragmentManager, "updateSheet")
        }
        binding?.address2?.setOnClickListener {
            UpdateProductDetailBottomSheet("Short Desc") {
                viewModel.updateSingleProducts(
                    data?.id ?: 0,
                    RequestUpdateProduct(shortDescription = it)
                )

            }.show(parentFragmentManager, "updateSheet")
        }
        binding?.city?.setOnClickListener {
            UpdateProductDetailBottomSheet("Regular Price") {
                viewModel.updateSingleProducts(
                    data?.id ?: 0,
                    RequestUpdateProduct(regularPrice = it)
                )

            }.show(parentFragmentManager, "updateSheet")
        }
        binding?.salePrice?.setOnClickListener {
            UpdateProductDetailBottomSheet("Sale Price") {
                viewModel.updateSingleProducts(
                    data?.id ?: 0,
                    RequestUpdateProduct(salePrice = it)
                )

            }.show(parentFragmentManager, "updateSheet")
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateProduct.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding?.progressBar?.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        binding?.progressBar?.hide()
                        updateProductData(it.item)
                        Log.e("handdy", it.item.toString())
                    }

                    is ResourceState.Error -> {
                        binding?.progressBar?.hide()
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
                        Log.e("handdy", "error")
                    }

                    else -> {}
                }
            }
        }
    }

    fun TextView.setValue(title: String, value: String) {
        val a = SpannableString(value)
        a.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this.context,
                    R.color.black
                )
            ),
            0,
            title.length + 1,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        this.setText(a)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}