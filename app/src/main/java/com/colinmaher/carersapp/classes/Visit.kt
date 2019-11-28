package com.colinmaher.carersapp.classes

import java.io.Serializable

data class Visit(
    val name: String,
    val location: String) : Serializable