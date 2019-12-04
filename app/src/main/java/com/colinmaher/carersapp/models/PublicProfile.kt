package com.colinmaher.carersapp.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

class PublicProfile (
    @PropertyName("name") val name: String = "",
    @ServerTimestamp val lastLogin: Timestamp? = null
)