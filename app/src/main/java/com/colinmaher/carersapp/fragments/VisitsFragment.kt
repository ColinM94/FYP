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
import com.colinmaher.carersapp.MainActivity
import com.colinmaher.carersapp.R
import com.colinmaher.carersapp.VisitActivity
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.models.Visit
import com.colinmaher.carersapp.models.Visits
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_visits.*
import kotlinx.android.synthetic.main.visit_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class VisitsFragment(private var currentUser: FirebaseUser, private var db: FirebaseFirestore) : Fragment() {

    private lateinit var adapter: VisitAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
    }

    private fun populateList(visits: MutableList<Visit>) {
        adapter = VisitAdapter(this.context!!)
        adapter.replaceItems(visits)
        recyclerview_visit.adapter = adapter

        // Adds divider between items.
        recyclerview_visit.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){(activity as MainActivity).showSpinner()}

            val visits = mutableListOf<Visit>()

            // Get current users list of visit Ids.
            val idList = db.collection("visits").document(currentUser.uid).get().await().toObject(Visits::class.java)

            // Get the details of these visits.
            idList?.visitIds?.forEach{ id ->
                val visit = db.collection("visitDetails").document(id).get().await().toObject(Visit::class.java)
                visit!!.id = id
                visits.add(visit)
            }

            withContext(Dispatchers.Main){
                if (visits.isNotEmpty()) {
                    populateList(visits)
                }

                (activity as MainActivity).hideSpinner()
            }
        }
    }

    inner class VisitAdapter(private var context: Context) : RecyclerView.Adapter<VisitAdapter.ViewHolder>() {
        private var items = listOf<Visit>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.visit_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            holder.containerView.textview_visit_name.text = "Jimmy Ryan"
            holder.containerView.textview_visit_location.text = item.startTime?.toDate().toString()


            holder.containerView.setOnClickListener {
                val intent = Intent(context, VisitActivity::class.java)
                log("ID: ${item.id}")
                intent.putExtra("id", item.id)

                context.startActivity(intent)
            }
        }

        fun replaceItems(items: List<Visit>) {
            this.items = items
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = items.size

        inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer

        private fun toast(msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }
}


