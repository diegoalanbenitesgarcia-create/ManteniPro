package com.example.mantenipro.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MaquinaDao {
    @Insert suspend fun insertar(maquina: Maquina): Long
    @Update suspend fun actualizar(maquina: Maquina)
    @Delete suspend fun eliminar(maquina: Maquina)

    @Query("SELECT * FROM maquinas ORDER BY nombre")
    fun obtenerTodas(): Flow<List<Maquina>>

    @Query("SELECT * FROM maquinas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Maquina?

    @Query("SELECT COUNT(*) FROM maquinas WHERE codigo = :codigo")
    suspend fun contarPorCodigo(codigo: String): Int

    // Para el panel principal
    @Query("SELECT COUNT(*) FROM maquinas")
    fun contarTotal(): Flow<Int>

    @Query("SELECT COUNT(*) FROM maquinas WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>
}