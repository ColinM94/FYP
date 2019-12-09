package com.colinmaher.carersapp.models

import java.io.Serializable

data class User(
    var name: String = "",
    var role: String = "",
    var address1: String = "",
    var address2: String = "",
    var county: String = "",
    var town: String = ""
) : Serializable




