package com.colinmaher.carersapp

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.extensions.toast
import com.colinmaher.carersapp.fragments.ClientsFragment
import com.colinmaher.carersapp.fragments.ProfileFragment
import com.colinmaher.carersapp.fragments.SettingsFragment
import com.colinmaher.carersapp.fragments.VisitsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity(){

    // Fragments
    private lateinit var visitsFragment: VisitsFragment
    private lateinit var clientsFragment: ClientsFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var settingsFragment: SettingsFragment

    private var manager = supportFragmentManager

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fragment initialisation.
        visitsFragment = VisitsFragment(currentUser, db)
        clientsFragment = ClientsFragment(currentUser, db)
        profileFragment = ProfileFragment(currentUser, db)
        settingsFragment = SettingsFragment()

        navigation.setOnNavigationItemSelectedListener(navigationSelectionListener)

        // Initial fragment to load.
        manager.beginTransaction()
            .replace(R.id.container, visitsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(
            Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        try {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
        }catch(e: Exception){

        }
    }

    override fun onPause() {
        super.onPause()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        try{
            nfcAdapter.disableForegroundDispatch(this)

        }catch(e: Exception){

        }
    }

    private suspend fun logThread(methodName: String){
        log("$methodName : ${Thread.currentThread().name}")
    }

    // Listens for navigation bar button presses.
    private val navigationSelectionListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when ("$item") {
            "Visits" -> {
                manager.beginTransaction()
                    .replace(R.id.container, visitsFragment)
                    //.addToBackStack(clientsFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
            "Clients" -> {
                log("Clients")
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
            //client.setText(intent.type)
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

    // Loading spinner.
    fun showSpinner(){
        progressbar_main_spinner.visibility = View.VISIBLE
    }

    fun hideSpinner(){
        progressbar_main_spinner.visibility = View.INVISIBLE
    }
}

