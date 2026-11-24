package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenipro.R
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.ReporteConMaquina
import com.example.mantenipro.databinding.FragmentFallasBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FallasFragment : Fragment(R.layout.fragment_fallas) {

    private val viewModel: ReporteViewModel by viewModels()
    private var _binding: FragmentFallasBinding? = null
    private val binding get() = _binding!!

    private var todos: List<ReporteConMaquina> = emptyList()
    private lateinit var adapter: ReporteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFallasBinding.bind(view)

        when (arguments?.getString("filtro")) {
            "abierto" -> binding.chipAbierto.isChecked = true
            "alta" -> binding.chipAlta.isChecked = true
        }

        adapter = ReporteAdapter { item -> mostrarDetalle(item) }
        binding.rvReportes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReportes.adapter = adapter

        viewModel.reportes.observe(viewLifecycleOwner) { lista ->
            todos = lista
            aplicarFiltro()
        }

        binding.chipGroup.setOnCheckedStateChangeListener { _, _ -> aplicarFiltro() }

        binding.fabReportar.setOnClickListener {
            findNavController().navigate(R.id.registrarFallaFragment)
        }
    }

    private fun aplicarFiltro() {
        val filtrados = when (binding.chipGroup.checkedChipId) {
            R.id.chipAbierto -> todos.filter { it.reporte.estado == Estados.REPORTE_ABIERTO }
            R.id.chipEnProceso -> todos.filter { it.reporte.estado == Estados.REPORTE_EN_PROCESO }
            R.id.chipResuelto -> todos.filter { it.reporte.estado == Estados.REPORTE_RESUELTO }
            R.id.chipAlta -> todos.filter {
                it.reporte.prioridad == "Alta" && it.reporte.estado != Estados.REPORTE_RESUELTO
            }
            else -> todos
        }
        adapter.submitList(filtrados)
        binding.tvVacio.visibility = if (filtrados.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun mostrarDetalle(item: ReporteConMaquina) {
        val r = item.reporte
        val mensaje = """
            Máquina: ${item.maquinaNombre} (${item.maquinaCodigo})
            Fecha: ${formatearFecha(r.fecha)}
            Prioridad: ${r.prioridad}
            Estado: ${r.estado}

            ${r.descripcion}
        """.trimIndent()

        val dialogo = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Detalle del reporte")
            .setMessage(mensaje)
            .setNegativeButton("Cerrar", null)

        when (r.estado) {
            Estados.REPORTE_ABIERTO -> dialogo.setPositiveButton("Marcar en proceso") { _, _ ->
                viewModel.cambiarEstado(r, Estados.REPORTE_EN_PROCESO)
            }
            Estados.REPORTE_EN_PROCESO -> dialogo.setPositiveButton("Marcar resuelto") { _, _ ->
                viewModel.cambiarEstado(r, Estados.REPORTE_RESUELTO)
            }
        }
        dialogo.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}