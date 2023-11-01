package com.param.kohinoor.ui.orderDetail

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.param.exercise.utils.ResourceState
import com.param.exercise.utils.gone
import com.param.exercise.utils.setStatus
import com.param.exercise.utils.show
import com.param.kohinoor.R
import com.param.kohinoor.databinding.DialogProductListingBinding
import com.param.kohinoor.databinding.FragmentOrderDetailBinding
import com.param.kohinoor.pojo.createOrder.RequestCreateOrder
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.order.RequestDeleteOrder
import com.param.kohinoor.pojo.order.ResponseOrderItem
import com.param.kohinoor.ui.bottomsheet.CreateDpdBottomSheet
import com.param.kohinoor.ui.bottomsheet.UpdateAddressBottomSheet
import com.param.kohinoor.ui.bottomsheet.UpdateStatusBottomSheet
import com.param.kohinoor.ui.gallery.OrderViewModel
import com.param.kohinoor.ui.home.ProductAdapter
import com.param.kohinoor.ui.home.ProductViewModel
import com.param.kohinoor.utils.BottomSheetDialog
import com.param.kohinoor.utils.RecyclerTouchListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentOrderDetailBinding? = null
    private val orderViewModel: OrderViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val args: OrderDetailFragmentArgs by navArgs()
    private var downloadUrl: String? = ""
    private var viewDpdPdf: String? = ""
    var adapterOrder: OrderDetailAdapter? = null
    var bottomSheet: BottomSheetDialog? = null
    var dataToReturn: LineItem? = null
    val list: MutableList<LineItem> = arrayListOf()
    private var STORAGE_PERMISSION_REQUEST = 1

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
            val touchListener = RecyclerTouchListener(activity, recyclerView)
            touchListener.setClickable(object : RecyclerTouchListener.OnRowClickListener {
                override fun onRowClicked(position: Int) {

                }

                override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
            })
                .setSwipeOptionViews(R.id.delete)
                .setSwipeable(
                    R.id.rowFG, R.id.rowBG
                ) { viewID, position ->
                    when (viewID) {
                        R.id.delete -> {
                            val da = adapterOrder?.differ?.currentList?.get(position)
                            orderViewModel.deleteOrder(
                                RequestDeleteOrder(da?.id, data.id)
                            )
                        }


                    }
                }
            recyclerView.addOnItemTouchListener(touchListener)

            adapterOrder = OrderDetailAdapter(data.currencySymbol ?: "") {

            }

            recyclerView.apply {
                adapter = adapterOrder
            }
            adapterOrder?.submitList(data.lineItems)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("datga", args.data.toString())
        val data = args.data
        viewLifecycleOwner.lifecycleScope.launch {
            productViewModel.singleProduct.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding?.progressBar?.show()
//                        binding.progressBar.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        showCustomDialog(it.item) { data ->
                            bottomSheet?.product?.setText(data.name)
                            dataToReturn = data
//                                product.setText(data.name.toString())
                        }
                        binding?.progressBar?.gone()
//                        binding.progressBar.hide()
//                        adapterProductListing?.submitList(it.item)
                        Log.e("handdy", it.item.toString())
                    }

                    is ResourceState.Error -> {
                        binding?.progressBar?.gone()
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG)
                            .show()
                        Log.e("handdy", "error")
                    }

                    else -> {}
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            orderViewModel.createOrders.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding?.progressBar?.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        binding?.progressBar?.gone()
                        if (it.item.billing == null) {
                            orderViewModel.getOrder(data.id ?: 0)
                        } else {
                            setData(it.item)
                        }
                        Log.e("handdy", it.item.toString())
                    }

                    is ResourceState.Error -> {
                        binding?.progressBar?.gone()
                        orderViewModel.getOrder(data.id ?: 0)
//                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
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
                        binding?.createShippingLabel?.gone()
                        binding?.trackingId?.show()

                        downloadUrl = it.item.data?.pdfUrl
                        viewDpdPdf = it.item.data?.viewDpdPdf
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
        bottomSheet = BottomSheetDialog(productViewModel) {
            if (dataToReturn == null) {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                return@BottomSheetDialog
            }
//            dataToReturn?.quantity = it.toInt()
//            if (dataToReturn != null) {
//                list.add(dataToReturn!!)
//            }
//            adapterOrder?.submitList(ArrayList(list))

            val lineItem = arrayListOf<com.param.kohinoor.pojo.createOrder.LineItem>()
//            adapterOrder?.differ?.currentList?.forEach {
            lineItem.add(
                com.param.kohinoor.pojo.createOrder.LineItem(
                    dataToReturn?.id,
                    dataToReturn?.quantity
                )
            )
//            }
            orderViewModel.updateOrder(data.id ?: 0, RequestCreateOrder(lineItems = lineItem))
        }
        binding?.apply {
            addProduct.setOnClickListener {
                bottomSheet?.product?.setText("")
                bottomSheet?.quantity?.setText("")

                bottomSheet?.show(parentFragmentManager, "ModalBottomSheet")
            }
            setData(data)
            createShippingLabel.setOnClickListener {
                CreateDpdBottomSheet(data.id) {
                    orderViewModel.createDpd(it)
                }.show(parentFragmentManager, "createDpd")

            }
            viewDpd.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(viewDpdPdf)
                startActivity(i)
            }
            trackingId.setOnClickListener {
                if (shouldAskPermission()) {
                    requireActivity().requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_REQUEST
                    )
                } else {
                    downloadFile(downloadUrl, "Dpd")
//            convertHtmlToPdf()
                }
//                val format = "https://drive.google.com/viewerng/viewer?embedded=true&url=%s"
//                val fullPath = String.format(Locale.ENGLISH, format, downloadUrl)
//                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
//                startActivity(browserIntent)
            }
            viewInvoice.setOnClickListener {
                val url = "https://kohinoormunich.de/view-pdf/?order-id-pdf=${args.data.id}"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            downloadInvoice.setOnClickListener {
                if (shouldAskPermission()) {
                    requireActivity().requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_REQUEST
                    )
                } else {
                    downloadFile(
                        "https://kohinoormunich.de/koh-app/uploads/wpo_wcpdf_7062b86d6d011d8523305a4b258c488c/attachments/invoice-${args.data.id}.pdf",
                        "Invoice"
                    )
                }

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
                        data.id ?: 0, RequestCreateOrder(billing = ls, shipping = ls)
                    )

                }.show(parentFragmentManager, "updateSheet")
            }
        }
    }

    private fun downloadFile(data: String?, type: String) {
        if (data?.isNotEmpty() == true) {
            val request = DownloadManager.Request(Uri.parse(data))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.addRequestHeader("Accept", "application/pdf")
            request.setTitle("Downloading $type for " + args.data.id)
//            (requireActivity() as MainActivity).setSnackbar(
//                binding?.root,
//                "Downloading $subjectName" + "_" + "$name.pdf"
//            )
            /** request.setDestinationInExternalFilesDir(
            context, Environment.DIRECTORY_DOWNLOADS, "$subjectName" + "_" + "$name.pdf"
            )
             **/

            /** val downloadManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
             **/
            Toast.makeText(activity, "Download Started", Toast.LENGTH_LONG).show()

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.addRequestHeader("Accept", "application/pdf")
            request.setTitle("$type" + "_" + "${args.data.id}.pdf")

            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, "$type" + "_" + "${args.data.id}.pdf"
            )
            val downloadManager =
                activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }
    }

    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        //changed requestCode value in below method so that it would not call parent class method as we are handling everything here
        requireActivity().onRequestPermissionsResult(0, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                convertHtmlToPdf()
                downloadFile(downloadUrl, "Kohinoor")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Storage permission denied, enabled it from settings",
                    Toast.LENGTH_SHORT
                ).show()
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