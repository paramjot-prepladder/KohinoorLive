package com.param.kohinoor.ui.orderDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.param.exercise.utils.setStatus
import com.param.kohinoor.R
import com.param.kohinoor.databinding.FragmentOrderDetailBinding
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentOrderDetailBinding? = null
    private val args: OrderDetailFragmentArgs by navArgs()


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("datga", args.data.toString())
        val data = args.data
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