package com.example.telephases.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Componente mejorado para mostrar el estado de conexión de dispositivos
 */
@Composable
fun DeviceConnectionStatus(
    deviceName: String,
    isConnected: Boolean,
    isConnecting: Boolean,
    isScanning: Boolean,
    isReconnecting: Boolean,
    statusMessage: String,
    reconnectionAttempts: Int = 0,
    maxReconnectionAttempts: Int = 5,
    lastError: String? = null,
    onConnect: () -> Unit = {},
    onDisconnect: () -> Unit = {},
    onRetry: () -> Unit = {},
    onStopReconnection: () -> Unit = {},
    showActions: Boolean = true
) {
    val statusInfo = getConnectionStatusInfo(
        isConnected, isConnecting, isScanning, isReconnecting, 
        reconnectionAttempts, maxReconnectionAttempts, lastError
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = statusInfo.backgroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con nombre del dispositivo y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = statusInfo.icon,
                    contentDescription = null,
                    tint = statusInfo.color,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = deviceName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = statusInfo.color
                    )
                    
                    Text(
                        text = statusInfo.statusText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = statusInfo.color.copy(alpha = 0.8f)
                    )
                }
                
                // Indicador de progreso si está conectando/reconectando
                if (isConnecting || isReconnecting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = statusInfo.color,
                        strokeWidth = 2.dp
                    )
                }
            }
            
            // Mensaje de estado
            if (statusMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Start
                )
            }
            
            // Información de reconexión
            if (isReconnecting && reconnectionAttempts > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                ReconnectionInfo(
                    attempts = reconnectionAttempts,
                    maxAttempts = maxReconnectionAttempts,
                    lastError = lastError
                )
            }
            
            // Botones de acción
            if (showActions) {
                Spacer(modifier = Modifier.height(12.dp))
                DeviceActionButtons(
                    isConnected = isConnected,
                    isConnecting = isConnecting,
                    isReconnecting = isReconnecting,
                    onConnect = onConnect,
                    onDisconnect = onDisconnect,
                    onRetry = onRetry,
                    onStopReconnection = onStopReconnection
                )
            }
        }
    }
}

@Composable
private fun ReconnectionInfo(
    attempts: Int,
    maxAttempts: Int,
    lastError: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Reconectando... Intento $attempts/$maxAttempts",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFF9800),
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Barra de progreso
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = attempts.toFloat() / maxAttempts.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFF9800),
                trackColor = Color(0xFFFF9800).copy(alpha = 0.3f)
            )
            
            // Último error si existe
            lastError?.let { error ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Último error: $error",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD84315),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
private fun DeviceActionButtons(
    isConnected: Boolean,
    isConnecting: Boolean,
    isReconnecting: Boolean,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onRetry: () -> Unit,
    onStopReconnection: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when {
            isConnected -> {
                OutlinedButton(
                    onClick = onDisconnect,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFF44336)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Desconectar")
                }
            }
            
            isReconnecting -> {
                OutlinedButton(
                    onClick = onStopReconnection,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF9800)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Detener")
                }
            }
            
            else -> {
                Button(
                    onClick = onConnect,
                    modifier = Modifier.weight(1f),
                    enabled = !isConnecting
                ) {
                    if (isConnecting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Conectar")
                }
                
                if (!isConnecting) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}

private data class ConnectionStatusInfo(
    val statusText: String,
    val icon: ImageVector,
    val color: Color,
    val backgroundColor: Color
)

private fun getConnectionStatusInfo(
    isConnected: Boolean,
    isConnecting: Boolean,
    isScanning: Boolean,
    isReconnecting: Boolean,
    reconnectionAttempts: Int,
    maxReconnectionAttempts: Int,
    lastError: String?
): ConnectionStatusInfo {
    return when {
        isConnected -> ConnectionStatusInfo(
            statusText = "Conectado",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF4CAF50),
            backgroundColor = Color(0xFFE8F5E8)
        )
        
        isReconnecting -> ConnectionStatusInfo(
            statusText = "Reconectando...",
            icon = Icons.Default.Sync,
            color = Color(0xFFFF9800),
            backgroundColor = Color(0xFFFFF3E0)
        )
        
        isConnecting -> ConnectionStatusInfo(
            statusText = "Conectando...",
            icon = Icons.Default.Sync,
            color = Color(0xFF2196F3),
            backgroundColor = Color(0xFFE3F2FD)
        )
        
        isScanning -> ConnectionStatusInfo(
            statusText = "Buscando dispositivo...",
            icon = Icons.Default.Search,
            color = Color(0xFF9C27B0),
            backgroundColor = Color(0xFFF3E5F5)
        )
        
        lastError != null -> ConnectionStatusInfo(
            statusText = "Error de conexión",
            icon = Icons.Default.Error,
            color = Color(0xFFF44336),
            backgroundColor = Color(0xFFFFEBEE)
        )
        
        else -> ConnectionStatusInfo(
            statusText = "Desconectado",
            icon = Icons.Default.BluetoothDisabled,
            color = Color(0xFF757575),
            backgroundColor = Color(0xFFF5F5F5)
        )
    }
}
