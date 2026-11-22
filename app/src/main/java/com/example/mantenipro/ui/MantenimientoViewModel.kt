package com.example.mantenipro.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mantenipro.data.AppDatabase
import com.example.mantenipro.data.Mantenimiento
import com.example.mantenipro.data.MantenimientoConMaquina
import kotlinx.coroutines.launch

class MantenimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).mantenimientoDao()

    val mantenimientos: LiveData<List<MantenimientoConMaquina>> = dao.obtenerTodos().asLiveData()

    fun programar(mantenimiento: Mantenimiento, onListo: () -> Unit) = viewModelScope.launch {
        dao.insertar(mantenimiento)
        onListo()
    }
}