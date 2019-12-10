package com.colinmaher.carersapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.models.Client
import com.colinmaher.carersapp.models.Visit
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_client.*

import kotlinx.android.synthetic.main.activity_visit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class VisitActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Visit Details"

        val id = intent.getStringExtra("id")


        CoroutineScope(Dispatchers.IO).launch {
            // Get client details.
            val visit = db.collection("visitDetails").document(id).get().await().toObject(Visit::class.java)

            if(visit != null){
                withContext(Dispatchers.Main) {
                    textview_visit_starttime.text = visit.startTime!!.toDate().toString()
                    textview_visit_endtime.text = visit.endTime!!.toDate().toString()
                }
            }
        }

    }
}
