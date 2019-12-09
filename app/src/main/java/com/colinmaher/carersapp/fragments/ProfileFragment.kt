package com.colinmaher.carersapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.colinmaher.carersapp.MainActivity
import com.colinmaher.carersapp.R
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.models.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProfileFragment(var currentUser: FirebaseUser, var db: FirebaseFirestore) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_profile_update.setOnClickListener {
            updateProfile()
        }

        loadData()

        // Make role uneditable.
        edittext_profile_role.isEnabled = false
    }

    private fun loadData() {
        // IO(Network interactions), Main(UI), Default(Heavy computation).
        CoroutineScope(Dispatchers.IO).launch {
            // Get users details.
            val user = db.collection("users").document("${currentUser.uid}/data/details").get().await().toObject(User::class.java)
            log("User: $user")

            // Get users name and role.
            val temp = db.collection("users").document(currentUser.uid).get().await().toObject(User::class.java)
            log("Temp: $temp")

            if(user != null && temp != null){
                user.name = temp.name
                user.role = temp.role

                withContext(Dispatchers.Main) {
                    edittext_profile_role.setText(user.role)
                    edittext_profile_name.setText(user.name)
                    edittext_profile_address1.setText(user.address1)
                    edittext_profile_address2.setText(user.address2)
                    edittext_profile_town.setText(user.town)
                    spinner_profile_counties.setSelection(getIndex(spinner_profile_counties, user.county))
                    edittext_profile_eircode.setText(user.eircode)
                }
            }
        }
    }

    private fun updateProfile() {
        (activity as MainActivity).showSpinner()

        val user = hashMapOf(
            "name" to edittext_profile_name.text.toString()
        )

        val details = hashMapOf(
            "address1" to edittext_profile_address1.text.toString(),
            "address2" to edittext_profile_address2.text.toString(),
            "town" to edittext_profile_town.text.toString(),
            "county" to spinner_profile_counties.selectedItem
        )

        CoroutineScope(Dispatchers.IO).launch {
            try{
                db.collection("users").document(currentUser.uid).set(user, SetOptions.merge()).await()
                db.collection("users").document("${currentUser.uid}/data/details").set(details, SetOptions.merge()).await()

                loadData()
                withContext(Dispatchers.Main) { toast("Updated Successfully") }
            }catch(e: FirebaseException){
                withContext(Dispatchers.Main) { toast("Update failed: ${e.message}") }
            }

            withContext(Dispatchers.Main) {(activity as MainActivity).hideSpinner()}
        }
    }

    // Returns index of string in a spinner.
    private fun getIndex(spinner: Spinner, string: String): Int{
        for(i in 0 until spinner.count){
            if(spinner.getItemAtPosition(i).toString() == string){
                return i
            }
        }

        return 0
    }

    private fun toast(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}

