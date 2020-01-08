package com.colinmaher.carersapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /*
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("Debug", "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            circleimageview_signup_selectphoto.setImageBitmap(bitmap)
            button_signup_selectphoto.alpha = 0f
        }
        */
    }

    private fun selectPhoto() {
        Log.d("Debug", "Photo selector")

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun signup() {
        val username = edittext_signup_name.text.toString()
        val email = edittext_signup_email.text.toString()
        val password = edittext_signup_password.text.toString()

        // Prevents registration if any fields are empty.
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(
            "Debug",
            "Attempting to signup with username: $username email: $email & password: $password"
        )

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    Log.d("Debug", "Successfully created user with uid: ${it.result?.user?.uid}")
                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()

                    //uploadImage()

                    saveUserToDatabase()

                    val intent = Intent(this, SigninActivity::class.java)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Log.d("Debug", "Failed to create user: ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
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

        db.collection("users").document(uid).set(data, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Debug", "Successfully saved user to database")
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
}
