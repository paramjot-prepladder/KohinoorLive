package com.param.kohinoor.ui.slideshow

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.anilokcun.uwmediapicker.model.UwMediaPickerMediaModel
import com.param.exercise.utils.ResourceState
import com.param.exercise.utils.gone
import com.param.exercise.utils.hide
import com.param.exercise.utils.loadImg
import com.param.exercise.utils.show
import com.param.kohinoor.R
import com.param.kohinoor.databinding.FragmentSlideshowBinding
import com.param.kohinoor.pojo.RequestAddBrand
import com.param.kohinoor.pojo.brand.TaxCategories
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.Image
import com.param.kohinoor.pojo.product.createRequest.Category
import com.param.kohinoor.pojo.product.createRequest.RequestCreateProduct
import com.param.kohinoor.ui.home.ProductViewModel
import com.param.kohinoor.utils.ProgressRequestBody
import com.starry.file_utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File

@AndroidEntryPoint
class SlideshowFragment : Fragment(), ProgressRequestBody.UploadCallbacks {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    var categoryAdapter: CategoryAdapter? = null
    var list: List<LineItem> = arrayListOf()
    var parts: ArrayList<MultipartBody.Part> = ArrayList()
    var productImagePath = ""
    val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                openPicker()
            }
        }
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {

            val a = activity?.let { FileUtils(it).getPath(uri) }
            binding.productImage.loadImg(a)
            onMediaSelected(a)
            Log.d("PhotoPicker", "Selected URI: $uri $a")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    val brandHashMap = mutableMapOf<String?, String?>()
    var selectedTaxSlab = ""
    var selectedBrand = ""
    private var listTaxClass: ArrayList<TaxCategories> = arrayListOf()

    //    val hashTaxSlab = mapOf<String, String>(
//        "Standard rate" to "standard",
//        "Greater reduced rate" to "greater-reduced-rate",
//        "GST 19%" to "gst-19",
//        "GST 7%" to "gst-7",
//        "Reduced rate" to "reduced-rate",
//        "Super reduced rate" to "super-reduced-rate",
//        "Zero rate" to "zero-rate"
//    )
    fun checkPermission() {
        val readImagePermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        if (activity?.let {
                checkSelfPermission(
                    it,
                    readImagePermission
                )
            } == PackageManager.PERMISSION_GRANTED) {
            //permission granted
            openPicker()
        } else {
            permReqLauncher.launch(arrayOf(readImagePermission))
            //permission not granted
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    fun showToast(value: String) {
        Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
    }

    private fun prepareFilePart(partName: String, fileUri: String): MultipartBody.Part {
        val file = File(fileUri)
        val fileBody = ProgressRequestBody(file, this)
        return MultipartBody.Part.createFormData(partName, file.name, fileBody)
    }

    private fun onMediaSelectionCancelled() {
        Toast.makeText(activity, "Media Selection Cancelled ", Toast.LENGTH_SHORT).show()
    }

    private fun onMediaSelected(selectedMediaList: String?) {
        if (selectedMediaList != null) {
            viewModel.addImage(prepareFilePart("product_image_gallery[]", selectedMediaList))
        }
    }

    private fun openPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBrand()
        viewModel.getTaxClass()

        binding.taxSlab.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val a = listTaxClass[position]
                selectedTaxSlab = a.slug ?: ""

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.brandSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val a = brandHashMap.keys.toList().get(position)
                    selectedBrand = brandHashMap[a] ?: ""
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTaxClass.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        listTaxClass.clear()
//                        binding.progressBar.show()
                    }

                    is ResourceState.Success -> {
                        binding.progressBar.hide()
                        listTaxClass.add(TaxCategories(-1, "Select Tax", ""))
                        listTaxClass.addAll(it.item)
//                        listTaxClass = it.item as ArrayList<TaxCategories>
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            listTaxClass.map { ls -> ls.name }
                        ).also { adapter ->
                            // Specify the layout to use when the list of choices appears
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            // Apply the adapter to the spinner
                            binding.taxSlab.adapter = adapter
                        }
                    }

                    is ResourceState.Error -> {
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
                    }

                    else -> {}
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.createProduct.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding.progressBar.show()
                    }

                    is ResourceState.Success -> {
                        binding.progressBar.hide()
                        viewModel.addBrand(RequestAddBrand(selectedBrand, it.item.id.toString()))
                    }

                    is ResourceState.Error -> {
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
                    }

                    else -> {}
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getBrand.collect {
                when (it) {
                    is ResourceState.Loading -> {
//                        binding.progressBar.show()
                    }

                    is ResourceState.Success -> {
                        brandHashMap["Select Brand"] = ""
                        it.item.data?.products?.taxCategoriesList?.forEach { tax ->
                            brandHashMap[tax.name] = tax.slug
                        }
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            brandHashMap.keys.toList()
                        ).also { adapter ->
                            // Specify the layout to use when the list of choices appears
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            // Apply the adapter to the spinner
                            binding.brandSpinner.adapter = adapter
                        }
                        binding.progressBar.hide()
                    }

                    is ResourceState.Error -> {
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
                    }

                    else -> {}
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addImage.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding.progressBar.show()
                    }

                    is ResourceState.Success -> {
                        productImagePath = it.item.data[0]
                        Log.e("got_image", it.item.toString())
                        binding.progressBar.hide()
                    }

                    is ResourceState.Error -> {
                        Toast.makeText(
                            activity,
                            "Something when wrong " + it.throwable.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> {}
                }
            }
        }
        binding.addProduct.setOnClickListener {
            if (list.isNotEmpty()) {
                showCategory(list)
            } else {
                viewModel.getCategories()
            }
        }
        binding.productImage.setOnClickListener {
            checkPermission()
        }
        binding.apply {
            addOrder.setOnClickListener {
                val listCategory = mutableListOf<Category>()
                categoryAdapter?.differ?.currentList?.forEach {
                    listCategory.add(Category(it.id))
                }
                if (listCategory.isEmpty()) {
                    showToast("Kindly add category")
                    return@setOnClickListener
                }
                if (sku.text.toString().trim().isBlank()) {
                    showToast("kindly enter sku")
                    return@setOnClickListener
                }
                if (firstName.text.toString().trim().isBlank()) {
                    showToast("kindly enter name")
                    return@setOnClickListener
                }
                if (address1.text.toString().trim().isBlank()) {
                    showToast("kindly enter description")
                    return@setOnClickListener
                }
                if (address2.text.toString().trim().isBlank()) {
                    showToast("kindly enter short description")
                    return@setOnClickListener
                }
                if (city.text.toString().trim().isBlank()) {
                    showToast("kindly enter price")
                    return@setOnClickListener
                }
                if (salePrice.text.toString().trim().isBlank()) {
                    showToast("kindly enter sale price")
                    return@setOnClickListener
                }
                if (selectedBrand.trim().isBlank() || selectedBrand == "Select Brand") {
                    showToast("kindly Select brand")
                    return@setOnClickListener
                }
                if (selectedTaxSlab.isBlank() || selectedTaxSlab == "Select Tax") {
                    showToast("kindly Select Tax slab")
                    return@setOnClickListener
                }
                viewModel.createProduct(
                    RequestCreateProduct(
                        categories = listCategory,
                        description = address1.text.toString(),
                        images = mutableListOf(Image(src = productImagePath)),
                        name = firstName.text.toString(),
                        regularPrice = city.text.toString(),
                        salePrice = salePrice.text.toString(),
                        shortDescription = address2.text.toString(),
                        sku = sku.text.toString(),
                        taxClass = selectedTaxSlab
                    )
                )
            }
        }
        categoryAdapter = CategoryAdapter {

        }
        binding.addProductRecycler.adapter = categoryAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newsSource.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding.progressBar.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        binding.progressBar.hide()
                        list = it.item
                        showCategory(list)
                        Log.e("handdy", it.item.toString())
                    }

                    is ResourceState.Error -> {
                        Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG).show()
                        Log.e("handdy", "error")
                    }

                    else -> {}
                }
            }
        }
    }

    fun showCategory(list: List<LineItem>) {
        CategoryBottomSheetDialog(list) { ls ->
            categoryAdapter?.submitList(ls)
        }.show(parentFragmentManager, "categorySheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProgressUpdate(percentage: Int) {
        Log.e("update_error", "" + percentage)
    }

    override fun onError() {
        binding.progressBar.gone()
    }

    override fun onFinish() {
        binding.progressBar.gone()
    }

    override fun uploadStart() {
        binding.progressBar.show()
        Toast.makeText(activity, "Uploading Image", Toast.LENGTH_LONG).show()
    }
}