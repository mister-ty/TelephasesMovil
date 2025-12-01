package com.example.telephases.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncProblem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Indicador de conectividad y estado de sincronización
 */
@Composable
fun ConnectivityIndicator(
    isOnline: Boolean,
    hasPendingSync: Boolean = false,
    isSyncing: Boolean = false,
    onSyncClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Icono de conectividad
        Icon(
            imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
            contentDescription = if (isOnline) "Conectado" else "Sin conexión",
            tint = if (isOnline) Color.White else Color(0xFFFFCDD2),
            modifier = Modifier.size(20.dp)
        )
        
        // Indicador de sincronización si hay datos pendientes
        if (hasPendingSync && onSyncClick != null) {
            IconButton(
                onClick = onSyncClick,
                modifier = Modifier.size(24.dp)
            ) {
                if (isSyncing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.SyncProblem,
                        contentDescription = "Sincronizar datos pendientes",
                        tint = Color(0xFFFFF3E0),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Banner de estado offline con información adicional
 */
@Composable
fun OfflineBanner(
    modifier: Modifier = Modifier,
    pendingItems: Int = 0,
    lastSyncTime: String? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = "Sin conexión",
                tint = Color(0xFF0277BD),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Modo offline",
                    color = Color(0xFF0277BD),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                val message = when {
                    pendingItems > 0 -> "$pendingItems elementos pendientes de sincronización"
                    lastSyncTime != null -> "Última sincronización: $lastSyncTime"
                    else -> "Los datos se sincronizarán al conectarse"
                }
                
                Text(
                    text = message,
                    color = Color(0xFF0277BD),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Indicador de progreso de sincronización
 */
@Composable
fun SyncProgressIndicator(
    isVisible: Boolean,
    progress: Float = 0f,
    message: String = "Sincronizando...",
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFF4CAF50),
                        strokeWidth = 2.dp
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = message,
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                if (progress > 0f) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF4CAF50),
                        trackColor = Color(0xFFE0E0E0)
                    )
                }
            }
        }
    }
}

/**
 * Estado de sincronización con estadísticas
 */
@Composable
fun SyncStatusCard(
    syncedItems: Int,
    unsyncedItems: Int,
    totalItems: Int,
    isOnline: Boolean,
    lastSyncResult: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (unsyncedItems == 0) Color(0xFFE8F5E8) else Color(0xFFFFF3E0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado de sincronización",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (unsyncedItems == 0) Color(0xFF2E7D32) else Color(0xFFE65100)
                )
                
                Icon(
                    imageVector = if (unsyncedItems == 0) Icons.Default.Sync else Icons.Default.SyncProblem,
                    contentDescription = null,
                    tint = if (unsyncedItems == 0) Color(0xFF4CAF50) else Color(0xFFFF9800),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sincronizados: $syncedItems",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4CAF50)
                )
                
                if (unsyncedItems > 0) {
                    Text(
                        text = "Pendientes: $unsyncedItems",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFF9800)
                    )
                }
            }
            
            if (totalItems > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = if (totalItems > 0) syncedItems.toFloat() / totalItems else 1f,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE0E0E0)
                )
            }
            
            lastSyncResult?.let { result ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = result,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}


