package com.mycompany.cartnshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.cartnshop.ui.activities.ProductDetailsActivity
import com.mycompany.cartnshop.ui.fragments.ProductFragment
import com.mycompany.cartnshop.R

import com.mycompany.cartnshop.model.Product
import com.mycompany.cartnshop.utils.LoadGlide
import kotlinx.android.synthetic.main.product_item_row.view.*


open class ProductsListAdapter(private val context: Context, private var list: ArrayList<Product>,private val fragment: ProductFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item_row, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is ProductViewHolder) {

            LoadGlide(context).loadProductImage(model.image, holder.itemView.iv_item_image)

            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_price.text = "$${model.price}"


            holder.itemView.ib_delete_product.setOnClickListener {
                fragment.deleteProduct(model.product_id)

            }

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra("extra_product_id", model.product_id)
                intent.putExtra("extra_product_owner_id",model.user_id)
                intent.putExtra("extra_edit_perm",1)
                context.startActivity(intent)
            }

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

}