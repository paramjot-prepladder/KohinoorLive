package com.param.kohinoor.ui.productDetails

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.param.kohinoor.R

class UpdateProductDetailBottomSheet(val hint: String, val listener: (String) -> Unit) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_update_price, container, false)

        val quantity = view.findViewById<EditText>(R.id.quantity)
        quantity.inputType = InputType.TYPE_CLASS_TEXT
        val addProduct = view.findViewById<TextView>(R.id.addProduct)
        quantity.hint = "Enter $hint"

        addProduct.setOnClickListener {
            if (quantity.text.isNotBlank()) {
                listener(quantity.text.toString())
                dismiss()
            } else {
                Toast.makeText(activity, "Kindly enter $hint", Toast.LENGTH_LONG).show()

            }
        }




        return view
    }
}