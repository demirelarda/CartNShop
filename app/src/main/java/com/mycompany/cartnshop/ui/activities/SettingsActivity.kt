package com.mycompany.cartnshop.ui.activities

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.model.User
import com.mycompany.cartnshop.utils.Constants
import com.mycompany.cartnshop.utils.LoadGlide
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.iv_user_photo

class SettingsActivity : UiComponentsActivity() {

    private lateinit var myUserDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar_settings_activity)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        toolbar_settings_activity.setNavigationOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_logout.setOnClickListener {
            showProgressBar("Logging Out...")
            Firebase.auth.signOut()
            hideProgressBar()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        tv_edit.setOnClickListener {

            val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
            intent.putExtra("fromSettings",1) //inform userprofile that we are coming from settings.
            intent.putExtra(Constants.EXTRA_USER_DETAILS,myUserDetails)
            startActivity(intent)
        }

        ll_address.setOnClickListener{
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
        }



    }




    private fun getUserDetails(){
        showProgressBar(resources.getString(R.string.please_wait))
        Database().getCurrentUserDetails(this)


    }

    fun userDetailsSuccess(user: User){
        myUserDetails = user
        hideProgressBar()
        LoadGlide(this@SettingsActivity).loadUserPicture(user.image,iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_gender.text = user.gender
        tv_email.text = user.email
        tv_mobile_number.text = user.mobile.toString()

    }


    override fun onResume() {
        super.onResume()
        getUserDetails()
    }










}