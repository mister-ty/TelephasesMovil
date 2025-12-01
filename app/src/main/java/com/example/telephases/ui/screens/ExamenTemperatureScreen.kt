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
fun ExamenTemperatureScreen(
    navController: NavController,
    citaId: Int,
    examenId: Int,
    examViewModel: ExamViewModel = hiltViewModel(),
    examenViewModel: ExamenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var temperatura by remember { mutableStateOf("") }
    var unidadTemperatura by remember { mutableStateOf("°C") }
    var isCompleted by remember { mutableStateOf(false) }
    var useBLE by remember { mutableStateOf(true) }
    var showInfo by remember { mutableStateOf(false) }
    val patientId by examenViewModel.patientId.collectAsStateWithLifecycle()
    
    // Obtener el patientId real de la cita
    LaunchedEffect(citaId) {
        examenViewModel.loadPatientId(citaId)
    }
    
    // Crear el manager BLE real
    val bleManager = remember { BLETemperatureManager(context) }
    val isConnected by bleManager.isConnected.collectAsStateWithLifecycle()
    val isScanning by bleManager.isScanning.collectAsStateWithLifecycle()
    val temperatureReading by bleManager.temperature.collectAsStateWithLifecycle()
    
    // Verificar permisos
    val hasPermissions = bleManager.hasBluetoothPermissions()
    
    // Auto-llenar campos cuando se recibe lectura BLE
    LaunchedEffect(temperatureReading) {
        temperatureReading?.let { reading ->
            temperatura = String.format("%.1f", reading.temperature)
            unidadTemperatura = "°C" // TemperatureReading no tiene unit, usar por defecto
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Examen de Temperatura") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { showInfo = true }) {
                        Icon(Icons.Filled.Info, contentDescription = "Información")
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
                    Text("Tipo: Temperatura")
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
                        
                        // Mensaje de permisos
                        if (!hasPermissions) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFF3E0)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFFF9800),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Se requieren permisos de Bluetooth para conectar dispositivos",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFFE65100)
                                    )
                                }
                            }
                        }
                        
                        // Botón de escaneo
                        Button(
                            onClick = { 
                                if (isConnected) {
                                    bleManager.disconnect()
                                } else if (isScanning) {
                                    bleManager.stopScan()
                                } else {
                                    bleManager.startScan()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = hasPermissions,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    !hasPermissions -> MaterialTheme.colorScheme.outline
                                    isConnected -> MaterialTheme.colorScheme.error
                                    isScanning -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            )
                        ) {
                            Icon(
                                imageVector = when {
                                    isConnected -> Icons.Filled.Stop
                                    isScanning -> Icons.Filled.HourglassEmpty
                                    else -> Icons.Filled.Bluetooth
                                },
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                when {
                                    !hasPermissions -> "Permisos Requeridos"
                                    isConnected -> "Desconectar"
                                    isScanning -> "Escaneando..."
                                    else -> "Iniciar Escaneo BLE"
                                }
                            )
                        }
                        
                        // Estado de conexión
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isConnected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    isScanning -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Estado de Conexión",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isConnected) "Conectado" else if (isScanning) "Escaneando..." else "Listo para conectar",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Mostrar lectura BLE si está disponible - MEJORADO
                        temperatureReading?.let { reading ->
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
                                    
                                    // Mostrar temperatura de forma destacada
                                    Text(
                                        text = "${String.format("%.1f", reading.temperature)}°C",
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
                                                    text = "Temperatura:",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color(0xFF666666)
                                                )
                                                Text(
                                                    text = "${String.format("%.1f", reading.temperature)}°C",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                            
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Modo:",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color(0xFF666666)
                                                )
                                                Text(
                                                    text = reading.mode,
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
                                            
                                            // Indicador de temperatura normal
                                            val tempValue = reading.temperature
                                            val tempStatus = when {
                                                tempValue < 36.0 -> "Hipotermia"
                                                tempValue <= 37.5 -> "Normal"
                                                tempValue <= 38.0 -> "Fiebre leve"
                                                tempValue <= 39.0 -> "Fiebre moderada"
                                                else -> "Fiebre alta"
                                            }
                                            
                                            val tempColor = when {
                                                tempValue < 36.0 -> Color(0xFF2196F3)
                                                tempValue <= 37.5 -> Color(0xFF4CAF50)
                                                tempValue <= 38.0 -> Color(0xFFFF9800)
                                                tempValue <= 39.0 -> Color(0xFFFF5722)
                                                else -> Color(0xFFF44336)
                                            }
                                            
                                            Spacer(modifier = Modifier.height(8.dp))
                                            
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = tempColor.copy(alpha = 0.1f)
                                                )
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = when {
                                                            tempValue <= 37.5 -> Icons.Filled.CheckCircle
                                                            tempValue <= 38.0 -> Icons.Filled.Warning
                                                            else -> Icons.Filled.Error
                                                        },
                                                        contentDescription = null,
                                                        tint = tempColor,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Estado: $tempStatus",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = tempColor,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                            }
                                        }
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
                            value = temperatura,
                            onValueChange = { temperatura = it },
                            label = { Text("Temperatura") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        OutlinedTextField(
                            value = unidadTemperatura,
                            onValueChange = { unidadTemperatura = it },
                            label = { Text("Unidad de Temperatura") },
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
                        if (!isCompleted && temperatura.isNotEmpty()) {
                            // Guardar el examen localmente y marcar como completado
                            scope.launch {
                                try {
                                    val examEntity = com.example.telephases.data.local.entities.ExamEntity.createForOffline(
                                        patientId = patientId ?: "temp-patient-id",
                                        tipoExamenNombre = "TEMPERATURE",
                                        titulo = "Medición de Temperatura",
                                        valor = temperatura,
                                        unidad = unidadTemperatura,
                                        observaciones = "Medición realizada con dispositivo BLE",
                                        datosAdicionales = mapOf(
                                            "cita_id" to citaId,
                                            "examen_id" to examenId,
                                            "metodo" to if (useBLE) "BLE" else "Manual",
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
                    modifier = Modifier.fillMaxWidth(),
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
        
        // Diálogo de información
        if (showInfo) {
            AlertDialog(
                onDismissRequest = { showInfo = false },
                title = { Text("Pasos para el Examen de Temperatura") },
                text = {
                    Column {
                        Text("1. Conecte el termómetro BLE o ingrese la temperatura manualmente")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("2. Coloque el termómetro en la axila del paciente")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("3. Asegúrese de que el termómetro esté en contacto con la piel")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("4. El paciente debe estar relajado y sin moverse")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("5. Espere a que el termómetro complete la medición")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("6. Registre la temperatura mostrada")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("7. Haga clic en 'Guardar y Completar' para finalizar")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showInfo = false }) {
                        Text("Entendido")
                    }
                }
            )
        }
    }
}