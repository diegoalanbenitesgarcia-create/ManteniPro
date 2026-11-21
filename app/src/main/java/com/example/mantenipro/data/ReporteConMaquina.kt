package com.example.mantenipro.data

import androidx.room.Embedded

data class ReporteConMaquina(
    @Embedded val reporte: ReporteFalla,
    val maquinaNombre: String,
    val maquinaCodigo: String
)