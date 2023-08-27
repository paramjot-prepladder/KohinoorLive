package com.param.kohinoor.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.param.exercise.utils.ResourceState
import com.param.exercise.utils.hide
import com.param.exercise.utils.show
import com.param.kohinoor.databinding.FragmentSlideshowBinding
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.createRequest.Category
import com.param.kohinoor.pojo.product.createRequest.RequestCreateProduct
import com.param.kohinoor.ui.home.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    var categoryAdapter: CategoryAdapter? = null
    var list: List<LineItem> = arrayListOf()

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addProduct.setOnClickListener {
            if (list.isNotEmpty()) {
                showCategory(list)
            } else {
                viewModel.getCategories()
            }
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
                if (firstName.text.toString().isBlank()) {
                    showToast("kindly enter name")
                    return@setOnClickListener
                }
                if (address1.text.toString().isBlank()) {
                    showToast("kindly enter description")
                    return@setOnClickListener
                }
                if (address2.text.toString().isBlank()) {
                    showToast("kindly enter short description")
                    return@setOnClickListener
                }
                if (city.text.toString().isBlank()) {
                    showToast("kindly enter price")
                    return@setOnClickListener
                }
                viewModel.createProduct(
                    RequestCreateProduct(
                        categories = listCategory,
                        description = address1.text.toString(),
                        images = null,
                        name = firstName.text.toString(),
                        regularPrice = city.text.toString(),
                        shortDescription = address2.text.toString()
                    )
                )
            }
        }
        categoryAdapter = CategoryAdapter() {

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
}