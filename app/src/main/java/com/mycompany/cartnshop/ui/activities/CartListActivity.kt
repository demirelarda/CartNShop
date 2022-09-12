package com.mycompany.cartnshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.adapter.CartItemAdapter
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.model.Cart
import com.mycompany.cartnshop.model.Product
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : UiComponentsActivity() {


    private lateinit var lastProductsList: ArrayList<Product>
    private lateinit var lastCartItems: ArrayList<Cart>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()

        btn_checkout.setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressActivity::class.java)
            intent.putExtra("extra_select_address",true)
            startActivity(intent)
        }
    }



    fun successCartItemList(cartList: ArrayList<Cart>){
        hideProgressBar()




        for (product in lastProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {

                    cart.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0){
                        cart.cart_quantity = product.stock_quantity
                    }
                }
            }
        }



        lastCartItems = cartList



        if (lastCartItems.size > 0) {

            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)

            val cartListAdapter = CartItemAdapter(this@CartListActivity, lastCartItems,true)
            rv_cart_items_list.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for (item in lastCartItems) {



                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()

                    subTotal += (price * quantity)
                }

            }

            tv_sub_total.text = "$$subTotal"

            tv_shipping_charge.text = "$10.0" //change the cargo fee logic here.

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 10
                tv_total_amount.text = "$$total"
            } else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }


    }

    private fun getCartItemsList(){
        //showProgressBar(getString(R.string.please_wait))
        Database().getCartList(this@CartListActivity)

    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductsFromFB()
    }



    private fun setupActionBar() {

        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }
        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductsFromFB(){
        showProgressBar(getString(R.string.please_wait))
        Database().getAllProductsList(this)

    }


    fun successProductListFromFS(allProductList : ArrayList<Product>){
        hideProgressBar()
        lastProductsList = allProductList
        getCartItemsList()


    }

    fun itemRemovedSuccess(){
        hideProgressBar()
        Toast.makeText(this, R.string.msg_item_removed_successfully,Toast.LENGTH_LONG).show()
        getCartItemsList()
    }

    fun itemUpdateSuccess(){
        hideProgressBar()
        getCartItemsList()


    }






}