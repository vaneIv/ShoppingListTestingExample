package com.example.shoppinglisttestingexample.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.shoppinglisttestingexample.R
import com.example.shoppinglisttestingexample.data.local.ShoppingItem
import kotlinx.android.synthetic.main.item_shopping.view.image_view_shopping_image
import kotlinx.android.synthetic.main.item_shopping.view.text_view_name
import kotlinx.android.synthetic.main.item_shopping.view.text_view_shopping_item_amount
import kotlinx.android.synthetic.main.item_shopping.view.text_view_shopping_item_price
import javax.inject.Inject

class ShoppingItemAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        return ShoppingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shopping,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.itemView.apply {
            glide.load(shoppingItem.imageUrl).into(image_view_shopping_image)

            val amountText = "${shoppingItem.amount}x"
            val priceText = "${shoppingItem.price}$"

            text_view_name.text = shoppingItem.name
            text_view_shopping_item_amount.text = amountText
            text_view_shopping_item_price.text = priceText
        }
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}