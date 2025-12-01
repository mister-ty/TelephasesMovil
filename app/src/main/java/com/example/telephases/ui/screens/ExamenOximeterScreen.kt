package com.example.telephases.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
fun ExamenOximeterScreen(
    navController: NavController,
    citaId: Int,
    examenId: Int,
    examViewModel: ExamViewModel = hiltViewModel(),
    examenViewModel: ExamenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var saturacionOxigeno by remember { mutableStateOf("") }
    var frecuenciaCardiaca by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var useBLE by remember { mutableStateOf(true) }
    val patientId by examenViewModel.patientId.collectAsStateWithLifecycle()
    
    // Obtener el patientId real de la cita
    LaunchedEffect(citaId) {
        examenViewModel.loadPatientId(citaId)
    }
    
    // Crear el manager BLE real
    val bleManager = remember { BLEOximeterManager(context) }
    val isConnected by bleManager.isConnected.collectAsStateWithLifecycle()
    val isConnecting by bleManager.isConnecting.collectAsStateWithLifecycle()
    val isScanning by bleManager.isScanning.collectAsStateWithLifecycle()
    val isMeasuring by bleManager.isMeasuring.collectAsStateWithLifecycle()
    val oximeterReading by bleManager.oximeterReading.collectAsStateWithLifecycle()
    val hasPermissions = true // Placeholder for now
    
    // Auto-llenar campos cuando se recibe lectura BLE
    LaunchedEffect(oximeterReading) {
        oximeterReading?.let { reading ->
            saturacionOxigeno = reading.spo2.toString()
            frecuenciaCardiaca = reading.pulseRate.toString()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Examen de Oxímetro") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Información de la cita
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Cita ID: $citaId",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Examen ID: $examenId",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Selector de modo
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Modo de Medición",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { useBLE = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (useBLE) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Bluetooth, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Bluetooth")
                        }
                        Button(
                            onClick = { useBLE = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!useBLE) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Manual")
                        }
                    }
                }
            }
            
            if (useBLE) {
                // Estado de conexión BLE
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isConnected -> MaterialTheme.colorScheme.primaryContainer
                            isConnecting || isScanning -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when {
                                    isConnected -> Icons.Filled.BluetoothConnected
                                    isConnecting || isScanning -> Icons.Filled.BluetoothSearching
                                    else -> Icons.Filled.Bluetooth
                                },
                                contentDescription = null,
                                tint = when {
                                    isConnected -> MaterialTheme.colorScheme.primary
                                    isConnecting || isScanning -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when {
                                    isConnected -> "Conectado al Oxímetro"
                                    isConnecting -> "Conectando..."
                                    isScanning -> "Escaneando..."
                                    else -> "Desconectado"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        if (isMeasuring) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Midiendo...")
                            }
                        }
                    }
                }
                
                // Botón de conexión BLE
                Button(
                    onClick = {
                        if (isConnected) {
                            bleManager.disconnect()
                        } else if (isConnecting || isScanning) {
                            bleManager.disconnect()
                        } else {
                            bleManager.startScan()
                        }
                    },
                    enabled = hasPermissions,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            !hasPermissions -> MaterialTheme.colorScheme.outline
                            isConnected -> MaterialTheme.colorScheme.error
                            isConnecting || isScanning -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.primary
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = when {
                            isConnected -> Icons.Filled.BluetoothDisabled
                            isConnecting || isScanning -> Icons.Filled.Stop
                            else -> Icons.Filled.BluetoothSearching
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        when {
                            !hasPermissions -> "Permisos Requeridos"
                            isConnected -> "Desconectar"
                            isConnecting || isScanning -> "Detener"
                            else -> "Iniciar Escaneo BLE"
                        }
                    )
                }
            }
            
            // Campos de entrada
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Datos de Medición",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = saturacionOxigeno,
                        onValueChange = { saturacionOxigeno = it },
                        label = { Text("Saturación de Oxígeno (%)") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !useBLE || isConnected,
                        suffix = { Text("%") }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = frecuenciaCardiaca,
                        onValueChange = { frecuenciaCardiaca = it },
                        label = { Text("Frecuencia Cardíaca (BPM)") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !useBLE || isConnected,
                        suffix = { Text("BPM") }
                    )
                }
            }
            
            // Mostrar datos BLE si están disponibles
            if (useBLE && oximeterReading != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Datos del Dispositivo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "SpO2",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${oximeterReading?.spo2}%",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column {
                                Text(
                                    text = "Frecuencia Cardíaca",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${oximeterReading?.pulseRate} BPM",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
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
                        if (!isCompleted && saturacionOxigeno.isNotEmpty() && frecuenciaCardiaca.isNotEmpty()) {
                            // Guardar el examen localmente y marcar como completado
                            scope.launch {
                                try {
                                    val examEntity = com.example.telephases.data.local.entities.ExamEntity.createForOffline(
                                        patientId = patientId ?: "temp-patient-id",
                                        tipoExamenNombre = "OXYGEN_SATURATION",
                                        titulo = "Medición de Saturación de Oxígeno",
                                        valor = saturacionOxigeno,
                                        unidad = "%",
                                        observaciones = "Medición realizada con dispositivo BLE",
                                        datosAdicionales = mapOf(
                                            "cita_id" to citaId,
                                            "examen_id" to examenId,
                                            "metodo" to if (useBLE) "BLE" else "Manual",
                                            "saturacion_oxigeno" to saturacionOxigeno,
                                            "frecuencia_cardiaca" to frecuenciaCardiaca,
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
                        containerColor = MaterialTheme.colorScheme.outline
                    )
                ) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancelar")
                }
            }
            
            // Mensaje de éxito
            if (isCompleted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Completado",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Examen completado exitosamente",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
