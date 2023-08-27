package com.param.kohinoor.ui.slideshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.param.exercise.utils.gone
import com.param.exercise.utils.show
import com.param.kohinoor.databinding.ItemCategoryBinding
import com.param.kohinoor.pojo.order.LineItem

class CategoryAdapter(var showCheckbox: Boolean = false, val listener: (LineItem) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: LineItem, showCheckbox: Boolean, listener: (LineItem) -> Unit) {
            binding.apply {
                checkbox.setOnClickListener {
                    data.isSelected = checkbox.isChecked
                    listener(data)
                }
                checkbox.isChecked = data.isSelected
                name.text = data.name
                if (showCheckbox) {
                    checkbox.show()
                } else {
                    checkbox.gone()
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<LineItem>() {
        override fun areItemsTheSame(
            oldItem: LineItem,
            newItem: LineItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LineItem,
            newItem: LineItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<LineItem>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val mView = ItemCategoryBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(differ.currentList[position], showCheckbox, listener)
    }
}