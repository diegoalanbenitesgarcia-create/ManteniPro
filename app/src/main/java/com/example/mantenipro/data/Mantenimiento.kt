package com.example.mantenipro.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mantenimientos",
    foreignKeys = [ForeignKey(
        entity = Maquina::class,
        parentColumns = ["id"],
        childColumns = ["maquinaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("maquinaId")]
)
data class Mantenimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val maquinaId: Int,
    val tipo: String,
    val fechaProgramada: Long,
    val descripcion: String = "",
    val estado: String = Estados.MANT_PENDIENTE,
    val actividadesRealizadas: String = "",
    val observaciones: String = "",
    val fechaRealizacion: Long? = null
)