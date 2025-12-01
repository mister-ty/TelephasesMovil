package com.example.telephases.ui.screens

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.telephases.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.telephases.ui.viewmodel.AuthViewModel
import com.example.telephases.ui.viewmodel.AuthState
import com.example.telephases.ui.viewmodel.AuthUiState
import com.example.telephases.ui.viewmodel.ExamViewModel
import com.example.telephases.ui.viewmodel.ExamUiState
import com.example.telephases.ui.viewmodel.PatientViewModel
import com.example.telephases.ui.viewmodel.PatientUiState
import com.example.telephases.ui.components.ConnectivityIndicator
import com.example.telephases.ui.components.OfflineBanner
import com.example.telephases.ui.components.SyncProgressIndicator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import com.example.telephases.network.ApiService
import com.example.telephases.network.ExamRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalExamScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    examViewModel: ExamViewModel = hiltViewModel(),
    patientViewModel: PatientViewModel = hiltViewModel(),
    patientId: String? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Observar estados de ViewModels
    val authUiState by authViewModel.uiState.collectAsState(
        initial = AuthUiState(
            authState = AuthState.Idle,
            currentUser = null,
            isOnline = false,
            hasValidSession = false
        )
    )
    val examUiState by examViewModel.uiState.collectAsState(
        initial = ExamUiState()
    )
    val patientUiState by patientViewModel.uiState.collectAsState(
        initial = PatientUiState()
    )
    
    // Colores del sistema Material Design
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    val cardColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    val progressColor = MaterialTheme.colorScheme.secondary
    val accentColor = MaterialTheme.colorScheme.tertiary
    val warningColor = MaterialTheme.colorScheme.error
    val dangerColor = MaterialTheme.colorScheme.error
    val successColor = Color(0xFF4CAF50) // Verde para éxito

    // Estados generales
    var selectedDevice by remember { mutableStateOf(0) } // Índice del dispositivo seleccionado
    var deviceCode by remember { mutableStateOf("EJ:000931") }
    var isConnecting by remember { mutableStateOf(true) }
    var showMenu by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf((selectedDevice + 1) * 0.2f) } // Progreso de la barra según dispositivo

    // Estados específicos para temperatura
    var bluetoothMode by remember { mutableStateOf("BLE") }
    var showDeviceDialog by remember { mutableStateOf(false) }
    var showManualInput by remember { mutableStateOf(false) }
    var manualTemperature by remember { mutableStateOf("") }

    // Estados específicos para presión arterial
    var showBPManualInput by remember { mutableStateOf(false) }
    var manualSystolic by remember { mutableStateOf("") }
    var manualDiastolic by remember { mutableStateOf("") }
    
    // Configurar paciente en el ExamViewModel
    LaunchedEffect(patientId) {
        if (patientId != null) {
            examViewModel.setCurrentPatient(patientId)
        }
    }
    var manualPulse by remember { mutableStateOf("") }
    
    // Estados manuales adicionales
    var showOxiManualInput by remember { mutableStateOf(false) }
    var manualSpO2 by remember { mutableStateOf("") }
    var manualOxiPulse by remember { mutableStateOf("") }

    var showGlucoseManualInput by remember { mutableStateOf(false) }
    var manualGlucose by remember { mutableStateOf("") }

    var showWeightManualInput by remember { mutableStateOf(false) }
    var manualWeight by remember { mutableStateOf("") }
    
    // Estados para mostrar datos manuales como si fueran de dispositivos
    var manualTemperatureReading by remember { mutableStateOf<TemperatureReading?>(null) }
    var manualBloodPressureReading by remember { mutableStateOf<BloodPressureReading?>(null) }
    var manualOximeterReading by remember { mutableStateOf<OximeterReading?>(null) }
    var manualGlucometerReading by remember { mutableStateOf<GlucometerReading?>(null) }
    var manualWeightReading by remember { mutableStateOf<WeightReading?>(null) }

    // Estados para diálogos de información por examen
    var showTempInfo by remember { mutableStateOf(false) }
    var showBPInfo by remember { mutableStateOf(false) }
    var showOxiInfo by remember { mutableStateOf(false) }
    var showGlucoseInfo by remember { mutableStateOf(false) }
    var showWeightInfo by remember { mutableStateOf(false) }

    // Estados específicos para glucómetro
    var glucometerCode by remember { mutableStateOf("00501-9999886") }
    var glucometerPassword by remember { mutableStateOf("") }
    
    // Barra de acciones para habilitar entradas manuales por examen
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(
            onClick = { showManualInput = !showManualInput },
            label = { Text(if (showManualInput) "Temp manual: ON" else "Temp manual") }
        )
        AssistChip(
            onClick = { showBPManualInput = !showBPManualInput },
            label = { Text(if (showBPManualInput) "PA manual: ON" else "PA manual") }
        )
        AssistChip(
            onClick = { showOxiManualInput = !showOxiManualInput },
            label = { Text(if (showOxiManualInput) "Oxi manual: ON" else "Oxi manual") }
        )
        AssistChip(
            onClick = { showGlucoseManualInput = !showGlucoseManualInput },
            label = { Text(if (showGlucoseManualInput) "Glucosa manual: ON" else "Glucosa manual") }
        )
        AssistChip(
            onClick = { showWeightManualInput = !showWeightManualInput },
            label = { Text(if (showWeightManualInput) "Peso manual: ON" else "Peso manual") }
        )
    }
    
    // Función para generar comentarios de presión arterial
    fun getBPComment(systolic: Int?, diastolic: Int?): String {
        if (systolic == null || diastolic == null) {
            return "Esperando lectura del tensiómetro..."
        }
        
        return when {
            systolic < 90 || diastolic < 60 -> "⚠️ Presión arterial baja. Consulte a su médico"
            systolic > 140 || diastolic > 90 -> "⚠️ Presión arterial alta. Consulte a su médico"
            systolic in 120..139 || diastolic in 80..89 -> "⚡ Prehipertensión. Mantenga un estilo de vida saludable"
            else -> "✅ Presión arterial normal. ¡Excelente!"
        }
    }
    
    // Función para generar comentarios de oxímetro
    fun getOximeterComment(spo2: Int?, pulseRate: Int?): String {
        if (spo2 == null || pulseRate == null) {
            return "Esperando lectura del oxímetro..."
        }
        
        val spo2Status = when {
            spo2 < 95 -> "⚠️ SpO₂ bajo. Consulte a su médico"
            spo2 in 95..97 -> "⚡ SpO₂ en límite normal. Monitoree regularmente"
            else -> "✅ SpO₂ excelente"
        }
        
        val pulseStatus = when {
            pulseRate < 60 -> "⚠️ Frecuencia cardíaca baja"
            pulseRate > 100 -> "⚠️ Frecuencia cardíaca alta"
            else -> "✅ Frecuencia cardíaca normal"
        }
        
        return "$spo2Status | $pulseStatus"
    }

    // Managers de Bluetooth para temperatura
    val bleManager = remember { BLETemperatureManager(context) }
    val btClassicManager = remember { BluetoothClassicManager(context) }
    
    // Manager para presión arterial
    val tensiometroManager = remember { BLETensiometroManager(context) }
    
    // Manager para oxímetro
    val oximeterManager = remember { BLEOximeterManager(context) }
    
    // Manager para glucómetro
    val glucometerManager = remember { 
        BLEGlucometerManager(context).apply {
            setDeviceCode(glucometerCode)
        }
    }

    // Estados de temperatura desde los managers
    val temperatureConnected by if (bluetoothMode == "BLE")
        bleManager.isConnected.collectAsState()
    else
        btClassicManager.isConnected.collectAsState()

    val isScanning by bleManager.isScanning.collectAsState()

    val temperature by if (bluetoothMode == "BLE")
        bleManager.temperature.collectAsState()
    else
        btClassicManager.temperature.collectAsState()

    val devicesFound by bleManager.devicesFound.collectAsState()
    val pairedDevices by btClassicManager.pairedDevices.collectAsState()

    // Estados de presión arterial desde el manager
    val bpConnected by tensiometroManager.isConnected.collectAsState()
    val bpConnecting by tensiometroManager.isConnecting.collectAsState()
    val bloodPressureReading by tensiometroManager.bloodPressureReading.collectAsState()
    
    // Estados de oxímetro desde el manager
    val oximeterConnected by oximeterManager.isConnected.collectAsState()
    val oximeterConnecting by oximeterManager.isConnecting.collectAsState()
    val oximeterScanning by oximeterManager.isScanning.collectAsState()
    val oximeterMeasuring by oximeterManager.isMeasuring.collectAsState()
    val oximeterReading by oximeterManager.oximeterReading.collectAsState()
    val oximeterCurrentReading by oximeterManager.currentReading.collectAsState()
    val oximeterStatusMessage by oximeterManager.statusMessage.collectAsState()
    val measurementProgress by oximeterManager.measurementProgress.collectAsState()
    
    // Estados de glucómetro desde el manager
    val glucometerConnected by glucometerManager.isConnected.collectAsState()
    val glucometerConnecting by glucometerManager.isConnecting.collectAsState()
    val glucometerScanning by glucometerManager.isScanning.collectAsState()
    val glucometerReading by glucometerManager.glucometerReading.collectAsState()
    val glucometerStatusMessage by glucometerManager.statusMessage.collectAsState()
    val glucometerDeviceFound by glucometerManager.deviceFound.collectAsState()
    val glucometerPairing by glucometerManager.isPairing.collectAsState()
    val glucometerPairingRequired by glucometerManager.pairingRequired.collectAsState()

    // Lanzador de permisos
    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            bleManager.startScan()
        }
    }

    // Función para solicitar permisos
    fun requestBluetoothPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        bluetoothPermissionLauncher.launch(permissions)
    }

    // API Service para guardar exámenes (mantenemos para compatibilidad)
    val apiService = remember { ApiService.create() }

    // Función para guardar examen usando ExamRepository (offline-first)
    fun saveExamToDatabase(
        tipoExamen: String,
        titulo: String,
        valor: String,
        unidad: String? = null,
        observaciones: String? = null,
        datosAdicionales: Map<String, Any>? = null,
        patientId: String? = null
    ) {
        scope.launch {
            try {
                // Obtener token del AuthViewModel
                val currentAuthState = authViewModel.state.value
                val token = if (currentAuthState is AuthState.Success) {
                    currentAuthState.token
                } else {
                    null
                }
                
                if (patientId != null) {
                    // Usar ExamRepository para guardado offline-first
                    examViewModel.createExam(
                        patientId = patientId,
                        tipoExamenNombre = tipoExamen,
                        titulo = titulo,
                        valor = valor,
                        unidad = unidad,
                        observaciones = observaciones,
                        datosAdicionales = datosAdicionales,
                        onSuccess = { exam ->
                            Log.d("ExamPages", "✅ Examen guardado (offline-first): $titulo = $valor ${unidad ?: ""}")
                            Log.d("ExamPages", "👤 Paciente ID: $patientId")
                            Log.d("ExamPages", "🆔 Examen ID: ${exam.id}")
                        }
                    )
                } else {
                    Log.e("ExamPages", "❌ PatientId requerido para guardar examen")
                }
            } catch (e: Exception) {
                Log.e("ExamPages", "❌ Error guardando examen: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Cleanup al salir
    DisposableEffect(Unit) {
        onDispose {
            bleManager.cleanup()
            btClassicManager.cleanup()
            tensiometroManager.disconnect()
            oximeterManager.disconnect()
            glucometerManager.disconnect()
        }
    }

    // Auto-guardado cuando se obtienen lecturas automáticas de dispositivos BLE
    LaunchedEffect(temperature) {
        temperature?.let { tempReading ->
            saveExamToDatabase(
                tipoExamen = "TEMPERATURE",
                titulo = "Temperatura Corporal",
                valor = String.format("%.1f", tempReading.temperature),
                unidad = "°C",
                observaciones = "Medición automática - Modo: ${tempReading.mode}",
                datosAdicionales = mapOf(
                    "modo" to tempReading.mode,
                    "metodo" to if (bluetoothMode == "BLE") "BLE" else "Bluetooth Classic",
                    "timestamp" to System.currentTimeMillis()
                ),
                patientId = patientId
            )
        }
    }

    LaunchedEffect(bloodPressureReading) {
        bloodPressureReading?.let { bpReading ->
            saveExamToDatabase(
                tipoExamen = "BLOOD_PRESSURE",
                titulo = "Presión Arterial",
                valor = "${bpReading.systolic}/${bpReading.diastolic}",
                unidad = "mmHg",
                observaciones = "Medición automática - Pulso: ${bpReading.pulse} bpm${if (bpReading.hasArrhythmia) " (Arritmia detectada)" else ""}",
                datosAdicionales = mapOf(
                    "sistolica" to bpReading.systolic,
                    "diastolica" to bpReading.diastolic,
                    "pulso" to bpReading.pulse,
                    "arritmia" to bpReading.hasArrhythmia,
                    "metodo" to "BLE_automatico",
                    "timestamp" to System.currentTimeMillis()
                ),
                patientId = patientId
            )
        }
    }

    LaunchedEffect(oximeterReading) {
        oximeterReading?.let { oxReading ->
            saveExamToDatabase(
                tipoExamen = "OXYGEN_SATURATION",
                titulo = "Saturación de Oxígeno",
                valor = "${oxReading.spo2}",
                unidad = "%",
                observaciones = "Medición automática - Frecuencia cardíaca: ${oxReading.pulseRate} bpm, PI: ${String.format("%.1f", oxReading.pi)}%",
                datosAdicionales = mapOf(
                    "spo2" to oxReading.spo2,
                    "frecuencia_cardiaca" to oxReading.pulseRate,
                    "pi" to oxReading.pi,
                    "metodo" to "BLE_automatico",
                    "timestamp" to System.currentTimeMillis()
                ),
                patientId = patientId
            )
        }
    }

    LaunchedEffect(glucometerReading) {
        glucometerReading?.let { glucReading ->
            val dateTime = String.format(
                "%02d/%02d/%d %02d:%02d",
                glucReading.day, glucReading.month, glucReading.year,
                glucReading.hour, glucReading.minute
            )
            
            saveExamToDatabase(
                tipoExamen = "GLUCOSE",
                titulo = "Glucosa en Sangre",
                valor = "${glucReading.glucose}",
                unidad = "mg/dL",
                observaciones = "Medición automática - Fecha: $dateTime, Registro #${glucReading.sequenceNumber}",
                datosAdicionales = mapOf(
                    "glucosa" to glucReading.glucose,
                    "fecha_medicion" to dateTime,
                    "numero_registro" to glucReading.sequenceNumber,
                    "dia" to glucReading.day,
                    "mes" to glucReading.month,
                    "año" to glucReading.year,
                    "hora" to glucReading.hour,
                    "minuto" to glucReading.minute,
                    "metodo" to "BLE_automatico",
                    "timestamp" to System.currentTimeMillis()
                ),
                patientId = patientId
            )
        }
    }

    // Lista de dispositivos en el orden del flujo: Temperatura -> Presión -> Oxímetro -> Glucómetro -> Peso
    val devices = listOf(
        DeviceData("Termómetro", R.drawable.temperatura, Color.Black),
        DeviceData("Presión Arterial", R.drawable.presion_arterial, Color.Gray),
        DeviceData("Oxímetro", R.drawable.oxigeno, Color.Gray),
        DeviceData("Glucómetro", R.drawable.glucosa, Color.Gray),
        DeviceData("Peso", R.drawable.ecg, Color.Gray)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when(selectedDevice) {
                            0 -> "Termómetro"
                            1 -> "Presión Arterial"
                            2 -> "Oxímetro"
                            3 -> "Glucómetro"
                            4 -> "Peso"
                            else -> "Examen Médico"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        (context as? Activity)?.finish()
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                ),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
        ) {
            val pageScroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(pageScroll)
            ) {
                // Barra de iconos superior moderna
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        devices.forEachIndexed { index, device ->
                            ModernDeviceIcon(
                                icon = device.icon,
                                name = device.name,
                                isSelected = selectedDevice == index,
                                onClick = {
                                    selectedDevice = index
                                    // Actualizar progreso según el dispositivo seleccionado
                                    progress = (index + 1) * 0.2f
                                    // Reiniciar estados cuando se selecciona un dispositivo
                                    isConnecting = false
                                    deviceCode = "EJ:000931"
                                }
                            )
                        }
                    }
                }

                // Barra de progreso moderna
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Progreso del Examen",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = textColor
                            )
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = progressColor
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = progressColor,
                            trackColor = Color(0xFFE0E0E0)
                        )
                    }
                }

                                // Contenido principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedDevice == 0) { // Termómetro
                        // Estado de conexión moderno para temperatura
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    temperatureConnected -> Color(0xFFE8F5E8)
                                    else -> Color(0xFFFFEBEE)
                                }
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(
                                                if (temperatureConnected) progressColor else dangerColor,
                                                RoundedCornerShape(6.dp)
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = if (temperatureConnected) "Conectado" else "Desconectado",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (temperatureConnected) Color(0xFF2E7D32) else Color(0xFFC62828)
                                    )
                                }
                                Text(
                                    text = "Termómetro",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }



                        // Lectura de temperatura moderna
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.temperatura),
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp),
                                        tint = primaryColor
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Temperatura Corporal",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    val currentReading = manualTemperatureReading ?: temperature
                                    Text(
                                        text = currentReading?.let { "%.1f".format(it.temperature) } ?: "--",
                                        fontSize = 56.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            currentReading?.temperature != null -> {
                                                val temp = currentReading.temperature
                                                when {
                                                    temp < 36.0 || temp > 37.2 -> dangerColor
                                                    temp in 36.0..37.2 -> progressColor
                                                    else -> warningColor
                                                }
                                            }
                                            else -> textColor
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "°C",
                                        fontSize = 28.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }

                                val currentReading = manualTemperatureReading ?: temperature
                                currentReading?.let {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = "Modo: ${it.mode}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = primaryColor,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Rango normal: 36.0 - 37.2°C",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                // Controles modernos: Entrada manual e información (Temperatura)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { showManualInput = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Entrada Manual",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = { showTempInfo = true },
                        modifier = Modifier
                            .background(
                                Color(0xFFF5F5F5),
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Información de temperatura",
                            tint = primaryColor
                        )
                    }
                }
                    } else if (selectedDevice == 1) { // Presión Arterial
                        // Estado de conexión para presión arterial
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Connection status:", color = textColor)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(
                                                when {
                                                    bpConnected -> progressColor
                                                    bpConnecting -> MaterialTheme.colorScheme.tertiary
                                                    else -> Color.Red
                                                },
                                            CircleShape
                                        )
                                )
                                    Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                        text = when {
                                            bpConnected -> "Connected"
                                            bpConnecting -> "Connecting..."
                                            else -> "No connection"
                                        },
                                        color = when {
                                            bpConnected -> progressColor
                                            bpConnecting -> MaterialTheme.colorScheme.tertiary
                                            else -> Color.Red
                                        },
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        }

                        // Estado del tensiómetro
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                    text = "Estado del Tensiómetro",
                                    style = MaterialTheme.typography.titleMedium,
                                        color = primaryColor,
                                    fontWeight = FontWeight.Bold
                                    )

                                Spacer(modifier = Modifier.height(8.dp))
                                
                                            Text(
                                    text = getBPComment(bloodPressureReading?.systolic, bloodPressureReading?.diastolic),
                                    fontSize = 14.sp,
                                    color = when {
                                        bloodPressureReading?.let { it.systolic < 90 || it.diastolic < 60 || it.systolic > 140 || it.diastolic > 90 } == true -> Color.Red
                                        bloodPressureReading?.let { it.systolic in 120..139 || it.diastolic in 80..89 } == true -> MaterialTheme.colorScheme.tertiary
                                        bloodPressureReading != null -> MaterialTheme.colorScheme.secondary
                                        else -> Color.Gray
                                    },
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Lectura de presión arterial
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Blood Pressure",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = textColor,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Systolic / Diastolic
                                val currentBPReading = manualBloodPressureReading ?: bloodPressureReading
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = currentBPReading?.let { "${it.systolic}" } ?: "---",
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                    Text(
                                        "/",
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    Text(
                                        text = currentBPReading?.let { "${it.diastolic}" } ?: "---",
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                    Text(
                                        "mmHg",
                                        fontSize = 20.sp,
                                        color = textColor,
                                        modifier = Modifier.padding(start = 8.dp, top = 16.dp)
                                    )
                                }

                                // Pulse
                                currentBPReading?.let { reading ->
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.pulso),
                                        contentDescription = "Pulso",
                                        tint = primaryColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                            text = "${reading.pulse} bpm",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = primaryColor,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    if (reading.hasArrhythmia) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "⚠️ Arritmia detectada",
                                            color = MaterialTheme.colorScheme.tertiary,
                                            fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    "Sistólica: 90-140 mmHg | Diastólica: 60-90 mmHg",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Controles locales: Entrada manual e información (Presión)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showBPManualInput = true },
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text("Entrada manual")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { showBPInfo = true }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Información de presión arterial",
                                    tint = Color.Gray
                                )
                            }
                        }

                    } else if (selectedDevice == 2) { // Oxímetro
                        // Estado de conexión para oxímetro
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Connection status:", color = textColor)
                                // Indicador de estado
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            color = when {
                                                oximeterConnected -> MaterialTheme.colorScheme.secondary
                                                oximeterConnecting || oximeterScanning -> MaterialTheme.colorScheme.tertiary
                                                else -> Color.Red
                                            },
                                            shape = CircleShape
                                        )
                                )
                            }
                        }

                        // Status del oxímetro
                        if (oximeterMeasuring) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = cardColor),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Midiendo oxígeno en sangre",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = primaryColor,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    // Progreso de medición
                                    Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        LinearProgressIndicator(
                                            progress = measurementProgress / 20f,
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(8.dp),
                                        color = primaryColor,
                                            trackColor = Color(0xFFE0E0E0)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "${measurementProgress}/20s",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = textColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                            Text(
                                        text = oximeterStatusMessage,
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = cardColor),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Oxímetro",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = primaryColor,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = getOximeterComment(oximeterCurrentReading?.spo2, oximeterCurrentReading?.pulseRate),
                                        fontSize = 14.sp,
                                        color = when {
                                                                                oximeterCurrentReading?.spo2 != null && oximeterCurrentReading!!.spo2 < 95 -> Color.Red
                                    oximeterCurrentReading?.spo2 != null && oximeterCurrentReading!!.spo2 in 95..97 -> MaterialTheme.colorScheme.tertiary
                                    oximeterCurrentReading?.spo2 != null -> MaterialTheme.colorScheme.secondary
                                            else -> Color.Gray
                                        },
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Lectura de oxímetro
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Saturación de Oxígeno",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = textColor,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )

                                // SpO₂
                                val currentOximeterReading = manualOximeterReading ?: oximeterCurrentReading
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Text(
                                        text = currentOximeterReading?.let { "${it.spo2}" } ?: "--",
                                        fontSize = 72.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                    Text(
                                        "%",
                                        fontSize = 24.sp,
                                        color = textColor,
                                        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                                    )
                                }

                                // Frecuencia cardíaca
                                currentOximeterReading?.let { reading ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.pulso),
                                        contentDescription = "Pulso",
                                        tint = primaryColor,
                                            modifier = Modifier.size(24.dp)
                                    )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${reading.pulseRate} bpm",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = primaryColor,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    
                                    // Índice de perfusión
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "PI: ${String.format("%.1f", reading.pi)}%",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                        Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    "SpO₂ normal: 95-100% | Frecuencia cardíaca: 60-100 bpm",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Controles locales: Entrada manual e información (Oxímetro)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showOxiManualInput = true },
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text("Entrada manual")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { showOxiInfo = true }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Información de oxímetro",
                                    tint = Color.Gray
                                )
                            }
                        }

                    } else if (selectedDevice == 3) { // Glucómetro
                        // Estado de conexión para glucómetro
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Connection status:", color = textColor)
                                // Indicador de estado
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            color = when {
                                                glucometerConnected -> MaterialTheme.colorScheme.secondary
                                                glucometerConnecting || glucometerScanning || glucometerPairing -> MaterialTheme.colorScheme.tertiary
                                                glucometerPairingRequired -> Color(0xFF2196F3) // Azul para emparejamiento
                                                else -> Color.Red
                                            },
                                            shape = CircleShape
                                        )
                                )
                            }
                        }

                        // Estado del glucómetro
                        Card(
                                modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Estado del Glucómetro",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = primaryColor,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = glucometerManager.getGlucoseComment(glucometerReading?.glucose),
                                    fontSize = 14.sp,
                                    color = when {
                                        glucometerReading?.glucose != null && glucometerReading!!.glucose < 70 -> Color.Red
                                        glucometerReading?.glucose != null && glucometerReading!!.glucose > 125 -> Color.Red
                                        glucometerReading?.glucose != null && glucometerReading!!.glucose in 100..125 -> MaterialTheme.colorScheme.tertiary
                                        glucometerReading?.glucose != null -> MaterialTheme.colorScheme.secondary
                                        else -> Color.Gray
                                    },
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                        )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Mensaje de estado del dispositivo
                                Text(
                                    text = glucometerStatusMessage,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                                                // Campo de código del glucómetro (número de serie)
                        Text(
                            text = "Número de serie GlucoLeader",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = glucometerCode,
                            onValueChange = { 
                                glucometerCode = it
                                glucometerManager.setDeviceCode(it)
                            },
                                modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            placeholder = { Text("00501-9999886 (del dorso)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor
                            ),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                // Ícono de verificación cuando el código está conectado
                                if (glucometerConnected) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Conectado",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        )
                        
                        // Campo de contraseña (solo visible cuando se requiere emparejamiento)
                        if (glucometerPairingRequired || glucometerPassword.isNotEmpty()) {
                            Text(
                                text = "Contraseña del glucómetro",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = glucometerPassword,
                                onValueChange = { 
                                    glucometerPassword = it
                                    glucometerManager.setPairingPassword(it)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                placeholder = { Text("665 (3 dígitos en pantalla)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryColor,
                                    unfocusedBorderColor = Color.Gray,
                                    focusedTextColor = textColor,
                                    unfocusedTextColor = textColor
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            
                            // Instrucciones para emparejamiento
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                        text = "📋 Instrucciones de emparejamiento:",
                                        fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                        color = Color(0xFF795548)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "1. Apague el glucómetro\n2. Presione 'Set' + 'Mem' hasta ver 'bLE'\n3. Ingrese la contraseña de 3 dígitos que aparece",
                                        fontSize = 12.sp,
                                        color = Color(0xFF795548)
                                )
                            }
                        }
                    }

                        // Lectura de glucosa
                    Card(
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                    "Glucosa en Sangre",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = textColor,
                                fontWeight = FontWeight.Bold
                            )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Valor de glucosa
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = glucometerReading?.let { "${it.glucose}" } ?: "---",
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "mg/dL",
                                        fontSize = 20.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }

                                glucometerReading?.let { reading ->
                            Spacer(modifier = Modifier.height(16.dp))

                                    // Fecha y hora de la medición
                                    Text(
                                        text = String.format(
                                            "%02d/%02d/%d %02d:%02d",
                                            reading.day, reading.month, reading.year,
                                            reading.hour, reading.minute
                                        ),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    
                                    // Información adicional
                                    Text(
                                        text = "Registro #${reading.sequenceNumber}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }

                            Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    "Glucosa normal: 70-99 mg/dL (en ayunas)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Controles locales: Entrada manual e información (Glucosa)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showGlucoseManualInput = true },
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text("Entrada manual")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { showGlucoseInfo = true }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Información de glucosa",
                                    tint = Color.Gray
                                )
                            }
                        }
                    } else if (selectedDevice == 4) { // Peso - Interfaz específica
                        // Estado de conexión para peso
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isConnecting -> Color(0xFFFFF3E0)
                                    else -> Color(0xFFE8F5E8)
                                }
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(
                                                if (isConnecting) warningColor else successColor,
                                                RoundedCornerShape(6.dp)
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = if (isConnecting) "Conectando..." else "Listo para pesar",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isConnecting) Color(0xFFE65100) else Color(0xFF2E7D32)
                                    )
                                }
                                Text(
                                    text = "Báscula",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Lectura de peso moderna
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ecg),
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp),
                                        tint = primaryColor
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Peso Corporal",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    val currentWeightReading = manualWeightReading
                                    Text(
                                        text = currentWeightReading?.let { "%.1f".format(it.weight) } ?: "---",
                                        fontSize = 56.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "kg",
                                        fontSize = 28.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Coloque la báscula en superficie plana y firme",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        // Controles locales: Entrada manual e información (Peso)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showWeightManualInput = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = accentColor
                                ),
                                shape = RoundedCornerShape(16.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Entrada Manual",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            IconButton(
                                onClick = { showWeightInfo = true },
                                modifier = Modifier
                                    .background(
                                        Color(0xFFF5F5F5),
                                        RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Información de peso",
                                    tint = primaryColor
                                )
                            }
                        }
                    } else {
                        // Interfaz genérica para otros dispositivos
                        // Estado del dispositivo
                        Text(
                            text = "Estado del Dispositivo",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 8.dp)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Status
                                Text(
                                    text = if (isConnecting) "Conectando..." else "Conectado",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isConnecting) accentColor else progressColor,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                // Descripción
                                Text(
                                    text = "Por favor asegúrese de que su dispositivo esté encendido y dentro del rango.",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 20.dp)
                                )

                                // Imagen del dispositivo
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .background(
                                            primaryColor.copy(alpha = 0.1f),
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ecg),
                                        contentDescription = "Dispositivo",
                                        modifier = Modifier.size(80.dp),
                                        tint = primaryColor
                                    )
                                }
                            }
                        }

                        // Campo de código
                        Text(
                            text = "Ingrese código",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = deviceCode,
                            onValueChange = { deviceCode = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            trailingIcon = {
                            Box(
                                modifier = Modifier
                                        .size(24.dp)
                                        .background(Color(0xFFE8F5E8), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(progressColor, CircleShape)
                                    )
                                }
                            }
                        )
                        
                    }

                        Spacer(modifier = Modifier.weight(1f))

                    // Botones inferiores
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (selectedDevice == 0) { // Termómetro - Botones específicos de temperatura
                            // Botón Start/Disconnect
                            Button(
                                onClick = {
                                    when {
                                        !bleManager.hasBluetoothPermissions() -> requestBluetoothPermissions()
                                        temperatureConnected -> {
                                            // Forzar desconexión
                                            bleManager.disconnect()
                                        }
                                        else -> {
                                            showDeviceDialog = true
                                            bleManager.startScan()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (temperatureConnected) Color.Red else progressColor
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                enabled = !isScanning
                            ) {
                                if (isScanning) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                Text(
                                        if (temperatureConnected) "Disconnect" else "Start",
                                    fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                )
                                }
                            }

                            
                            // Botón Siguiente
                            Button(
                                onClick = { 
                                    // Avanzar al siguiente examen en la secuencia
                                    if (selectedDevice < devices.size - 1) {
                                        selectedDevice += 1
                                        // Actualizar progreso según el dispositivo
                                        progress = (selectedDevice + 1) * 0.2f
                                        // Reiniciar estados para el nuevo dispositivo
                                        isConnecting = false
                                        deviceCode = "EJ:000931"
                                        // Reiniciar estados específicos de temperatura
                                        if (bluetoothMode == "BLE") {
                                            bleManager.disconnect()
                                        } else {
                                            btClassicManager.disconnect()
                                        }
                                    } else {
                                        // Finalizar exámenes y regresar a la información del paciente
                                        navController.popBackStack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
        modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                                Text(
                                    text = if (selectedDevice >= devices.size - 1) "Finalizar" else "Siguiente",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        } else if (selectedDevice == 1) { // Presión Arterial - Botones específicos
                            // Botón Start/Disconnect para presión arterial
                            Button(
                                onClick = {
                                    when {
                                        !tensiometroManager.hasBluetoothPermissions() -> requestBluetoothPermissions()
                                        bpConnected || bpConnecting -> {
                                            // Forzar desconexión
                                            tensiometroManager.disconnect()
                                        }
                                        else -> tensiometroManager.findAndConnect()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        bpConnected -> Color.Red
                                        bpConnecting -> MaterialTheme.colorScheme.tertiary
                                        else -> progressColor
                                    }
                                ),
            modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp)
                        ) {
                            Text(
                                    text = when {
                                        bpConnected -> "Disconnect"
                                        bpConnecting -> "Buscando..."
                                        else -> "Start"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }

                            // Botón Siguiente para presión arterial
                            Button(
                                onClick = { 
                                    // Avanzar al siguiente examen en la secuencia
                                    if (selectedDevice < devices.size - 1) {
                                        selectedDevice += 1
                                        // Actualizar progreso según el dispositivo
                                        progress = (selectedDevice + 1) * 0.2f
                                        // Reiniciar estados para el nuevo dispositivo
                                        isConnecting = false
                                        deviceCode = "EJ:000931"
                                        // Reiniciar estados específicos de presión arterial
                                        tensiometroManager.disconnect()
                                    } else {
                                        // Finalizar exámenes y regresar a la información del paciente
                                        navController.popBackStack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp)
                            ) {
        Text(
                                    text = if (selectedDevice >= devices.size - 1) "Finalizar" else "Siguiente",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
        )
    }
                        } else if (selectedDevice == 2) { // Oxímetro - Botones específicos
                            // Botón Start/Disconnect para oxímetro
                            Button(
                                onClick = {
                                    when {
                                        !oximeterManager.hasBluetoothPermissions() -> requestBluetoothPermissions()
                                        oximeterConnected || oximeterMeasuring -> {
                                            // Forzar desconexión
                                            oximeterManager.disconnect()
                                            oximeterManager.resetReading()
                                        }
                                        else -> oximeterManager.findAndConnect()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        oximeterConnected -> Color.Red
                                        oximeterConnecting || oximeterScanning -> MaterialTheme.colorScheme.tertiary
                                        oximeterMeasuring -> Color(0xFF9C27B0)
                                        else -> progressColor
                                    }
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                                    text = when {
                                        oximeterMeasuring -> "Midiendo..."
                                        oximeterConnected -> "Disconnect"
                                        oximeterConnecting || oximeterScanning -> "Buscando..."
                                        else -> "Start"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
        )
    }

                            // Botón Siguiente para oxímetro
                            Button(
                                onClick = { 
                                    // Avanzar al siguiente examen en la secuencia
                                    if (selectedDevice < devices.size - 1) {
                                        selectedDevice += 1
                                        // Actualizar progreso según el dispositivo
                                        progress = (selectedDevice + 1) * 0.2f
                                        // Reiniciar estados para el nuevo dispositivo
                                        isConnecting = false
                                        deviceCode = "EJ:000931"
                                        // Reiniciar estados específicos de oxímetro
                                        oximeterManager.disconnect()
                                        oximeterManager.resetReading()
                                    } else {
                                        // Finalizar exámenes y regresar a la información del paciente
                                        navController.popBackStack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
        modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                Text(
                                    text = if (selectedDevice >= devices.size - 1) "Finalizar" else "Siguiente",
                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        } else if (selectedDevice == 3) { // Glucómetro - Botones específicos
                            // Botón Start/Disconnect para glucómetro
                            Button(
                                onClick = {
                                    when {
                                        glucometerConnected -> {
                                            // Forzar desconexión
                                            glucometerManager.disconnect()
                                        }
                                        glucometerConnecting || glucometerScanning -> {
                                            // Cancelar operación en curso
                                            glucometerManager.disconnect()
                                        }
                                        else -> {
                                            if (glucometerPassword.isNotEmpty()) {
                                                glucometerManager.setPairingPassword(glucometerPassword)
                                            }
                                            glucometerManager.startScan()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        glucometerConnected -> Color.Red
                                        glucometerConnecting || glucometerScanning -> MaterialTheme.colorScheme.tertiary
                                        else -> progressColor
                                    }
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                                    text = when {
                                        glucometerConnected -> "Desconectar"
                                        glucometerConnecting || glucometerScanning -> "Buscando..."
                                        else -> "Conectar"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                )
                            }

                Spacer(modifier = Modifier.width(8.dp))

                            // Botón de ayuda para problemas E62
                            if (glucometerPairingRequired && !glucometerConnected) {
                                Button(
                                    onClick = {
                                        glucometerManager.retryPairing()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2196F3)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                Text(
                                        text = "Reintentar",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                )
            }
                            }
                            
                            // Botón Solicitar Medición (solo si está conectado)
                            if (glucometerConnected) {
                                Button(
                                    onClick = {
                                        glucometerManager.requestGlucoseRecords()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF009688)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
            Text(
                                        text = "Medir",
                    fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            } else {
                                // Botón Siguiente para glucómetro
                                Button(
                                    onClick = { 
                                        // Avanzar al siguiente examen en la secuencia
                                        if (selectedDevice < devices.size - 1) {
                                            selectedDevice += 1
                                            // Actualizar progreso según el dispositivo
                                            progress = (selectedDevice + 1) * 0.2f
                                            // Reiniciar estados para el nuevo dispositivo
                                            isConnecting = false
                                            deviceCode = "EJ:000931"
                                            // Reiniciar estados específicos de glucómetro
                                            glucometerManager.disconnect()
                                        } else {
                                            // Finalizar exámenes y regresar a la información del paciente
                                            navController.popBackStack()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = primaryColor
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text(
                                        text = if (selectedDevice >= devices.size - 1) "Finalizar" else "Siguiente",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        } else { // Otros dispositivos - Botones genéricos
                            // Botón Conectar dispositivos
                            Button(
                                onClick = { 
                                    if (!isConnecting) {
                                        isConnecting = true
                                        progress = 0.1f
                                        
                                        GlobalScope.launch {
                                            for (i in 1..10) {
                                                delay(300)
                                                progress = i * 0.1f
                                            }
                                            isConnecting = false
                                        }
                                    }
                                    // TODO: Agregar lógica de conexión más tarde
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = progressColor,
                                    disabledContainerColor = Color.Gray
                                ),
        modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                enabled = !isConnecting
                            ) {
                Text(
                                    text = if (isConnecting) "Conectando..." else "Conectar dispositivos",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Botón Siguiente
                            Button(
                                onClick = { 
                                    // Avanzar al siguiente examen en la secuencia
                                    if (selectedDevice < devices.size - 1) {
                                        selectedDevice += 1
                                        // Actualizar progreso según el dispositivo
                                        progress = (selectedDevice + 1) * 0.2f
                                        // Reiniciar estados para el nuevo dispositivo
                                        isConnecting = false
                                        deviceCode = "EJ:000931"
                                    } else {
                                        // Finalizar exámenes y regresar a la información del paciente
                                        navController.popBackStack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor,
                                    disabledContainerColor = Color.Gray
                                ),
            modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp)
        ) {
            Text(
                                    text = if (selectedDevice >= devices.size - 1) "Finalizar" else "Siguiente",
                color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Menú desplegable
            if (showMenu) {
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                                modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 56.dp, end = 16.dp)
                        .width(200.dp)
                ) {
                    devices.forEachIndexed { index, device ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = device.icon),
                                        contentDescription = device.name,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = device.name)
        }
                            },
                                                         onClick = {
                                 selectedDevice = index
                                 showMenu = false
                                 // Actualizar progreso según el dispositivo seleccionado
                                 progress = (index + 1) * 0.2f
                                 // Reiniciar estados para el nuevo dispositivo
                                 isConnecting = false
                                 deviceCode = "EJ:000931"
                             }
                        )
                        if (index < devices.size - 1) {
                            HorizontalDivider()
                }
            }
        }
    }
}

        // Diálogo de dispositivos para temperatura
        if (showDeviceDialog) {
            Dialog(onDismissRequest = {
                showDeviceDialog = false
                bleManager.stopScan()
            }) {
                Card(
                    modifier = Modifier.fillMaxWidth().height(400.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
        Text(
                            "Dispositivos encontrados",
                            style = MaterialTheme.typography.titleLarge,
                            color = textColor
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (devicesFound.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    "Buscando dispositivos...",
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn {
                                items(devicesFound) { device ->
                                    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
                                            .clickable {
                                                scope.launch {
                                                    bleManager.connectToDevice(device.address)
                                                    showDeviceDialog = false
                                                }
                                            },
                                        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                                                Icons.Default.Bluetooth,
                                                "Bluetooth",
                                                tint = primaryColor,
                modifier = Modifier.size(24.dp)
            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(device.name, color = textColor, fontWeight = FontWeight.Bold)
                                                Text(device.address, color = Color.Gray, fontSize = 12.sp)
    }
}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de entrada manual para temperatura
        if (showManualInput) {
            AlertDialog(
                onDismissRequest = { showManualInput = false; manualTemperature = "" },
                title = { Text("Ingrese la temperatura", color = textColor) },
                text = {
                    OutlinedTextField(
                        value = manualTemperature,
                        onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) manualTemperature = it },
                        label = { Text("Temperatura (°C)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val temp = manualTemperature.toDoubleOrNull()
                            if (temp != null) {
                                // Crear lectura manual para mostrar en pantalla
                                manualTemperatureReading = TemperatureReading(
                                    temperature = temp,
                                    mode = "Manual"
                                )
                                
                                // Guardar temperatura manual en la base de datos
                                saveExamToDatabase(
                                    tipoExamen = "TEMPERATURE",
                                    titulo = "Temperatura Corporal",
                                    valor = String.format("%.1f", temp),
                                    unidad = "°C",
                                    observaciones = "Medición manual",
                                    datosAdicionales = mapOf(
                                        "metodo" to "manual",
                                        "timestamp" to System.currentTimeMillis()
                                    ),
                                    patientId = patientId
                                )
                                showManualInput = false
                                manualTemperature = ""
                            }
                        },
                        enabled = manualTemperature.isNotEmpty()
                    ) {
                        Text("Confirmar", color = primaryColor)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showManualInput = false; manualTemperature = "" }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                containerColor = cardColor
            )
        }

        // Info Temperatura
        if (showTempInfo) {
            AlertDialog(
                onDismissRequest = { showTempInfo = false },
                title = { Text("Cómo medir Temperatura", color = textColor) },
                text = {
                    Text(
                        "1) Enciende el termómetro y acércalo al paciente.\n" +
                        "2) Coloca el sensor en la frente o bajo la axila (según modelo).\n" +
                        "3) Mantén inmóvil durante la medición (2–5 s).\n" +
                        "4) La lectura aparecerá automáticamente si el dispositivo está conectado (o usa Entrada manual).\n" +
                        "Rango normal: 36.0–37.2 °C",
                        color = Color.DarkGray
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showTempInfo = false }) { Text("Entendido") }
                },
                containerColor = cardColor
            )
        }

        // Diálogo de entrada manual para presión arterial
        if (showBPManualInput) {
            AlertDialog(
                onDismissRequest = { 
                    showBPManualInput = false
                    manualSystolic = ""
                    manualDiastolic = ""
                    manualPulse = ""
                },
                title = { Text("Ingrese presión arterial", color = textColor) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = manualSystolic,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) manualSystolic = it },
                            label = { Text("Sistólica (mmHg)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Color.Gray
                            ),
                            singleLine = true,
                modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = manualDiastolic,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) manualDiastolic = it },
                            label = { Text("Diastólica (mmHg)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Color.Gray
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = manualPulse,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) manualPulse = it },
                            label = { Text("Pulso (bpm)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Color.Gray
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
        )
    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val systolic = manualSystolic.toIntOrNull()
                            val diastolic = manualDiastolic.toIntOrNull()
                            val pulse = manualPulse.toIntOrNull()
                            if (systolic != null && diastolic != null && pulse != null) {
                                // Crear lectura manual para mostrar en pantalla
                                manualBloodPressureReading = BloodPressureReading(
                                    systolic = systolic,
                                    diastolic = diastolic,
                                    pulse = pulse,
                                    hasArrhythmia = false
                                )
                                
                                // Guardar presión arterial manual en la base de datos
                                saveExamToDatabase(
                                    tipoExamen = "BLOOD_PRESSURE",
                                    titulo = "Presión Arterial",
                                    valor = "$systolic/$diastolic",
                                    unidad = "mmHg",
                                    observaciones = "Medición manual - Pulso: $pulse bpm",
                                    datosAdicionales = mapOf(
                                        "sistolica" to systolic,
                                        "diastolica" to diastolic,
                                        "pulso" to pulse,
                                        "metodo" to "manual",
                                        "timestamp" to System.currentTimeMillis()
                                    ),
                                    patientId = patientId
                                )
                                showBPManualInput = false
                                manualSystolic = ""
                                manualDiastolic = ""
                                manualPulse = ""
            }
        },
                        enabled = manualSystolic.isNotEmpty() && manualDiastolic.isNotEmpty() && manualPulse.isNotEmpty()
                    ) {
                        Text("Confirmar", color = primaryColor)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { 
                        showBPManualInput = false
                        manualSystolic = ""
                        manualDiastolic = ""
                        manualPulse = ""
                    }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                containerColor = cardColor
            )
        }

        // Info Presión Arterial
        if (showBPInfo) {
            AlertDialog(
                onDismissRequest = { showBPInfo = false },
                title = { Text("Cómo medir Presión Arterial", color = textColor) },
                text = {
                    Text(
                        "1) Coloca el brazalete 2–3 cm por encima del codo (tubo hacia abajo).\n" +
                        "2) El paciente debe estar sentado, espalda apoyada, pies en el piso, 5 min de reposo.\n" +
                        "3) Mantén el brazo a la altura del corazón y sin hablar.\n" +
                        "4) Inicia la medición desde el tensiómetro; la app leerá automáticamente.\n" +
                        "Valores de referencia: 120/80 mmHg aprox.",
                        color = Color.DarkGray
                    )
                },
                confirmButton = { TextButton(onClick = { showBPInfo = false }) { Text("Entendido") } },
                containerColor = cardColor
            )
        }

        // Diálogo de entrada manual para Oxímetro
        if (showOxiManualInput) {
            AlertDialog(
                onDismissRequest = {
                    showOxiManualInput = false
                    manualSpO2 = ""
                    manualOxiPulse = ""
                },
                title = { Text("Ingrese lectura del oxímetro", color = textColor) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = manualSpO2,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) manualSpO2 = it },
                            label = { Text("SpO₂ (%)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Color.Gray
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = manualOxiPulse,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) manualOxiPulse = it },
                            label = { Text("Pulso (bpm)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Color.Gray
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val spo2 = manualSpO2.toIntOrNull()
                            val pulse = manualOxiPulse.toIntOrNull()
                            if (spo2 != null && pulse != null) {
                                // Crear lectura manual para mostrar en pantalla
                                manualOximeterReading = OximeterReading(
                                    spo2 = spo2,
                                    pulseRate = pulse,
                                    pi = 0.0
                                )
                                
                                saveExamToDatabase(
                                    tipoExamen = "OXYGEN_SATURATION",
                                    titulo = "Oxímetro",
                                    valor = "$spo2",
                                    unidad = "%",
                                    observaciones = "Medición manual - Pulso: $pulse bpm",
                                    datosAdicionales = mapOf(
                                        "spo2" to spo2,
                                        "pulso" to pulse,
                                        "metodo" to "manual",
                                        "timestamp" to System.currentTimeMillis()
                                    ),
                                    patientId = patientId
                                )
                                showOxiManualInput = false
                                manualSpO2 = ""
                                manualOxiPulse = ""
                            }
                        },
                        enabled = manualSpO2.isNotEmpty() && manualOxiPulse.isNotEmpty()
                    ) { Text("Confirmar", color = primaryColor) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showOxiManualInput = false
                        manualSpO2 = ""
                        manualOxiPulse = ""
                    }) { Text("Cancelar", color = Color.Gray) }
                },
                containerColor = cardColor
            )
        }

        // Info Oxímetro
        if (showOxiInfo) {
            AlertDialog(
                onDismissRequest = { showOxiInfo = false },
                title = { Text("Cómo usar el Oxímetro", color = textColor) },
                text = {
                    Text(
                        "1) Limpia el dedo (preferir índice) y retira esmalte.\n" +
                        "2) Inserta el dedo hasta el fondo del clip.\n" +
                        "3) Mantén la mano quieta y por debajo del nivel del corazón.\n" +
                        "4) Espera 10–20 s hasta estabilizarse.\n" +
                        "Interpretación: SpO₂ 95–100% normal; FC 60–100 bpm.",
                        color = Color.DarkGray
                    )
                },
                confirmButton = { TextButton(onClick = { showOxiInfo = false }) { Text("Entendido") } },
                containerColor = cardColor
            )
        }

        // Diálogo de entrada manual para Glucosa
        if (showGlucoseManualInput) {
            AlertDialog(
                onDismissRequest = { showGlucoseManualInput = false; manualGlucose = "" },
                title = { Text("Ingrese glucosa", color = textColor) },
                text = {
                    OutlinedTextField(
                        value = manualGlucose,
                        onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) manualGlucose = it },
                        label = { Text("Glucosa (mg/dL)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val glucose = manualGlucose.toDoubleOrNull()
                            if (glucose != null) {
                                saveExamToDatabase(
                                    tipoExamen = "GLUCOSE",
                                    titulo = "Glucosa",
                                    valor = String.format("%.0f", glucose),
                                    unidad = "mg/dL",
                                    observaciones = "Medición manual",
                                    datosAdicionales = mapOf(
                                        "metodo" to "manual",
                                        "timestamp" to System.currentTimeMillis()
                                    ),
                                    patientId = patientId
                                )
                                showGlucoseManualInput = false
                                manualGlucose = ""
                            }
                        },
                        enabled = manualGlucose.isNotEmpty()
                    ) { Text("Confirmar", color = primaryColor) }
                },
                dismissButton = {
                    TextButton(onClick = { showGlucoseManualInput = false; manualGlucose = "" }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                containerColor = cardColor
            )
        }

        // Info Glucosa
        if (showGlucoseInfo) {
            AlertDialog(
                onDismissRequest = { showGlucoseInfo = false },
                title = { Text("Cómo medir Glucosa", color = textColor) },
                text = {
                    Text(
                        "1) Lávate las manos y seca bien.\n" +
                        "2) Inserta tira reactiva y pincha lateral del dedo.\n" +
                        "3) Aplica la gota a la tira sin frotar.\n" +
                        "4) Espera el resultado y se sincronizará con la app si está conectado.\n" +
                        "Referencia ayunas: 70–99 mg/dL.",
                        color = Color.DarkGray
                    )
                },
                confirmButton = { TextButton(onClick = { showGlucoseInfo = false }) { Text("Entendido") } },
                containerColor = cardColor
            )
        }

        // Diálogo de entrada manual para Peso
        if (showWeightManualInput) {
            AlertDialog(
                onDismissRequest = { showWeightManualInput = false; manualWeight = "" },
                title = { Text("Ingrese peso", color = textColor) },
                text = {
                    OutlinedTextField(
                        value = manualWeight,
                        onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) manualWeight = it },
                        label = { Text("Peso (kg)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val weight = manualWeight.toDoubleOrNull()
                            if (weight != null) {
                                // Crear lectura manual para mostrar en pantalla
                                manualWeightReading = WeightReading(
                                    weight = weight.toFloat()
                                )
                                
                                saveExamToDatabase(
                                    tipoExamen = "WEIGHT",
                                    titulo = "Peso",
                                    valor = String.format("%.1f", weight),
                                    unidad = "kg",
                                    observaciones = "Medición manual",
                                    datosAdicionales = mapOf(
                                        "metodo" to "manual",
                                        "timestamp" to System.currentTimeMillis()
                                    ),
                                    patientId = patientId
                                )
                                showWeightManualInput = false
                                manualWeight = ""
                            }
                        },
                        enabled = manualWeight.isNotEmpty()
                    ) { Text("Confirmar", color = primaryColor) }
                },
                dismissButton = {
                    TextButton(onClick = { showWeightManualInput = false; manualWeight = "" }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                containerColor = cardColor
            )
        }

        // Info Peso
        if (showWeightInfo) {
            AlertDialog(
                onDismissRequest = { showWeightInfo = false },
                title = { Text("Cómo medir Peso", color = textColor) },
                text = {
                    Text(
                        "1) Coloca la báscula en superficie plana y firme.\n" +
                        "2) Paciente descalzo, sin objetos pesados.\n" +
                        "3) Mantenerse quieto hasta estabilizar la lectura.\n" +
                        "4) El dato se sincroniza automáticamente si la báscula está conectada.",
                        color = Color.DarkGray
                    )
                },
                confirmButton = { TextButton(onClick = { showWeightInfo = false }) { Text("Entendido") } },
                containerColor = cardColor
            )
        }
    }
}

@Composable
fun ModernDeviceIcon(
    icon: Int,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    if (isSelected) primaryColor else surfaceColor,
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = name,
                modifier = Modifier.size(28.dp),
                tint = if (isSelected) Color.White else onSurfaceColor.copy(alpha = 0.6f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) primaryColor else onSurfaceColor.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DeviceIcon(
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    tintColor: Color
    ) {
        Box(
            modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = tintColor
        )
        
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
    }
}
}

data class DeviceData(
    val name: String,
    val icon: Int,
    val tintColor: Color
)

// Clase de datos para lectura manual de peso
data class WeightReading(
    val weight: Float
)