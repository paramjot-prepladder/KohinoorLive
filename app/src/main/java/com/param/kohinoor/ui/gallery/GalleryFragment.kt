package com.param.kohinoor.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.param.exercise.utils.ResourceState
import com.param.exercise.utils.hide
import com.param.exercise.utils.show
import com.param.kohinoor.R
import com.param.kohinoor.databinding.FragmentGalleryBinding
import com.param.kohinoor.ui.orderDetail.OrderDetailFragment
import com.param.kohinoor.ui.orderDetail.OrderDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    var adapterOrderListing: OrderListingAdapter? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterOrderListing = OrderListingAdapter {
            findNavController().navigate(
                GalleryFragmentDirections.actionNavGalleryToOrderDetailFragment(it)
            )
        }
        binding.recyclerView.adapter = adapterOrderListing
        viewModel.getOrders()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orders.collect {
                when (it) {
                    is ResourceState.Loading -> {
                        binding.progressBar.show()
                        Log.e("handdy", "Loadong")
                    }

                    is ResourceState.Success -> {
                        binding.progressBar.hide()
                        adapterOrderListing?.submitList(it.item)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}