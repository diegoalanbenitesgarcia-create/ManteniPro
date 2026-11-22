package com.example.mantenipro.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenipro.data.MantenimientoConMaquina
import com.example.mantenipro.databinding.ItemHistorialBinding

class HistorialAdapter(
    private val onClick: (MantenimientoConMaquina) -> Unit
) : ListAdapter<MantenimientoConMaquina, HistorialAdapter.VH>(Diff) {

    class VH(val binding: ItemHistorialBinding) : RecyclerView.ViewHolder(binding.root)

    object Diff : DiffUtil.ItemCallback<MantenimientoConMaquina>() {
        override fun areItemsTheSame(a: MantenimientoConMaquina, b: MantenimientoConMaquina) =
            a.mantenimiento.id == b.mantenimiento.id

        override fun areContentsTheSame(a: MantenimientoConMaquina, b: MantenimientoConMaquina) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemHistorialBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val m = item.mantenimiento
        with(holder.binding) {
            tvFecha.text = formatearFecha(m.fechaRealizacion ?: m.fechaProgramada)
            tvMaquina.text = "${item.maquinaNombre} (${item.maquinaCodigo})"
            tvTipo.text = "Trabajo realizado: ${m.tipo}"
            tvDescripcion.text = m.actividadesRealizadas.ifEmpty { m.descripcion }
            root.setOnClickListener { onClick(item) }
        }
    }
}