package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mantenipro.R
import com.example.mantenipro.databinding.FragmentPanelBinding

class PanelFragment : Fragment(R.layout.fragment_panel) {

    private val viewModel: PanelViewModel by viewModels()
    private var _binding: FragmentPanelBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPanelBinding.bind(view)

        viewModel.total.observe(viewLifecycleOwner) { binding.tvTotal.text = it.toString() }
        viewModel.funcionando.observe(viewLifecycleOwner) { binding.tvFuncionando.text = it.toString() }
        viewModel.fueraServicio.observe(viewLifecycleOwner) { binding.tvFueraServicio.text = it.toString() }
        viewModel.pendientes.observe(viewLifecycleOwner) { binding.tvPendientes.text = it.toString() }

        val nav = findNavController()
        binding.btnNuevaMaquina.setOnClickListener { nav.navigate(R.id.registroMaquinaFragment) }
        binding.btnProgramar.setOnClickListener { nav.navigate(R.id.programarMantenimientoFragment) }
        binding.btnReportar.setOnClickListener { nav.navigate(R.id.registrarFallaFragment) }
        binding.btnHistorial.setOnClickListener { nav.navigate(R.id.historialFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}