package com.example.telephases.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Instant

/**
 * Convertidores de tipos para Room Database
 * Permite almacenar tipos complejos en SQLite
 */
class Converters {

    private val gson = Gson()

    /**
     * Convierte una Map a JSON string para almacenar en base de datos
     */
    @TypeConverter
    fun fromMapToString(map: Map<String, Any>?): String? {
        return if (map == null) null else gson.toJson(map)
    }

    /**
     * Convierte un JSON string a Map
     */
    @TypeConverter
    fun fromStringToMap(value: String?): Map<String, Any>? {
        return if (value == null) null else {
            val mapType = object : TypeToken<Map<String, Any>>() {}.type
            gson.fromJson(value, mapType)
        }
    }

    /**
     * Convierte una List<String> a JSON string
     */
    @TypeConverter
    fun fromStringListToString(list: List<String>?): String? {
        return if (list == null) null else gson.toJson(list)
    }

    /**
     * Convierte un JSON string a List<String>
     */
    @TypeConverter
    fun fromStringToStringList(value: String?): List<String>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(value, listType)
        }
    }

    /**
     * Convierte Instant a Long (timestamp)
     */
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    /**
     * Convierte Long (timestamp) a Instant
     */
    @TypeConverter
    fun toInstant(timestamp: Long?): Instant? {
        return timestamp?.let { Instant.ofEpochMilli(it) }
    }

    /**
     * Convierte Boolean a Int (SQLite no tiene tipo Boolean nativo)
     */
    @TypeConverter
    fun fromBoolean(bool: Boolean?): Int? {
        return when (bool) {
            true -> 1
            false -> 0
            null -> null
        }
    }

    /**
     * Convierte Int a Boolean
     */
    @TypeConverter
    fun toBoolean(int: Int?): Boolean? {
        return when (int) {
            1 -> true
            0 -> false
            else -> null
        }
    }

    /**
     * Convierte Array<String> a JSON string
     */
    @TypeConverter
    fun fromStringArrayToString(array: Array<String>?): String? {
        return if (array == null) null else gson.toJson(array)
    }

    /**
     * Convierte JSON string a Array<String>
     */
    @TypeConverter
    fun fromStringToStringArray(value: String?): Array<String>? {
        return if (value == null) null else {
            val arrayType = object : TypeToken<Array<String>>() {}.type
            gson.fromJson(value, arrayType)
        }
    }
}


