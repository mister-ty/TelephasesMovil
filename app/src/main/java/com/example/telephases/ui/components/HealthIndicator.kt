package com.example.telephases.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.telephases.data.local.entities.ExamEntity

/**
 * Sistema de indicadores de salud para exámenes médicos
 * Evalúa el estado de salud basado en los valores de los exámenes
 */

/**
 * Estados de salud posibles
 */
enum class HealthStatus {
    GOOD,       // Bien - Valores normales
    WARNING,    // Advertencia - Valores fuera de rango pero no críticos
    URGENT      // Urgencia - Valores críticos que requieren atención inmediata
}

/**
 * Resultado de evaluación de salud
 */
data class HealthEvaluation(
    val status: HealthStatus,
    val message: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color,
    val recommendation: String? = null
)

/**
 * Evaluador de salud para exámenes médicos
 */
object HealthEvaluator {
    
    /**
     * Evalúa el estado de salud de un examen
     */
    fun evaluateExam(exam: ExamEntity): HealthEvaluation {
        return when (exam.tipoExamenNombre.uppercase()) {
            "TEMPERATURE" -> evaluateTemperature(exam.valor)
            "GLUCOSE" -> evaluateGlucose(exam.valor)
            "BLOOD_PRESSURE" -> evaluateBloodPressure(exam.valor)
            "OXYGEN_SATURATION" -> evaluateOxygenSaturation(exam.valor)
            "WEIGHT" -> evaluateWeight(exam.valor)
            else -> HealthEvaluation(
                status = HealthStatus.GOOD,
                message = "Examen normal",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF4CAF50)
            )
        }
    }
    
    /**
     * Evalúa temperatura corporal
     */
    private fun evaluateTemperature(value: String): HealthEvaluation {
        val temp = value.replace("°C", "").replace("°F", "").toDoubleOrNull() ?: 0.0
        return when {
            temp < 35.0 -> HealthEvaluation(
                status = HealthStatus.URGENT,
                message = "Hipotermia - Requiere atención inmediata",
                icon = Icons.Default.Warning,
                color = Color(0xFFE53935),
                recommendation = "Buscar atención médica urgente"
            )
            temp < 36.0 -> HealthEvaluation(
                status = HealthStatus.WARNING,
                message = "Temperatura baja",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                recommendation = "Monitorear y abrigarse"
            )
            temp <= 37.5 -> HealthEvaluation(
                status = HealthStatus.GOOD,
                message = "Temperatura normal",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF4CAF50)
            )
            temp <= 38.5 -> HealthEvaluation(
                status = HealthStatus.WARNING,
                message = "Fiebre leve",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                recommendation = "Reposo y hidratación"
            )
            else -> HealthEvaluation(
                status = HealthStatus.URGENT,
                message = "Fiebre alta - Requiere atención",
                icon = Icons.Default.Warning,
                color = Color(0xFFE53935),
                recommendation = "Buscar atención médica"
            )
        }
    }
    
    /**
     * Evalúa glucosa en sangre
     */
    private fun evaluateGlucose(value: String): HealthEvaluation {
        val glucose = value.replace("mg/dL", "").toDoubleOrNull() ?: 0.0
        return when {
            glucose < 70 -> HealthEvaluation(
                status = HealthStatus.URGENT,
                message = "Hipoglucemia - Requiere atención",
                icon = Icons.Default.Warning,
                color = Color(0xFFE53935),
                recommendation = "Consumir azúcar inmediatamente"
            )
            glucose <= 100 -> HealthEvaluation(
                status = HealthStatus.GOOD,
                message = "Glucosa normal",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF4CAF50)
            )
            glucose <= 125 -> HealthEvaluation(
                status = HealthStatus.WARNING,
                message = "Glucosa elevada",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                recommendation = "Monitorear dieta y ejercicio"
            )
            else -> HealthEvaluation(
                status = HealthStatus.URGENT,
                message = "Hiperglucemia - Requiere atención",
                icon = Icons.Default.Warning,
                color = Color(0xFFE53935),
                recommendation = "Buscar atención médica"
            )
        }
    }
    
    /**
     * Evalúa presión arterial
     */
    private fun evaluateBloodPressure(value: String): HealthEvaluation {
        val parts = value.split("/")
        if (parts.size != 2) return HealthEvaluation(
            status = HealthStatus.GOOD,
            message = "Presión arterial normal",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF4CAF50)
        )
        
        val systolic = parts[0].toDoubleOrNull() ?: 0.0
        val diastolic = parts[1].toDoubleOrNull() ?: 0.0
        
        return when {
            systolic < 90 || diastolic < 60 -> HealthEvaluation(
                status = HealthStatus.WARNING,
                message = "Presión arterial baja",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                recommendation = "Monitorear y consultar médico"
            )
            systolic <= 120 && diastolic <= 80 -> HealthEvaluation(
                status = HealthStatus.GOOD,
                message = "Presión arterial normal",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF4CAF50)
            )
            systolic <= 140 && diastolic <= 90 -> HealthEvaluation(
                status = HealthStatus.WARNING,
                message = "Presión arterial elevada",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                recommendation = "Cambios en estilo de vida"
            )
            else -> HealthEvaluation(
                status = HealthStatus.URGENT,
                message = "Hipertensión - Requiere atención",
                icon = Icons.Default.Warning,
                color = Color(0xFFE53935),
                recommendation = "Buscar atención médica urgente"
            )
        }
    }
    
    /**
     * Evalúa saturación de oxígeno
     */
    private fun evaluateOxygenSaturation(value: String): HealthEvaluation {
        val oxygen = value.replace("%", "").toDoubleOrNull() ?: 0.0
        return when {
            oxygen < 90 -> HealthEvaluation(
                status = HealthStatus.URGENT,
                message = "Saturación baja - Requiere atención",
                icon = Icons.Default.Warning,
                color = Color(0xFFE53935),
                recommendation = "Buscar atención médica urgente"
            )
            oxygen < 95 -> HealthEvaluation(
                status = HealthStatus.WARNING,
                message = "Saturación reducida",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                recommendation = "Monitorear y consultar médico"
            )
            else -> HealthEvaluation(
                status = HealthStatus.GOOD,
                message = "Saturación normal",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF4CAF50)
            )
        }
    }
    
    /**
     * Evalúa peso corporal
     */
    private fun evaluateWeight(value: String): HealthEvaluation {
        val weight = value.replace("kg", "").replace("lbs", "").toDoubleOrNull() ?: 0.0
        return when {
            weight < 30 -> HealthEvaluation(
                status = HealthStatus.WARNING,
                message = "Peso muy bajo",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                recommendation = "Consultar nutricionista"
            )
            weight > 200 -> HealthEvaluation(
                status = HealthStatus.WARNING,
                message = "Peso elevado",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                recommendation = "Consultar nutricionista"
            )
            else -> HealthEvaluation(
                status = HealthStatus.GOOD,
                message = "Peso normal",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

/**
 * Composable para mostrar el indicador de salud de un examen
 */
@Composable
fun HealthIndicatorCard(
    exam: ExamEntity,
    modifier: Modifier = Modifier
) {
    val evaluation = remember(exam) { HealthEvaluator.evaluateExam(exam) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = evaluation.color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exam.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${exam.valor} ${exam.unidad ?: ""}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Icon(
                    imageVector = evaluation.icon,
                    contentDescription = evaluation.message,
                    tint = evaluation.color,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = evaluation.message,
                style = MaterialTheme.typography.bodyMedium,
                color = evaluation.color,
                fontWeight = FontWeight.Medium
            )
            
            evaluation.recommendation?.let { recommendation ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💡 $recommendation",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Composable para mostrar el resumen de salud de múltiples exámenes
 */
@Composable
fun HealthSummaryCard(
    exams: List<ExamEntity>,
    modifier: Modifier = Modifier
) {
    val evaluations = remember(exams) { 
        exams.map { HealthEvaluator.evaluateExam(it) }
    }
    
    val urgentCount = evaluations.count { it.status == HealthStatus.URGENT }
    val warningCount = evaluations.count { it.status == HealthStatus.WARNING }
    val goodCount = evaluations.count { it.status == HealthStatus.GOOD }
    
    val overallStatus = when {
        urgentCount > 0 -> HealthStatus.URGENT
        warningCount > 0 -> HealthStatus.WARNING
        else -> HealthStatus.GOOD
    }
    
    val overallEvaluation = when (overallStatus) {
        HealthStatus.URGENT -> HealthEvaluation(
            status = HealthStatus.URGENT,
            message = "Atención médica requerida",
            icon = Icons.Default.Warning,
            color = Color(0xFFE53935)
        )
        HealthStatus.WARNING -> HealthEvaluation(
            status = HealthStatus.WARNING,
            message = "Monitoreo recomendado",
            icon = Icons.Default.Info,
            color = Color(0xFFFF9800)
        )
        HealthStatus.GOOD -> HealthEvaluation(
            status = HealthStatus.GOOD,
            message = "Estado de salud estable",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF4CAF50)
        )
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            overallEvaluation.color.copy(alpha = 0.05f),
                            Color.White
                        )
                    )
                )
                .padding(20.dp)
        ) {
            // Header con icono grande y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dashboard de Salud",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${exams.size} exámenes evaluados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Icono grande con fondo circular
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            overallEvaluation.color.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = overallEvaluation.icon,
                        contentDescription = overallEvaluation.message,
                        tint = overallEvaluation.color,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mensaje de estado en tarjeta destacada
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = overallEvaluation.color.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = overallEvaluation.icon,
                        contentDescription = null,
                        tint = overallEvaluation.color,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = overallEvaluation.message,
                        style = MaterialTheme.typography.titleMedium,
                        color = overallEvaluation.color,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Estadísticas en grid 2x2
            Text(
                text = "Distribución de Resultados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Grid de estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Columna izquierda
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    HealthStatusItem(
                        label = "Estable",
                        count = goodCount,
                        color = Color(0xFF4CAF50),
                        icon = Icons.Default.CheckCircle,
                        isHighlighted = goodCount > 0 && urgentCount == 0 && warningCount == 0
                    )
                }
                
                // Columna derecha
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    HealthStatusItem(
                        label = "Monitoreo",
                        count = warningCount,
                        color = Color(0xFFFF9800),
                        icon = Icons.Default.Info,
                        isHighlighted = warningCount > 0 && urgentCount == 0
                    )
                }
            }
            
            // Fila inferior para urgente (si existe)
            if (urgentCount > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    HealthStatusItem(
                        label = "Urgente",
                        count = urgentCount,
                        color = Color(0xFFE53935),
                        icon = Icons.Default.Warning,
                        isHighlighted = true
                    )
                }
            }
            
            // Barra de progreso visual mejorada
            if (exams.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Progreso Visual",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Barra verde (estable)
                    if (goodCount > 0) {
                        Box(
                            modifier = Modifier
                                .weight(goodCount.toFloat())
                                .height(12.dp)
                                .background(
                                    Color(0xFF4CAF50),
                                    RoundedCornerShape(6.dp)
                                )
                        )
                    }
                    
                    // Barra naranja (monitoreo)
                    if (warningCount > 0) {
                        Box(
                            modifier = Modifier
                                .weight(warningCount.toFloat())
                                .height(12.dp)
                                .background(
                                    Color(0xFFFF9800),
                                    RoundedCornerShape(6.dp)
                                )
                        )
                    }
                    
                    // Barra roja (urgente)
                    if (urgentCount > 0) {
                        Box(
                            modifier = Modifier
                                .weight(urgentCount.toFloat())
                                .height(12.dp)
                                .background(
                                    Color(0xFFE53935),
                                    RoundedCornerShape(6.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HealthStatusItem(
    label: String,
    count: Int,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isHighlighted: Boolean = false
) {
    Card(
        modifier = Modifier.width(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) color.copy(alpha = 0.1f) else Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = if (isHighlighted) CardDefaults.cardElevation(4.dp) else CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono con fondo circular
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color.copy(alpha = if (isHighlighted) 0.2f else 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Contador
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) color else MaterialTheme.colorScheme.onSurface
            )
            
            // Etiqueta
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = if (isHighlighted) color else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = if (isHighlighted) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
