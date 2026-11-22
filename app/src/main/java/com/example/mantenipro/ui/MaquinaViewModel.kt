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

    // TEMPORAL: se elimina cuando exista el formulario de registro
    fun insertarDemo() = viewModelScope.launch {
        dao.insertar(
            Maquina(
                nombre = "Torno CNC",
                codigo = "MAQ-${(100..999).random()}",
                area = "Producción",
                fechaRegistro = System.currentTimeMillis()
            )
        )
    }
}