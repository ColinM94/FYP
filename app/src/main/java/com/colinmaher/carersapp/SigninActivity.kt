package com.colinmaher.carersapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signin.*
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.extensions.toast
import com.google.firebase.auth.FirebaseAuth


class SigninActivity : AppCompatActivity(){

    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // ---------------------------------------
        //         Test code
           // edittext_signin_email.setText("colinmaher94@gmail.com")
           // edittext_signin_password.setText("password")
            //signin()
        // --------------------------------------


        button_signin_signin.setOnClickListener {
            signin()
        }

        textview_signin_noaccount.setOnClickListener{
            noAccount()
        }
    }

    // Handles user signin.
    private fun signin()
    {
        showSpinner()

        val email = edittext_signin_email.text.toString()
        val password = edittext_signin_password.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            toast("Please fill in all fields")
            log("All fields not filled")

            return
        }

        log("Attempting to log in with email: $email & password: $password")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    hideSpinner()
                    return@addOnCompleteListener
                } else if(user?.isEmailVerified == false){
                    hideSpinner()
                    log("Email not verified")
                    verifyEmail()
                } else {
                    log("Signin Successful: ${it.result?.user?.uid}")

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // Clears back stack. Prevents returning to signin using back button.
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

    // Opens Signup activity.
    private fun noAccount()
    {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    // Loading spinner.
    private fun showSpinner(){
        progressbar_signin_spinner.visibility = View.VISIBLE
    }

    private fun hideSpinner(){
        progressbar_signin_spinner.visibility = View.INVISIBLE
    }
}
