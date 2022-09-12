package com.mycompany.cartnshop.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.cartnshop.ui.activities.CheckoutActivity
import com.mycompany.cartnshop.ui.activities.EditAddAddressActivity
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.model.Address
import com.mycompany.cartnshop.utils.Constants
import kotlinx.android.synthetic.main.address_row.view.*


open class AddressAdapter(private val context: Context, private var addressList: ArrayList<Address>,private val selectAddress: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddressViewHolder(
            LayoutInflater.from(context).inflate(R.layout.address_row, parent, false)
        )
    }

    fun notifyEditItem(activity: Activity, position: Int){
        val intent = Intent(context, EditAddAddressActivity::class.java)
        intent.putExtra("extra_address_details",addressList[position])
        activity.startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = addressList[position]

        if(holder is AddressViewHolder){
            holder.itemView.tv_address_full_name.text = model.fullName
            holder.itemView.tv_address_details.text = "${model.address}, ${model.zipCode}"
            holder.itemView.tv_address_mobile_number.text = model.mobileNumber
            holder.itemView.tv_address_type.text = model.type


            if(selectAddress){
                holder.itemView.setOnClickListener {
                    val selectAddressString : String = (context.getString(R.string.selected_address))+"${model.address}, ${model.zipCode}"
                    Toast.makeText(context,selectAddressString,Toast.LENGTH_LONG).show()
                    val intent = Intent(context, CheckoutActivity::class.java)
                    intent.putExtra("extra_selected_address",model)
                    context.startActivity(intent)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return addressList.size
    }


}