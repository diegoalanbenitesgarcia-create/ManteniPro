package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenipro.R
import com.example.mantenipro.data.Maquina
import com.example.mantenipro.data.MantenimientoConMaquina
import com.example.mantenipro.databinding.FragmentHistorialBinding
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider

class HistorialFragment : Fragment(R.layout.fragment_historial) {

    private val viewModel: HistorialViewModel by viewModels()
    private val maquinaVM: MaquinaViewModel by viewModels()
    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private val textoTodas = "Todas las máquinas"
    private var todos: List<MantenimientoConMaquina> = emptyList()
    private var maquinas: List<Maquina> = emptyList()
    private var filtroMaquinaId: Int? = null
    private lateinit var adapter: HistorialAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHistorialBinding.bind(view)

        adapter = HistorialAdapter { item ->
            findNavController().navigate(
                R.id.detalleMantenimientoFragment,
                bundleOf("mantenimientoId" to item.mantenimiento.id)
            )
        }
        binding.rvHistorial.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistorial.adapter = adapter

        binding.actFiltro.setText(textoTodas, false)

        maquinaVM.maquinas.observe(viewLifecycleOwner) { lista ->
            maquinas = lista
            val opciones = listOf(textoTodas) + lista.map { "${it.nombre} (${it.codigo})" }
            binding.actFiltro.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, opciones)
            )
        }

        binding.actFiltro.setOnItemClickListener { _, _, posicion, _ ->
            filtroMaquinaId = if (posicion == 0) null else maquinas[posicion - 1].id
            aplicarFiltro()
        }

        viewModel.historial.observe(viewLifecycleOwner) { lista ->
            todos = lista
            aplicarFiltro()
        }
        binding.btnExportar.setOnClickListener { exportar() }
    }

    private fun exportar() {
        val datos = adapter.currentList
        if (datos.isEmpty()) {
            Toast.makeText(requireContext(), "No hay registros para exportar", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val archivo = ExportadorCsv.historial(requireContext(), datos)
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                archivo
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Historial de mantenimiento - ManteniPro")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Compartir historial"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "No se pudo generar el archivo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun aplicarFiltro() {
        val filtrados = filtroMaquinaId?.let { id ->
            todos.filter { it.mantenimiento.maquinaId == id }
        } ?: todos
        adapter.submitList(filtrados)
        binding.tvVacio.visibility = if (filtrados.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}