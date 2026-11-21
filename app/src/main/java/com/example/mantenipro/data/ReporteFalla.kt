package com.example.mantenipro.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reportes_falla",
    foreignKeys = [ForeignKey(
        entity = Maquina::class,
        parentColumns = ["id"],
        childColumns = ["maquinaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("maquinaId")]
)
data class ReporteFalla(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val maquinaId: Int,
    val descripcion: String,
    val prioridad: String,
    val fecha: Long,
    val estado: String = Estados.REPORTE_ABIERTO
)