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
fun ExamenTensiometroScreen(
    navController: NavController,
    citaId: Int,
    examenId: Int,
    examViewModel: ExamViewModel = hiltViewModel(),
    examenViewModel: ExamenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var presionSistolica by remember { mutableStateOf("") }
    var presionDiastolica by remember { mutableStateOf("") }
    var pulso by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var useBLE by remember { mutableStateOf(true) }
    var showInfo by remember { mutableStateOf(false) }
    val patientId by examenViewModel.patientId.collectAsStateWithLifecycle()
    
    // Obtener el patientId real de la cita
    LaunchedEffect(citaId) {
        examenViewModel.loadPatientId(citaId)
    }
    
    // Crear el manager BLE real
    val bleManager = remember { BLETensiometroManager(context) }
    val isConnected by bleManager.isConnected.collectAsStateWithLifecycle()
    val isConnecting by bleManager.isConnecting.collectAsStateWithLifecycle()
    val bloodPressureReading by bleManager.bloodPressureReading.collectAsStateWithLifecycle()
    val statusMessage by bleManager.statusMessage.collectAsStateWithLifecycle()
    
    // Verificar permisos
    val hasPermissions = bleManager.hasBluetoothPermissions()
    
    // Auto-llenar campos cuando se recibe lectura BLE
    LaunchedEffect(bloodPressureReading) {
        bloodPressureReading?.let { reading ->
            presionSistolica = reading.systolic.toString()
            presionDiastolica = reading.diastolic.toString()
            pulso = reading.pulse.toString()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Examen de Presión Arterial") },
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
                    Text("Tipo: Presión Arterial")
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
                                } else if (isConnecting) {
                                    bleManager.disconnect()
                                } else {
                                    bleManager.findAndConnect()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = hasPermissions,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    !hasPermissions -> MaterialTheme.colorScheme.outline
                                    isConnected -> MaterialTheme.colorScheme.error
                                    isConnecting -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            )
                        ) {
                            Icon(
                                imageVector = when {
                                    isConnected -> Icons.Filled.Stop
                                    isConnecting -> Icons.Filled.HourglassEmpty
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
                                    isConnecting -> "Conectando..."
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
                                    isConnecting -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
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
                                    text = statusMessage,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        // Mostrar lectura BLE si está disponible - MEJORADO
                        bloodPressureReading?.let { reading ->
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
                                    
                                    // Mostrar valores de forma destacada
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "Sistólica",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFF666666)
                                            )
                                            Text(
                                                text = "${reading.systolic}",
                                                style = MaterialTheme.typography.headlineLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                            Text(
                                                text = "mmHg",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF666666)
                                            )
                                        }
                                        
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "Diastólica",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFF666666)
                                            )
                                            Text(
                                                text = "${reading.diastolic}",
                                                style = MaterialTheme.typography.headlineLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                            Text(
                                                text = "mmHg",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF666666)
                                            )
                                        }
                                        
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "Pulso",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFF666666)
                                            )
                                            Text(
                                                text = "${reading.pulse}",
                                                style = MaterialTheme.typography.headlineLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                            Text(
                                                text = "bpm",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF666666)
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Mostrar arritmia si está presente
                                    if (reading.hasArrhythmia) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFFFFF3E0)
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Warning,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFF9800),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "Arritmia detectada",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color(0xFFE65100),
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
                            value = presionSistolica,
                            onValueChange = { presionSistolica = it },
                            label = { Text("Presión Sistólica (mmHg)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        OutlinedTextField(
                            value = presionDiastolica,
                            onValueChange = { presionDiastolica = it },
                            label = { Text("Presión Diastólica (mmHg)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        
                        OutlinedTextField(
                            value = pulso,
                            onValueChange = { pulso = it },
                            label = { Text("Pulso (bpm)") },
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
                        if (!isCompleted && presionSistolica.isNotEmpty() && presionDiastolica.isNotEmpty()) {
                            // Guardar el examen localmente y marcar como completado
                            scope.launch {
                                try {
                                    val examEntity = com.example.telephases.data.local.entities.ExamEntity.createForOffline(
                                        patientId = patientId ?: "temp-patient-id",
                                        tipoExamenNombre = "BLOOD_PRESSURE",
                                        titulo = "Medición de Presión Arterial",
                                        valor = "$presionSistolica/$presionDiastolica",
                                        unidad = "mmHg",
                                        observaciones = "Medición realizada con dispositivo BLE",
                                        datosAdicionales = mapOf(
                                            "cita_id" to citaId,
                                            "examen_id" to examenId,
                                            "metodo" to if (useBLE) "BLE" else "Manual",
                                            "presion_sistolica" to presionSistolica,
                                            "presion_diastolica" to presionDiastolica,
                                            "pulso" to pulso,
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
                title = { Text("Pasos para el Examen de Presión Arterial") },
                text = {
                    Column {
                        Text("1. Conecte el tensiómetro BLE o ingrese los datos manualmente")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("2. Coloque el brazalete en el brazo izquierdo del paciente")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("3. Asegúrese de que el brazalete esté a la altura del corazón")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("4. El paciente debe estar sentado y relajado")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("5. Inicie la medición y espere a que termine")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("6. Registre los valores de presión sistólica y diastólica")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("7. Anote la frecuencia cardíaca si está disponible")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("8. Haga clic en 'Guardar y Completar' para finalizar")
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