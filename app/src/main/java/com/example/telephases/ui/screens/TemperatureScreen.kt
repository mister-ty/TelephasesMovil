package com.example.telephases.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.telephases.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureScreen(
    navController: NavHostController
) {
    // Colores principales
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = Color(0xFF202020)
    val cardColor = Color(0xFF2D2D2D)
    val textColor = Color.White
    val errorColor = Color(0xFFFF5252)

    // Información del usuario
    val userName = "Ely"
    val userGender = "Female"
    val userAge = "32"

    // Context y scope
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Managers de Bluetooth
    val bleManager = remember { BLETemperatureManager(context) }
    val btClassicManager = remember { BluetoothClassicManager(context) }

    // Estado para cambiar entre BLE y Clásico
    var bluetoothMode by remember { mutableStateOf("CLASSIC") } // Empezar con Clásico

    // Estados según el modo seleccionado
    val isConnected by if (bluetoothMode == "BLE")
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

    val logs by if (bluetoothMode == "BLE")
        bleManager.logs.collectAsState()
    else
        btClassicManager.logs.collectAsState()

    // Estados UI
    var showDeviceDialog by remember { mutableStateOf(false) }
    var showManualInput by remember { mutableStateOf(false) }
    var showLogs by remember { mutableStateOf(false) }
    var manualTemperature by remember { mutableStateOf("") }

    // Lanzador de permisos
    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted && bluetoothMode == "BLE") {
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

    // Cleanup al salir
    DisposableEffect(Unit) {
        onDispose {
            bleManager.cleanup()
            btClassicManager.cleanup()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_tele_vertical),
                            contentDescription = "Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("iDoctorCloud", color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = textColor
                ),
                actions = {
                    Text("$userName $userGender $userAge", color = textColor, modifier = Modifier.padding(end = 8.dp))

                    IconButton(onClick = { showLogs = !showLogs }) {
                        Icon(Icons.Default.List, "Logs", tint = textColor)
                    }

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = primaryColor),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text("RETURN", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            // Estado de conexión
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardColor),
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
                                .background(if (isConnected) Color.Green else errorColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (isConnected) "Connected" else "No connection",
                            color = if (isConnected) Color.Green else errorColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de modo Bluetooth
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Modo de conexión", style = MaterialTheme.typography.titleSmall, color = primaryColor)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        // Botón BLE
                        OutlinedButton(
                            onClick = {
                                bluetoothMode = "BLE"
                                btClassicManager.disconnect()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (bluetoothMode == "BLE") primaryColor else Color.Transparent,
                                contentColor = if (bluetoothMode == "BLE") Color.White else primaryColor
                            ),
                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                        ) {
                            Text("BLE", fontWeight = FontWeight.Bold)
                        }

                        // Botón Clásico
                        OutlinedButton(
                            onClick = {
                                bluetoothMode = "CLASSIC"
                                bleManager.disconnect()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (bluetoothMode == "CLASSIC") primaryColor else Color.Transparent,
                                contentColor = if (bluetoothMode == "CLASSIC") Color.White else primaryColor
                            ),
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        ) {
                            Text("Clásico", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        if (bluetoothMode == "BLE")
                            "Para dispositivos modernos que no requieren emparejar"
                        else
                            "Para dispositivos que aparecen en Bluetooth normal",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lectura de temperatura
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Temperature", style = MaterialTheme.typography.headlineMedium, color = textColor, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = temperature?.let { "%.1f".format(it.temperature) } ?: "--?--",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text("°C", fontSize = 24.sp, color = textColor, modifier = Modifier.padding(top = 8.dp))
                    }

                    temperature?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Modo: ${it.mode}", style = MaterialTheme.typography.bodyLarge, color = primaryColor)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Normal range: 36-37.2°C", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }

            // Mostrar logs si está activado
            if (showLogs) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(logs.takeLast(10)) { log ->
                            Text(log, fontSize = 11.sp, color = textColor, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))




            // Botones de acción
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        when {
                            bluetoothMode == "BLE" && !bleManager.hasBluetoothPermissions() -> requestBluetoothPermissions()
                            isConnected -> {
                                if (bluetoothMode == "BLE") bleManager.disconnect()
                                else btClassicManager.disconnect()
                            }
                            else -> {
                                showDeviceDialog = true
                                if (bluetoothMode == "BLE") bleManager.startScan()
                                else btClassicManager.getPairedDevices()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isConnected) errorColor else primaryColor),
                    modifier = Modifier.weight(1f).padding(end = 8.dp).height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    enabled = !isScanning
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(if (isConnected) "Disconnect" else "Start", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Button(
                    onClick = { showManualInput = true },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    modifier = Modifier.weight(1f).padding(start = 8.dp).height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Manual input", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }

    // Diálogo de dispositivos
    if (showDeviceDialog) {
        Dialog(onDismissRequest = {
            showDeviceDialog = false
            if (bluetoothMode == "BLE") bleManager.stopScan()
        }) {
            Card(
                modifier = Modifier.fillMaxWidth().height(400.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        if (bluetoothMode == "BLE") "Dispositivos BLE" else "Dispositivos emparejados",
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val devices = if (bluetoothMode == "BLE") devicesFound else pairedDevices

                    if (devices.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                if (bluetoothMode == "BLE") "Buscando..." else "No hay dispositivos emparejados",
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn {
                            items(devices) { device ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            scope.launch {
                                                if (bluetoothMode == "BLE") {
                                                    bleManager.connectToDevice(device.address)
                                                } else {
                                                    btClassicManager.connectToDevice(device.address)
                                                }
                                                showDeviceDialog = false
                                            }
                                        },
                                    colors = CardDefaults.cardColors(containerColor = backgroundColor)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Bluetooth, "Bluetooth", tint = primaryColor, modifier = Modifier.size(24.dp))
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

    // Diálogo de entrada manual
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
                            // TODO: Guardar temperatura
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
}