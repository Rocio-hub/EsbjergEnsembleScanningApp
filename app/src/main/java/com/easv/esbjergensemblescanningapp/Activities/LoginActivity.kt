package com.easv.esbjergensemblescanningapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.easv.esbjergensemblescanningapp.Model.Users
import com.easv.esbjergensemblescanningapp.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    private lateinit var users: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        users = Users()

        textView_error.setVisibility(View.INVISIBLE)

        button_ok.setOnClickListener { v -> onClickOk() }
    }

    private fun onClickOk() {
        if(validateCode()){
            val code = editText_code.text.toString()
            if(code.isEmpty())
                textView_error.setVisibility(View.VISIBLE)
            else{
                textView_error.setVisibility(View.INVISIBLE)
                if(users.checkUserExists(code)!=null) {
                    val intent = Intent(this, ScannerActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    // Checking if the input in form is valid
    fun validateCode(): Boolean {
        if (editText_code.text.toString() == "") {
            textView_error.setVisibility(View.VISIBLE)
            return false
        }
        return true
    }
}
