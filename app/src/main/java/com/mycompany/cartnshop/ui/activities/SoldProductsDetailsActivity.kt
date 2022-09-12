package com.mycompany.cartnshop.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.model.SoldProduct
import com.mycompany.cartnshop.utils.LoadGlide
import kotlinx.android.synthetic.main.activity_sold_products_details.*
import java.text.SimpleDateFormat
import java.util.*

class SoldProductsDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_products_details)
        setupActionBar()

        var productDetails : SoldProduct = SoldProduct()

        if(intent.hasExtra("extra_sold_product_details")){
            productDetails = intent.getParcelableExtra<SoldProduct>("extra_sold_product_details")!!
        }

        fillDetails(productDetails)

    }







    private fun fillDetails(productDetails: SoldProduct) {

        tv_sold_product_details_id.text = productDetails.order_id

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date
        tv_sold_product_details_date.text = formatter.format(calendar.time)

        LoadGlide(this@SoldProductsDetailsActivity).loadProductImage(
            productDetails.image,
            iv_product_item_image
        )

        tv_sold_product_quantity.text = productDetails.sold_quantity
        tv_product_item_name.text = productDetails.title
        tv_product_item_price.text ="$${productDetails.price}"


        tv_sold_details_address_type.text = productDetails.address.type
        tv_sold_details_full_name.text = productDetails.address.fullName
        tv_sold_details_address.text =
            "${productDetails.address.address}, ${productDetails.address.zipCode}"
        tv_sold_details_additional_note.text = productDetails.address.additionalNote

        if (productDetails.address.otherDetails.isNotEmpty()) {
            tv_sold_details_other_details.visibility = View.VISIBLE
            tv_sold_details_other_details.text = productDetails.address.otherDetails
        } else {
            tv_sold_details_other_details.visibility = View.GONE
        }
        tv_sold_details_mobile_number.text = productDetails.address.mobileNumber

        tv_sold_product_sub_total.text = productDetails.sub_total_amount
        tv_sold_product_shipping_charge.text = productDetails.shipping_charge
        tv_sold_product_total_amount.text = productDetails.total_amount
    }




    private fun setupActionBar() {

        setSupportActionBar(toolbar_sold_product_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        toolbar_sold_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }



}