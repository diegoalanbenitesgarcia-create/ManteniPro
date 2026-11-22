package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mantenipro.R
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Maquina
import com.example.mantenipro.data.Mantenimiento
import com.example.mantenipro.databinding.FragmentProgramarMantenimientoBinding
import com.google.android.material.datepicker.MaterialDatePicker

class ProgramarMantenimientoFragment : Fragment(R.layout.fragment_programar_mantenimiento) {

    private val maquinaVM: MaquinaViewModel by viewModels()
    private val viewModel: MantenimientoViewModel by viewModels()
    private var _binding: FragmentProgramarMantenimientoBinding? = null
    private val binding get() = _binding!!

    private var maquinas: List<Maquina> = emptyList()
    private var maquinaSeleccionada: Maquina? = null
    private var fechaProgramada: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProgramarMantenimientoBinding.bind(view)

        binding.actTipo.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, Estados.TIPOS_MANTENIMIENTO)
        )

        maquinaVM.maquinas.observe(viewLifecycleOwner) { lista ->
            maquinas = lista
            val etiquetas = lista.map { "${it.nombre} (${it.codigo})" }
            binding.actMaquina.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, etiquetas)
            )
            binding.tilMaquina.helperText =
                if (lista.isEmpty()) "Primero registra una máquina en el módulo Máquinas" else null
        }

        binding.actMaquina.setOnItemClickListener { _, _, posicion, _ ->
            maquinaSeleccionada = maquinas[posicion]
            binding.tilMaquina.error = null
        }

        binding.etFecha.setOnClickListener { mostrarSelectorFecha() }
        binding.btnGuardar.setOnClickListener { guardar() }
        binding.btnCancelar.setOnClickListener { findNavController().popBackStack() }
    }

    private fun mostrarSelectorFecha() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha programada")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        picker.addOnPositiveButtonClickListener { seleccion ->
            val local = utcALocal(seleccion)
            fechaProgramada = local
            binding.etFecha.setText(formatearFecha(local))
            binding.tilFecha.error = null
        }
        picker.show(childFragmentManager, "selector_fecha")
    }

    private fun guardar() {
        val maquina = maquinaSeleccionada
        val tipo = binding.actTipo.text.toString().trim()
        val fecha = fechaProgramada
        val descripcion = binding.etDescripcion.text.toString().trim()

        binding.tilMaquina.error = if (maquina == null) "Selecciona una máquina" else null
        binding.tilTipo.error = if (tipo.isEmpty()) "Selecciona el tipo" else null
        binding.tilFecha.error = if (fecha == null) "Elige una fecha" else null
        binding.tilDescripcion.error = if (descripcion.isEmpty()) "Describe las actividades" else null
        if (maquina == null || tipo.isEmpty() || fecha == null || descripcion.isEmpty()) return

        val mantenimiento = Mantenimiento(
            maquinaId = maquina.id,
            tipo = tipo,
            fechaProgramada = fecha,
            descripcion = descripcion
        )

        viewModel.programar(mantenimiento) {
            Toast.makeText(requireContext(), "Mantenimiento programado", Toast.LENGTH_SHORT).show()
            if (isAdded) findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}