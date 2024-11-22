package com.idfm.hackathon.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.US)
    return dateFormat.parse(this)
}

fun Date.toTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.US)
    return dateFormat.format(this)
}