package com.example.advancedandroidcourse.presentation.composables

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp?.formatToDate(): String {
    if (this == null) return ""
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(this.toDate())
}