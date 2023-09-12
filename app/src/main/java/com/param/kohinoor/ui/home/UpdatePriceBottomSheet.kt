package com.param.kohinoor.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.param.kohinoor.R
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.ui.slideshow.CategoryAdapter

class UpdatePriceBottomSheet(val isPrice: Boolean, val listener: (String) -> Unit) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_update_price, container, false)

        val quantity = view.findViewById<EditText>(R.id.quantity)
        val addProduct = view.findViewById<TextView>(R.id.addProduct)
        quantity.hint = if (isPrice) {
            "Enter Regular Price"
        } else {
            "Enter Stock Quantity"
        }
        addProduct.setOnClickListener {
            if (quantity.text.isNotBlank()) {
                listener(quantity.text.toString())
                dismiss()
            } else {
                if (isPrice) {
                    Toast.makeText(activity, "Kindly enter updated Price", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, "Kindly enter updated Quantity", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }




        return view
    }
}