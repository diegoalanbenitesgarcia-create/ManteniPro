package com.example.mantenipro.ui

import android.content.Context
import com.example.mantenipro.data.MantenimientoConMaquina
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportadorCsv {

    // Si Excel abre todo en una sola columna, cambia la coma por ";"
    private const val SEPARADOR = ","

    private fun campo(texto: String): String =
        "\"" + texto.replace("\"", "\"\"").replace("\r", " ").replace("\n", " ") + "\""

    fun historial(context: Context, datos: List<MantenimientoConMaquina>): File {
        val carpeta = File(context.cacheDir, "exportaciones").apply { mkdirs() }
        carpeta.listFiles()?.forEach { it.delete() } // limpia exportaciones anteriores

        val marca = SimpleDateFormat("yyyyMMdd_HHmm", Locale.US).format(Date())
        val archivo = File(carpeta, "historial_mantenimiento_$marca.csv")

        archivo.bufferedWriter(Charsets.UTF_8).use { w ->
            w.write("\uFEFF") // marca UTF-8 para que Excel muestre bien los acentos

            val encabezados = listOf(
                "Fecha", "Código", "Máquina", "Tipo de trabajo",
                "Descripción programada", "Actividades realizadas", "Observaciones"
            )
            w.write(encabezados.joinToString(SEPARADOR) { campo(it) })
            w.write("\r\n")

            datos.forEach { item ->
                val m = item.mantenimiento
                val fila = listOf(
                    formatearFecha(m.fechaRealizacion ?: m.fechaProgramada),
                    item.maquinaCodigo,
                    item.maquinaNombre,
                    m.tipo,
                    m.descripcion,
                    m.actividadesRealizadas,
                    m.observaciones
                )
                w.write(fila.joinToString(SEPARADOR) { campo(it) })
                w.write("\r\n")
            }
        }
        return archivo
    }
}