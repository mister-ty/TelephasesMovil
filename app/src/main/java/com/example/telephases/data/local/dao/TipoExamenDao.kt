package com.example.telephases.data.local.dao

import androidx.room.*
import com.example.telephases.data.local.entities.TipoExamenEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de tipos de examen
 */
@Dao
interface TipoExamenDao {

    /**
     * Inserta un tipo de examen
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTipoExamen(tipoExamen: TipoExamenEntity)

    /**
     * Inserta múltiples tipos de examen
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiposExamen(tiposExamen: List<TipoExamenEntity>)

    /**
     * Actualiza un tipo de examen
     */
    @Update
    suspend fun updateTipoExamen(tipoExamen: TipoExamenEntity)

    /**
     * Elimina un tipo de examen
     */
    @Query("UPDATE exam_types SET activo = 0 WHERE id = :tipoExamenId")
    suspend fun deleteTipoExamen(tipoExamenId: Int)

    /**
     * Obtiene un tipo de examen por ID
     */
    @Query("SELECT * FROM exam_types WHERE id = :id AND activo = 1")
    suspend fun getTipoExamenById(id: Int): TipoExamenEntity?

    /**
     * Obtiene un tipo de examen por nombre
     */
    @Query("SELECT * FROM exam_types WHERE nombre = :nombre AND activo = 1")
    suspend fun getTipoExamenByNombre(nombre: String): TipoExamenEntity?

    /**
     * Obtiene todos los tipos de examen activos
     */
    @Query("SELECT * FROM exam_types WHERE activo = 1 ORDER BY nombre ASC")
    suspend fun getAllTiposExamen(): List<TipoExamenEntity>

    /**
     * Obtiene tipos de examen como Flow
     */
    @Query("SELECT * FROM exam_types WHERE activo = 1 ORDER BY nombre ASC")
    fun getAllTiposExamenFlow(): Flow<List<TipoExamenEntity>>

    /**
     * Obtiene tipos de examen que requieren dispositivo BLE
     */
    @Query("SELECT * FROM exam_types WHERE requiere_dispositivo_ble = 1 AND activo = 1 ORDER BY nombre ASC")
    suspend fun getTiposExamenBLE(): List<TipoExamenEntity>

    /**
     * Obtiene tipos de examen que NO requieren dispositivo BLE
     */
    @Query("SELECT * FROM exam_types WHERE requiere_dispositivo_ble = 0 AND activo = 1 ORDER BY nombre ASC")
    suspend fun getTiposExamenManual(): List<TipoExamenEntity>

    /**
     * Busca tipos de examen por descripción
     */
    @Query("""
        SELECT * FROM exam_types 
        WHERE activo = 1 
        AND (nombre LIKE '%' || :searchTerm || '%' OR descripcion LIKE '%' || :searchTerm || '%')
        ORDER BY nombre ASC
    """)
    suspend fun searchTiposExamen(searchTerm: String): List<TipoExamenEntity>

    /**
     * Verifica si existe un tipo de examen con el nombre dado
     */
    @Query("SELECT EXISTS(SELECT 1 FROM exam_types WHERE nombre = :nombre AND activo = 1)")
    suspend fun existsTipoExamenWithNombre(nombre: String): Boolean

    /**
     * Cuenta total de tipos de examen activos
     */
    @Query("SELECT COUNT(*) FROM exam_types WHERE activo = 1")
    suspend fun getTotalTiposExamenCount(): Int

    /**
     * Cuenta tipos de examen BLE
     */
    @Query("SELECT COUNT(*) FROM exam_types WHERE requiere_dispositivo_ble = 1 AND activo = 1")
    suspend fun getTiposExamenBLECount(): Int

    /**
     * Inicializa los tipos de examen por defecto si no existen
     */
    suspend fun initializeDefaultTypes() {
        if (getTotalTiposExamenCount() == 0) {
            insertTiposExamen(TipoExamenEntity.getDefaultExamTypes())
        }
    }

    /**
     * Obtiene los nombres de todos los tipos de examen activos
     */
    @Query("SELECT nombre FROM exam_types WHERE activo = 1 ORDER BY nombre ASC")
    suspend fun getAllTiposExamenNames(): List<String>

    /**
     * Actualiza la configuración de un tipo de examen
     */
    @Query("""
        UPDATE exam_types 
        SET unidad_default = :unidad,
            valor_minimo = :valorMinimo,
            valor_maximo = :valorMaximo,
            requiere_dispositivo_ble = :requiereBle
        WHERE id = :tipoExamenId
    """)
    suspend fun updateTipoExamenConfig(
        tipoExamenId: Int,
        unidad: String?,
        valorMinimo: Double?,
        valorMaximo: Double?,
        requiereBle: Boolean
    )
}


