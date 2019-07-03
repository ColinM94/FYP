package com.colinmaher.carersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d("Debug", "Login activity")

        button_login_loginBtn.setOnClickListener {
            login()
        }

        textView_login_noAccount.setOnClickListener{
            noAccount()
        }
    }

    // Handles user login.
    private fun login()
    {
        val email = editText_login_email.text.toString()
        val password = editText_login_password.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Debug", "Attempting to log in with email: $email & password: $password")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    Log.d("Debug", "Login Successful: ${it.result?.user?.uid}")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Log.d("Debug", "${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Changes to registration activity.
    private fun noAccount()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}