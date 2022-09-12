package com.mycompany.cartnshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.ui.activities.SoldProductsDetailsActivity
import com.mycompany.cartnshop.model.SoldProduct
import com.mycompany.cartnshop.utils.LoadGlide
import kotlinx.android.synthetic.main.order_row.view.*


class SoldProductAdapter(private val context: Context, private var soldProductList: ArrayList<SoldProduct>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SoldProductViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SoldProductViewHolder(
            LayoutInflater.from(context).inflate(R.layout.order_row,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = soldProductList[position]

        if (holder is SoldProductViewHolder) {

            LoadGlide(context).loadProductImage(
                model.image,
                holder.itemView.iv_item_image
            )

            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "$${model.price}"
            holder.itemView.ib_delete_product.visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context, SoldProductsDetailsActivity::class.java)
                intent.putExtra("extra_sold_product_details",model)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return soldProductList.size
    }

}