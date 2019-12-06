package com.colinmaher.carersapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.colinmaher.carersapp.MainActivity
import com.colinmaher.carersapp.R
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.models.Client
import com.colinmaher.carersapp.models.User
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
        var view = inflater.inflate(R.layout.fragment_clients, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
    }

    private fun populateList(clients : MutableList<Client>){
        adapter = ClientAdapter()
        adapter.replaceItems(clients)

        recyclerview_client.adapter = adapter
        // Adds divider between items.
        recyclerview_client.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            log("$currentUser")

            val clients = mutableListOf<Client>()

            var user = (activity as MainActivity).getDocument("users", currentUser.uid).toObject(User::class.java)
            log("$user")

            if(user != null){
               for(clientId in user.clients){
                   var client = db.collection("clients").document(clientId).get().await()
                   clients.add(client.toObject(Client::class.java)!!)
               }

                withContext(Dispatchers.Main) {
                    populateList(clients)
                }
            }
        }
    }

    fun openProfile(){
        log("profile opened")
    }

    private fun toast(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}

class ClientAdapter : RecyclerView.Adapter<ClientAdapter.ViewHolder>(){
    private var items = listOf<Client>()
    lateinit var selectedId : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.client_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        selectedId = item.id
        holder.containerView.textview_client_name.text = item.name
        holder.containerView.textview_client_mobile.text = item.mobile


        holder.containerView.setOnClickListener {
            log("Clicked ${item.name}")

        }

        //holder.containerView.setOnClickListener(onClick(this.ViewHolder()))
    }


    fun replaceItems(items: List<Client>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

}