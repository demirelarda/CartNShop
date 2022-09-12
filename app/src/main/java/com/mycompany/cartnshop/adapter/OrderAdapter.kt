package com.mycompany.cartnshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.cartnshop.ui.activities.OrderDetailsActivity
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.model.Order
import com.mycompany.cartnshop.utils.LoadGlide
import kotlinx.android.synthetic.main.order_row.view.*

class OrderAdapter(private val context: Context,private var list: ArrayList<Order>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderViewHolder(
        LayoutInflater.from(context).inflate(R.layout.order_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        if (holder is OrderViewHolder) {

            LoadGlide(context).loadProductImage(
                model.image,
                holder.itemView.iv_item_image
            )

            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "$${model.total_amount}"
            holder.itemView.ib_delete_product.visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context, OrderDetailsActivity::class.java)
                intent.putExtra("extra_order_details",model)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}