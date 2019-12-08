package com.colinmaher.carersapp.models

import java.io.Serializable

data class VisitItem(
    var id: String = "",
    var name: String = "",
    var town: String = "") : Serializable