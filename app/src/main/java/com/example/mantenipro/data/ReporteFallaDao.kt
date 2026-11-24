package com.example.mantenipro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteFallaDao {
    @Insert suspend fun insertar(reporte: ReporteFalla): Long
    @Update suspend fun actualizar(reporte: ReporteFalla)

    @Query("""
        SELECT r.*, q.nombre AS maquinaNombre, q.codigo AS maquinaCodigo
        FROM reportes_falla r INNER JOIN maquinas q ON r.maquinaId = q.id
        ORDER BY r.fecha DESC
    """)
    fun obtenerTodos(): Flow<List<ReporteConMaquina>>

    @Query("""
        SELECT r.*, q.nombre AS maquinaNombre, q.codigo AS maquinaCodigo
        FROM reportes_falla r INNER JOIN maquinas q ON r.maquinaId = q.id
        WHERE r.id = :id
    """)
    suspend fun obtenerPorId(id: Int): ReporteConMaquina?

    @Query("SELECT COUNT(*) FROM reportes_falla WHERE estado = 'Abierto'")
    fun contarAbiertos(): Flow<Int>

    @Query("SELECT COUNT(*) FROM reportes_falla WHERE prioridad = 'Alta' AND estado != 'Resuelto'")
    fun contarAltaSinResolver(): Flow<Int>

    @Query("""
    SELECT r.*, q.nombre AS maquinaNombre, q.codigo AS maquinaCodigo
    FROM reportes_falla r INNER JOIN maquinas q ON r.maquinaId = q.id
    ORDER BY r.fecha DESC, r.id DESC
    LIMIT :limite
""")
    fun recientes(limite: Int): Flow<List<ReporteConMaquina>>
}