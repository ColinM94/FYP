package com.colinmaher.carersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        log("Login activity started")

        // Test code.
        edittext_login_email.setText("colinmaher94@gmail.com")
        edittext_login_password.setText("password")

        button_login_login.setOnClickListener {
            login()
        }

        textView_login_noaccount.setOnClickListener{
            noAccount()
        }
    }

    // Handles user login.
    private fun login()
    {
        val email = edittext_login_email.text.toString()
        val password = edittext_login_password.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            toast("Please fill in all fields")
            log("All fields not filled")

            return
        }

        log("Attempting to log in with email: $email & password: $password")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                } else if(user?.isEmailVerified == false){
                    log("Email not verified")
                    verifyEmail()
                } else {
                    log("Login Successful: ${it.result?.user?.uid}")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                log("${it.message}")
                toast("${it.message}")
            }
    }

    // Firebase email verification.
    private fun verifyEmail()
    {
        if(user?.isEmailVerified == true)
            return

        user?.sendEmailVerification()
            ?.addOnSuccessListener {
                toast("Please verify email to log in. Verification email sent to ${user.email}")
                log("Verification email sent to ${user.email}")
            }
            ?.addOnFailureListener {
                toast("Verification email failed to send. ${it.message}")
                log("Verification email failed to send ${it.message}")
            }
    }

    // Opens registration activity.
    private fun noAccount()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    // Writes debug messages to Logcat.
    private fun log(msg: String)
    {
        Log.d("Debug", msg)
    }

    // Creates Toast messages to display to user.
    private fun toast(msg: String)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}