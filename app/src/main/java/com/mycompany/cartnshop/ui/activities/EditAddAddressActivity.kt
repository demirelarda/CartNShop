package com.mycompany.cartnshop.ui.activities


import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.model.Address
import kotlinx.android.synthetic.main.activity_edit_add_address.*

class EditAddAddressActivity : UiComponentsActivity() {

    private var lastAddressDetails: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_add_address)

        setSupportActionBar(toolbar_add_edit_address_activity)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        toolbar_add_edit_address_activity.setNavigationOnClickListener {onBackPressed()}

        if(intent.hasExtra("extra_address_details")){
            lastAddressDetails = intent.getParcelableExtra("extra_address_details")
        }
        if(lastAddressDetails != null){
            if(lastAddressDetails!!.id.isNotEmpty()){
                tv_title.text = resources.getString(R.string.title_edit_address)
                btn_submit_address.text = resources.getString(R.string.btn_lbl_update)

                et_full_name.setText(lastAddressDetails?.fullName)
                et_phone_number.setText(lastAddressDetails?.mobileNumber)
                et_address.setText(lastAddressDetails?.address)
                et_zip_code.setText(lastAddressDetails?.zipCode)
                et_additional_note.setText(lastAddressDetails?.additionalNote)

                when (lastAddressDetails?.type) {
                    "Home" -> {
                        rb_home.isChecked = true
                    }
                    "Office" -> {
                        rb_office.isChecked = true
                    }
                    else -> {
                        rb_other.isChecked = true
                        til_other_details.visibility = View.VISIBLE
                        et_other_details.setText(lastAddressDetails?.otherDetails)
                    }
                }
            }
        }

        btn_submit_address.setOnClickListener {
            saveAddressToDatabase()
        }

        rg_type.setOnCheckedChangeListener {_,checkedId ->
            if(checkedId == R.id.rb_other){
                til_other_details.visibility = View.VISIBLE
            }else{
                til_other_details.visibility = View.GONE
            }
        }

    }



    private fun saveAddressToDatabase(){

        val fullName: String = et_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = et_phone_number.text.toString().trim { it <= ' ' }
        val address: String = et_address.text.toString().trim { it <= ' ' }
        val zipCode: String = et_zip_code.text.toString().trim { it <= ' ' }
        val additionalNote: String = et_additional_note.text.toString().trim { it <= ' ' }
        val otherDetails: String = et_other_details.text.toString().trim { it <= ' ' }

        if (validateData()) {

            showProgressBar(resources.getString(R.string.please_wait))

            val addressType: String = when {
                rb_home.isChecked -> {
                    "Home"
                }
                rb_office.isChecked -> {
                    "Office"
                }
                else -> {
                    "Other"
                }
            }


            val addressModel = Address(
                Database().getUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,
                otherDetails
            )

            if(lastAddressDetails != null && lastAddressDetails!!.id.isNotEmpty()){
                Database().updateAddress(this,addressModel,lastAddressDetails!!.id)
            }else{
                Database().addAddress(this,addressModel)
            }

        }

    }




    fun editAddAddressToDBSuccess(){
        hideProgressBar()
        val successMessage: String = if(lastAddressDetails!=null && lastAddressDetails!!.id.isNotEmpty()){
            resources.getString(R.string.msg_your_address_updated_successfully)
        }else{
            resources.getString(R.string.err_your_address_added_successfully)
        }
        Toast.makeText(this@EditAddAddressActivity,successMessage,Toast.LENGTH_LONG).show()
        setResult(RESULT_OK)
        finish()



    }



    private fun validateData(): Boolean {
        return when {

            TextUtils.isEmpty(et_full_name.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, R.string.err_msg_please_enter_full_name, Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_phone_number.text.toString().trim { it <= ' ' }) -> {

                Toast.makeText(this, R.string.err_msg_please_enter_phone_number, Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_address.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, R.string.err_msg_please_enter_address, Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_zip_code.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, R.string.err_msg_please_enter_zip_code, Toast.LENGTH_LONG).show()
                false
            }

            rb_other.isChecked && TextUtils.isEmpty(
                et_zip_code.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, R.string.err_msg_please_enter_zip_code, Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true
            }
        }
    }


}