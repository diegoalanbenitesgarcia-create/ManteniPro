package com.example.mantenipro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mantenipro.data.AppDatabase
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Maquina
import com.example.mantenipro.data.Mantenimiento
import kotlinx.coroutines.launch

data class DetalleMaquina(
    val maquina: Maquina,
    val ultimo: Mantenimiento?,
    val proximo: Mantenimiento?
)

class DetalleMaquinaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    private val _detalle = MutableLiveData<DetalleMaquina?>()
    val detalle: LiveData<DetalleMaquina?> = _detalle

    fun cargar(id: Int) = viewModelScope.launch {
        val maquina = db.maquinaDao().obtenerPorId(id)
        if (maquina == null) {
            _detalle.value = null
            return@launch
        }
        _detalle.value = DetalleMaquina(
            maquina = maquina,
            ultimo = db.mantenimientoDao().ultimoRealizado(id),
            proximo = db.mantenimientoDao().proximoPendiente(id)
        )
    }

    fun cambiarEstado(maquina: Maquina) = viewModelScope.launch {
        val nuevo = if (maquina.estado == Estados.MAQUINA_FUNCIONANDO)
            Estados.MAQUINA_FUERA_SERVICIO else Estados.MAQUINA_FUNCIONANDO
        db.maquinaDao().actualizar(maquina.copy(estado = nuevo))
        cargar(maquina.id)
    }

    fun eliminar(maquina: Maquina, onListo: () -> Unit) = viewModelScope.launch {
        db.maquinaDao().eliminar(maquina)
        onListo()
    }
}