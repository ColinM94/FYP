package com.colinmaher.carersapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(){
    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar!!.title = "Carers App"

        createFragment("calls")

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Check if user logged in.
        verifyUserLogin()

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_calls -> {
                createFragment("calls")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_clients -> {
                createFragment("clients")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat -> {
                createFragment("chat")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                createFragment("profile")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                createFragment("preferences")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun createFragment(option:String){
        val transaction = manager.beginTransaction()

        when(option){
            "calls" -> transaction.replace(R.id.fragmentholder, HomeFragment())
            "clients" -> transaction.replace(R.id.fragmentholder, ClientsFragment())
            "messenger" -> transaction.replace(R.id.fragmentholder, ChatFragment())
            "profile" -> transaction.replace(R.id.fragmentholder, ProfileFragment())
            "preferences" -> transaction.replace(R.id.fragmentholder, SettingsFragment())

        }

        transaction.commit()
    }

    // Ends session and returns user to login screen.
    private fun verifyUserLogin()
    {
        val uid = FirebaseAuth.getInstance().uid

        if(uid == null){
            signOut()
        }
    }

    // Handles requests from fragments. Should be replaced with interfaces or ViewModel.
    fun fragmentMessage(option:String) {
        when(option){
            "signOut" -> signOut()
        }
    }

    private fun signOut(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // Clears back button stack.
        startActivity(intent)

        FirebaseAuth.getInstance().signOut()
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
