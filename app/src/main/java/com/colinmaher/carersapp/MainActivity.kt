package com.colinmaher.carersapp

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.colinmaher.carersapp.fragments.ClientsFragment
import com.colinmaher.carersapp.fragments.ProfileFragment
import com.colinmaher.carersapp.fragments.SettingsFragment
import com.colinmaher.carersapp.fragments.VisitsFragment
import com.colinmaher.carersapp.helpers.log
import com.colinmaher.carersapp.helpers.toast
import com.colinmaher.carersapp.interfaces.SettingsInterface
import com.colinmaher.carersapp.models.User
import com.colinmaher.carersapp.models.Visit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_clients.*
import kotlin.math.log

class MainActivity : AppCompatActivity(){
    private val TAG = "Main"

    // Firebase
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    // Fragments
    private lateinit var visitsFragment: VisitsFragment
    private lateinit var clientsFragment: ClientsFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var settingsFragment: SettingsFragment

    private var manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fragment initialisation.
        visitsFragment = VisitsFragment()
        clientsFragment = ClientsFragment()
        profileFragment = ProfileFragment()
        settingsFragment = SettingsFragment()

        navigation.setOnNavigationItemSelectedListener(navigationSelectionListener)

        // Initial fragment to load.
        manager.beginTransaction()
            .replace(R.id.container, visitsFragment)
            //.addToBackStack(clientsFragment.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        log(currentUser)

    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(
            Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.disableForegroundDispatch(this)
    }

    fun getUserData(){
        val docRef = db.collection("users").document(currentUser)

       // log("current user " + currentUser)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    if(document.data != null){
                        var name = ""
                        var role = ""

                        if(document.data!!["name"] != null ){
                            name = document.data!!["name"] as String
                        }

                        if(document.data!!["role"] != null){
                            role = document.data!!["role"] as String
                        }

                        log("$role $name")
                        val user = User(name, role)
                        profileFragment.loadValues(user)
                    }

                } else {
                    log("No such document")
                }
            }
            .addOnFailureListener { e ->
                log("get failed with $e")
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

    fun getVisits(){
        log("$currentUser")
        //var visits : MutableList<Visit>

        var visits = mutableListOf<Visit>()

        db.collection("visits/$currentUser/visits")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    var name = document.data["name"] as String
                    var town = document.data["town"] as String

                    val visit = Visit(name, town)

                    visits.add(visit)

                }

                visitsFragment.populateList(visits)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    fun signOut(){
        val intent = Intent(this, SigninActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) // Clears back button stack.
        startActivity(intent)

        FirebaseAuth.getInstance().signOut()
    }


    // Listens for navigation bar button presses.
    private val navigationSelectionListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when ("$item") {
            "Visits" -> {
                log("Visits choses")
                manager.beginTransaction()
                    .replace(R.id.container, visitsFragment)
                    //.addToBackStack(clientsFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
            "Clients" -> {
                log("Clients selected")
                manager.beginTransaction()
                    .replace(R.id.container, clientsFragment)
                    //.addToBackStack(clientsFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
            "Profile" -> {
                manager.beginTransaction()
                    .replace(R.id.container, profileFragment)
                    //.addToBackStack(clientsFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()

            }
            "Settings" -> {
                manager.beginTransaction()
                    .replace(R.id.container, settingsFragment)
                    //.addToBackStack(clientsFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
            else -> {

            }
        }
       true
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            toast("MESSAGE: ${intent.type}")
            edittext_clients_search.setText(intent.type)
            val rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            if (rawMessage != null) {
                val message = arrayOfNulls<NdefMessage?>(rawMessage.size)// Array<NdefMessage>(rawMessages.size, {})
                for (i in rawMessage.indices) {
                    message[i] = rawMessage[i] as NdefMessage
                }
                // Process the messages array.
                processNdefMessages(message)
            }
        }
    }

    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>) {
        // Go through all NDEF messages found on the NFC tag
        for (curMsg in ndefMessages) {
            if (curMsg != null) {
                // Print generic information about the NDEF message
                log("Message " + curMsg.toString())
                // The NDEF message usually contains 1+ records - print the number of recoreds
                log("Records "+ curMsg.records.size.toString())

                // Loop through all the records contained in the message
                for (curRecord in curMsg.records) {
                    if (curRecord.toUri() != null) {
                        // URI NDEF Tag
                        log("- URI " +  curRecord.toUri().toString())
                    } else {
                        // Other NDEF Tags - simply print the payload
                        log("- Contents " +  curRecord.payload.contentToString())
                    }
                }
            }
        }
    }
}

