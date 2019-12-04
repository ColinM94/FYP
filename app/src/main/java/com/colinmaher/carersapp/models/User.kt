package com.colinmaher.carersapp.models

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class User(
    var name: String = "",
    var role: String = "",
    var clients: MutableList<String> = ArrayList()
) : Serializable




