package com.colinmaher.carersapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinmaher.carersapp.R
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.models.Visit
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_visits.*
import kotlinx.android.synthetic.main.visit_item.view.*

class VisitsFragment(private var currentUser: FirebaseUser, var db: FirebaseFirestore) : Fragment() {
    private lateinit var adapter: VisitAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visits, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()

    }

    private fun populateList(visits : MutableList<Visit>){
        adapter = VisitAdapter()
        adapter.replaceItems(visits)
        recyclerview_visit.adapter = adapter

        // Adds divider between items.
        recyclerview_visit.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun loadData(){
        log("$currentUser")
        //var visits : MutableList<Visit>

        val visits = mutableListOf<Visit>()

        db.collection("visits/${currentUser.uid}/visits")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    visits.add(document.toObject(Visit::class.java))
                }

                if(visits.isNotEmpty()){
                    populateList(visits)
                }
            }
            .addOnFailureListener { exception ->
                toast("${exception.message}")
            }
    }

    class VisitAdapter : RecyclerView.Adapter<VisitAdapter.ViewHolder>() {
        private var items = listOf<Visit>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.visit_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            holder.containerView.textview_visit_name.text = item.name
            holder.containerView.textview_visit_location.text = item.town

        }

        fun replaceItems(items: List<Visit>) {
            this.items = items
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = items.size

        inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer
    }

    private fun toast(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}