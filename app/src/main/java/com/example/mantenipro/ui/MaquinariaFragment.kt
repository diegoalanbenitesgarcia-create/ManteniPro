package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenipro.R
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Maquina
import com.example.mantenipro.databinding.FragmentMaquinariaBinding

class MaquinariaFragment : Fragment(R.layout.fragment_maquinaria) {

    private val viewModel: MaquinaViewModel by viewModels()
    private var _binding: FragmentMaquinariaBinding? = null
    private val binding get() = _binding!!

    private var todas: List<Maquina> = emptyList()
    private lateinit var adapter: MaquinaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMaquinariaBinding.bind(view)

        adapter = MaquinaAdapter { maquina ->
            findNavController().navigate(
                R.id.detalleMaquinaFragment,
                bundleOf("maquinaId" to maquina.id)
            )
        }
        binding.rvMaquinas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMaquinas.adapter = adapter

        viewModel.maquinas.observe(viewLifecycleOwner) { lista ->
            todas = lista
            aplicarFiltros()
        }

        binding.etBuscar.doAfterTextChanged { aplicarFiltros() }
        binding.chipGroup.setOnCheckedStateChangeListener { _, _ -> aplicarFiltros() }

        binding.fabAgregar.setOnClickListener {
            findNavController().navigate(R.id.registroMaquinaFragment)
        }
    }

    private fun aplicarFiltros() {
        val texto = binding.etBuscar.text.toString().trim().lowercase()

        val porEstado = when (binding.chipGroup.checkedChipId) {
            R.id.chipFuncionando -> todas.filter { it.estado == Estados.MAQUINA_FUNCIONANDO }
            R.id.chipFuera -> todas.filter { it.estado == Estados.MAQUINA_FUERA_SERVICIO }
            else -> todas
        }

        val filtradas = if (texto.isEmpty()) porEstado else porEstado.filter {
            it.nombre.lowercase().contains(texto) ||
                    it.codigo.lowercase().contains(texto) ||
                    it.area.lowercase().contains(texto)
        }

        adapter.submitList(filtradas)
        binding.tvVacio.text =
            if (todas.isEmpty()) "Aún no hay maquinaria registrada"
            else "Ninguna máquina coincide con la búsqueda"
        binding.tvVacio.visibility = if (filtradas.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}