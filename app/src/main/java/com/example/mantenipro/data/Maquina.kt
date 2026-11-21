package com.example.mantenipro.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "maquinas", indices = [Index(value = ["codigo"], unique = true)])
data class Maquina(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val codigo: String,
    val area: String,
    val fechaRegistro: Long,
    val descripcion: String = "",
    val estado: String = Estados.MAQUINA_FUNCIONANDO
)