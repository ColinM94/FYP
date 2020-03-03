package com.colinmaher.carersapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
            //edittext_signin_email.setText("colinmaher94@gmail.com")
            //edittext_signin_password.setText("password")
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
        // Hide keyboard.
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val email = edittext_signin_email.text.toString()
        val password = edittext_signin_password.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            toast("Please fill in all fields")
            log("All fields not filled")
            return
        }else{
            showSpinner()
        }

        log("Attempting to log in with email: $email & password: $password")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    hideSpinner()
                    return@addOnCompleteListener
                } else if(user?.isEmailVerified == false){
                    hideSpinner()
                    user?.sendEmailVerification()
                    toast("Please verify email to log in. Verification email sent to ${user.email}")
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
