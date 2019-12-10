package com.colinmaher.carersapp.models

import com.google.firebase.Timestamp
import java.io.Serializable

data class Visit(
    var id: String = "",
    var name: String = "",
    var clientId: String = "",
    var userId: String = "",
    var clockInTime: Timestamp? = null,
    var clockOutTime: Timestamp? = null,
    var startTime: Timestamp? = null,
    var endTime: Timestamp? = null) : Serializable