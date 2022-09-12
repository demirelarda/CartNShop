package com.mycompany.cartnshop.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mycompany.cartnshop.R
import com.mycompany.cartnshop.database.Database
import com.mycompany.cartnshop.databinding.ActivityRegisterBinding
import com.mycompany.cartnshop.model.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.progress_bar.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var myProgressDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        btn_register.setOnClickListener {
            registerUser()

        }


        setSupportActionBar(toolbar_register_activity)
        val actionBar = supportActionBar
        if(actionBar!=null){

            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)


        }
        toolbar_register_activity.setNavigationOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }




        tv_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun checkRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_first_name),Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_last_name),Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_email),Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_password),Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_enter_confirm_password),Toast.LENGTH_LONG).show()
                false
            }

            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),Toast.LENGTH_LONG).show()
                false
            }
            !cb_terms_and_condition.isChecked -> {
                Toast.makeText(this,resources.getString(R.string.err_msg_agree_terms_and_conditions),Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun registerUser(){

        if(checkRegisterDetails()){
            showProgressBar()
            val email : String = et_email.text.toString().trim{it<= ' '}
            val password : String = et_password.text.toString().trim{it<= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {task->
                    if(task.isSuccessful){
                        val currentUser = Firebase.auth.currentUser
                        val firstName = et_first_name.text.toString()
                        val lastName = et_last_name.text.toString()
                        val email = et_email.text.toString()

                        val user = User(currentUser!!.uid,firstName,lastName,email)
                        Database().registerUser(this@RegisterActivity, user)


                    }else{
                        hideProgressBar()
                        Toast.makeText(this,"Registration failed.",Toast.LENGTH_LONG).show()
                    }

                }




        }


    }

    fun userRegistrationSuccess() {
        hideProgressBar()
        Toast.makeText(this, "You've registered successfully.", Toast.LENGTH_LONG).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }


    fun showProgressBar() {
        myProgressDialog = Dialog(this)
        myProgressDialog.setContentView(R.layout.progress_bar)
        myProgressDialog.setCanceledOnTouchOutside(false)
        myProgressDialog.setCancelable(false)
        myProgressDialog.tv_progress_text.setText(R.string.please_wait)
        myProgressDialog.show()
    }

    fun hideProgressBar() {
        myProgressDialog.dismiss()
    }





}

