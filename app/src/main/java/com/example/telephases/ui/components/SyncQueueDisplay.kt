package com.example.telephases.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.telephases.data.local.entities.PatientEntity
import com.example.telephases.data.local.entities.ExamEntity
import com.example.telephases.data.local.entities.UserEntity

/**
 * Cola visual de elementos pendientes de sincronización
 */
@Composable
fun SyncQueueDisplay(
    pendingPatients: List<PatientEntity> = emptyList(),
    pendingExams: List<ExamEntity> = emptyList(),
    pendingUsers: List<UserEntity> = emptyList(),
    isExpanded: Boolean = false,
    onToggleExpanded: () -> Unit = {},
    onSyncItem: ((String, String) -> Unit)? = null, // (itemId, itemType)
    modifier: Modifier = Modifier
) {
    val totalPending = pendingPatients.size + pendingExams.size + pendingUsers.size
    
    if (totalPending == 0) return
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudQueue,
                        contentDescription = "Cola de sincronización",
                        tint = Color(0xFFE65100),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cola de sincronización",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Badge(
                        containerColor = Color(0xFFFF9800)
                    ) {
                        Text(
                            text = totalPending.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = onToggleExpanded,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Contraer" else "Expandir",
                            tint = Color(0xFFE65100)
                        )
                    }
                }
            }
            
            // Resumen
            if (!isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        if (pendingPatients.isNotEmpty()) append("${pendingPatients.size} pacientes")
                        if (pendingExams.isNotEmpty()) {
                            if (isNotEmpty()) append(", ")
                            append("${pendingExams.size} exámenes")
                        }
                        if (pendingUsers.isNotEmpty()) {
                            if (isNotEmpty()) append(", ")
                            append("${pendingUsers.size} usuarios")
                        }
                        append(" pendientes")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFBF360C)
                )
            }
            
            // Lista expandida
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pacientes pendientes
                    if (pendingPatients.isNotEmpty()) {
                        item {
                            SectionHeader("Pacientes", pendingPatients.size)
                        }
                        items(pendingPatients) { patient ->
                            SyncQueueItem(
                                title = patient.nombreCompleto,
                                subtitle = "Doc: ${patient.numeroDocumento}",
                                icon = Icons.Default.Person,
                                timestamp = patient.fechaModificacionLocal,
                                onSyncClick = { onSyncItem?.invoke(patient.id, "patient") }
                            )
                        }
                    }
                    
                    // Exámenes pendientes
                    if (pendingExams.isNotEmpty()) {
                        item {
                            SectionHeader("Exámenes", pendingExams.size)
                        }
                        items(pendingExams) { exam ->
                            SyncQueueItem(
                                title = exam.titulo,
                                subtitle = "${exam.tipoExamenNombre} - ${exam.valor} ${exam.unidad ?: ""}",
                                icon = Icons.Default.Assignment,
                                timestamp = exam.fechaModificacionLocal,
                                onSyncClick = { onSyncItem?.invoke(exam.id, "exam") }
                            )
                        }
                    }
                    
                    // Usuarios pendientes
                    if (pendingUsers.isNotEmpty()) {
                        item {
                            SectionHeader("Usuarios", pendingUsers.size)
                        }
                        items(pendingUsers) { user ->
                            SyncQueueItem(
                                title = user.nombreCompleto ?: user.username,
                                subtitle = user.email,
                                icon = Icons.Default.AccountCircle,
                                timestamp = user.fechaModificacionLocal,
                                onSyncClick = { onSyncItem?.invoke(user.id, "user") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE65100)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Badge(
            containerColor = Color(0xFFFF9800)
        ) {
            Text(
                text = count.toString(),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun SyncQueueItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    timestamp: String?,
    onSyncClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBE6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFBF360C),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8D6E63),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                timestamp?.let {
                    Text(
                        text = formatTimestamp(it),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }
            
            onSyncClick?.let { onClick ->
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Sincronizar elemento",
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Pequeño indicador de elementos pendientes para mostrar en barras de herramientas
 */
@Composable
fun PendingSyncBadge(
    pendingCount: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (pendingCount == 0) return
    
    Badge(
        modifier = modifier,
        containerColor = Color(0xFFFF9800)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CloudQueue,
                contentDescription = "Elementos pendientes",
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = pendingCount.toString(),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

private fun formatTimestamp(timestamp: String?): String {
    if (timestamp == null) return ""
    
    return try {
        val instant = java.time.Instant.parse(timestamp)
        val now = java.time.Instant.now()
        val duration = java.time.Duration.between(instant, now)
        
        when {
            duration.toDays() > 0 -> "hace ${duration.toDays()} días"
            duration.toHours() > 0 -> "hace ${duration.toHours()} horas"
            duration.toMinutes() > 0 -> "hace ${duration.toMinutes()} min"
            else -> "hace un momento"
        }
    } catch (e: Exception) {
        "fecha desconocida"
    }
}


