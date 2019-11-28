package com.colinmaher.carersapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.colinmaher.carersapp.R
import com.colinmaher.extensions.log
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_visits.*


class VisitsFragment : Fragment() {
    companion object {
        private const val TEXT_NAME = "Colin"
    }

    private var text = "A"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("VISIT FRAG: Visits view created ")

        if (savedInstanceState != null) {
            log("VISIT FRAG: savedInstance not null. Getting value: " + savedInstanceState.getString(TEXT_NAME).toString())
            text = savedInstanceState.getString(TEXT_NAME).toString()

        }
        setText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        log("VISIT FRAG: WHY AM I NOT BEING CALLED")
        log("VISIT FRAG: OnSaveInstance Visits. Savings " + editText2.text.toString() + " to instance state")
        outState.putString(TEXT_NAME, editText2.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()

        log("VISIT FRAG: Visits is being destroyed")

    }

    private fun setText() {
        log("VISIT FRAG: Setting text to $text")
        editText2.setText(text)
    }
}
