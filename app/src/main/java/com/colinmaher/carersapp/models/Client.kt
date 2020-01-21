package com.colinmaher.carersapp.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

data class Client(
    var id: String? = "",
    var name: String? = "",
    var dob: String? = "",
    var address1: String? = "",
    var address2: String? = "",
    var town: String? = "",
    var county: String? = "",
    var eircode: String? = "",
    var mobile: String? = "",
    var marital: String? = "",
    var active: Boolean? = null) : Serializable