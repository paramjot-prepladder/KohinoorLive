package com.param.kohinoor.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.param.kohinoor.R
import com.param.kohinoor.pojo.order.LineItem

class CategoryBottomSheetDialog(val list: List<LineItem>, val listener: (List<LineItem>) -> Unit) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_category, container, false)

        val selectCategory = view.findViewById<TextView>(R.id.selectCategory)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = CategoryAdapter(true) {

        }
        recyclerView.adapter = adapter
        adapter.submitList(list)
        selectCategory.setOnClickListener {
            dismiss()
            listener(adapter.differ.currentList.filter { data -> data.isSelected })
        }



        return view
    }
}