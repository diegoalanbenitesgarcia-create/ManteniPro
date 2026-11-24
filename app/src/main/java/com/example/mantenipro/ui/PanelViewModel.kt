package com.example.mantenipro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.mantenipro.data.AppDatabase
import com.example.mantenipro.data.Estados
import kotlinx.coroutines.flow.combine

data class Alertas(val vencidos: Int, val abiertas: Int, val altas: Int)

class PanelViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val maquinaDao = db.maquinaDao()
    private val mantDao = db.mantenimientoDao()
    private val reporteDao = db.reporteFallaDao()

    // Contadores
    val total = maquinaDao.contarTotal().asLiveData()
    val funcionando = maquinaDao.contarPorEstado(Estados.MAQUINA_FUNCIONANDO).asLiveData()
    val fueraServicio = maquinaDao.contarPorEstado(Estados.MAQUINA_FUERA_SERVICIO).asLiveData()
    val pendientes = mantDao.contarPendientes().asLiveData()

    // Requiere atención
    val alertas: LiveData<Alertas> = combine(
        mantDao.contarVencidos(inicioDeHoy()),
        reporteDao.contarAbiertos(),
        reporteDao.contarAltaSinResolver()
    ) { vencidos, abiertas, altas -> Alertas(vencidos, abiertas, altas) }.asLiveData()

    // Indicadores: (parte, total)
    val disponibilidad: LiveData<Pair<Int, Int>> = combine(
        maquinaDao.contarPorEstado(Estados.MAQUINA_FUNCIONANDO),
        maquinaDao.contarTotal()
    ) { funcionando, total -> Pair(funcionando, total) }.asLiveData()

    val cumplimiento: LiveData<Pair<Int, Int>> = combine(
        mantDao.contarRealizados(),
        mantDao.contarTotal()
    ) { realizados, total -> Pair(realizados, total) }.asLiveData()

    // Listas del panel
    val proximos = mantDao.proximosPendientes(4).asLiveData()
    val fallasRecientes = reporteDao.recientes(3).asLiveData()
}