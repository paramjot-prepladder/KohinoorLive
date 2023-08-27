package com.param.kohinoor.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.param.exercise.utils.setStatus
import com.param.kohinoor.R
import com.param.kohinoor.databinding.ItemOrderBinding
import com.param.kohinoor.databinding.ItemProductBinding
import com.param.kohinoor.pojo.order.ResponseOrderItem
import com.param.kohinoor.pojo.product.ResponseProductListingItem

class OrderListingAdapter(val listener: (ResponseOrderItem) -> Unit) :
    RecyclerView.Adapter<OrderListingAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseOrderItem, listener: (ResponseOrderItem) -> Unit) {
            binding.apply {
                root.setOnClickListener {
                    listener(data)
                }
                statusText.setStatus(data.status, status)
                price.text = data.total
                mobile.text = data.billing?.phone
                orderNo.text = "Order #" + data.id
                name.text = "${data.billing?.firstName} ${data.billing?.lastName}"
                address.text =
                    "${data.billing?.address1}, ${data.billing?.postcode} ${data.billing?.city}"
            }
        }
    }


    private val diffCallback = object : DiffUtil.ItemCallback<ResponseOrderItem>() {
        override fun areItemsTheSame(
            oldItem: ResponseOrderItem,
            newItem: ResponseOrderItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ResponseOrderItem,
            newItem: ResponseOrderItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<ResponseOrderItem>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val mView = ItemOrderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(differ.currentList[position], listener)
    }
}