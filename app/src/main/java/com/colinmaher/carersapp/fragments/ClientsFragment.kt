package com.colinmaher.carersapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.colinmaher.carersapp.R
import com.colinmaher.extensions.log
import kotlinx.android.synthetic.main.fragment_clients.*

class ClientsFragment : Fragment() {
    companion object {
        private const val STATE_CLICKS = "clicks"
    }

    private var clicks = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clients, container, false)

        log("Clients Fragment Loaded")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            clicks = savedInstanceState.getInt(STATE_CLICKS)
        }


        // Initialize our text
        setCountText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_CLICKS, clicks)
    }

    private fun setCountText() {
        //count_text.text = context?.resources?.getQuantityString(R.plurals.count_text, clicks, clicks)
    }
}
