package com.colinmaher.carersapp

import android.app.PendingIntent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SenderActivity : AppCompatActivity() {
    // NFC adapter for checking NFC state in the device
    private var nfcAdapter : NfcAdapter? = null

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null
    // Optional: filter NDEF tags this app receives through the pending intent.
    //private var nfcIntentFilters: Array<IntentFilter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        toast("NFC supported" + (nfcAdapter != null).toString())
        toast("NFC enabled" + (nfcAdapter?.isEnabled).toString())
    }

    // Creates Toast messages to display to user.
    private fun toast(msg: String)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

