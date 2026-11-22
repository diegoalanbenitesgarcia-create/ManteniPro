package com.example.mantenipro.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenipro.data.Estados
import com.example.mantenipro.data.Maquina
import com.example.mantenipro.databinding.ItemMaquinaBinding

class MaquinaAdapter(
    private val onClick: (Maquina) -> Unit
) : ListAdapter<Maquina, MaquinaAdapter.VH>(Diff) {

    class VH(val binding: ItemMaquinaBinding) : RecyclerView.ViewHolder(binding.root)

    object Diff : DiffUtil.ItemCallback<Maquina>() {
        override fun areItemsTheSame(a: Maquina, b: Maquina) = a.id == b.id
        override fun areContentsTheSame(a: Maquina, b: Maquina) = a == b
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMaquinaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val m = getItem(position)
        with(holder.binding) {
            tvNombre.text = m.nombre
            tvCodigo.text = "Código: ${m.codigo}"
            tvArea.text = "Área: ${m.area}"
            tvEstado.text = m.estado
            tvEstado.setTextColor(
                if (m.estado == Estados.MAQUINA_FUNCIONANDO) Color.parseColor("#2E7D32")
                else Color.parseColor("#C62828")
            )
            root.setOnClickListener { onClick(m) }
        }
    }
}