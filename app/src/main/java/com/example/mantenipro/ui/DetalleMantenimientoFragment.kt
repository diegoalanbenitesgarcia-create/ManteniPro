package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mantenipro.R
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.MantenimientoConMaquina
import com.example.mantenipro.databinding.FragmentDetalleMantenimientoBinding

class DetalleMantenimientoFragment : Fragment(R.layout.fragment_detalle_mantenimiento) {

    private val viewModel: DetalleMantenimientoViewModel by viewModels()
    private var _binding: FragmentDetalleMantenimientoBinding? = null
    private val binding get() = _binding!!

    private var actual: MantenimientoConMaquina? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetalleMantenimientoBinding.bind(view)

        val id = requireArguments().getInt("mantenimientoId")

        viewModel.detalle.observe(viewLifecycleOwner) { item ->
            if (item == null) findNavController().popBackStack() else mostrar(item)
        }
        viewModel.cargar(id)

        binding.btnRealizado.setOnClickListener { finalizar() }
    }

    private fun mostrar(item: MantenimientoConMaquina) {
        actual = item
        val m = item.mantenimiento
        val estado = estadoMostrado(m)
        val realizado = m.estado == Estados.MANT_REALIZADO

        with(binding) {
            tvMaquina.text = "${item.maquinaNombre} (${item.maquinaCodigo})"
            tvTipo.text = "Tipo: ${m.tipo}"
            tvFechaProgramada.text = "Fecha programada: ${formatearFecha(m.fechaProgramada)}"
            tvEstado.text = estado
            tvEstado.setTextColor(colorEstado(estado))
            tvDescripcion.text = m.descripcion.ifEmpty { "Sin descripción" }

            if (realizado) {
                tvFechaRealizacion.visibility = View.VISIBLE
                tvFechaRealizacion.text =
                    "Realizado el: ${formatearFecha(m.fechaRealizacion ?: m.fechaProgramada)}"
                etActividades.setText(m.actividadesRealizadas)
                etObservaciones.setText(m.observaciones)
                tilActividades.isEnabled = false
                tilObservaciones.isEnabled = false
                btnRealizado.visibility = View.GONE
            }
        }
    }

    private fun finalizar() {
        val item = actual ?: return
        val actividades = binding.etActividades.text.toString().trim()
        val observaciones = binding.etObservaciones.text.toString().trim()

        binding.tilActividades.error =
            if (actividades.isEmpty()) "Describe las actividades realizadas" else null
        if (actividades.isEmpty()) return

        viewModel.finalizar(item.mantenimiento, actividades, observaciones) {
            Toast.makeText(requireContext(), "Mantenimiento registrado como realizado", Toast.LENGTH_SHORT).show()
            if (isAdded) findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}