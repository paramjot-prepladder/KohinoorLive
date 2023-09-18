package com.param.kohinoor.ui.orderDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.param.exercise.utils.ResourceState
import com.param.exercise.utils.gone
import com.param.exercise.utils.setStatus
import com.param.exercise.utils.show
import com.param.kohinoor.databinding.FragmentOrderDetailBinding
import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.order.ResponseOrderItem
import com.param.kohinoor.ui.bottomsheet.CreateDpdBottomSheet
import com.param.kohinoor.ui.bottomsheet.UpdateAddressBottomSheet
import com.param.kohinoor.ui.bottomsheet.UpdateStatusBottomSheet
import com.param.kohinoor.ui.gallery.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentOrderDetailBinding? = null
    private val orderViewModel: OrderViewModel by viewModels()
    private val args: OrderDetailFragmentArgs by navArgs()
    private var downloadUrl: String? = ""

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

        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun setData(data: ResponseOrderItem) {
        binding?.apply {
            orderNo.text = "Order #${data.id}"
            date.text = data.dateCreated
            mobile.text = data.billing?.phone
            name.text = "${data.billing?.firstName} ${data.billing?.lastName}"
            statusText.setStatus(data.status, status)
            address.text =
                "${data.billing?.address1}, ${data.billing?.postcode} ${data.billing?.city}"
            data.lineItems
            val adapterOrder = OrderDetailAdapter(data.currencySymbol ?: "") {

            }

            recyclerView.apply {
                adapter = adapterOrder
            }
            adapterOrder.submitList(data.lineItems)
            var isParcelCreated = false
            data.metaData?.forEach {
                if (it?.key == "dpd_parcel_number") {
                    isParcelCreated = true
                    orderViewModel.getDpd(data.id ?: 0)
                }
            }
            if (!isParcelCreated) {
                binding?.createShippingLabel?.show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("datga", args.data.toString())
        val data = args.data
        viewLifecycleOwner.lifecycleScope.launch {
            orderViewModel.createOrders.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding?.progressBar?.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        binding?.progressBar?.gone()
                        setData(it.item)
                        Log.e("handdy", it.item.toString())
                    }

                    is ResourceState.Error -> {
                        binding?.progressBar?.gone()
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
                        Log.e("handdy", "error")
                    }

                    else -> {}
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            orderViewModel.getDpd.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding?.progressBar?.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        binding?.progressBar?.gone()
                        binding?.trackingId?.show()

                        downloadUrl = it.item.data?.pdfUrl
                        binding?.trackingId?.text = it.item.data?.trackingUrl
                        Log.e("handdy", it.item.toString())
                    }

                    is ResourceState.Error -> {
                        binding?.progressBar?.gone()
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
                        Log.e("handdy", "error")
                    }

                    else -> {}
                }
            }
        }
        binding?.apply {
            setData(data)
            createShippingLabel.setOnClickListener {
                CreateDpdBottomSheet {
                    orderViewModel.createDpd(it)
                }.show(parentFragmentManager, "createDpd")

            }
            trackingId.setOnClickListener {
                val format = "https://drive.google.com/viewerng/viewer?embedded=true&url=%s"
                val fullPath = String.format(Locale.ENGLISH, format, downloadUrl)
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                startActivity(browserIntent)
            }
            status.setOnClickListener {
                UpdateStatusBottomSheet { ls ->
                    orderViewModel.updateOrder(
                        data.id ?: 0, RequestCreateOrder(status = ls)
                    )

                }.show(parentFragmentManager, "statusSheet")
            }
            editCustomer.setOnClickListener {
                UpdateAddressBottomSheet(data.billing) { ls ->
                    orderViewModel.updateOrder(
                        data.id ?: 0, RequestCreateOrder(billing = ls)
                    )

                }.show(parentFragmentManager, "updateSheet")
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}