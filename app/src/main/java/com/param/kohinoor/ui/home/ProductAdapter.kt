package com.param.kohinoor.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.param.exercise.utils.loadImg
import com.param.exercise.utils.setHtml
import com.param.kohinoor.databinding.ItemProductBinding
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.product.ResponseProductListingItem

class ProductAdapter(val listener: (LineItem) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: LineItem, listener: (LineItem) -> Unit) {
            binding.apply {
                root.setOnClickListener {
                    listener(data)
                }
                productName.text = data.name
                desc.setHtml(data.shortDescription ?: "")
                price.text = data.price.toString()
                if (data.images?.isNotEmpty() == true)
                    productImage.loadImg(data.images[0]?.src)
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
        val mView = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(differ.currentList[position],listener)
    }

}