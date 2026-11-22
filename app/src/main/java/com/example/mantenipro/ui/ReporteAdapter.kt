package com.example.mantenipro.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenipro.data.ReporteConMaquina
import com.example.mantenipro.databinding.ItemReporteBinding

class ReporteAdapter(
    private val onClick: (ReporteConMaquina) -> Unit
) : ListAdapter<ReporteConMaquina, ReporteAdapter.VH>(Diff) {

    class VH(val binding: ItemReporteBinding) : RecyclerView.ViewHolder(binding.root)

    object Diff : DiffUtil.ItemCallback<ReporteConMaquina>() {
        override fun areItemsTheSame(a: ReporteConMaquina, b: ReporteConMaquina) =
            a.reporte.id == b.reporte.id

        override fun areContentsTheSame(a: ReporteConMaquina, b: ReporteConMaquina) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemReporteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val r = item.reporte
        with(holder.binding) {
            tvMaquina.text = "${item.maquinaNombre} (${item.maquinaCodigo})"
            tvDescripcion.text = r.descripcion
            tvFecha.text = "Reportada el ${formatearFecha(r.fecha)}"
            tvPrioridad.text = "Prioridad ${r.prioridad}"
            tvPrioridad.setTextColor(colorPrioridad(r.prioridad))
            tvEstado.text = r.estado
            tvEstado.setTextColor(colorEstadoReporte(r.estado))
            root.setOnClickListener { onClick(item) }
        }
    }
}