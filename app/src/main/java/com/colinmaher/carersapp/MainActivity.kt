package com.colinmaher.carersapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.colinmaher.carersapp.fragments.ClientsFragment
import com.colinmaher.carersapp.fragments.VisitsFragment
import com.colinmaher.extensions.log
import com.colinmaher.extensions.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val STATE_HELPER = "helper"
    }

    private lateinit var stateHelper: FragmentStateHelper

    private val fragments = mutableMapOf<Int, Fragment>()

    private val navigationSelectionListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        val newFragment = if(fragments[item.itemId] != null){
            fragments[item.itemId] ?: VisitsFragment()
        }else{
            VisitsFragment()
        }
        fragments[item.itemId] = newFragment

        if (navigation.selectedItemId != 0) {
            log("NAV LISTENER: Saving Current State Fragment: $newFragment | Item: ${item.itemId} ")
            saveCurrentState()
            stateHelper.restoreState(newFragment, item.itemId)

        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, newFragment)
            .commitNowAllowingStateLoss()

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stateHelper = FragmentStateHelper(supportFragmentManager)

        navigation.setOnNavigationItemSelectedListener(navigationSelectionListener)

        if (savedInstanceState == null) {
            navigation.selectedItemId = R.id.navigation_visits
        } else {
            val helperState = savedInstanceState.getBundle(STATE_HELPER)
            stateHelper.restoreHelperState(helperState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Make sure we save the current tab's state too!
        saveCurrentState()

        outState.putBundle(STATE_HELPER, stateHelper.saveHelperState())

        super.onSaveInstanceState(outState)
    }

    private fun saveCurrentState() {
        fragments[navigation.selectedItemId]?.let { oldFragment->
            stateHelper.saveState(oldFragment, navigation.selectedItemId)
        }
    }
}


/*
//val newFragment = VisitsFragment()
        log(item.itemId.toString())


        when(item.itemId){
            R.id.navigation_clients -> {
                saveCurrentState()
                stateHelper.restoreState(VisitsFragment(), item.itemId)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, VisitsFragment())
                    .commitNowAllowingStateLoss()
            }
            R.id.navigation_visits -> {
                saveCurrentState()
                stateHelper.restoreState(VisitsFragment(), item.itemId)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, VisitsFragment())
                    .commitNowAllowingStateLoss()
            }
            R.id.navigation_profile -> {
                saveCurrentState()
                stateHelper.restoreState(VisitsFragment(), item.itemId)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, VisitsFragment())
                    .commitNowAllowingStateLoss()
            }
            R.id.navigation_settings -> {
                saveCurrentState()
                stateHelper.restoreState(SettingsFragment(), item.itemId)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment())
                    .commitNowAllowingStateLoss()
            }
            else -> {
                saveCurrentState()
                stateHelper.restoreState(VisitsFragment(), item.itemId)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, VisitsFragment())
                    .commitNowAllowingStateLoss()
            }
        }

        true
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stateHelper = FragmentStateHelper(supportFragmentManager)

        navigation.setOnNavigationItemSelectedListener(navigationSelectionListener)

        val helperState = savedInstanceState.getBundle(STATE_HELPER)
        stateHelper.restoreHelperState(helperState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Make sure we save the current tab's state too!
        saveCurrentState()

        outState.putBundle(STATE_HELPER, stateHelper.saveHelperState())

        super.onSaveInstanceState(outState)
    }

    private fun saveCurrentState() {
        fragments[navigation.selectedItemId]?.let { oldFragment->
            stateHelper.saveState(oldFragment, navigation.selectedItemId)
        }
    }
}



/*package com.colinmaher.carersapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.colinmaher.carersapp.fragments.*
import log
import toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity(){

    //private val manager = supportFragmentManager
    private val db = FirebaseFirestore.getInstance()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var userInfo = null

    lateinit var callsFragment: CallsFragment
    lateinit var chatFragment : ChatFragment

    private val fragmentSavedStates = mutableMapOf<String, Fragment.SavedState?>()

    //private

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        verifyUser()

        //navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //createFragments()
        //callsFragment = CallsFragment().newInstance()
       // chatFragment =

        //loadData()

        //createFragments()
        //switchFragment("calls")

        var int = 10
        

    }

    private var authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser

        if(user != null) {
            toast("Welcome")

        }
    }

    override fun onStart(){
        super.onStart()
//        firebaseAuth.addAuthStateListener(authStateListener)

    }

    override fun onPause(){
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun createFragments(){
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentholder, CallsFragment())
            .add(R.id.fragmentholder, ClientsFragment())
            .add(R.id.fragmentholder, ChatFragment())
            .add(R.id.fragmentholder, ProfileFragment())
            .add(R.id.fragmentholder, SettingsFragment())
            .commit()


    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_calls -> {
                switchFragment("calls")

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_clients -> {
                switchFragment("clients")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chat -> {
                switchFragment("chat")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                switchFragment("profile")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                switchFragment("preferences")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    // Unloads fragments on switch.
    private fun switchFragment(option:String){
        val transaction = supportFragmentManager.beginTransaction()

        when(option){
            "calls" -> {
                transaction.replace(R.id.fragmentholder, CallsFragment(), "CALLS_TAG")
                    .addToBackStack("CALLS_TAG")
            }

            "clients" -> {
                transaction.replace(R.id.fragmentholder, ClientsFragment(), "CLIENTS_TAG")
                    .addToBackStack("CLIENTS_TAG")
            }
            "chat" -> {
                transaction.replace(R.id.fragmentholder, ChatFragment(), "CHAT_FRAGMENT")
                    .addToBackStack("CHAT_FRAGMENT")
            }
            "profile" -> {
                transaction.replace(R.id.fragmentholder, ProfileFragment(), "PROFILE_FRAGMENT")
                    .addToBackStack("PROFILE_FRAGMENT")

            }
            "preferences" -> {
                transaction.replace(R.id.fragmentholder, SettingsFragment(), "PREFERENCES_FRAGMENT")
                    .addToBackStack("PREFERENCES_FRAGMENT")
            }
        }

        transaction.commit()
    }

    // Keeps fragments loaded in the background.
    private fun switchFragmentPersistent(option:String){
        when(option){
            "calls" -> {
                if(supportFragmentManager.findFragmentByTag("calls") != null){
                    log("Switching to calls")
                    supportFragmentManager.beginTransaction().show(CallsFragment()).commit()

                }else {
                    log("Creating calls")
                    supportFragmentManager.beginTransaction().add(R.id.fragmentholder, CallsFragment(), "calls").commit()
                }
            }
            "clients" -> {
                if(supportFragmentManager.findFragmentByTag("clients") != null){
                    supportFragmentManager.beginTransaction().show(ClientsFragment()).commit()
                    log("Switching to clients")

                }else {
                    log("Creating clients")
                    supportFragmentManager.beginTransaction().add(R.id.fragmentholder, ClientsFragment(), "clients").commit()
                }
            }
            "chat" -> {
                if(supportFragmentManager.findFragmentByTag("chat") != null){
                    log("Switching to chat")
                    supportFragmentManager.beginTransaction().show(ChatFragment()).commit()

                }else {
                    log("Creating chat")
                    supportFragmentManager.beginTransaction().add(R.id.fragmentholder, ChatFragment(), "chat").commit()
                }
            }
            "profile" -> {
                if(supportFragmentManager.findFragmentByTag("profile") != null){
                    supportFragmentManager.beginTransaction().show(ProfileFragment()).commit()

                }else {
                    supportFragmentManager.beginTransaction().add(R.id.fragmentholder, ProfileFragment(), "profile").commit()
                }
            }
            "settings" -> {
                if(supportFragmentManager.findFragmentByTag("settings") != null){
                    supportFragmentManager.beginTransaction().show(SettingsFragment()).commit()

                }else {
                    supportFragmentManager.beginTransaction().add(R.id.fragmentholder, SettingsFragment(), "settings").commit()
                }
            }
        }

        if(option != "calls"){
            supportFragmentManager.beginTransaction().detach(CallsFragment()).commit()
            log("Hiding calls")
        }

        if(option != "clients"){
            supportFragmentManager.beginTransaction().detach(ClientsFragment()).commit()
            log("Hiding clients")
        }

        if(option != "chat"){
            supportFragmentManager.beginTransaction().detach(ChatFragment()).commit()
            log("Hiding chat")
        }

        if(option != "profile"){
            supportFragmentManager.beginTransaction().detach(ProfileFragment()).commit()
            log("Hiding profile")
        }


        if(option != "settings"){
            supportFragmentManager.beginTransaction().detach(SettingsFragment()).commit()
            log("Hiding settings")
        }


    }

    // If user is not signed in, ends session and returns user to signin screen.
    private fun verifyUser()
    {
        val uid = FirebaseAuth.getInstance().uid

        if(uid == null){
            signOut()
        }
    }

    private fun loadData(){
        log("Loading data")
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    log("${document.id} => ${document.data}")


                    //edittext_profile_name.setText()
                }
            }
            .addOnFailureListener { e ->
                log("Error getting users: $e")
            }
    }

    private fun getDocument(collection: String, docId: String){
        log("Getting document $docId from $collection")
        val docRef = db.collection(collection).document(docId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    log("DocumentSnapshot data: ${document.data}")
                } else {
                    log("No such document")
                }
            }
            .addOnFailureListener { e ->
                log("get failed with $e")
            }
    }

    // Handles requests from fragments. Should be replaced with interfaces or ViewModel.
    fun fragmentMessage(option:String) {
        when(option){
            "signOut" -> signOut()
        }
    }

    private fun signOut(){
        val intent = Intent(this, SigninActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // Clears back button stack.
        startActivity(intent)

        FirebaseAuth.getInstance().signOut()
    }
}
*/