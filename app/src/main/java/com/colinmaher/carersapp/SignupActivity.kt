package com.colinmaher.carersapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.extensions.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        Log.d("Debug", "signup activity started")

        /*
        button_signup_selectphoto.setOnClickListener {
            selectPhoto()
        }
    */
        button_signup_signup.setOnClickListener {
            signup()
        }

        textview_signup_existingaccount.setOnClickListener {
            existingAccount()
        }
    }

    // private var selectedPhotoUri: Uri? = null

    private fun selectPhoto() {
        Log.d("Debug", "Photo selector")

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun signup() {

        // Hide keyboard.
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val username = edittext_signup_name.text.toString()
        val email = edittext_signup_email.text.toString()
        val password = edittext_signup_password.text.toString()

        // Prevents registration if any fields are empty.
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }else{
            showSpinner()
        }

        Log.d("Debug", "Attempting to signup with username: $username email: $email & password: $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    hideSpinner()
                    return@addOnCompleteListener
                } else {
                    hideSpinner()
                    Log.d("Debug", "Successfully created user with uid: ${it.result?.user?.uid}")

                    val user = it.result?.user

                    verifyEmail(user!!)

                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                    Log.d("Debug", "$it")


                    saveUserToDatabase()
                    existingAccount()
                }
            }
            .addOnFailureListener {
                Log.d("Debug", "Failed to create user: ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                hideSpinner()
            }
    }

    /*
    private fun uploadImage()
    {
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener{
                Log.d("Debug", "Successfully uploaded image: ${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener{
                    Log.d("Debug", "File location: $it")
                }
            }
            .addOnFailureListener {
                Log.d("Debug", "Error uploading image")
            }
    }
    */

    private fun saveUserToDatabase() {
        val db = FirebaseFirestore.getInstance()

        // ?: = Elvis operator, if null defaults to blank string.
        val uid = FirebaseAuth.getInstance().uid ?: ""

        val data = hashMapOf(
            "name" to edittext_signup_name.text.toString(),
            "role" to "Carer",
            "address1" to "",
            "address2" to "",
            "town" to "",
            "county" to "",
            "eircode" to "",
            "active" to true
        )

        db.collection("users").document(uid).set(data)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.d("Debug", "${it.message}")
            }
    }

    private fun existingAccount() {
        Log.d("Debug", "Existing account button clicked")

        val intent = Intent(this, SigninActivity::class.java)
        startActivity(intent)
    }

    // Firebase email verification.
    private fun verifyEmail(user: FirebaseUser)
    {
        if(user.isEmailVerified)
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

    // Loading spinner.
    private fun showSpinner(){
        progressbar_signup_spinner.visibility = View.VISIBLE
    }

    private fun hideSpinner(){
        progressbar_signup_spinner.visibility = View.INVISIBLE
    }
}
