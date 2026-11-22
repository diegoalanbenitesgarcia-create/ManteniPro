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
import com.example.mantenipro.data.ReporteFalla
import com.example.mantenipro.databinding.FragmentRegistrarFallaBinding
import com.google.android.material.datepicker.MaterialDatePicker

class RegistrarFallaFragment : Fragment(R.layout.fragment_registrar_falla) {

    private val maquinaVM: MaquinaViewModel by viewModels()
    private val viewModel: ReporteViewModel by viewModels()
    private var _binding: FragmentRegistrarFallaBinding? = null
    private val binding get() = _binding!!

    private var maquinas: List<Maquina> = emptyList()
    private var maquinaSeleccionada: Maquina? = null
    private var fechaReporte: Long = System.currentTimeMillis()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistrarFallaBinding.bind(view)

        binding.actPrioridad.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, Estados.PRIORIDADES)
        )
        binding.actPrioridad.setText("Media", false)
        binding.etFecha.setText(formatearFecha(fechaReporte))

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
        binding.btnEnviar.setOnClickListener { enviar() }
        binding.btnCancelar.setOnClickListener { findNavController().popBackStack() }
    }

    private fun mostrarSelectorFecha() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha del reporte")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        picker.addOnPositiveButtonClickListener { seleccion ->
            fechaReporte = utcALocal(seleccion)
            binding.etFecha.setText(formatearFecha(fechaReporte))
        }
        picker.show(childFragmentManager, "selector_fecha")
    }

    private fun enviar() {
        val maquina = maquinaSeleccionada
        val descripcion = binding.etDescripcion.text.toString().trim()
        val prioridad = binding.actPrioridad.text.toString().trim()

        binding.tilMaquina.error = if (maquina == null) "Selecciona una máquina" else null
        binding.tilDescripcion.error = if (descripcion.isEmpty()) "Describe la falla" else null
        binding.tilPrioridad.error = if (prioridad.isEmpty()) "Selecciona la prioridad" else null
        if (maquina == null || descripcion.isEmpty() || prioridad.isEmpty()) return

        val reporte = ReporteFalla(
            maquinaId = maquina.id,
            descripcion = descripcion,
            prioridad = prioridad,
            fecha = fechaReporte
        )
        val fueraDeServicio = if (binding.cbFueraServicio.isChecked) maquina else null

        viewModel.registrar(reporte, fueraDeServicio) {
            Toast.makeText(requireContext(), "Reporte enviado", Toast.LENGTH_SHORT).show()
            if (isAdded) findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}