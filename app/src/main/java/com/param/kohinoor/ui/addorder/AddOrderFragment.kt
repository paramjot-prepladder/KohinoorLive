package com.param.kohinoor.ui.addorder

import android.R.attr.country
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.param.exercise.utils.ResourceState
import com.param.exercise.utils.gone
import com.param.exercise.utils.hide
import com.param.exercise.utils.show
import com.param.kohinoor.R
import com.param.kohinoor.databinding.DialogProductListingBinding
import com.param.kohinoor.databinding.FragmentAddOrderBinding
import com.param.kohinoor.pojo.createOrder.Billing
import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.createOrder.ShippingLine
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.ui.gallery.OrderViewModel
import com.param.kohinoor.ui.home.ProductAdapter
import com.param.kohinoor.ui.home.ProductViewModel
import com.param.kohinoor.ui.orderDetail.OrderDetailAdapter
import com.param.kohinoor.utils.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class AddOrderFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentAddOrderBinding? = null
    private val viewModel: ProductViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    var orderDetailAdapter: OrderDetailAdapter? = null
    val list: MutableList<LineItem> = arrayListOf()
    var bottomSheet: BottomSheetDialog? = null
    var dataToReturn: LineItem? = null

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
//        return inflater.inflate(R.layout.fragment_add_order, container, false)
        binding = FragmentAddOrderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.state,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding?.state?.adapter = adapter
        }
        binding?.state?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(activity, "" + position, Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        orderDetailAdapter = OrderDetailAdapter("") {

        }
        binding?.addProductRecycler?.adapter = orderDetailAdapter
        bottomSheet = BottomSheetDialog(viewModel) {
            if (dataToReturn==null){
                Toast.makeText(activity,"Something went wrong",Toast.LENGTH_LONG).show()
                return@BottomSheetDialog
            }
            dataToReturn?.quantity = it.toInt()
            if (dataToReturn != null) {
                list.add(dataToReturn!!)
            }
            orderDetailAdapter?.submitList(ArrayList(list))
        }
        binding?.addProduct?.setOnClickListener {
            bottomSheet?.product?.setText("")
            bottomSheet?.quantity?.setText("")
            dataToReturn = null
            bottomSheet?.show(parentFragmentManager, "ModalBottomSheet")
        }
        binding?.addOrder?.setOnClickListener {
            binding?.apply {

                val billing = Billing(
                    address1 = address1.text.toString(),
                    address2 = address2.text.toString(),
                    city = city.text.toString(),
                    country = country.hint.toString(),
                    email = email.text.toString(),
                    firstName = firstName.text.toString(),
                    lastName = lastName.text.toString(),
                    phone = phone.text.toString(),
                    postcode = zipcode.text.toString(),
                    state = "" //TODO set state and validation
                )
                val lineItem = arrayListOf<com.param.kohinoor.pojo.createOrder.LineItem>()
                orderDetailAdapter?.differ?.currentList?.forEach {
                    lineItem.add(
                        com.param.kohinoor.pojo.createOrder.LineItem(
                            it.id,
                            it.quantity,
                            it.variationId
                        )
                    )
                }
                val shippingLine =
                    arrayOf(ShippingLine("flat_rate", "Flat rate Delivery Cost", "4.90"))
                val request = RequestCreateOrder(
                    billing = billing,
                    lineItems = lineItem,
                    paymentMethod = "cod",
                    paymentMethodTitle = "Cash on delivery",
                    setPaid = false,
                    shipping = billing,
                    shippingLines = shippingLine.toList()
                )
                orderViewModel.createOrders(request)
                Log.e("error", Gson().toJson(request))
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.singleProduct.collect {
                    when (it) {
                        is ResourceState.Loading -> {
                            showProgress()
//                        binding.progressBar.show()
                            Log.e("handdy", "Loadong")
                        }

                        is ResourceState.Success -> {
                            showCustomDialog(it.item) { data ->
                                bottomSheet?.product?.setText(data.name)
                                dataToReturn = data
//                                product.setText(data.name.toString())
                            }
                            hideProgress()
//                        binding.progressBar.hide()
//                        adapterProductListing?.submitList(it.item)
                            Log.e("handdy", it.item.toString())
                        }

                        is ResourceState.Error -> {
                            hideProgress()
                            Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG)
                                .show()
                            Log.e("handdy", "error")
                        }

                        else -> {}
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            orderViewModel.createOrders.collect {
                when (it) {
                    is ResourceState.Loading -> {
//                        showProgress()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        hideProgress()
                        Log.e("handdy", it.item.toString())
                    }

                    is ResourceState.Error -> {
                        hideProgress()
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
                        Log.e("handdy", "error")

                    }

                    else -> {}
                }
            }
        }
    }

    private fun showCustomDialog(data: List<LineItem>, listener: (LineItem) -> Unit) {
        val dialogBinding: DialogProductListingBinding =
            DialogProductListingBinding.inflate(layoutInflater)

        val customDialog = AlertDialog.Builder(requireActivity(), 0).create()

        customDialog.apply {
            setView(dialogBinding?.root)
            setCancelable(true)
        }.show()
        val adapterProduct = ProductAdapter() {
            listener(it)
            customDialog.dismiss()
        }
        dialogBinding.recyclerView.apply {
            adapter = adapterProduct
        }
        adapterProduct.submitList(data)
    }

    fun showProgress() {
        binding?.progress?.show()
    }

    fun hideProgress() {
        binding?.progress?.gone()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}