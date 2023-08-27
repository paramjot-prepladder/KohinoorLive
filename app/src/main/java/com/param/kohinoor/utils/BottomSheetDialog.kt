package com.param.kohinoor.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.param.kohinoor.R
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.ui.home.ProductViewModel

class BottomSheetDialog(
    val viewModel: ProductViewModel,
    val listener: (String) -> Unit
) :
    BottomSheetDialogFragment() {
    private var imm: InputMethodManager? = null
    var product: EditText? = null
    var quantity: EditText? = null
    //    var dataToReturn: LineItem? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_search_product, container, false)

        val addProduct = view.findViewById<TextView>(R.id.addProduct)

        product = view.findViewById<EditText>(R.id.product)

        quantity = view.findViewById<EditText>(R.id.quantity)
        imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        product.doAfterTextChanged {
//            if ((it?.length ?: 0) > 2) {
//                viewModel.getSingleProducts(it.toString())
//            }
//        }
        product?.setOnEditorActionListener { v, actionId, event ->
            imm?.hideSoftInputFromWindow(product?.windowToken, 0)
            viewModel.getSingleProducts(product?.text.toString())
            true

        }

        addProduct.setOnClickListener {
            if (product?.text.isNullOrBlank()){
                Toast.makeText(activity,"Kindly enter Name",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (quantity?.text.isNullOrBlank()){
                Toast.makeText(activity,"Kindly enter Quantity",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
//            dataToReturn?.quantity = quantity.text.toString().toInt()
            listener(quantity?.text.toString())
            dismiss()
        }



        return view
    }



}