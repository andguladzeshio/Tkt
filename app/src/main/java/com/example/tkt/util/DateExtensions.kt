package com.example.tkt.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun Long.toFormattedDate(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("dd/mm/yyyy")
    return formatter.format(date)
}