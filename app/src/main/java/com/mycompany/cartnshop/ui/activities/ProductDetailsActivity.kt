package com.mycompany.cartnshop.ui.activities


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.model.Cart
import com.mycompany.cartnshop.model.Product
import com.mycompany.cartnshop.utils.Constants
import com.mycompany.cartnshop.utils.LoadGlide

import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : UiComponentsActivity(),View.OnClickListener {

    private var myProductId: String = ""
    private var editPerm : Int = 0
    private lateinit var productModel: Product
    var productOwnerId : String = ""
    private lateinit var myProductDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionBar()


        if(intent.hasExtra("extra_product_id")){
            myProductId = intent.getStringExtra("extra_product_id")!!
            editPerm = intent.getIntExtra("extra_edit_perm",0)

        }


        if(intent.hasExtra("extra_product_owner_id")){
            productOwnerId = intent.getStringExtra("extra_product_owner_id").toString()
        }

        if(Database().getUserID() == productOwnerId){
            btn_add_to_cart.visibility = View.GONE
            btn_go_to_cart.visibility = View.GONE

        }
        else{
            btn_add_to_cart.visibility = View.VISIBLE
        }


        getProductDetails()

        btn_add_to_cart.setOnClickListener(this)

        btn_go_to_cart.setOnClickListener {
            startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_product_menu,menu)
        if(editPerm == 0){
            //hide menu if user has no permission to edit product.(If its not user's product)
            val editMenu = menu!!.findItem(R.id.edit_product_menu_item)
            editMenu.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)

    }



    fun getProductDetails(){
        showProgressBar(getString(R.string.please_wait))
        Database().getProductDetails(this@ProductDetailsActivity,myProductId)

    }



    fun productDetailsSuccess(product: Product){
        myProductDetails = product
        LoadGlide(this@ProductDetailsActivity).loadProductImage(product.image,iv_product_detail_image)
        tv_product_details_available_quantity.text = product.stock_quantity
        tv_product_details_description.text = product.description
        tv_product_details_price.text = "$${product.price}"
        tv_product_details_title.text = product.title
        product.product_id = myProductId
        productModel = product

        if(product.stock_quantity.toInt() == 0){
            hideProgressBar()
            btn_add_to_cart.visibility = View.GONE
            tv_product_details_available_quantity.text = getString(R.string.lbl_out_of_stock)
            tv_product_details_available_quantity.setTextColor(ContextCompat.getColor(this@ProductDetailsActivity,
                R.color.colorSnackBarError
            ))
        }
        else{
            if(Database().getUserID() == product.user_id){
                hideProgressBar()
            }
            else{
                Database().checkIfItemExistsInCart(this,product.product_id)
            }
        }


    }

    fun productExistsInCard(){
        hideProgressBar()
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }






    private fun setupActionBar() {

        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }



        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this@ProductDetailsActivity, AddProductActivity::class.java)
        intent.putExtra("edit",1)
        intent.putExtra("productModel",productModel)
        when (item.itemId){
            //EDIT PRODUCT
            R.id.edit_product_menu_item -> startActivity(intent)

        }
        return super.onOptionsItemSelected(item)
    }

    private fun addToCart(){
        val cartItem = Cart(
            Database().getUserID(),
            productOwnerId,
            myProductId,
            myProductDetails.title,
            myProductDetails.price,
            myProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressBar(getString(R.string.please_wait))
        Database().addItemToCart(this,cartItem)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
            }

        }
    }

    fun addToCartSuccess(){
        hideProgressBar()
        Toast.makeText(this,getString(R.string.add_to_cart_success),Toast.LENGTH_LONG).show()
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }







}