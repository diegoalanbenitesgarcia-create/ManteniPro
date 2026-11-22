package com.example.mantenipro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mantenipro.data.AppDatabase
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Maquina
import com.example.mantenipro.data.ReporteConMaquina
import com.example.mantenipro.data.ReporteFalla
import kotlinx.coroutines.launch

class ReporteViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    val reportes: LiveData<List<ReporteConMaquina>> = db.reporteFallaDao().obtenerTodos().asLiveData()

    // Si ponerFueraServicio no es null, esa máquina pasa a "Fuera de servicio"
    fun registrar(
        reporte: ReporteFalla,
        ponerFueraServicio: Maquina?,
        onListo: () -> Unit
    ) = viewModelScope.launch {
        db.reporteFallaDao().insertar(reporte)
        if (ponerFueraServicio != null) {
            db.maquinaDao().actualizar(ponerFueraServicio.copy(estado = Estados.MAQUINA_FUERA_SERVICIO))
        }
        onListo()
    }

    fun cambiarEstado(reporte: ReporteFalla, nuevoEstado: String) = viewModelScope.launch {
        db.reporteFallaDao().actualizar(reporte.copy(estado = nuevoEstado))
    }
}