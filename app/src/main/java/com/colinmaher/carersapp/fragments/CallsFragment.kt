package com.colinmaher.carersapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinmaher.carersapp.R
import kotlinx.android.synthetic.main.fragment_calls.*

class CallsFragment : Fragment() {

    data class Call(val name: String, val location: String)

    private val calls = listOf(
        Call("Jimmy", "Boherlahan"),
        Call("Catherine", "Knockavilla"),
        Call("Anita", "Cashel"),
        Call("Thomas", "Limerick"),
        Call("Jimmy", "Boherlahan"),
        Call("Catherine", "Knockavilla"),
        Call("Anita", "Cashel"),
        Call("Thomas", "Limerick"),
        Call("Jimmy", "Boherlahan"),
        Call("Catherine", "Knockavilla"),
        Call("Anita", "Cashel"),
        Call("Thomas", "Limerick")
    )

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.fragment_calls, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview_calls_calls.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = ListAdapter(calls)
        }
    }

    companion object {
        fun newInstance(): CallsFragment = CallsFragment()
    }

    class CallViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.call_item, parent, false)) {
            private var callName: TextView? = null
            private var callLocation: TextView? = null

            init {
                callName = itemView.findViewById(R.id.textview_call_name)
                callLocation = itemView.findViewById(R.id.textview_call_location)
            }

        fun bind(call: Call) {
            callName?.text = call.name
            callLocation?.text = call.location
        }
    }

    class ListAdapter(private val list: List<Call>)
        : RecyclerView.Adapter<CallViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return CallViewHolder(inflater, parent)
        }

        override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
            val call: Call = list[position]
            holder.bind(call)
        }

        override fun getItemCount(): Int = list.size

    }
}




