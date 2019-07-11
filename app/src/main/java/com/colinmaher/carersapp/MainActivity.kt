package com.colinmaher.carersapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.colinmaher.carersapp.Fragments.HomeFragment
import com.colinmaher.carersapp.Fragments.MapFragment
import com.colinmaher.carersapp.Fragments.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home_old.*

class MainActivity : AppCompatActivity() {

    private val manager = supportFragmentManager

    //private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //textMessage.setText(R.string.title_home)
                createFragment("home")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                //textMessage.setText(R.string.title_dashboard)
                createFragment("map")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //textMessage.setText(R.string.title_notifications)
                createFragment("settings")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar!!.title = "Carers App"

        createFragment("home")

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        Log.d("Debug", "Home activity started")

        // Check if user logged in.
        verifyUserLogin()

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private fun createFragment(option:String){
        val transaction = manager.beginTransaction()

        when(option){
            "home" -> transaction.replace(R.id.fragmentholder, HomeFragment())
            "map" -> transaction.replace(R.id.fragmentholder, MapFragment())
            "settings" -> transaction.replace(R.id.fragmentholder, SettingsFragment())
        }

        transaction.addToBackStack(null)
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
