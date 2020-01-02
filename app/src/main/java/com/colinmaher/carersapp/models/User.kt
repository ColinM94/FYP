package com.colinmaher.carersapp.models

import java.io.Serializable

data class User(
    var id: String = "",
    var name: String = "",
    var role: String = "",
    var address1: String = "",
    var address2: String = "",
    var town: String = "",
    var county: String = "",
    var eircode: String = "",
    var active: Boolean = true
)




