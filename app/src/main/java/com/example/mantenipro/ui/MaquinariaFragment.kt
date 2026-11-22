package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenipro.R
import com.example.mantenipro.databinding.FragmentMaquinariaBinding

class MaquinariaFragment : Fragment(R.layout.fragment_maquinaria) {

    private val viewModel: MaquinaViewModel by viewModels()
    private var _binding: FragmentMaquinariaBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMaquinariaBinding.bind(view)

        val adapter = MaquinaAdapter { maquina ->
            // Aquí irá la navegación al detalle (interfaz 4)
            Toast.makeText(requireContext(), maquina.nombre, Toast.LENGTH_SHORT).show()
        }
        binding.rvMaquinas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMaquinas.adapter = adapter

        viewModel.maquinas.observe(viewLifecycleOwner) { lista ->
            adapter.submitList(lista)
            binding.tvVacio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }

        // TEMPORAL: se reemplaza por la navegación al formulario
        binding.fabAgregar.setOnClickListener { viewModel.insertarDemo() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}