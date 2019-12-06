package com.colinmaher.carersapp.models

import java.io.Serializable

data class Client (
    var id: String = "",
    var name: String = "",
    var maritalstatus: String = "",
    var mobile: String = "",
    var telephone: String = "") : Serializable