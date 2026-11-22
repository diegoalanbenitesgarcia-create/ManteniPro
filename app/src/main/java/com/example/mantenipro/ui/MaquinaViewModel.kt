package com.example.mantenipro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mantenipro.data.AppDatabase
import com.example.mantenipro.data.Maquina
import kotlinx.coroutines.launch

class MaquinaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).maquinaDao()

    val maquinas: LiveData<List<Maquina>> = dao.obtenerTodas().asLiveData()

    // Devuelve false si ya existe una máquina con ese código
    fun registrar(maquina: Maquina, onResultado: (Boolean) -> Unit) = viewModelScope.launch {
        if (dao.contarPorCodigo(maquina.codigo) > 0) {
            onResultado(false)
        } else {
            dao.insertar(maquina)
            onResultado(true)
        }
    }
    suspend fun obtenerPorId(id: Int): Maquina? = dao.obtenerPorId(id)

    fun actualizar(maquina: Maquina, onResultado: (Boolean) -> Unit) = viewModelScope.launch {
        if (dao.contarPorCodigoExcepto(maquina.codigo, maquina.id) > 0) {
            onResultado(false)
        } else {
            dao.actualizar(maquina)
            onResultado(true)
        }
    }

    }
