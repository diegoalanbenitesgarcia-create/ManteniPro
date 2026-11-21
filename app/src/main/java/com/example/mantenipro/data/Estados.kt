package com.example.mantenipro.data

object Estados {
    // Maquinaria
    const val MAQUINA_FUNCIONANDO = "Funcionando"
    const val MAQUINA_FUERA_SERVICIO = "Fuera de servicio"

    // Mantenimientos (Vencido se calcula, no se guarda)
    const val MANT_PENDIENTE = "Pendiente"
    const val MANT_REALIZADO = "Realizado"

    // Reportes de falla
    const val REPORTE_ABIERTO = "Abierto"
    const val REPORTE_EN_PROCESO = "En proceso"
    const val REPORTE_RESUELTO = "Resuelto"

    // Prioridades
    val PRIORIDADES = listOf("Baja", "Media", "Alta")
    val TIPOS_MANTENIMIENTO = listOf("Preventivo", "Correctivo", "Predictivo")
}