package com.colinmaher.carersapp.extensions

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun toast2(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun log(msg: String) {
    Log.d("Debug: ", msg)
}