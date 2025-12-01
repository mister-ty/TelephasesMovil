package com.example.telephases

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.telephases.ui.screens.*
import com.example.telephases.ui.screens.BLETensiometroManager
import com.example.telephases.ui.screens.BLEGlucometerManager
import com.example.telephases.ui.screens.BLEScaleManager
import com.example.telephases.ui.screens.BLETemperatureManager
import com.example.telephases.ui.theme.TelemonitoreoTheme
import com.example.telephases.ui.viewmodel.AuthViewModel
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import com.example.telephases.data.local.database.TestUserCreator
import com.example.telephases.data.local.database.UserUpdater
import com.example.telephases.data.local.dao.UserDao
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.launch
import com.example.telephases.workers.WorkScheduler

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var scaleManager: BLEScaleManager
    
    @Inject
    lateinit var userDao: UserDao
    
    @Inject
    lateinit var workScheduler: WorkScheduler
    
    override fun onDestroy() {
        super.onDestroy()
        // Forzar logout al cerrar la app
        Log.d("MainActivity", "🔄 Cerrando app - limpiando sesión")
        // Esto se puede hacer mejor con un ViewModel, pero por ahora es directo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scaleManager = BLEScaleManager(this)
        
        // Crear usuario de prueba para testing
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            TestUserCreator.createTestUser(userDao)
            
            // Actualizar usuarios existentes para que tengan tokens válidos
            UserUpdater.updateExistingUsers(userDao)
        }
        
        // Sincronizar usuarios inmediatamente al abrir la app
        Log.d("MainActivity", "👥 Programando sincronización inmediata de usuarios...")
        workScheduler.scheduleImmediateUserSync(forceSync = true) // FORZAR sincronización
        setContent {
            TelemonitoreoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = hiltViewModel()
                    
                    // Log para debug de navegación
                    LaunchedEffect(Unit) {
                        Log.d("MainActivity", "🚀 Iniciando app con destino: welcome")
                        // Nota: No limpiar la sesión al iniciar para no romper la asociación de maleta (FK SET NULL)
                    }
                    
                    NavHost(
                        navController = navController,
                        startDestination = "welcome"
                    ) {
                        // Pantalla de bienvenida
                        composable("welcome") {
                            WelcomeScreen(navController = navController)
                        }
                        
                        // Pantalla de login
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                viewModel = authViewModel
                            )
                        }
                        
                        // Configuración inicial de maleta
                        composable("maleta_setup") {
                            MaletaSetupScreen(
                                navController = navController,
                                onSetupComplete = {
                                    navController.navigate("admin_dashboard") {
                                        popUpTo("maleta_setup") { inclusive = true }
                                    }
                                }
                            )
                        }
                        
                        // Panel de administración
                        composable("admin_dashboard") {
                            AdminDashboard(navController = navController)
                        }
                        
                        // Selector de pacientes
                        composable("patient_selector") {
                            PatientSelector(navController = navController)
                        }
                        
                        // Búsqueda de pacientes
                        composable("patient_search") {
                            PatientSearchScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        
                        // Registro de pacientes
                        composable("patient_registration") {
                            PatientRegistrationScreen(
                                navController = navController,
                                authViewModel = authViewModel,
                                onRegistrationSuccess = { patientId ->
                                    navController.navigate("home?patientId=$patientId") {
                                        popUpTo("patient_registration") { inclusive = true }
                                    }
                                }
                            )
                        }
                        
                        // Registro de pacientes con cédula predefinida
                        composable(
                            route = "patient_registration?cedula={cedula}"
                        ) { backStackEntry ->
                            val cedula = backStackEntry.arguments?.getString("cedula")
                            PatientRegistrationScreen(
                                navController = navController,
                                authViewModel = authViewModel,
                                onRegistrationSuccess = { patientId ->
                                    navController.navigate("home?patientId=$patientId") {
                                        popUpTo("patient_registration") { inclusive = true }
                                    }
                                }
                            )
                        }
                        
                        // Registro de paciente (modificado)
                        composable("register_patient") {
                            RegisterScreen(
                                navController = navController,
                                viewModel = authViewModel
                            )
                        }
                        
                        // Pantalla principal/home (con parámetros de paciente)
                        composable(
                            route = "home?patientId={patientId}&patientName={patientName}"
                        ) { backStackEntry ->
                            val patientId = backStackEntry.arguments?.getString("patientId")
                            val patientName = backStackEntry.arguments?.getString("patientName")
                            HomeScreen(
                                navController = navController,
                                authViewModel = authViewModel,
                                patientId = patientId,
                                patientName = patientName,
                                onLogoutClick = {
                                    navController.navigate("admin_dashboard") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                        
                        // Perfil de paciente
                        composable(
                            route = "patient_profile/{patientId}"
                        ) { backStackEntry ->
                            val patientId = backStackEntry.arguments?.getString("patientId")
                            if (patientId != null) {
                                PatientProfileScreen(
                                    navController = navController,
                                    patientId = patientId
                                )
                            } else {
                                Text("Error: ID de paciente inválido")
                            }
                        }
                        
                        // Pantalla de exámenes médicos (con paciente)
                        composable(
                            route = "medical_exam?patientId={patientId}"
                        ) { backStackEntry ->
                            val patientId = backStackEntry.arguments?.getString("patientId")
                            MedicalExamScreen(
                                navController = navController,
                                authViewModel = authViewModel,
                                patientId = patientId
                            )
                        }
                        
                        // Pantalla de exámenes médicos (sin paciente)
                        composable("medical_exam") {
                            MedicalExamScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        
                        // Gestión de citas
                        composable("citas_management") {
                            CitasManagementScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        
                        // Detalle de cita con lista de chequeos
                        composable(
                            route = "cita_detalle/{citaId}"
                        ) { backStackEntry ->
                            val citaId = backStackEntry.arguments?.getString("citaId")?.toIntOrNull()
                            if (citaId != null) {
                                CitaDetalleScreen(
                                    navController = navController,
                                    citaId = citaId
                                )
                            } else {
                                // Fallback si no hay citaId válido
                                Text("Error: ID de cita inválido")
                            }
                        }
                        
                        // Rutas para exámenes BLE desde citas
                        composable("ble_tensiometro/{citaId}/{examenId}") { backStackEntry ->
                            val citaId = backStackEntry.arguments?.getString("citaId")?.toIntOrNull()
                            val examenId = backStackEntry.arguments?.getString("examenId")?.toIntOrNull()
                            if (citaId != null && examenId != null) {
                                ExamenTensiometroScreen(
                                    navController = navController,
                                    citaId = citaId,
                                    examenId = examenId
                                )
                            }
                        }
                        
                        composable("ble_glucometer/{citaId}/{examenId}") { backStackEntry ->
                            val citaId = backStackEntry.arguments?.getString("citaId")?.toIntOrNull()
                            val examenId = backStackEntry.arguments?.getString("examenId")?.toIntOrNull()
                            if (citaId != null && examenId != null) {
                                ExamenGlucometerScreen(
                                    navController = navController,
                                    citaId = citaId,
                                    examenId = examenId
                                )
                            }
                        }
                        
                        composable("ble_scale/{citaId}/{examenId}") { backStackEntry ->
                            val citaId = backStackEntry.arguments?.getString("citaId")?.toIntOrNull()
                            val examenId = backStackEntry.arguments?.getString("examenId")?.toIntOrNull()
                            if (citaId != null && examenId != null) {
                                ExamenScaleScreen(
                                    navController = navController,
                                    citaId = citaId,
                                    examenId = examenId
                                )
                            }
                        }
                        
                        composable("ble_temperature/{citaId}/{examenId}") { backStackEntry ->
                            val citaId = backStackEntry.arguments?.getString("citaId")?.toIntOrNull()
                            val examenId = backStackEntry.arguments?.getString("examenId")?.toIntOrNull()
                            if (citaId != null && examenId != null) {
                                ExamenTemperatureScreen(
                                    navController = navController,
                                    citaId = citaId,
                                    examenId = examenId
                                )
                            }
                        }
                        
                        // Exámenes BLE implementados
                        composable("ble_pulso/{citaId}/{examenId}") { backStackEntry ->
                            val citaId = backStackEntry.arguments?.getString("citaId")?.toIntOrNull() ?: 0
                            val examenId = backStackEntry.arguments?.getString("examenId")?.toIntOrNull() ?: 0
                            ExamenHeartRateScreen(
                                navController = navController,
                                citaId = citaId,
                                examenId = examenId
                            )
                        }
                        
                        composable("ble_oxigeno/{citaId}/{examenId}") { backStackEntry ->
                            val citaId = backStackEntry.arguments?.getString("citaId")?.toIntOrNull() ?: 0
                            val examenId = backStackEntry.arguments?.getString("examenId")?.toIntOrNull() ?: 0
                            ExamenOximeterScreen(
                                navController = navController,
                                citaId = citaId,
                                examenId = examenId
                            )
                        }
                        
                        composable("ble_ecg/{citaId}/{examenId}") { backStackEntry ->
                            Text("Examen de ECG - En desarrollo")
                        }
                        
                        // Mantener la pantalla de pruebas de báscula disponible si es necesaria
                        composable("scale_test") {
                            ScaleTestScreen(manager = scaleManager)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScaleTestScreen(manager: BLEScaleManager) {
    val isScanning by manager.isScanning.collectAsStateWithLifecycle()
    val scaleReading by manager.scaleReading.collectAsStateWithLifecycle()
    val logs by manager.logs.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) manager.startScan()
    }

    val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION)
    } else {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Pruebas de Báscula (Peso + Impedancia)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (manager.hasBluetoothPermissions()) manager.startScan()
                else permissionLauncher.launch(requiredPermissions)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !isScanning
        ) {
            Text(if (isScanning) "Escaneando..." else "Iniciar Medición", fontSize = 16.sp)
        }

        Spacer(Modifier.height(24.dp))

        // Muestra el resultado final cuando esté disponible
        scaleReading?.let { reading ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Lectura Estable Recibida", style = MaterialTheme.typography.titleMedium)
                    Divider()
                    Text("Peso: ${String.format("%.2f", reading.weight)} ${reading.unit}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("Impedancia: ${reading.impedance}", fontSize = 22.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Logs:", fontWeight = FontWeight.Bold)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(logs) { log ->
                Text(log, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}