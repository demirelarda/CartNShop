package com.mycompany.cartnshop.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.adapter.CartItemAdapter
import com.mycompany.cartnshop.model.Order
import kotlinx.android.synthetic.main.activity_order_details.*
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        setupActionBar()

        var orderDetails : Order = Order()
        if(intent.hasExtra("extra_order_details")){
            orderDetails = intent.getParcelableExtra<Order>("extra_order_details")!!
        }

        setOrderDetails(orderDetails)

    }

    private fun setOrderDetails(orderDetails: Order){
        tv_order_details_id.text = orderDetails.title
        tv_order_details_date.text = dateTimeFormatter(orderDetails)

        rv_my_order_items_list.layoutManager = LinearLayoutManager(this@OrderDetailsActivity)
        rv_my_order_items_list.setHasFixedSize(true)

        val cartItemAdapter = CartItemAdapter(this@OrderDetailsActivity,orderDetails.items,false)

        rv_my_order_items_list.adapter = cartItemAdapter
        tv_my_order_details_address_type.text = orderDetails.address.type
        tv_my_order_details_full_name.text = orderDetails.address.fullName
        tv_my_order_details_address.text =
            "${orderDetails.address.address}, ${orderDetails.address.zipCode}"
        tv_my_order_details_additional_note.text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetails.isNotEmpty()) {
            tv_my_order_details_other_details.visibility = View.VISIBLE
            tv_my_order_details_other_details.text = orderDetails.address.otherDetails
        } else {
            tv_my_order_details_other_details.visibility = View.GONE
        }
        tv_my_order_details_mobile_number.text = orderDetails.address.mobileNumber

        tv_order_details_sub_total.text = orderDetails.sub_total_amount
        tv_order_details_shipping_charge.text = orderDetails.shipping_charge
        tv_order_details_total_amount.text = orderDetails.total_amount

    }







    private fun dateTimeFormatter(orderDetails: Order): String {
        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_date
        return formatter.format(calendar.time)
    }







    private fun setupActionBar() {

        setSupportActionBar(toolbar_my_order_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }
        toolbar_my_order_details_activity.setNavigationOnClickListener { onBackPressed() }
    }



}