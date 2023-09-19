package com.param.kohinoor.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.param.kohinoor.R
import com.param.kohinoor.pojo.createOrder.Billing
import com.param.kohinoor.pojo.dpd.createDpd.RequestCreateDpd

class CreateDpdBottomSheet(private val orderID: Int?, val listener: (RequestCreateDpd) -> Unit) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_create_dpd, container, false)

        val quantity = view.findViewById<EditText>(R.id.quantity)
        val checkBox = view.findViewById<CheckBox>(R.id.checkbox)
        val addProduct = view.findViewById<TextView>(R.id.addProduct)

        addProduct.setOnClickListener {
            if (quantity.text.trim().isNotBlank()) {
                listener(
                    RequestCreateDpd(
                        quantity.text.toString(), checkBox.isChecked, orderID
                    )
                )
                dismiss()
            } else {
                Toast.makeText(activity, "Kindly enter Description", Toast.LENGTH_LONG).show()
            }
        }




        return view
    }
}