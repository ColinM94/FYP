package com.colinmaher.carersapp

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.extensions.toast

class SenderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            toast("MESSAGE: ${intent.type}")
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

