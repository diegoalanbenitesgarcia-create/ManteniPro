package com.example.mantenipro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MantenimientoDao {
    @Insert suspend fun insertar(mantenimiento: Mantenimiento): Long
    @Update suspend fun actualizar(mantenimiento: Mantenimiento)

    // Interfaz 5: listado completo
    @Query("""
        SELECT m.*, q.nombre AS maquinaNombre, q.codigo AS maquinaCodigo
        FROM mantenimientos m INNER JOIN maquinas q ON m.maquinaId = q.id
        ORDER BY m.fechaProgramada ASC
    """)
    fun obtenerTodos(): Flow<List<MantenimientoConMaquina>>

    // Interfaz 7: detalle
    @Query("""
        SELECT m.*, q.nombre AS maquinaNombre, q.codigo AS maquinaCodigo
        FROM mantenimientos m INNER JOIN maquinas q ON m.maquinaId = q.id
        WHERE m.id = :id
    """)
    suspend fun obtenerPorId(id: Int): MantenimientoConMaquina?

    // Interfaz 10: historial (solo realizados, más recientes primero)
    @Query("""
        SELECT m.*, q.nombre AS maquinaNombre, q.codigo AS maquinaCodigo
        FROM mantenimientos m INNER JOIN maquinas q ON m.maquinaId = q.id
        WHERE m.estado = 'Realizado'
        ORDER BY m.fechaRealizacion DESC
    """)
    fun obtenerHistorial(): Flow<List<MantenimientoConMaquina>>

    // Interfaz 4: último y próximo mantenimiento de una máquina
    @Query("""
        SELECT * FROM mantenimientos
        WHERE maquinaId = :maquinaId AND estado = 'Realizado'
        ORDER BY fechaRealizacion DESC LIMIT 1
    """)
    suspend fun ultimoRealizado(maquinaId: Int): Mantenimiento?

    @Query("""
        SELECT * FROM mantenimientos
        WHERE maquinaId = :maquinaId AND estado = 'Pendiente'
        ORDER BY fechaProgramada ASC LIMIT 1
    """)
    suspend fun proximoPendiente(maquinaId: Int): Mantenimiento?

    // Panel principal
    @Query("SELECT COUNT(*) FROM mantenimientos WHERE estado = 'Pendiente'")
    fun contarPendientes(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mantenimientos WHERE estado = 'Pendiente' AND fechaProgramada < :hoy")
    fun contarVencidos(hoy: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM mantenimientos")
    fun contarTotal(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mantenimientos WHERE estado = 'Realizado'")
    fun contarRealizados(): Flow<Int>

    @Query("""
    SELECT m.*, q.nombre AS maquinaNombre, q.codigo AS maquinaCodigo
    FROM mantenimientos m INNER JOIN maquinas q ON m.maquinaId = q.id
    WHERE m.estado = 'Pendiente'
    ORDER BY m.fechaProgramada ASC
    LIMIT :limite
""")
    fun proximosPendientes(limite: Int): Flow<List<MantenimientoConMaquina>>
}