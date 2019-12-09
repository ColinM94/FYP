package com.colinmaher.carersapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.models.Client
import com.colinmaher.carersapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_client.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ClientActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        val clientId = intent.getStringExtra("id")

        CoroutineScope(Dispatchers.IO).launch {
            // Get client details.
            val client = db.collection("clients").document("$clientId/data/details").get().await().toObject(Client::class.java)

            // Get client name and mobile.
            val temp = db.collection("clients").document(clientId).get().await().toObject(Client::class.java)

            if(client != null && temp != null){
                client.name = temp.name
                client.mobile = temp.mobile

                withContext(Dispatchers.Main) {
                    textview_client_name.text = client.name
                    textview_client_address.text = "${client.address1}, ${client.address2}, ${client.town}, ${client.county}, ${client.eircode}"
                    textview_client_mobile.text = client.mobile
                    textview_client_marital.text = client.marital
                }
            }
        }
    }
}
