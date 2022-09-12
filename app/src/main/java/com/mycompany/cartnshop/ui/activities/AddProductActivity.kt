package com.mycompany.cartnshop.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.databinding.ActivityAddProductBinding
import com.mycompany.cartnshop.model.Product
import com.mycompany.cartnshop.utils.Constants
import com.mycompany.cartnshop.utils.LoadGlide
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_user_profile.view.*

class AddProductActivity : UiComponentsActivity() {

    private lateinit var binding : ActivityAddProductBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    private var productURL: String = ""
    private var edit : Int = 0
    private lateinit var productModel : Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()
        setContentView(R.layout.activity_add_product)

        setSupportActionBar(toolbar_add_product_activity)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        toolbar_add_product_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        iv_product_image.setOnClickListener {
            imageViewClicked()
        }

        btn_send_product.setOnClickListener {
            if(checkProductDetails()){
                uploadProductImage()
            }
        }

        if(intent.hasExtra("edit")){
            edit = intent.getIntExtra("edit",0)
        }

        if(edit == 1){
            btn_send_product.text = getString(R.string.update_product_btn)
            toolbar_add_product_activity.tv_title.text = getString(R.string.edit_product_title)
            iv_product_image.isClickable = false
            productModel = intent.getParcelableExtra<Product>("productModel")!!
            et_product_title.setText(productModel.title)
            et_product_price.setText(productModel.price)
            et_product_description.setText(productModel.description)
            et_product_quantity.setText(productModel.stock_quantity)
            LoadGlide(this).loadProductImage(productModel.image,iv_product_image)
            btn_send_product.setOnClickListener {
                updateProduct()
            }

        }





    }

    private fun imageViewClicked(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(this.et_product_price.rootView,getString(R.string.permission_needed_for_gallery), Snackbar.LENGTH_INDEFINITE).setAction(getString(
                    R.string.give_permission
                )){
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
                    iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(this@AddProductActivity,
                        R.drawable.ic_baseline_edit_24
                    ))
                    intentFromResult.data
                    selectedPicture = intentFromResult.data
                    selectedPicture.let {
                        LoadGlide(this).loadUserPicture(selectedPicture!!,iv_product_image)
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
                Toast.makeText(this@AddProductActivity,getString(R.string.permission_needed), Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun checkProductDetails(): Boolean {
        return when {

            selectedPicture == null -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_select_product_image),Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_product_title.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_product_title),Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_product_price.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_product_price),Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_product_description.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,
                    resources.getString(R.string.err_msg_enter_product_description),
                    Toast.LENGTH_LONG
                ).show()
                false
            }

            TextUtils.isEmpty(et_product_quantity.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,
                    resources.getString(R.string.err_msg_enter_product_quantity),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun uploadProductImage(){
        showProgressBar(resources.getString(R.string.please_wait))
        Database().uploadImageToStorage(this@AddProductActivity,selectedPicture!!,"Product_Image")


    }


    fun imageUploadSuccess(imageURL : String){
        hideProgressBar()
        productURL = imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails(){
        val username = this.getSharedPreferences(
            Constants.SHOP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.CURRENT_NAME,"")!!

        val product = Product(
            Database().getUserID(),
            username,
            et_product_title.text.toString().trim { it <= ' ' },
            et_product_price.text.toString().trim { it <= ' ' },
            et_product_description.text.toString().trim { it <= ' ' },
            et_product_quantity.text.toString().trim { it <= ' ' },
            productURL
        )

        Database().uploadProductDetails(this,product)


    }

    fun productUploadSuccess(){
        hideProgressBar()
        Toast.makeText(this,getString(R.string.product_uploaded_success_message),Toast.LENGTH_LONG).show()
        finish()

    }

    private fun updateProduct(){
        showProgressBar(getString(R.string.please_wait))
        val productList = HashMap<String,Any>()
        productList.put("description",et_product_description.text.toString())
        productList.put("price",et_product_price.text.toString())
        productList.put("title",et_product_title.text.toString())
        productList.put("stock_quantity",et_product_quantity.text.toString())

        Database().updateProduct(this,productList,productModel)

    }

    fun productUpdateSuccess(){
        hideProgressBar()
        Toast.makeText(this,getString(R.string.product_updated_successfully),Toast.LENGTH_LONG).show()

    }







}