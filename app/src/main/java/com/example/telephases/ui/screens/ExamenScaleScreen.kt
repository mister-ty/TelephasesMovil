package com.example.telephases.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.telephases.ui.viewmodel.ExamViewModel
import com.example.telephases.ui.viewmodel.ExamenViewModel
import java.time.Instant
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamenScaleScreen(
    navController: NavController,
    citaId: Int,
    examenId: Int,
    examViewModel: ExamViewModel = hiltViewModel(),
    examenViewModel: ExamenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var peso by remember { mutableStateOf("") }
    var unidadPeso by remember { mutableStateOf("kg") }
    var isCompleted by remember { mutableStateOf(false) }
    var useBLE by remember { mutableStateOf(true) }
    val patientId by examenViewModel.patientId.collectAsStateWithLifecycle()
    
    // Obtener el patientId real de la cita
    LaunchedEffect(citaId) {
        examenViewModel.loadPatientId(citaId)
    }
    
    // Crear el manager BLE real
    val bleManager = remember { BLEScaleManager(context) }
    val isScanning by bleManager.isScanning.collectAsStateWithLifecycle()
    val scaleReading by bleManager.scaleReading.collectAsStateWithLifecycle()
    val logs by bleManager.logs.collectAsStateWithLifecycle()
    
    // Verificar permisos (asumir que están disponibles por ahora)
    val hasPermissions = true
    
    // Auto-llenar campos cuando se recibe lectura BLE
    LaunchedEffect(scaleReading) {
        scaleReading?.let { reading ->
            peso = String.format("%.2f", reading.weight)
            unidadPeso = reading.unit
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Examen de Peso") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Información del examen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información del Examen",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Cita ID: $citaId")
                    Text("Examen ID: $examenId")
                    Text("Tipo: Peso")
                }
            }
            
            // Selector de modo de medición
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Modo de Medición",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { useBLE = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (useBLE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Bluetooth, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Bluetooth")
                        }
                        
                        Button(
                            onClick = { useBLE = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!useBLE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Manual")
                        }
                    }
                }
            }
            
            // Contenido según el modo seleccionado
            if (useBLE) {
                // Modo Bluetooth
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Medición Bluetooth",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // Botón de escaneo
                        Button(
                            onClick = { 
                                bleManager.startScan()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = hasPermissions,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    !hasPermissions -> MaterialTheme.colorScheme.outline
                                    isScanning -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            )
                        ) {
                            Icon(
                                imageVector = if (isScanning) Icons.Filled.Stop else Icons.Filled.Bluetooth,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                when {
                                    !hasPermissions -> "Permisos Requeridos"
                                    isScanning -> "Escaneando..."
                                    else -> "Iniciar Escaneo BLE"
                                }
                            )
                        }
                        
                        // Estado de escaneo
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isScanning) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Estado de Escaneo",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isScanning) "Escaneando dispositivos BLE..." else "Listo para escanear",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Mostrar lectura BLE si está disponible - MEJORADO
                        scaleReading?.let { reading ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F5E8)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(48.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = "Medición Completada",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CAF50)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Mostrar peso de forma destacada
                                    Text(
                                        text = "${String.format("%.2f", reading.weight)} ${reading.unit}",
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Mostrar información adicional
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFFF1F8E9)
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp)
                                        ) {
                                            Text(
                                                text = "Información de la Medición",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Peso:",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color(0xFF666666)
                                                )
                                                Text(
                                                    text = "${String.format("%.2f", reading.weight)} ${reading.unit}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                            
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Impedancia:",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color(0xFF666666)
                                                )
                                                Text(
                                                    text = "${reading.impedance} Ω",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                            
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Estado:",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color(0xFF666666)
                                                )
                                                Text(
                                                    text = "Estable",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color(0xFF4CAF50)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Logs de BLE
                        if (logs.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Logs BLE",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    logs.takeLast(3).forEach { log ->
                                        Text(
                                            text = log,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Modo Manual
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Medición Manual",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        OutlinedTextField(
                            value = peso,
                            onValueChange = { peso = it },
                            label = { Text("Peso") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        OutlinedTextField(
                            value = unidadPeso,
                            onValueChange = { unidadPeso = it },
                            label = { Text("Unidad de Peso") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { 
                        if (!isCompleted && peso.isNotEmpty()) {
                            // Guardar el examen localmente y marcar como completado
                            scope.launch {
                                try {
                                    val examEntity = com.example.telephases.data.local.entities.ExamEntity.createForOffline(
                                        patientId = patientId ?: "temp-patient-id",
                                        tipoExamenNombre = "WEIGHT",
                                        titulo = "Medición de Peso",
                                        valor = peso,
                                        unidad = unidadPeso,
                                        observaciones = "Medición realizada con dispositivo BLE",
                                        datosAdicionales = mapOf(
                                            "cita_id" to citaId,
                                            "examen_id" to examenId,
                                            "metodo" to if (useBLE) "BLE" else "Manual",
                                            "peso" to peso,
                                            "unidad" to unidadPeso,
                                            "timestamp" to Instant.now().toString()
                                        )
                                    )
                                    examViewModel.saveExam(examEntity)
                                    isCompleted = true // Marcar como completado automáticamente
                                } catch (e: Exception) {
                                    println("Error guardando examen: ${e.message}")
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isCompleted) "Completado" else "Guardar y Completar")
                }
                
                Button(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar y Salir")
                }
            }
            
            // Estado del examen
            if (isCompleted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Completado",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Examen completado exitosamente",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}