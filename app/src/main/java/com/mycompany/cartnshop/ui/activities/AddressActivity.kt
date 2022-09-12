package com.mycompany.cartnshop.ui.activities

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.adapter.AddressAdapter
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.model.Address
import com.mycompany.cartnshop.utils.Constants
import com.mycompany.cartnshop.utils.SwipeToDelete
import com.mycompany.cartnshop.utils.SwipeToEdit
import kotlinx.android.synthetic.main.activity_address.*


class AddressActivity : UiComponentsActivity() {


    private var selectedAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        setSupportActionBar(toolbar_address_list_activity)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        toolbar_address_list_activity.setNavigationOnClickListener {onBackPressed()}

        tv_add_address.setOnClickListener {
            val intent = Intent(this@AddressActivity, EditAddAddressActivity::class.java)
            startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        }

        if(intent.hasExtra("extra_select_address")){
            selectedAddress = intent.getBooleanExtra("extra_select_address",false)
        }

        if(selectedAddress){
            tv_title.text = getString(R.string.title_select_address)
        }

        getAddressList()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            getAddressList()
        }
    }

    fun addressListFromDBSuccess(addressList: ArrayList<Address>){
        hideProgressBar()

        if(addressList.size > 0){
            tv_no_address_found.visibility = View.GONE
            rv_address_list.visibility = View.VISIBLE

            rv_address_list.layoutManager = LinearLayoutManager(this@AddressActivity)
            rv_address_list.setHasFixedSize(true)
            val addressAdapter = AddressAdapter(this,addressList,selectedAddress)
            rv_address_list.adapter = addressAdapter

            if(!selectedAddress){
                val editSwipeHandler = object: SwipeToEdit(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = rv_address_list.adapter as AddressAdapter
                        adapter.notifyEditItem(this@AddressActivity,viewHolder.adapterPosition)
                    }
                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(rv_address_list)

                val deleteSwipeHandler = object : SwipeToDelete(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showProgressBar(getString(R.string.please_wait))

                        Database().deleteAddress(this@AddressActivity,addressList[viewHolder.adapterPosition].id)
                    }
                }

                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(rv_address_list)
            }

        }else{
            tv_no_address_found.visibility = View.VISIBLE
        }

    }

    private fun getAddressList(){
        showProgressBar(getString(R.string.please_wait))
        Database().getAddresses(this)
    }

    fun deleteAddressSuccessful(){
        hideProgressBar()
        Toast.makeText(this, R.string.err_your_address_deleted_successfully,Toast.LENGTH_LONG).show()
        getAddressList()
    }










}