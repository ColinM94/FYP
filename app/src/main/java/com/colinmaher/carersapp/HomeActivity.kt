package com.colinmaher.carersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_registration.*

class HomeActivity : AppCompatActivity() {

    private val user = FirebaseAuth.getInstance().currentUser
    class User(val uid: String, val name: kotlin.String, val profileImageUrl: kotlin.String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Log.d("Debug", "Home activity started")

        // Check if user logged in.
        if(user == null)
            logout()

        user?.sendEmailVerification()?.addOnCompleteListener{
            Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
        }

        button_home_logout.setOnClickListener{
            logout()
        }
    }

    private fun loginMsg()
    {
        val uid = FirebaseAuth.getInstance().uid ?: ""



        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val name = ref.child("name")

        textview_home_loginmessage.text = "Welcome, ${name}"
    }

    // Ends session and returns user to login screen.
    private fun logout()
    {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
