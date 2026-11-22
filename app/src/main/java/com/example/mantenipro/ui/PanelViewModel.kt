package com.example.mantenipro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.example.mantenipro.data.AppDatabase
import com.example.mantenipro.data.Estados

class PanelViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)

    val total = db.maquinaDao().contarTotal().asLiveData()
    val funcionando = db.maquinaDao().contarPorEstado(Estados.MAQUINA_FUNCIONANDO).asLiveData()
    val fueraServicio = db.maquinaDao().contarPorEstado(Estados.MAQUINA_FUERA_SERVICIO).asLiveData()
    val pendientes = db.mantenimientoDao().contarPendientes().asLiveData()
}