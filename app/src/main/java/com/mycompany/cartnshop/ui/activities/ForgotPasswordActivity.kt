package com.mycompany.cartnshop.ui.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mycompany.cartnshop.R
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.progress_bar.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var myProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        setSupportActionBar(toolbar_forgot_password_activity)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        toolbar_forgot_password_activity.setNavigationOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_send_reset_email.setOnClickListener {
            sendResetMail()
        }

    }

    fun sendResetMail(){
        val email = et_emailForgotPass.text.toString()
        showProgressBar()
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                hideProgressBar()
                if (task.isSuccessful) {
                    Toast.makeText(this,"An Email Has Been Sent to you, please check your mailbox.", Toast.LENGTH_LONG).show()
                    Toast.makeText(this,"Check Your Spam Folder If you can't see the mail.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this,"An error occurred! Please enter your email correctly.",Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun showProgressBar() {
        myProgressDialog = Dialog(this)
        myProgressDialog.setContentView(R.layout.progress_bar)
        myProgressDialog.setCanceledOnTouchOutside(false)
        myProgressDialog.setCancelable(false)
        myProgressDialog.tv_progress_text.setText(R.string.please_wait)
        myProgressDialog.show()
    }

    private fun hideProgressBar() {
        myProgressDialog.dismiss()
    }




}