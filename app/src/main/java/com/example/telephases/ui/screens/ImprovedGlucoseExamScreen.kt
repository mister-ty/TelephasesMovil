package com.example.telephases.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.example.telephases.ui.components.*
import kotlinx.coroutines.launch

/**
 * Pantalla mejorada de examen de glucosa con reconexión automática
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImprovedGlucoseExamScreen(
    onBack: () -> Unit,
    onExamComplete: (reading: GlucometerReading) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Estados del manager BLE mejorado
    val glucometerManager = remember { ImprovedBLEGlucometerManager(context) }
    
    // Estados observables
    val isConnected by glucometerManager.isConnected.collectAsState()
    val isConnecting by glucometerManager.isConnecting.collectAsState()
    val isScanning by glucometerManager.isScanning.collectAsState()
    val isReconnecting by glucometerManager.isReconnecting.collectAsState()
    val statusMessage by glucometerManager.statusMessage.collectAsState()
    val reconnectionAttempts by glucometerManager.reconnectionAttempts.collectAsState()
    val lastError by glucometerManager.lastError.collectAsState()
    val reading by glucometerManager.glucometerReading.collectAsState()
    
    // Estados de UI
    var showConnectionDialog by remember { mutableStateOf(false) }
    var showReadingDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var autoReconnectionEnabled by remember { mutableStateOf(true) }
    
    // Limpiar recursos al salir
    DisposableEffect(Unit) {
        onDispose {
            glucometerManager.cleanup()
        }
    }
    
    // Manejar cambios de estado
    LaunchedEffect(isConnected, reading) {
        when {
            isConnected && reading != null -> {
                showReadingDialog = true
                showSuccessDialog = true
            }
            !isConnected && lastError != null && !isReconnecting -> {
                showErrorDialog = true
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Examen de Glucosa") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { autoReconnectionEnabled = !autoReconnectionEnabled }
                    ) {
                        Icon(
                            imageVector = if (autoReconnectionEnabled) Icons.Default.Sync else Icons.Default.SyncDisabled,
                            contentDescription = if (autoReconnectionEnabled) "Reconexión habilitada" else "Reconexión deshabilitada",
                            tint = if (autoReconnectionEnabled) Color(0xFF4CAF50) else Color(0xFF757575)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Indicador de estado de conexión mejorado
            DeviceConnectionStatus(
                deviceName = "Glucómetro BLE",
                isConnected = isConnected,
                isConnecting = isConnecting,
                isScanning = isScanning,
                isReconnecting = isReconnecting,
                statusMessage = statusMessage,
                reconnectionAttempts = reconnectionAttempts,
                maxReconnectionAttempts = 5,
                lastError = lastError,
                onConnect = {
                    scope.launch {
                        glucometerManager.startScanning()
                    }
                },
                onDisconnect = {
                    glucometerManager.disconnect()
                },
                onRetry = {
                    scope.launch {
                        glucometerManager.startScanning()
                    }
                },
                onStopReconnection = {
                    glucometerManager.stopAutoReconnection()
                }
            )
            
            // Configuración de reconexión automática
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (autoReconnectionEnabled) Color(0xFFE8F5E8) else Color(0xFFFFF3E0)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (autoReconnectionEnabled) Icons.Default.Sync else Icons.Default.SyncDisabled,
                        contentDescription = null,
                        tint = if (autoReconnectionEnabled) Color(0xFF4CAF50) else Color(0xFFFF9800),
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Reconexión Automática",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (autoReconnectionEnabled) "Habilitada - Se reconectará automáticamente en caso de fallo" else "Deshabilitada - Reconexión manual requerida",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Switch(
                        checked = autoReconnectionEnabled,
                        onCheckedChange = { enabled ->
                            autoReconnectionEnabled = enabled
                            glucometerManager.setAutoReconnectionEnabled(enabled)
                        }
                    )
                }
            }
            
            // Información del examen
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Instrucciones del Examen",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val instructions = listOf(
                        "1. Asegúrate de que el glucómetro esté encendido",
                        "2. Coloca una tira reactiva en el dispositivo",
                        "3. Presiona 'Conectar' para buscar el dispositivo",
                        "4. Una vez conectado, realiza la medición",
                        "5. Los resultados aparecerán automáticamente"
                    )
                    
                    instructions.forEach { instruction ->
                        Text(
                            text = instruction,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
            
            // Resultado de la medición
            if (reading != null) {
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
                            imageVector = Icons.Default.CheckCircle,
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
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        reading?.let { currentReading ->
                            Text(
                                text = "${currentReading.glucose} mg/dL",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        reading?.let { currentReading ->
                            Button(
                                onClick = { onExamComplete(currentReading) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50)
                                )
                            ) {
                                Text("Guardar Resultado")
                            }
                        }
                    }
                }
            }
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isConnected && !isConnecting && !isScanning) {
                    Button(
                        onClick = {
                            scope.launch {
                                glucometerManager.startScanning()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Buscar Dispositivo")
                    }
                }
                
                if (isConnected) {
                    OutlinedButton(
                        onClick = { glucometerManager.disconnect() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFF44336)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Desconectar")
                    }
                }
            }
        }
    }
    
    // Diálogos de alerta
    ExamAlertDialog(
        isVisible = showErrorDialog,
        onDismiss = { showErrorDialog = false },
        title = "Error de Conexión",
        message = lastError ?: "No se pudo conectar al dispositivo",
        type = AlertType.ERROR,
        primaryAction = AlertAction("Reintentar") {
            showErrorDialog = false
            scope.launch {
                glucometerManager.startScanning()
            }
        },
        secondaryAction = AlertAction("Cancelar") {
            showErrorDialog = false
        }
    )
    
    ExamAlertDialog(
        isVisible = showSuccessDialog,
        onDismiss = { showSuccessDialog = false },
        title = "Medición Exitosa",
        message = "Se ha completado la medición de glucosa correctamente",
        type = AlertType.SUCCESS,
        primaryAction = AlertAction("Continuar") {
            showSuccessDialog = false
        }
    )
    
    // Diálogo de progreso de reconexión
    ExamProgressDialog(
        isVisible = isReconnecting,
        title = "Reconectando",
        message = "Intentando reconectar al dispositivo...",
        progress = reconnectionAttempts.toFloat() / 5f,
        onCancel = {
            glucometerManager.stopAutoReconnection()
        }
    )
    
    // Notificaciones toast
    ExamToast(
        isVisible = isConnected && !showSuccessDialog,
        message = "Dispositivo conectado exitosamente",
        type = AlertType.SUCCESS,
        duration = 2000L,
        onDismiss = { }
    )
}
