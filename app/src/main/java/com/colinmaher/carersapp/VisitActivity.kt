package com.colinmaher.carersapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.extensions.log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_visit.*

class VisitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Visit Details"

        val id = intent.getStringExtra("id")
        textview_visit_name.text = id

    }
}
