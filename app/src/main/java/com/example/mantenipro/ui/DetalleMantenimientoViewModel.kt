package com.example.mantenipro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mantenipro.data.AppDatabase
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Mantenimiento
import com.example.mantenipro.data.MantenimientoConMaquina
import kotlinx.coroutines.launch

class DetalleMantenimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).mantenimientoDao()

    private val _detalle = MutableLiveData<MantenimientoConMaquina?>()
    val detalle: LiveData<MantenimientoConMaquina?> = _detalle

    fun cargar(id: Int) = viewModelScope.launch {
        _detalle.value = dao.obtenerPorId(id)
    }

    fun finalizar(
        m: Mantenimiento,
        actividades: String,
        observaciones: String,
        onListo: () -> Unit
    ) = viewModelScope.launch {
        dao.actualizar(
            m.copy(
                estado = Estados.MANT_REALIZADO,
                actividadesRealizadas = actividades,
                observaciones = observaciones,
                fechaRealizacion = System.currentTimeMillis()
            )
        )
        onListo()
    }
}