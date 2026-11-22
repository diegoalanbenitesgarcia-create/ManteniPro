package com.example.mantenipro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.mantenipro.data.AppDatabase
import com.example.mantenipro.data.MantenimientoConMaquina

class HistorialViewModel(application: Application) : AndroidViewModel(application) {

    val historial: LiveData<List<MantenimientoConMaquina>> =
        AppDatabase.getDatabase(application).mantenimientoDao().obtenerHistorial().asLiveData()
}