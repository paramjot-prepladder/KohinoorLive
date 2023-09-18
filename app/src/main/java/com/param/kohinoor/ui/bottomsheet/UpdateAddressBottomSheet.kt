package com.param.kohinoor.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.param.kohinoor.R
import com.param.kohinoor.pojo.createOrder.Billing

class UpdateAddressBottomSheet(val bill: com.param.kohinoor.pojo.order.Billing?, val listener: (Billing) -> Unit) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_address, container, false)

        val name = view.findViewById<EditText>(R.id.name)
        val lastname = view.findViewById<EditText>(R.id.lastname)
        val address = view.findViewById<EditText>(R.id.address)
        val zipcode = view.findViewById<EditText>(R.id.zipcode)
        val city = view.findViewById<EditText>(R.id.city)
        val addProduct = view.findViewById<TextView>(R.id.addProduct)
        val mobile = view.findViewById<EditText>(R.id.mobile)
        name.hint = "Enter Name"
        lastname.hint = "Enter lastname"
        address.hint = "Enter address"
        zipcode.hint = "Enter zipcode "
        city.hint = "Enter city"
        mobile.hint = "Enter mobile"
        name.setText(bill?.firstName)
        lastname.setText(bill?.lastName)
        address.setText(bill?.address1)
        zipcode.setText(bill?.postcode)
        city.setText(bill?.city)
        mobile.setText(bill?.phone)


        addProduct.setOnClickListener {
            if (name.text.isNotBlank()) {
                Toast.makeText(activity, "Kindly enter name", Toast.LENGTH_LONG).show()
                dismiss()
            } else if (lastname.text.isNotBlank()) {
                Toast.makeText(activity, "Kindly enter lastname", Toast.LENGTH_LONG).show()
                dismiss()
            } else if (address.text.isNotBlank()) {
                Toast.makeText(activity, "Kindly enter address", Toast.LENGTH_LONG).show()
                dismiss()
            } else if (zipcode.text.isNotBlank()) {
                Toast.makeText(activity, "Kindly enter zipcode", Toast.LENGTH_LONG).show()
                dismiss()
            } else if (city.text.isNotBlank()) {
                Toast.makeText(activity, "Kindly enter city", Toast.LENGTH_LONG).show()
                dismiss()
            } else if (mobile.text.isNotBlank()) {
                Toast.makeText(activity, "Kindly enter mobile", Toast.LENGTH_LONG).show()
                dismiss()
            } else {
                listener(
                    Billing(
                        address1 = address.text.toString(),
                        address2 = null,
                        city = city.text.toString(),
                        country = null,
                        email = null,
                        firstName = name.text.toString(),
                        lastName = lastname.text.toString(),
                        phone = mobile.text.toString(),
                        postcode = zipcode.text.toString(),
                        state = null
                    )
                )
            }
        }




        return view
    }
}