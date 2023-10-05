package com.param.kohinoor.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.param.exercise.utils.gone
import com.param.exercise.utils.show
import com.param.kohinoor.R

class UpdateStatusBottomSheet(var isProduct: Boolean = false, val listener: (String) -> Unit) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_status, container, false)

        val completed = view.findViewById<TextView>(R.id.name)
        val cancelled = view.findViewById<TextView>(R.id.lastname)
        val onHold = view.findViewById<TextView>(R.id.address)
        val refunded = view.findViewById<TextView>(R.id.zipcode)
        val pendingPayment = view.findViewById<TextView>(R.id.city)
        val draft = view.findViewById<TextView>(R.id.draft)
        val pending = view.findViewById<TextView>(R.id.pending)
        val published = view.findViewById<TextView>(R.id.published)
        if (isProduct) {
            completed.gone()
            cancelled.gone()
            onHold.gone()
            refunded.gone()
            pendingPayment.gone()
            draft.show()
            pending.show()
            published.show()
        }
        draft.setOnClickListener {
            listener("draft")
            dismiss()
        }
        pending.setOnClickListener {
            listener("pending")
            dismiss()
        }
        published.setOnClickListener {
            listener("publish")
            dismiss()
        }
        completed.setOnClickListener {
            listener("completed")
            dismiss()
        }
        cancelled.setOnClickListener {
            listener("cancelled")
            dismiss()
        }
        onHold.setOnClickListener {
            listener("on-hold")
            dismiss()
        }
        refunded.setOnClickListener {
            listener("refunded")
            dismiss()
        }
        pendingPayment.setOnClickListener {
            listener("pending")
            dismiss()
        }




        return view
    }
}