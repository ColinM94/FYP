package com.colinmaher.carersapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.colinmaher.carersapp.ClientActivity
import com.colinmaher.carersapp.MainActivity
import com.colinmaher.carersapp.R
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.models.ClientItem
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.client_item.view.*
import kotlinx.android.synthetic.main.fragment_clients.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ClientsFragment(private var currentUser: FirebaseUser, private var db: FirebaseFirestore) : Fragment() {
    private lateinit var adapter: ClientAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
    }

    private fun populateList(clients : MutableList<ClientItem>){
        adapter = ClientAdapter(this.context!!)
        adapter.replaceItems(clients)

        recyclerview_client.adapter = adapter

        // Adds divider line between items.
        recyclerview_client.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

       (activity as MainActivity).hideSpinner()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        log("Instance saved")
    }

    private fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {

            withContext(Dispatchers.Main){(activity as MainActivity).hideSpinner()}

            val docRef = db.collection("connections").document(currentUser.uid)
            var clientIds = ArrayList<String>()
            val clients = mutableListOf<ClientItem>()

            docRef.get()
                .addOnSuccessListener { document ->
                    clientIds = document.get("clients") as ArrayList<String>
                    log("firebase complete")
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                }.await()

            clientIds.forEach{ id ->
                log("Foreach")
                val client = db.collection("clients").document(id).get().await().toObject(ClientItem::class.java)!!
                client.id = id
                clients.add(client)
            }

            withContext(Dispatchers.Main) {
                populateList(clients)
            }
        }
    }
}

class ClientAdapter(val context: Context) : RecyclerView.Adapter<ClientAdapter.ViewHolder>(){
    private var items = listOf<ClientItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.client_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

       // selectedId = item.id
        holder.containerView.textview_client_name.text = item.name
        holder.containerView.textview_client_mobile.text = item.mobile

        holder.containerView.setOnClickListener {
            val intent = Intent(context, ClientActivity::class.java)
            intent.putExtra("id", item.id)
            context.startActivity(intent)
        }
    }

    fun replaceItems(items: List<ClientItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}