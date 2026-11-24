package com.example.mantenipro.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenipro.R
import com.example.mantenipro.databinding.FragmentPanelBinding
import android.content.Intent
import com.example.mantenipro.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PanelFragment : Fragment(R.layout.fragment_panel) {

    private val viewModel: PanelViewModel by viewModels()
    private var _binding: FragmentPanelBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPanelBinding.bind(view)
        val nav = findNavController()

        // ---- Contadores ----
        viewModel.total.observe(viewLifecycleOwner) { binding.tvTotal.text = it.toString() }
        viewModel.funcionando.observe(viewLifecycleOwner) { binding.tvFuncionando.text = it.toString() }
        viewModel.fueraServicio.observe(viewLifecycleOwner) { binding.tvFueraServicio.text = it.toString() }
        viewModel.pendientes.observe(viewLifecycleOwner) { binding.tvPendientes.text = it.toString() }

        // ---- Requiere atención ----
        viewModel.alertas.observe(viewLifecycleOwner) { a ->
            with(binding) {
                mostrarAlerta(rowVencidos, a.vencidos, "mantenimiento vencido", "mantenimientos vencidos")
                mostrarAlerta(rowAbiertas, a.abiertas, "reporte de falla abierto", "reportes de falla abiertos")
                mostrarAlerta(rowAlta, a.altas, "falla de prioridad alta sin resolver", "fallas de prioridad alta sin resolver")
                tvTodoEnOrden.visibility =
                    if (a.vencidos + a.abiertas + a.altas == 0) View.VISIBLE else View.GONE
            }
        }
        binding.rowVencidos.setOnClickListener {
            nav.navigate(R.id.mantenimientosFragment, bundleOf("filtro" to "vencido"))
        }
        binding.rowAbiertas.setOnClickListener {
            nav.navigate(R.id.fallasFragment, bundleOf("filtro" to "abierto"))
        }
        binding.rowAlta.setOnClickListener {
            nav.navigate(R.id.fallasFragment, bundleOf("filtro" to "alta"))
        }

        // ---- Indicadores ----
        viewModel.disponibilidad.observe(viewLifecycleOwner) { (funcionando, total) ->
            val pct = if (total == 0) 0 else funcionando * 100 / total
            binding.tvDisponibilidad.text =
                if (total == 0) "Sin máquinas registradas"
                else "$pct% · $funcionando de $total máquinas funcionando"
            binding.progressDisponibilidad.setProgressCompat(pct, true)
        }
        viewModel.cumplimiento.observe(viewLifecycleOwner) { (realizados, total) ->
            val pct = if (total == 0) 0 else realizados * 100 / total
            binding.tvCumplimiento.text =
                if (total == 0) "Sin mantenimientos programados"
                else "$pct% · $realizados de $total mantenimientos realizados"
            binding.progressCumplimiento.setProgressCompat(pct, true)
        }

        // ---- Próximos mantenimientos ----
        val proximosAdapter = MantenimientoAdapter { item ->
            nav.navigate(
                R.id.detalleMantenimientoFragment,
                bundleOf("mantenimientoId" to item.mantenimiento.id)
            )
        }
        binding.rvProximos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProximos.adapter = proximosAdapter
        viewModel.proximos.observe(viewLifecycleOwner) { lista ->
            proximosAdapter.submitList(lista)
            binding.tvSinProximos.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }
        binding.btnVerMantenimientos.setOnClickListener { nav.navigate(R.id.mantenimientosFragment) }

        // ---- Fallas recientes ----
        val fallasAdapter = ReporteAdapter { nav.navigate(R.id.fallasFragment) }
        binding.rvFallas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFallas.adapter = fallasAdapter
        viewModel.fallasRecientes.observe(viewLifecycleOwner) { lista ->
            fallasAdapter.submitList(lista)
            binding.tvSinFallas.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }
        binding.btnVerFallas.setOnClickListener { nav.navigate(R.id.fallasFragment) }

        // ---- Accesos rápidos ----
        binding.chipNuevaMaquina.setOnClickListener { nav.navigate(R.id.registroMaquinaFragment) }
        binding.chipProgramar.setOnClickListener { nav.navigate(R.id.programarMantenimientoFragment) }
        binding.chipReportar.setOnClickListener { nav.navigate(R.id.registrarFallaFragment) }
        binding.chipHistorial.setOnClickListener { nav.navigate(R.id.historialFragment) }

        // ---- Cerrar sesión ----
        binding.btnCerrarSesion.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseas salir de ManteniPro?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salir") { _, _ ->
                    startActivity(
                        Intent(requireContext(), LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }
                .show()
        }
    }

    private fun mostrarAlerta(fila: android.widget.TextView, cantidad: Int, singular: String, plural: String) {
        fila.visibility = if (cantidad > 0) View.VISIBLE else View.GONE
        fila.setTextColor(android.graphics.Color.parseColor("#C62828"))
        fila.text = "$cantidad ${if (cantidad == 1) singular else plural}  ›"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}