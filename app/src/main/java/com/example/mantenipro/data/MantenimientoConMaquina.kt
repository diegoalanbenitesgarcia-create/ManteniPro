package com.example.mantenipro.data

import androidx.room.Embedded

data class MantenimientoConMaquina(
    @Embedded val mantenimiento: Mantenimiento,
    val maquinaNombre: String,
    val maquinaCodigo: String
)