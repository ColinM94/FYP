package com.colinmaher.carersapp.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.colinmaher.carersapp.MainActivity
import com.colinmaher.carersapp.R
import com.colinmaher.carersapp.extensions.log
import com.colinmaher.carersapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.colinmaher.carersapp.extensions.toast
import kotlinx.android.synthetic.main.activity_main.*

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
            (activity as MainActivity).showSpinner()

            var user = (activity as MainActivity).getDocument("users", currentUser.uid).toObject(User::class.java)
            if(user!=null){
                withContext(Dispatchers.Main) {
                    edittext_profile_name.setText(user.name)
                    edittext_profile_role.setText(user.role)
                }
            }

            (activity as MainActivity).hideSpinner()
        }
    }

    private fun updateProfile() {
        val user = hashMapOf(
            "name" to edittext_profile_name.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.collection("users").document(currentUser.uid).set(user, SetOptions.merge())
                .addOnSuccessListener {
                    toast("Updated Successfully")
                    loadData()
                }
                .addOnFailureListener{
                    toast("Update Failed")
                }
        }
    }

    private fun toast(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}

