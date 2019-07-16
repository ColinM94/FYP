package com.colinmaher.carersapp.fragments


import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.colinmaher.carersapp.LoginActivity
import com.colinmaher.carersapp.R
import com.google.firebase.auth.FirebaseAuth


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return when(preference?.key){
            getString(R.string.settings_signOut) ->{
                // ToDO: call signout method in MainActivity using an interface etc. Keep fragment decoupled.
                true
            }
            else -> super.onPreferenceTreeClick(preference)
        }
    }
}
