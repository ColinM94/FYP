package com.colinmaher.carersapp.models

import java.io.Serializable

data class Visit(
    var id: String = "",
    var clientId: String = "",
    var userId: String = "",
    var clockInTime: String = "",
    var clockOutTime: String = "",
    var startDate: String = "",
    var startTime: String = "",
    var endDate: String = "",
    var endTime: String = "",
    var notes : MutableList<String> = arrayListOf()) : Serializable