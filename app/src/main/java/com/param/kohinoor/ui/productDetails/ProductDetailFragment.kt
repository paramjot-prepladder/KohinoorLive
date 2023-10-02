package com.param.kohinoor.ui.productDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.param.exercise.utils.loadImg
import com.param.exercise.utils.setHtml
import com.param.kohinoor.R
import com.param.kohinoor.databinding.FragmentOrderDetailBinding
import com.param.kohinoor.databinding.FragmentProductDetailBinding
import com.param.kohinoor.ui.orderDetail.OrderDetailFragmentArgs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val args: ProductDetailFragmentArgs by navArgs()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = args.productDetail
        binding?.firstName?.setHtml("Name ${data?.name}")
        binding?.address1?.setHtml(data?.description ?: "")
        binding?.city?.text = "Regular Price \n" + data?.regularPrice.toString()
        binding?.salePrice?.text = "Sale Price\n" + data?.salePrice.toString()
        binding?.brandSpinner?.text = data?.salePrice.toString()
        binding?.taxSlab?.text = data?.taxClass.toString()
        binding?.address2?.setHtml(data?.shortDescription ?: "")
        binding?.productImage?.loadImg(data?.images?.get(0)?.src)
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