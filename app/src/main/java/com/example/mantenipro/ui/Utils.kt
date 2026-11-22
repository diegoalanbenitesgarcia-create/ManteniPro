package com.example.mantenipro.ui

import android.graphics.Color
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Mantenimiento
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

fun inicioDeHoy(): Long = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis

// Pendiente + fecha pasada = Vencido
fun estadoMostrado(m: Mantenimiento): String =
    if (m.estado == Estados.MANT_PENDIENTE && m.fechaProgramada < inicioDeHoy())
        Estados.MANT_VENCIDO
    else m.estado

fun colorEstado(estado: String): Int = when (estado) {
    Estados.MANT_REALIZADO -> Color.parseColor("#2E7D32")
    Estados.MANT_VENCIDO -> Color.parseColor("#C62828")
    else -> Color.parseColor("#EF6C00") // Pendiente
}

fun colorPrioridad(prioridad: String): Int = when (prioridad) {
    "Alta" -> Color.parseColor("#C62828")
    "Media" -> Color.parseColor("#EF6C00")
    else -> Color.parseColor("#2E7D32") // Baja
}

fun colorEstadoReporte(estado: String): Int = when (estado) {
    Estados.REPORTE_RESUELTO -> Color.parseColor("#2E7D32")
    Estados.REPORTE_EN_PROCESO -> Color.parseColor("#EF6C00")
    else -> Color.parseColor("#C62828") // Abierto
}