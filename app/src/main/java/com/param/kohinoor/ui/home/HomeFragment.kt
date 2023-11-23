package com.param.kohinoor.ui.home


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.param.exercise.utils.ResourceState
import com.param.exercise.utils.delayOnLifecycle
import com.param.exercise.utils.hide
import com.param.exercise.utils.show
import com.param.kohinoor.R
import com.param.kohinoor.databinding.FragmentHomeBinding
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.createRequest.RequestUpdateProduct
import com.param.kohinoor.ui.bottomsheet.UpdatePriceBottomSheet
import com.param.kohinoor.utils.RecyclerTouchListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var adapterProductListing: ProductAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private var imm: InputMethodManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.toolbarSearch.doAfterTextChanged {
            if ((it?.length ?: 0) > 2) {
                viewModel.getSingleProducts(it.toString())
            }
            if (it?.length == 0) {
                viewModel.getProducts()
            }
        }
        binding.toolbarSearch.setOnEditorActionListener { v, actionId, event ->
            imm?.hideSoftInputFromWindow(binding.toolbarSearch.windowToken, 0)
            viewModel.getSingleProducts(binding.toolbarSearch.text.toString())
            true

        }
        val touchListener = RecyclerTouchListener(activity, binding.recyclerView)
        touchListener.setClickable(object : RecyclerTouchListener.OnRowClickListener {
            override fun onRowClicked(position: Int) {
                findNavController().navigate(
                    HomeFragmentDirections.actionNavHomeToProductDetailFragment(
                        adapterProductListing?.differ?.currentList?.get(position)
                    )
                )
//                Toast.makeText(
//                    activity,
//                    adapterProductListing?.differ?.currentList?.get(position)?.name,
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
        })
            .setSwipeOptionViews(R.id.hide,R.id.outStock, R.id.prices)
            .setSwipeable(
                R.id.rowFG, R.id.rowBG
            ) { viewID, position ->
                when (viewID) {
                    R.id.prices -> {
                        showUpdateBottomSheet(
                            true,
                            adapterProductListing?.differ?.currentList?.get(position)
                        )
                    }

                    R.id.hide -> {
                        viewModel.updateSingleProducts(
                            adapterProductListing?.differ?.currentList?.get(position)?.id ?: 0,
                            RequestUpdateProduct(stockStatus = "instock")
                        )
                    }
                    R.id.outStock -> {
                        viewModel.updateSingleProducts(
                            adapterProductListing?.differ?.currentList?.get(position)?.id ?: 0,
                            RequestUpdateProduct(stockStatus = "outofstock")
                        )
                    }
                }
            }
        binding.recyclerView.addOnItemTouchListener(touchListener)

        adapterProductListing = ProductAdapter {

        }
        binding.recyclerView.adapter = adapterProductListing
        viewModel.getProducts()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newsSource.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding.progressBar.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        binding.progressBar.hide()
                        adapterProductListing?.submitList(it.item)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateProduct.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding.progressBar.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        binding.progressBar.hide()
                        viewModel.getProducts()
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


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.singleProduct.collect {
                    when (it) {
                        is ResourceState.Loading -> {
                            binding.progressBar.show()
//                        binding.progressBar.show()
                            Log.e("handdy", "Loadong")
                        }

                        is ResourceState.Success -> {
                            adapterProductListing?.submitList(it.item)
                            binding.progressBar.hide()
//                        binding.progressBar.hide()
//                        adapterProductListing?.submitList(it.item)
                            Log.e("handdy", it.item.toString())
                        }

                        is ResourceState.Error -> {
                            binding.progressBar.hide()
                            Toast.makeText(activity, "Something when wrong", Toast.LENGTH_LONG)
                                .show()
                            Log.e("handdy", "error")
                        }

                        else -> {}
                    }
                }
            }
        }
        binding.progressBar.delayOnLifecycle(1000){
            imm?.hideSoftInputFromWindow(binding.toolbarSearch.windowToken, 0)
        }
    }

    private fun showUpdateBottomSheet(isPrice: Boolean, lineItem: LineItem?) {
        UpdatePriceBottomSheet(isPrice) { ls ->
            viewModel.updateSingleProducts(
                lineItem?.id ?: 0,
                if (isPrice) {
                    RequestUpdateProduct(regularPrice = ls)
                } else {
                    RequestUpdateProduct(stockQuantity = ls)
                }
            )

        }.show(parentFragmentManager, "updateSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}