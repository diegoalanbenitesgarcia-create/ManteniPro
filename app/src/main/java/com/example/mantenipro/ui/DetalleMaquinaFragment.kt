package com.example.mantenipro.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mantenipro.R
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Maquina
import com.example.mantenipro.databinding.FragmentDetalleMaquinaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetalleMaquinaFragment : Fragment(R.layout.fragment_detalle_maquina) {

    private val viewModel: DetalleMaquinaViewModel by viewModels()
    private var _binding: FragmentDetalleMaquinaBinding? = null
    private val binding get() = _binding!!

    private var maquinaActual: Maquina? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetalleMaquinaBinding.bind(view)

        val maquinaId = requireArguments().getInt("maquinaId")

        viewModel.detalle.observe(viewLifecycleOwner) { detalle ->
            if (detalle == null) {
                findNavController().popBackStack()
            } else {
                mostrar(detalle)
            }
        }
        viewModel.cargar(maquinaId)

        binding.btnCambiarEstado.setOnClickListener {
            maquinaActual?.let { viewModel.cambiarEstado(it) }
        }
        binding.btnEditar.setOnClickListener {
            findNavController().navigate(
                R.id.registroMaquinaFragment,
                bundleOf("maquinaId" to maquinaId)
            )
        }
        binding.btnEliminar.setOnClickListener { confirmarEliminar() }
    }

    private fun mostrar(d: DetalleMaquina) {
        val m = d.maquina
        maquinaActual = m
        with(binding) {
            tvNombre.text = m.nombre
            tvCodigo.text = "Código: ${m.codigo}"
            tvArea.text = "Área: ${m.area}"
            tvFechaRegistro.text = "Registrada el ${formatearFecha(m.fechaRegistro)}"
            tvDescripcion.text = m.descripcion.ifEmpty { "Sin descripción" }

            tvEstado.text = m.estado
            tvEstado.setTextColor(
                if (m.estado == Estados.MAQUINA_FUNCIONANDO) Color.parseColor("#2E7D32")
                else Color.parseColor("#C62828")
            )

            tvUltimo.text = d.ultimo?.let {
                "${formatearFecha(it.fechaRealizacion ?: it.fechaProgramada)} · ${it.tipo}"
            } ?: "Sin mantenimientos realizados"

            tvProximo.text = d.proximo?.let {
                "${formatearFecha(it.fechaProgramada)} · ${it.tipo}"
            } ?: "Sin mantenimientos programados"
        }
    }

    private fun confirmarEliminar() {
        val m = maquinaActual ?: return
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar maquinaria")
            .setMessage("Se eliminarán también sus mantenimientos y reportes de falla. Esta acción no se puede deshacer.")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminar(m) {
                    if (isAdded) findNavController().popBackStack()
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}