package com.colinmaher.carersapp.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.colinmaher.carersapp.MainActivity
import com.colinmaher.carersapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        (activity as MainActivity).fragmentMessage(preference.key)
        return true
    }
}
