package com.param.kohinoor.ui.orderDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.param.exercise.utils.gone
import com.param.exercise.utils.loadImg
import com.param.exercise.utils.setStatus
import com.param.kohinoor.databinding.ItemOrderBinding
import com.param.kohinoor.databinding.ItemOrderProductBinding
import com.param.kohinoor.pojo.order.LineItem
import com.param.kohinoor.pojo.order.ResponseOrderItem
import com.param.kohinoor.ui.gallery.OrderListingAdapter
import java.text.DecimalFormat

class OrderDetailAdapter(val currencySymbol: String, val listener: (LineItem) -> Unit) :
    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemOrderProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val formatter = DecimalFormat("0.00")
        fun onBind(data: LineItem, currencySymbol: String, listener: (LineItem) -> Unit) {
            binding.apply {
                root.setOnClickListener {
                    listener(data)
                }
                if (data.total == null) {
                    price.gone()
                    productImage.loadImg(data.images?.get(0)?.src)
                } else {
                    price.text = currencySymbol + " " + data.total
                    productImage.loadImg(data.image?.src)
                }
                productName.text = "QTY: " + data.quantity
                desc.text = data.name
                pricePerItem.text = formatter.format(data.price)
                productImage.loadImg(data.image?.src)
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
        val mView = ItemOrderProductBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(mView)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(differ.currentList[position], currencySymbol, listener)
    }
}