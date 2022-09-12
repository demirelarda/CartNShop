package com.mycompany.cartnshop.ui.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.databinding.ActivityUserProfileBinding
import com.mycompany.cartnshop.model.User
import com.mycompany.cartnshop.utils.Constants
import com.mycompany.cartnshop.utils.LoadGlide
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.iv_user_photo
import kotlinx.android.synthetic.main.activity_user_profile.view.*
import kotlinx.android.synthetic.main.progress_bar.*

class UserProfileActivity : AppCompatActivity() {
    private lateinit var myUserDetails : User
    private lateinit var binding : ActivityUserProfileBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    var userProfileImageURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()


        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            myUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }


        val comingFromSettings = intent.getIntExtra("fromSettings",0)
        if(comingFromSettings == 1){
            toolbar_user_profile_activity.tv_title.text = getString(R.string.edit_profile)
            setupActionBar()
            LoadGlide(this@UserProfileActivity).loadUserPicture(myUserDetails.image, iv_user_photo)
            et_first_name.setText(myUserDetails.firstName)
            et_last_name.setText(myUserDetails.lastName)

            et_email.isEnabled = false
            et_email.setText(myUserDetails.email)

            if (myUserDetails.mobile != 0L) {
                et_mobile_number.setText(myUserDetails.mobile.toString())
            }
            if (myUserDetails.gender == "Male") {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }

            
        }

        et_first_name.isEnabled = false
        et_first_name.setText(myUserDetails.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(myUserDetails.lastName)

        et_email.isEnabled = false
        et_email.setText(myUserDetails.email)






        iv_user_photo.setOnClickListener {
            imageViewClicked(view)

        }

        btn_submit.setOnClickListener {
            if(checkUserDetails()){
                showProgressBar()
                if(selectedPicture != null){
                    Database().uploadImageToStorage(this,selectedPicture!!,"User_Profile_Image")
                }
                else{
                    updateUserProfileDetails()
                }
            }


        }


    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_user_profile_activity)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        toolbar_user_profile_activity.setNavigationOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    private fun updateUserProfileDetails(){

        val userDetailsHashMap = HashMap<String, Any>()

        val mobileNumber = et_mobile_number.text.toString()
        val firstName = et_first_name.text.toString().trim { it <= ' ' }
        if (firstName != myUserDetails.firstName) {
            userDetailsHashMap["firstName"] = firstName
        }

        val lastName = et_last_name.text.toString().trim { it <= ' ' }
        if (lastName != myUserDetails.lastName) {
            userDetailsHashMap["lastName"] = lastName
        }


        val gender = if(rb_male.isChecked){
            "Male"
        }else{
            "Female"
        }
        if(mobileNumber.isNotEmpty() && mobileNumber != myUserDetails.mobile.toString()){
            userDetailsHashMap["mobile"] = mobileNumber.toLong()
        }
        else{
            //Toast.makeText(this,"Don't leave the entry blank.",Toast.LENGTH_LONG).show()
        }
        userDetailsHashMap["gender"] = gender
        userDetailsHashMap["profileCompleted"] = 1
        if(userProfileImageURL.isNotEmpty()){
            userDetailsHashMap["image"] = userProfileImageURL
        }


        Database().updateProfileDetails(this,userDetailsHashMap)

    }

    fun userDetailsUpdateSuccess(){
        hideProgressBar()
        Toast.makeText(this,"Your profile has been updated successfully.",Toast.LENGTH_LONG).show()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }



    private fun imageViewClicked(view: View){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission Needed For Gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intentToGallery)

        }

    }




    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    intentFromResult.data
                    selectedPicture = intentFromResult.data
                    selectedPicture.let {
                        LoadGlide(this).loadUserPicture(selectedPicture!!,iv_user_photo)
                    }
                }
            }

        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //permission denied
                Toast.makeText(this@UserProfileActivity,"Permission Required", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun checkUserDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_mobile_number.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,"Please enter your phone number.",Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true //everything is ok
            }
        }
    }

    fun imageUploadSuccess(imageURL : String){
        userProfileImageURL = imageURL
        updateUserProfileDetails()
    }

    fun showProgressBar() {
        myProgressDialog = Dialog(this)
        myProgressDialog.setContentView(R.layout.progress_bar)
        myProgressDialog.tv_progress_text.setText(R.string.please_wait)
        myProgressDialog.setCancelable(false)
        myProgressDialog.setCanceledOnTouchOutside(false)
        myProgressDialog.show()
    }

    fun hideProgressBar() {
        myProgressDialog.dismiss()
    }



}