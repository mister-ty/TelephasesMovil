package com.example.telephases.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Data
import com.example.telephases.data.repository.SyncManager
import com.example.telephases.data.local.database.AppDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker para tareas de mantenimiento de la base de datos
 * Ejecuta limpieza y optimización periódica
 */
@HiltWorker
class MaintenanceWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncManager: SyncManager,
    private val database: AppDatabase
) : CoroutineWorker(context, workerParams) {

    private val tag = "MaintenanceWorker"

    companion object {
        const val WORK_NAME = "maintenance_work"
    }

    override suspend fun doWork(): Result {
        Log.d(tag, "🧹 Iniciando trabajo de mantenimiento...")

        try {
            val startTime = System.currentTimeMillis()
            var tasksCompleted = 0
            val errors = mutableListOf<String>()

            // 1. Ejecutar mantenimiento de base de datos
            try {
                Log.d(tag, "🗄️ Ejecutando mantenimiento de base de datos...")
                database.performMaintenance()
                tasksCompleted++
                Log.d(tag, "✅ Mantenimiento de base de datos completado")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error en mantenimiento de base de datos", e)
                errors.add("Base de datos: ${e.message}")
            }

            // 2. Ejecutar mantenimiento de repositories
            try {
                Log.d(tag, "📚 Ejecutando mantenimiento de repositories...")
                syncManager.performMaintenance()
                tasksCompleted++
                Log.d(tag, "✅ Mantenimiento de repositories completado")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error en mantenimiento de repositories", e)
                errors.add("Repositories: ${e.message}")
            }

            // 3. Verificar integridad de datos
            try {
                Log.d(tag, "🔍 Verificando integridad de datos...")
                val integrityReport = database.checkDataIntegrity()
                
                if (integrityReport.hasIntegrityIssues) {
                    Log.w(tag, "⚠️ Problemas de integridad detectados: $integrityReport")
                    errors.add("Integridad: ${integrityReport.orphanedExamsCount} exámenes huérfanos, ${integrityReport.duplicatePatientsCount} pacientes duplicados")
                } else {
                    tasksCompleted++
                    Log.d(tag, "✅ Integridad de datos verificada")
                }
            } catch (e: Exception) {
                Log.e(tag, "❌ Error verificando integridad", e)
                errors.add("Integridad: ${e.message}")
            }

            // 4. Actualizar estadísticas de sincronización
            try {
                Log.d(tag, "📊 Actualizando estadísticas...")
                val syncStats = syncManager.getSyncStats()
                
                // Log de estadísticas para debugging
                Log.d(tag, "📈 Estadísticas actuales: ${syncStats.totalPendingItems} elementos pendientes")
                tasksCompleted++
                Log.d(tag, "✅ Estadísticas actualizadas")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error actualizando estadísticas", e)
                errors.add("Estadísticas: ${e.message}")
            }

            // 5. Limpiar archivos temporales (si los hay)
            try {
                Log.d(tag, "🗑️ Limpiando archivos temporales...")
                cleanupTemporaryFiles()
                tasksCompleted++
                Log.d(tag, "✅ Archivos temporales limpiados")
            } catch (e: Exception) {
                Log.e(tag, "❌ Error limpiando archivos temporales", e)
                errors.add("Archivos temporales: ${e.message}")
            }

            val endTime = System.currentTimeMillis()
            val totalTime = endTime - startTime
            val totalTasks = 5

            // Resultado del mantenimiento
            val success = errors.isEmpty()
            val message = if (success) {
                "Mantenimiento completado: $tasksCompleted/$totalTasks tareas"
            } else {
                "Mantenimiento parcial: $tasksCompleted/$totalTasks tareas, ${errors.size} errores"
            }

            Log.d(tag, "📋 $message en ${totalTime}ms")

            return Result.success(createOutputData(
                success = success,
                tasksCompleted = tasksCompleted,
                totalTasks = totalTasks,
                totalTime = totalTime,
                errors = errors.joinToString("; ")
            ))

        } catch (e: Exception) {
            Log.e(tag, "❌ Error crítico en mantenimiento", e)
            return Result.failure(createOutputData(
                success = false,
                tasksCompleted = 0,
                totalTasks = 5,
                totalTime = 0,
                errors = "Error crítico: ${e.message}"
            ))
        }
    }

    /**
     * Limpia archivos temporales y cache
     */
    private fun cleanupTemporaryFiles() {
        try {
            // Limpiar cache de la aplicación si es necesario
            val cacheDir = applicationContext.cacheDir
            if (cacheDir.exists()) {
                val cacheSize = cacheDir.listFiles()?.sumOf { it.length() } ?: 0
                Log.d(tag, "📂 Tamaño de cache: ${cacheSize / 1024} KB")
                
                // Solo limpiar si el cache es muy grande (>10MB)
                if (cacheSize > 10 * 1024 * 1024) {
                    cacheDir.listFiles()?.forEach { file ->
                        if (file.isFile && file.lastModified() < System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000) {
                            try {
                                file.delete()
                                Log.d(tag, "🗑️ Archivo temporal eliminado: ${file.name}")
                            } catch (e: Exception) {
                                Log.w(tag, "⚠️ No se pudo eliminar archivo: ${file.name}", e)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error limpiando archivos temporales", e)
            throw e
        }
    }

    /**
     * Crea datos de salida del worker
     */
    private fun createOutputData(
        success: Boolean,
        tasksCompleted: Int,
        totalTasks: Int,
        totalTime: Long,
        errors: String
    ): Data {
        return Data.Builder()
            .putBoolean("success", success)
            .putInt("tasks_completed", tasksCompleted)
            .putInt("total_tasks", totalTasks)
            .putLong("total_time", totalTime)
            .putString("errors", errors)
            .putLong("timestamp", System.currentTimeMillis())
            .build()
    }
}


