package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenipro.R
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.MantenimientoConMaquina
import com.example.mantenipro.databinding.FragmentMantenimientosBinding

class MantenimientosFragment : Fragment(R.layout.fragment_mantenimientos) {

    private val viewModel: MantenimientoViewModel by viewModels()
    private var _binding: FragmentMantenimientosBinding? = null
    private val binding get() = _binding!!

    private var todos: List<MantenimientoConMaquina> = emptyList()
    private lateinit var adapter: MantenimientoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMantenimientosBinding.bind(view)

        if (arguments?.getString("filtro") == "vencido") {
            binding.chipVencido.isChecked = true
        }

        adapter = MantenimientoAdapter { item ->
            findNavController().navigate(
                R.id.detalleMantenimientoFragment,
                bundleOf("mantenimientoId" to item.mantenimiento.id)
            )
        }
        binding.rvMantenimientos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMantenimientos.adapter = adapter

        viewModel.mantenimientos.observe(viewLifecycleOwner) { lista ->
            todos = lista
            aplicarFiltro()
        }

        binding.chipGroup.setOnCheckedStateChangeListener { _, _ -> aplicarFiltro() }

        binding.fabProgramar.setOnClickListener {
            findNavController().navigate(R.id.programarMantenimientoFragment)
        }
    }

    private fun aplicarFiltro() {
        val filtrados = when (binding.chipGroup.checkedChipId) {
            R.id.chipPendiente -> todos.filter { estadoMostrado(it.mantenimiento) == Estados.MANT_PENDIENTE }
            R.id.chipRealizado -> todos.filter { estadoMostrado(it.mantenimiento) == Estados.MANT_REALIZADO }
            R.id.chipVencido -> todos.filter { estadoMostrado(it.mantenimiento) == Estados.MANT_VENCIDO }
            else -> todos
        }
        adapter.submitList(filtrados)
        binding.tvVacio.visibility = if (filtrados.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}