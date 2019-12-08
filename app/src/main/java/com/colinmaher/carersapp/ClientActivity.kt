package com.colinmaher.carersapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.extensions.log

import kotlinx.android.synthetic.main.activity_client.*

class ClientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        //setSupportActionBar(toolbar)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.title = "Client Profile"

        val id = intent.getStringExtra("id")
        log(id)
    }
}
