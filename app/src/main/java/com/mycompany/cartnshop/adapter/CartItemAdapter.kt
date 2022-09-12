package com.mycompany.cartnshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.cartnshop.ui.activities.CartListActivity
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.model.Cart
import com.mycompany.cartnshop.utils.LoadGlide
import kotlinx.android.synthetic.main.cart_item_row.view.*

open class CartItemAdapter(private val context: Context, private var itemList: ArrayList<Cart>,private val updateCartItems: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class CartItemViewHolder(view: View) : RecyclerView.ViewHolder(view)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.cart_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = itemList[position]
        LoadGlide(context).loadProductImage(model.image,holder.itemView.iv_cart_item_image)
        holder.itemView.tv_cart_item_price.text = "$${model.price}"
        holder.itemView.tv_cart_item_title.text = model.title
        holder.itemView.tv_cart_quantity.text = model.cart_quantity

        if (model.cart_quantity == "0") {
            holder.itemView.ib_remove_cart_item.visibility = View.GONE
            holder.itemView.ib_add_cart_item.visibility = View.GONE

            if(updateCartItems){
                holder.itemView.ib_delete_cart_item.visibility = View.VISIBLE
            }
            else{
                holder.itemView.ib_delete_cart_item.visibility = View.GONE
            }

            holder.itemView.tv_cart_quantity.text =
                context.resources.getString(R.string.lbl_out_of_stock)

            holder.itemView.tv_cart_quantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSnackBarError
                )
            )
        } else {

            if(updateCartItems){
                holder.itemView.ib_remove_cart_item.visibility = View.VISIBLE
                holder.itemView.ib_add_cart_item.visibility = View.VISIBLE
                holder.itemView.ib_delete_cart_item.visibility = View.VISIBLE
            }
            else{
                holder.itemView.ib_remove_cart_item.visibility = View.GONE
                holder.itemView.ib_add_cart_item.visibility = View.GONE
                holder.itemView.ib_delete_cart_item.visibility = View.GONE
            }



            holder.itemView.tv_cart_quantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSecondaryText
                )
            )
        }

        holder.itemView.ib_delete_cart_item.setOnClickListener {
            when(context){
                is CartListActivity ->{
                    context.showProgressBar(context.getString(R.string.please_wait))
                }
            }
            Database().removeItemInCart(context,model.id)

        }

        holder.itemView.ib_remove_cart_item.setOnClickListener {
            if(model.cart_quantity == "1"){
                Database().removeItemInCart(context,model.id)
            }
            else{
                val cartQuantity: Int = model.cart_quantity.toInt()
                val itemHashMap = HashMap<String,Any>()
                itemHashMap["cart_quantity"] = (cartQuantity-1).toString()
                if(context is CartListActivity){
                    context.showProgressBar(context.getString(R.string.please_wait))
                }
                Database().updateCartList(context,model.id,itemHashMap)
            }
        }

        holder.itemView.ib_add_cart_item.setOnClickListener {
            val cartQuantity: Int = model.cart_quantity.toInt()
            if(cartQuantity<model.stock_quantity.toInt()){
                val itemHashMap = HashMap<String,Any>()
                itemHashMap["cart_quantity"] = (cartQuantity+1).toString()

                if(context is CartListActivity){
                    context.showProgressBar(context.getString(R.string.please_wait))
                }
                Database().updateCartList(context,model.id,itemHashMap)
            }else{
                if(context is CartListActivity){
                    val stringForStock : String = context.getString(R.string.msg_for_available_stock,model.stock_quantity)
                    Toast.makeText(context,stringForStock,Toast.LENGTH_LONG).show()
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}