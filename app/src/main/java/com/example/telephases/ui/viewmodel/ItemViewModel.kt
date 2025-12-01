package com.example.telephases.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telephases.network.ApiService
import com.example.telephases.network.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    private val api = ApiService.create()

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    init { fetchItems() }

    private fun fetchItems() {
        viewModelScope.launch {
            try {
                _items.value = api.getItems()
            } catch (e: Exception) {
                // log o manejo de error
            }
        }
    }

    fun addItem(nombre: String, descripcion: String) {
        viewModelScope.launch {
            try {
                val nuevo = api.createItem(Item(0, nombre, descripcion))
                _items.value = _items.value + nuevo
            } catch (e: Exception) {
                // log o manejo de error
            }
        }
    }
}
