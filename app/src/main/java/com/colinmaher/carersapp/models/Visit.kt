package com.colinmaher.carersapp.models

import java.io.Serializable

data class Visit(
    var id: String = "",
    var name: String = "",
    var address1: String = "",
    var address2: String = "",
    var town: String = "",
    var county: String = "") : Serializable