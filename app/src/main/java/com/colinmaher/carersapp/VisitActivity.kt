package com.colinmaher.carersapp

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.extensions.toast
import com.colinmaher.carersapp.models.Client
import com.colinmaher.carersapp.models.Visit
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_visit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class VisitActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var nfcId = ""
    var clientId = ""
    var visitId = ""
    var visit: Visit? = null
    //private val clientId = intent.getStringExtra"id")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Visit Details"

        visitId = intent.getStringExtra("visitId")!!

        //val id = intent.getStringExtra("id")
        log(visitId)
        editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                log("NFC" + nfcId)

                if(nfcId == clientId){
                    if(editText2.text.toString() == "Clock In"){
                        clockIn()
                    }else{
                        clockOut()
                    }
                }else {
                    log("ClientID: " + clientId)
                    log("NFCID: " + nfcId)
                    toast("Incorrect Tag!")
                }
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            // Get client details.
            val doc = db.collection("visits").document(visitId).get().await()
            visit = doc.toObject(Visit::class.java)
            visit!!.id = doc.id

            val client = db.collection("clients").document(visit!!.clientId).get().await().toObject(Client::class.java)

            val datePattern = "dd/mm/yyyy"
            val timePattern = "HH:SS"
            val dateFormat = SimpleDateFormat(datePattern)
            val timeFormat = SimpleDateFormat(timePattern)

            if (client != null) {
                withContext(Dispatchers.Main) {
                    clientId = visit!!.clientId
                    textview_visit_date.text = visit!!.startDate
                    textview_visit_starttime.text = visit!!.startTime
                    textview_visit_endtime.text = visit!!.endTime
                    textview_visititem_name.text = client.name
                    textview_visit_address.text = "${client.address1}, ${client.address2}, ${client.town}, ${client.county}, ${client.eircode}"

                    for (note in visit!!.notes) {
                        textview_visit_notes.append(note + "\n")
                    }
                    //textview_visit_endtime.text = visit.endTime!!.toDate().toString()
                }
            }
        }
        btn_visit_savenote.setOnClickListener {
            saveNote()
        }
    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        try {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
        }catch(e: Exception){

        }
    }

    override fun onPause() {
        super.onPause()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            //textview_visit_clockintime.text = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
            val listItems = arrayOf("Clock In", "Clock Out")
            val mBuilder = AlertDialog.Builder(this@VisitActivity)
            mBuilder.setTitle("")
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
                editText2.setText(listItems[i])
                dialogInterface.dismiss()
            }
            // Set the neutral/cancel button click listener
            mBuilder.setNeutralButton("Cancel") { dialog, _ ->
                // Do something when click the neutral button
                dialog.cancel()
            }

            val mDialog = mBuilder.create()
            mDialog.show()

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

    private fun saveNote(){
        val notes = visit!!.notes
        val note = edittext_visit_note.text.toString()
        notes.add(note)

        db.collection("visits").document(visit!!.id).update("notes", notes)

        visit!!.notes = notes

        textview_visit_notes.text = ""
        for (note in visit!!.notes) {
            textview_visit_notes.append(note + "\n")
        }

    }

    private fun clockIn() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val clockInTime = current.format(formatter).toString()
        textview_visit_clockintime.text = clockInTime
        visit!!.clockInTime = clockInTime

        db.collection("visits").document(visit!!.id).update("clockInTime", clockInTime)
    }

    private fun clockOut(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val clockOutTime = current.format(formatter).toString()
        textview_visit_clockouttime.text = clockOutTime
        visit!!.clockOutTime = clockOutTime

        db.collection("visits").document(visit!!.id).update("clockOutTime", clockOutTime)
    }

    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>) {
        // Go through all NDEF messages found on the NFC tag
        for (curMsg in ndefMessages) {
            if (curMsg != null) {
                // Loop through all the records contained in the message
                for (curRecord in curMsg.records) {
                    if (curRecord.toUri() != null) {
                        // URI NDEF Tag
                        nfcId = curRecord.toUri().toString()
                        nfcId = nfcId.replace("http://", "")
                    }
                }
            }
        }
    }
}
