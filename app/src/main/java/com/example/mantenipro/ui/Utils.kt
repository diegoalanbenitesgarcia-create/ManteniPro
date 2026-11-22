package com.example.mantenipro.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar
import java.util.TimeZone

fun formatearFecha(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale("es", "MX")).format(Date(millis))

fun utcALocal(utcMillis: Long): Long {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcMillis }
    return Calendar.getInstance().apply {
        clear()
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis
}