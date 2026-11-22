package com.example.mantenipro.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenipro.data.MantenimientoConMaquina
import com.example.mantenipro.databinding.ItemMantenimientoBinding

class MantenimientoAdapter(
    private val onClick: (MantenimientoConMaquina) -> Unit
) : ListAdapter<MantenimientoConMaquina, MantenimientoAdapter.VH>(Diff) {

    class VH(val binding: ItemMantenimientoBinding) : RecyclerView.ViewHolder(binding.root)

    object Diff : DiffUtil.ItemCallback<MantenimientoConMaquina>() {
        override fun areItemsTheSame(a: MantenimientoConMaquina, b: MantenimientoConMaquina) =
            a.mantenimiento.id == b.mantenimiento.id

        override fun areContentsTheSame(a: MantenimientoConMaquina, b: MantenimientoConMaquina) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMantenimientoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val m = item.mantenimiento
        val estado = estadoMostrado(m)
        with(holder.binding) {
            tvMaquina.text = "${item.maquinaNombre} (${item.maquinaCodigo})"
            tvTipo.text = "Tipo: ${m.tipo}"
            tvFecha.text = "Fecha programada: ${formatearFecha(m.fechaProgramada)}"
            tvEstado.text = estado
            tvEstado.setTextColor(colorEstado(estado))
            root.setOnClickListener { onClick(item) }
        }
    }
}