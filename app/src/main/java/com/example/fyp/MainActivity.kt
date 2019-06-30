package com.example.fyp

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.TouchDelegate
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_login_loginBtn.setOnClickListener {
            val email = editText_login_email.text.toString()
            val password = editText_login_password.text.toString()

            Log.d("Debug", "Attempting to log in with email: $email & password: $password")
        }

        textView_login_noAccount.setOnClickListener{
            Log.d("Debug", "Create account button clicked")

            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}