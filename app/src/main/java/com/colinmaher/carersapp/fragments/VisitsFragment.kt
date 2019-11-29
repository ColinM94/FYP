package com.colinmaher.carersapp.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinmaher.carersapp.MainActivity
import com.colinmaher.carersapp.R
import com.colinmaher.carersapp.helpers.log
import com.colinmaher.carersapp.models.Visit
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_visits.*
import kotlinx.android.synthetic.main.visit_item.view.*

class VisitsFragment : Fragment() {
    private lateinit var adapter: VisitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visits, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).getVisits()
    }

    fun populateList(visits : MutableList<Visit>){
        log("OnActivityCreated")

        adapter = VisitAdapter()
        adapter.replaceItems(visits)
        recyclerview_visit.adapter = adapter

        recyclerview_visit.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }



    class VisitAdapter : RecyclerView.Adapter<VisitAdapter.ViewHolder>() {
        private var items = listOf<Visit>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.visit_item, parent, false)
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

}