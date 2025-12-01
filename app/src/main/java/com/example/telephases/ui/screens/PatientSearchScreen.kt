package com.example.telephases.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telephases.data.local.entities.PatientEntity
import com.example.telephases.ui.viewmodel.AuthState
import com.example.telephases.ui.viewmodel.AuthViewModel
import com.example.telephases.ui.viewmodel.PatientViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientSearchScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    patientViewModel: PatientViewModel = hiltViewModel()
) {
    val turquoiseColor = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()
    
    // Estados desde ViewModels
    val authState by authViewModel.state.collectAsState()
    val patientUiState by patientViewModel.uiState.collectAsState()
    val allPatients by patientViewModel.patients.collectAsState()
    
    // Estados locales para UI
    var searchQuery by remember { mutableStateOf("") }
    var filteredPatients by remember { mutableStateOf<List<PatientEntity>>(emptyList()) }
    
    // Snackbar para errores
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Función para cargar todos los pacientes (offline-first)
    fun cargarPacientes() {
        Log.d("PatientSearch", "🔄 Iniciando carga de pacientes offline-first")
        patientViewModel.loadPatients()
    }
    
    // Función para filtrar pacientes
    fun filtrarPacientes(query: String) {
        searchQuery = query
        if (query.isBlank()) {
            filteredPatients = allPatients
        } else {
            filteredPatients = allPatients.filter { paciente ->
                paciente.nombreCompleto.contains(query, ignoreCase = true) ||
                paciente.numeroDocumento.contains(query) ||
                (paciente.email?.contains(query, ignoreCase = true) == true)
            }
        }
        Log.d("PatientSearch", "🔍 Filtrado: ${filteredPatients.size} pacientes de ${allPatients.size} totales")
    }
    
    // Actualizar pacientes filtrados cuando cambian los pacientes principales
    LaunchedEffect(allPatients) {
        Log.d("PatientSearch", "🔄 allPatients cambió: ${allPatients.size} pacientes")
        filtrarPacientes(searchQuery)
    }
    
    // Función para registrar nuevo paciente
    fun registrarNuevoPaciente() {
        navController.navigate("patient_registration")
    }
    
    // Función para ir a exámenes del paciente
    fun irAExamenes(paciente: PatientEntity) {
        navController.navigate("home?patientId=${paciente.id}&patientName=${paciente.nombreCompleto}")
    }
    
    // Función para sincronizar pacientes
    fun sincronizarPacientes() {
        Log.d("PatientSearch", "🔄 Iniciando sincronización manual de pacientes")
        patientViewModel.syncPatients()
    }

    // Cargar pacientes al inicio
    LaunchedEffect(Unit) {
        cargarPacientes()
    }
    
    // Mostrar errores en Snackbar
    LaunchedEffect(patientUiState.error) {
        patientUiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Long
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pacientes Registrados",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = turquoiseColor
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Botón de sincronización
                    IconButton(
                        onClick = { sincronizarPacientes() },
                        enabled = !patientUiState.isSyncing
                    ) {
                        if (patientUiState.isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Sincronizar Pacientes",
                                tint = Color.White
                            )
                        }
                    }
                    
                    // Botón de registro
                    IconButton(
                        onClick = { registrarNuevoPaciente() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Registrar Paciente",
                            tint = Color.White
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
        ) {
            // Barra de búsqueda
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🔍 Buscar Pacientes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = turquoiseColor
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { filtrarPacientes(it) },
                        label = { Text("Buscar por nombre, documento o email") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = turquoiseColor
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${filteredPatients.size} pacientes encontrados",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    
                    // Indicador de sincronización
                    if (patientUiState.hasPendingSync) {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(16.dp)
                            )
                            
                            Text(
                                text = "Hay pacientes pendientes de sincronización",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFFF9800)
                            )
                        }
                    }
                }
            }
            
            // Lista de pacientes
            if (patientUiState.isLoading) {
                Log.d("PatientSearch", "⏳ Mostrando indicador de carga")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = turquoiseColor)
                }
            } else if (filteredPatients.isEmpty()) {
                Log.d("PatientSearch", "❌ filteredPatients está vacío: allPatients=${allPatients.size}, searchQuery='$searchQuery'")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Sin pacientes",
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = if (searchQuery.isBlank()) "No hay pacientes registrados" else "No se encontraron pacientes",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        
                        if (searchQuery.isBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Button(
                                onClick = { registrarNuevoPaciente() },
                                colors = ButtonDefaults.buttonColors(containerColor = turquoiseColor)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Registrar Primer Paciente")
                            }
                        }
                    }
                }
            } else {
                Log.d("PatientSearch", "✅ Mostrando ${filteredPatients.size} pacientes en la lista")
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPatients) { paciente ->
                        PatientCard(
                            paciente = paciente,
                            onSelectPatient = { irAExamenes(paciente) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PatientCard(
    paciente: PatientEntity,
    onSelectPatient: () -> Unit
) {
    val turquoiseColor = MaterialTheme.colorScheme.primary
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onSelectPatient
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Nombre del paciente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = paciente.nombreCompleto,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Información adicional
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = "Documento",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = paciente.numeroDocumento,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Paciente",
                    tint = turquoiseColor,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Información de contacto
            paciente.email?.let { email ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            paciente.telefono?.let { telefono ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Teléfono",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = telefono,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botón para ir a exámenes
            Button(
                onClick = onSelectPatient,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = turquoiseColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Ver Exámenes",
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
} 