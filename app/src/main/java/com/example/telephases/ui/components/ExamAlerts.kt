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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

/**
 * Componente para mostrar alertas y notificaciones en exámenes
 */
@Composable
fun ExamAlertDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    title: String,
    message: String,
    type: AlertType = AlertType.INFO,
    primaryAction: AlertAction? = null,
    secondaryAction: AlertAction? = null,
    showProgress: Boolean = false,
    progressMessage: String = "Procesando..."
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icono según el tipo de alerta
                    Icon(
                        imageVector = type.icon,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = type.color
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Título
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = type.color,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Mensaje
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // Indicador de progreso
                    if (showProgress) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = type.color
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = progressMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        secondaryAction?.let { action ->
                            OutlinedButton(
                                onClick = action.onClick,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(action.text)
                            }
                        }
                        
                        primaryAction?.let { action ->
                            Button(
                                onClick = action.onClick,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = type.color
                                )
                            ) {
                                Text(action.text)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente para notificaciones toast mejoradas
 */
@Composable
fun ExamToast(
    isVisible: Boolean,
    message: String,
    type: AlertType = AlertType.INFO,
    duration: Long = 3000L,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        LaunchedEffect(isVisible) {
            delay(duration)
            onDismiss()
        }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = type.backgroundColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = type.icon,
                    contentDescription = null,
                    tint = type.color,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = type.color,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Componente para indicador de estado de conexión mejorado
 */
@Composable
fun ConnectionStatusIndicator(
    isConnected: Boolean,
    isConnecting: Boolean,
    isScanning: Boolean,
    statusMessage: String,
    onRetry: () -> Unit = {},
    showRetryButton: Boolean = false
) {
    val statusColor = when {
        isConnected -> Color(0xFF4CAF50) // Verde
        isConnecting || isScanning -> Color(0xFFFF9800) // Naranja
        else -> Color(0xFFF44336) // Rojo
    }
    
    val statusIcon = when {
        isConnected -> Icons.Default.CheckCircle
        isConnecting || isScanning -> Icons.Default.Sync
        else -> Icons.Default.Error
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = statusColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = statusIcon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = statusColor,
                modifier = Modifier.weight(1f)
            )
            
            if (showRetryButton && !isConnected && !isConnecting) {
                IconButton(onClick = onRetry) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reintentar",
                        tint = statusColor
                    )
                }
            }
        }
    }
}

/**
 * Componente para mostrar progreso de operación
 */
@Composable
fun ExamProgressDialog(
    isVisible: Boolean,
    title: String,
    message: String,
    progress: Float? = null,
    onCancel: (() -> Unit)? = null
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = { onCancel?.invoke() },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (progress != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    onCancel?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(onClick = it) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tipos de alerta
 */
enum class AlertType(
    val icon: ImageVector,
    val color: Color,
    val backgroundColor: Color
) {
    SUCCESS(
        icon = Icons.Default.CheckCircle,
        color = Color(0xFF4CAF50),
        backgroundColor = Color(0xFFE8F5E8)
    ),
    ERROR(
        icon = Icons.Default.Error,
        color = Color(0xFFF44336),
        backgroundColor = Color(0xFFFFEBEE)
    ),
    WARNING(
        icon = Icons.Default.Warning,
        color = Color(0xFFFF9800),
        backgroundColor = Color(0xFFFFF3E0)
    ),
    INFO(
        icon = Icons.Default.Info,
        color = Color(0xFF2196F3),
        backgroundColor = Color(0xFFE3F2FD)
    ),
    CONNECTION(
        icon = Icons.Default.Bluetooth,
        color = Color(0xFF9C27B0),
        backgroundColor = Color(0xFFF3E5F5)
    )
}

/**
 * Acción de alerta
 */
data class AlertAction(
    val text: String,
    val onClick: () -> Unit
)
