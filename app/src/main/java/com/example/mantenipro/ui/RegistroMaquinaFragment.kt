package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mantenipro.R
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Maquina
import com.example.mantenipro.databinding.FragmentRegistroMaquinaBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch

class RegistroMaquinaFragment : Fragment(R.layout.fragment_registro_maquina) {

    private val viewModel: MaquinaViewModel by viewModels()
    private var _binding: FragmentRegistroMaquinaBinding? = null
    private val binding get() = _binding!!

    private var maquinaId = 0                       // 0 = registrar, >0 = editar
    private var estadoActual = Estados.MAQUINA_FUNCIONANDO
    private var fechaRegistro: Long = System.currentTimeMillis()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistroMaquinaBinding.bind(view)

        maquinaId = arguments?.getInt("maquinaId", 0) ?: 0

        binding.actArea.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, Estados.AREAS)
        )
        binding.etFecha.setText(formatearFecha(fechaRegistro))

        if (maquinaId > 0) cargarParaEditar()

        binding.etFecha.setOnClickListener { mostrarSelectorFecha() }
        binding.btnGuardar.setOnClickListener { guardar() }
        binding.btnCancelar.setOnClickListener { findNavController().popBackStack() }
    }

    private fun cargarParaEditar() {
        binding.tvTituloForm.text = "Editar maquinaria"
        viewLifecycleOwner.lifecycleScope.launch {
            val m = viewModel.obtenerPorId(maquinaId) ?: return@launch
            estadoActual = m.estado
            fechaRegistro = m.fechaRegistro
            binding.etNombre.setText(m.nombre)
            binding.etCodigo.setText(m.codigo)
            binding.actArea.setText(m.area, false)   // false = no filtrar la lista
            binding.etFecha.setText(formatearFecha(m.fechaRegistro))
            binding.etDescripcion.setText(m.descripcion)
        }
    }

    private fun mostrarSelectorFecha() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha de registro")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        picker.addOnPositiveButtonClickListener { seleccion ->
            fechaRegistro = utcALocal(seleccion)
            binding.etFecha.setText(formatearFecha(fechaRegistro))
        }
        picker.show(childFragmentManager, "selector_fecha")
    }

    private fun guardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val codigo = binding.etCodigo.text.toString().trim().uppercase()
        val area = binding.actArea.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()

        binding.tilNombre.error = if (nombre.isEmpty()) "Ingresa el nombre" else null
        binding.tilCodigo.error = if (codigo.isEmpty()) "Ingresa el código" else null
        binding.tilArea.error = if (area.isEmpty()) "Selecciona un área" else null
        if (nombre.isEmpty() || codigo.isEmpty() || area.isEmpty()) return

        val maquina = Maquina(
            id = maquinaId,
            nombre = nombre,
            codigo = codigo,
            area = area,
            fechaRegistro = fechaRegistro,
            descripcion = descripcion,
            estado = estadoActual
        )

        val alTerminar: (Boolean) -> Unit = { exito ->
            if (exito) {
                val msg = if (maquinaId > 0) "Cambios guardados" else "Máquina registrada"
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                _binding?.tilCodigo?.error = "Ya existe una máquina con ese código"
            }
        }

        if (maquinaId > 0) viewModel.actualizar(maquina, alTerminar)
        else viewModel.registrar(maquina, alTerminar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}