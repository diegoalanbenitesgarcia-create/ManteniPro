package com.example.mantenipro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Maquina::class, Mantenimiento::class, ReporteFalla::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun maquinaDao(): MaquinaDao
    abstract fun mantenimientoDao(): MantenimientoDao
    abstract fun reporteFallaDao(): ReporteFallaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mantenipro.db"
                )
                    .fallbackToDestructiveMigration(true) // en desarrollo: si cambias tablas, se recrea la BD
                    .build()
                    .also { INSTANCE = it }
            }
    }
}