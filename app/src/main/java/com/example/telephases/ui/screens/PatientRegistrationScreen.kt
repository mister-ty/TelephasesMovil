package com.example.telephases.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.telephases.network.PatientRegistrationRequest
import com.example.telephases.ui.viewmodel.AuthViewModel
import com.example.telephases.ui.viewmodel.AuthState
import com.example.telephases.ui.viewmodel.AuthUiState
import com.example.telephases.data.local.entities.PatientEntity
import com.example.telephases.ui.viewmodel.PatientViewModel
import com.example.telephases.ui.viewmodel.PatientUiState
import kotlinx.coroutines.launch
import android.util.Log
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

// Datos estáticos para los dropdowns
private val departamentosColombia = listOf(
    "Antioquia", "Atlántico", "Bogotá D.C.", "Bolívar", "Boyacá", "Caldas", "Caquetá", 
    "Cauca", "Cesar", "Córdoba", "Cundinamarca", "Chocó", "Huila", "La Guajira", 
    "Magdalena", "Meta", "Nariño", "Norte de Santander", "Quindío", "Risaralda", 
    "Santander", "Sucre", "Tolima", "Valle del Cauca", "Arauca", "Casanare", 
    "Putumayo", "San Andrés y Providencia", "Amazonas", "Guainía", "Guaviare", "Vaupés", "Vichada"
)

private val municipiosPorDepartamento = mapOf(
    "Antioquia" to listOf("Medellín", "Bello", "Itagüí", "Envigado", "Apartadó", "Turbo", "Rionegro", "Barbosa", "Copacabana", "Girardota"),
    "Atlántico" to listOf("Barranquilla", "Soledad", "Malambo", "Sabanagrande", "Galapa", "Puerto Colombia", "Tubará", "Usiacurí"),
    "Bogotá D.C." to listOf("Bogotá"),
    "Bolívar" to listOf("Cartagena", "Magangué", "Turbaco", "Arjona", "Mahates", "San Pablo", "Santa Rosa", "Simití"),
    "Boyacá" to listOf("Tunja", "Duitama", "Sogamoso", "Chiquinquirá", "Paipa", "Villa de Leyva", "Monguí", "Ráquira"),
    "Caldas" to listOf("Manizales", "La Dorada", "Riosucio", "Anserma", "Aranzazu", "Belalcázar", "Chinchiná", "Filadelfia"),
    "Valle del Cauca" to listOf("Cali", "Palmira", "Buenaventura", "Tuluá", "Cartago", "Buga", "Yumbo", "Ginebra"),
    "Santander" to listOf("Bucaramanga", "Floridablanca", "Girón", "Piedecuesta", "Barrancabermeja", "San Gil", "Socorro", "Vélez")
)

private val tiposUsuario = listOf(
    "Capita",
    "Contributivo", 
    "Contributivo UPC",
    "Evento",
    "Otro",
    "Particular",
    "Sin Información",
    "Subsidiado",
    "Vinculado",
    "Víctima con afiliación al Régimen Contributivo",
    "Víctima con afiliación al Régimen Subsidiado",
    "Víctima no asegurado (Vinculado)"
)

private val tiposIdentificacion = listOf(
    "Cédula de Ciudadanía",
    "Tarjeta de Identidad", 
    "Cédula de Extranjería",
    "Pasaporte",
    "Registro Civil",
    "Otro"
)

private val estadosCiviles = listOf(
    "Soltero(a)",
    "Casado(a)",
    "Divorciado(a)",
    "Viudo(a)",
    "Unión Libre",
    "Separado(a)"
)

private val generos = listOf(
    "Masculino",
    "Femenino", 
    "Otro"
)

// Funciones de utilidad para el formulario (SIN puntos automáticos)
private fun formatPhoneInput(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return when {
        digitsOnly.length <= 3 -> digitsOnly
        digitsOnly.length <= 7 -> "${digitsOnly.substring(0, 3)} ${digitsOnly.substring(3)}"
        else -> "${digitsOnly.substring(0, 3)} ${digitsOnly.substring(3, 7)} ${digitsOnly.substring(7, minOf(11, digitsOnly.length))}"
    }
}

private fun isValidEmail(email: String): Boolean {
    return email.isBlank() || Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun isValidDate(date: String): Boolean {
    if (date.length != 10) return false
    return try {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        format.isLenient = false
        val parsedDate = format.parse(date)
        val currentDate = Date()
        parsedDate != null && parsedDate.before(currentDate)
    } catch (e: Exception) {
        false
    }
}

private fun calculateAge(birthDate: String): Int? {
    return try {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val birth = format.parse(birthDate)
        val today = Calendar.getInstance()
        val birthCal = Calendar.getInstance()
        birthCal.time = birth!!
        
        var age = today.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        age
    } catch (e: Exception) {
        null
    }
}

private fun convertDateFormatForAPI(dateString: String?): String? {
    if (dateString.isNullOrBlank()) return null
    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        if (date != null) {
            outputFormat.format(date)
        } else {
            null
        }
    } catch (e: Exception) {
        // Si no se puede convertir, retornar el valor original
        dateString
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientRegistrationScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    patientViewModel: PatientViewModel = hiltViewModel(),
    onRegistrationSuccess: ((patientId: String) -> Unit)? = null
) {
    val primaryColor = MaterialTheme.colorScheme.primary
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
    val patientUiState by patientViewModel.uiState.collectAsState(
        initial = PatientUiState()
    )
    
    // Estados del formulario
    var primerNombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var primerApellido by remember { mutableStateOf("") }
    var segundoApellido by remember { mutableStateOf("") }
    var numeroDocumento by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    
    // Nuevos campos
    var tipoIdentificacion by remember { mutableStateOf("") }
    var estadoCivil by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("Colombia") }
    var departamento by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var tipoUsuario by remember { mutableStateOf("") }
    
    // Estados de UI
    var showSuccess by remember { mutableStateOf(false) }
    var currentStep by remember { mutableStateOf(1) }
    var registeredPatientId by remember { mutableStateOf<String?>(null) }
    
    // Estados para dropdowns
    var showTipoIdentificacionDropdown by remember { mutableStateOf(false) }
    var showEstadoCivilDropdown by remember { mutableStateOf(false) }
    var showGeneroDropdown by remember { mutableStateOf(false) }
    var showDepartamentoDropdown by remember { mutableStateOf(false) }
    var showMunicipioDropdown by remember { mutableStateOf(false) }
    var showTipoUsuarioDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    
    // Snackbar para mensajes
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Estados adicionales para validaciones en tiempo real
    var emailError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var calculatedAge by remember { mutableStateOf<Int?>(null) }
    
    // Municipios disponibles según departamento seleccionado
    val municipiosDisponibles = remember(departamento) {
        if (departamento.isNotBlank()) {
            municipiosPorDepartamento[departamento] ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    // Validaciones mejoradas
    val isStep1Valid = primerNombre.isNotBlank() && primerApellido.isNotBlank() && 
                      numeroDocumento.isNotBlank() && tipoIdentificacion.isNotBlank() &&
                      fechaNacimiento.isNotBlank() && isValidDate(fechaNacimiento) &&
                      genero.isNotBlank()
    val isStep2Valid = (email.isNotBlank() && isValidEmail(email)) || telefono.isNotBlank()
    val isStep3Valid = estadoCivil.isNotBlank() && departamento.isNotBlank() && municipio.isNotBlank() && tipoUsuario.isNotBlank()
    val isFormValid = isStep1Valid && isStep2Valid && isStep3Valid
    
    // Función para registrar paciente (offline-first)
    fun registrarPaciente() {
        if (!isFormValid) {
            scope.launch {
                snackbarHostState.showSnackbar("Por favor complete todos los campos obligatorios")
            }
            return
        }
        
        val request = PatientRegistrationRequest(
            primer_nombre = primerNombre.trim(),
            segundo_nombre = segundoNombre.trim().takeIf { it.isNotBlank() },
            primer_apellido = primerApellido.trim(),
            segundo_apellido = segundoApellido.trim().takeIf { it.isNotBlank() },
            tipo_documento_id = 1, // Cédula de ciudadanía
            numero_documento = numeroDocumento.trim(),
            email = email.trim().takeIf { it.isNotBlank() },
            telefono = telefono.trim().takeIf { it.isNotBlank() },
            direccion = direccion.trim().takeIf { it.isNotBlank() },
            fecha_nacimiento = convertDateFormatForAPI(fechaNacimiento.takeIf { it.isNotBlank() }),
            genero = when (genero.trim()) {
                "Masculino" -> "M"
                "Femenino" -> "F"
                "Otro" -> "O"
                else -> null
            },
            ciudad_id = 1, // Por defecto Bogotá - en producción esto debería venir de un dropdown
            tipo_identificacion = tipoIdentificacion.trim().takeIf { it.isNotBlank() },
            estado_civil = estadoCivil.trim().takeIf { it.isNotBlank() },
            pais = pais.trim().takeIf { it.isNotBlank() },
            departamento = departamento.trim().takeIf { it.isNotBlank() },
            municipio = municipio.trim().takeIf { it.isNotBlank() },
            tipo_usuario = tipoUsuario.trim().takeIf { it.isNotBlank() },
            entidad_salud_id = authUiState.currentUser?.entidadSaludId
        )
        
        patientViewModel.registerPatient(request) { patient ->
            registeredPatientId = patient.id
            showSuccess = true
            onRegistrationSuccess?.invoke(patient.id)
            Log.d("PatientRegistration", "Paciente registrado exitosamente: ${patient.nombreCompleto}")
        }
    }
    
    // Efectos para manejar errores
    LaunchedEffect(patientUiState.error) {
        patientUiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            patientViewModel.clearError()
        }
    }
    
    // Función para ir a exámenes del paciente registrado
    fun irAExamenes() {
        navController.navigate("admin_dashboard")
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registrar Paciente",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
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
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header con progreso
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Registro de Paciente",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Stepper visual
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StepIndicator(
                            step = 1,
                            currentStep = currentStep,
                            title = "Datos Personales",
                            isCompleted = isStep1Valid
                        )
                        StepIndicator(
                            step = 2,
                            currentStep = currentStep,
                            title = "Contacto",
                            isCompleted = isStep2Valid
                        )
                        StepIndicator(
                            step = 3,
                            currentStep = currentStep,
                                title = "Ubicación",
                            isCompleted = isStep3Valid
                        )
                    }
                }
            }
            }
            
            // Formulario por pasos
            when (currentStep) {
                1 -> {
                // Paso 1: Datos Personales
                    item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                                modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                Text(
                                    text = "Datos Personales",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )
                                
                                // Nombres
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                    OutlinedTextField(
                                    value = primerNombre,
                                    onValueChange = { primerNombre = it },
                                        label = { Text("Primer Nombre *") },
                                    modifier = Modifier.weight(1f),
                                        isError = primerNombre.isNotBlank() && primerNombre.isBlank(),
                                        keyboardOptions = KeyboardOptions(
                                            capitalization = KeyboardCapitalization.Words,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    
                                    OutlinedTextField(
                                    value = segundoNombre,
                                    onValueChange = { segundoNombre = it },
                                        label = { Text("Segundo Nombre") },
                                    modifier = Modifier.weight(1f),
                                        keyboardOptions = KeyboardOptions(
                                            capitalization = KeyboardCapitalization.Words,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                }
                                
                                // Apellidos
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                    OutlinedTextField(
                                    value = primerApellido,
                                    onValueChange = { primerApellido = it },
                                        label = { Text("Primer Apellido *") },
                                    modifier = Modifier.weight(1f),
                                        isError = primerApellido.isNotBlank() && primerApellido.isBlank(),
                                        keyboardOptions = KeyboardOptions(
                                            capitalization = KeyboardCapitalization.Words,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                    
                                    OutlinedTextField(
                                    value = segundoApellido,
                                    onValueChange = { segundoApellido = it },
                                        label = { Text("Segundo Apellido") },
                                    modifier = Modifier.weight(1f),
                                        keyboardOptions = KeyboardOptions(
                                            capitalization = KeyboardCapitalization.Words,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                }
                                
                                // Tipo de identificación y número
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                // Dropdown Tipo Identificación
                                ExposedDropdownMenuBox(
                                    expanded = showTipoIdentificacionDropdown,
                                    onExpandedChange = { showTipoIdentificacionDropdown = !showTipoIdentificacionDropdown },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    OutlinedTextField(
                                        value = tipoIdentificacion,
                                        onValueChange = { },
                                        readOnly = true,
                                        label = { Text("Tipo Identificación *") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = showTipoIdentificacionDropdown
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = showTipoIdentificacionDropdown,
                                        onDismissRequest = { showTipoIdentificacionDropdown = false }
                                    ) {
                                        tiposIdentificacion.forEach { tipo ->
                                            DropdownMenuItem(
                                                text = { Text(tipo) },
                                                onClick = {
                                                    tipoIdentificacion = tipo
                                                    showTipoIdentificacionDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }
                                    
                                    OutlinedTextField(
                                        value = numeroDocumento,
                                        onValueChange = { numeroDocumento = it },
                                        label = { Text("Número Documento *") },
                                        modifier = Modifier.weight(1f),
                                        isError = numeroDocumento.isNotBlank() && numeroDocumento.isBlank(),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Next
                                        )
                                    )
                                }
                                
                                // Fecha de nacimiento con DatePicker
                                ExposedDropdownMenuBox(
                                    expanded = showDatePicker,
                                    onExpandedChange = { showDatePicker = !showDatePicker },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                    value = fechaNacimiento,
                                        onValueChange = { },
                                        readOnly = true,
                                        label = { Text("Fecha de Nacimiento *") },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = { showDatePicker = true }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.DateRange,
                                                    contentDescription = "Seleccionar fecha"
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        isError = fechaNacimiento.isNotBlank() && !isValidDate(fechaNacimiento)
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = showDatePicker,
                                        onDismissRequest = { showDatePicker = false }
                                    ) {
                                        // El DatePicker se mostrará como un diálogo separado
                                        DropdownMenuItem(
                                            text = { Text("Seleccionar fecha") },
                                            onClick = {
                                                showDatePicker = false
                                                // Mostrar el DatePickerDialog
                                                showDatePickerDialog = true
                                            }
                                        )
                                    }
                                }
                                
                                // Mostrar edad calculada
                                if (calculatedAge != null) {
                                    Text(
                                        text = "Edad: $calculatedAge años",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = primaryColor
                                    )
                                }
                                
                                // Dropdown Género
                                ExposedDropdownMenuBox(
                                    expanded = showGeneroDropdown,
                                    onExpandedChange = { showGeneroDropdown = !showGeneroDropdown },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = genero,
                                        onValueChange = { },
                                        readOnly = true,
                                        label = { Text("Género *") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = showGeneroDropdown
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = showGeneroDropdown,
                                        onDismissRequest = { showGeneroDropdown = false }
                                    ) {
                                        generos.forEach { gen ->
                                            DropdownMenuItem(
                                                text = { Text(gen) },
                                                onClick = {
                                                    genero = gen
                                                    showGeneroDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }
                    
                    // Botón siguiente
                    Button(
                        onClick = { currentStep = 2 },
                                    enabled = isStep1Valid,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Siguiente")
                                }
                            }
                        }
                    }
                }
                
                2 -> {
                // Paso 2: Información de Contacto
                    item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                                modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                Text(
                                    text = "Información de Contacto",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )
                            
                                OutlinedTextField(
                                value = email,
                                    onValueChange = { 
                                        email = it
                                        emailError = if (it.isNotBlank() && !isValidEmail(it)) "Email inválido" else null
                                    },
                                    label = { Text("Email") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = emailError != null,
                                    supportingText = { 
                                        if (emailError != null) Text(emailError!!, color = MaterialTheme.colorScheme.error)
                                    },
                                    keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                
                                OutlinedTextField(
                                value = telefono,
                                    onValueChange = { 
                                        telefono = formatPhoneInput(it)
                                        phoneError = if (it.isNotBlank() && it.length < 10) "Teléfono debe tener al menos 10 dígitos" else null
                                    },
                                    label = { Text("Teléfono") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = phoneError != null,
                                    supportingText = { 
                                        if (phoneError != null) Text(phoneError!!, color = MaterialTheme.colorScheme.error)
                                    },
                                    keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                
                                OutlinedTextField(
                                value = direccion,
                                onValueChange = { direccion = it },
                                    label = { Text("Dirección") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                        imeAction = ImeAction.Next
                                    )
                                )
                                
                                // Dropdown Estado Civil
                                ExposedDropdownMenuBox(
                                    expanded = showEstadoCivilDropdown,
                                    onExpandedChange = { showEstadoCivilDropdown = !showEstadoCivilDropdown },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = estadoCivil,
                                        onValueChange = { },
                                        readOnly = true,
                                        label = { Text("Estado Civil *") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = showEstadoCivilDropdown
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = showEstadoCivilDropdown,
                                        onDismissRequest = { showEstadoCivilDropdown = false }
                                    ) {
                                        estadosCiviles.forEach { estado ->
                                            DropdownMenuItem(
                                                text = { Text(estado) },
                                                onClick = {
                                                    estadoCivil = estado
                                                    showEstadoCivilDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }
                                
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                                    Button(
                            onClick = { currentStep = 1 },
                            modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.outline
                                        )
                        ) {
                            Text("Anterior")
                        }
                        
                        Button(
                            onClick = { currentStep = 3 },
                                        enabled = isStep2Valid,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Siguiente")
                                    }
                                }
                            }
                        }
                    }
                }
                
                3 -> {
                    // Paso 3: Ubicación y Tipo de Usuario
                    item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                                modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                Text(
                                    text = "Ubicación y Tipo de Usuario",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor
                                )
                                
                                // País (fijo)
                                OutlinedTextField(
                                    value = pais,
                                    onValueChange = { },
                                    label = { Text("País") },
                                modifier = Modifier.fillMaxWidth(),
                                    readOnly = true
                                )
                                
                                // Dropdown Departamento
                                ExposedDropdownMenuBox(
                                    expanded = showDepartamentoDropdown,
                                    onExpandedChange = { showDepartamentoDropdown = !showDepartamentoDropdown },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = departamento,
                                        onValueChange = { },
                                        readOnly = true,
                                        label = { Text("Departamento *") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = showDepartamentoDropdown
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = showDepartamentoDropdown,
                                        onDismissRequest = { showDepartamentoDropdown = false }
                                    ) {
                                        departamentosColombia.forEach { dept ->
                                            DropdownMenuItem(
                                                text = { Text(dept) },
                                                onClick = {
                                                    departamento = dept
                                                    municipio = "" // Reset municipio when department changes
                                                    showDepartamentoDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }
                                
                                // Dropdown Municipio (dependiente del departamento)
                                ExposedDropdownMenuBox(
                                    expanded = showMunicipioDropdown,
                                    onExpandedChange = { 
                                        if (departamento.isNotBlank()) {
                                            showMunicipioDropdown = !showMunicipioDropdown 
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = municipio,
                                        onValueChange = { },
                                        readOnly = true,
                                        enabled = departamento.isNotBlank(),
                                        label = { Text("Municipio *") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = showMunicipioDropdown
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = showMunicipioDropdown,
                                        onDismissRequest = { showMunicipioDropdown = false }
                                    ) {
                                        municipiosDisponibles.forEach { mun ->
                                            DropdownMenuItem(
                                                text = { Text(mun) },
                                                onClick = {
                                                    municipio = mun
                                                    showMunicipioDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }
                                
                                // Dropdown Tipo de Usuario
                                ExposedDropdownMenuBox(
                                    expanded = showTipoUsuarioDropdown,
                                    onExpandedChange = { showTipoUsuarioDropdown = !showTipoUsuarioDropdown },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = tipoUsuario,
                                        onValueChange = { },
                                        readOnly = true,
                                        label = { Text("Tipo de Usuario *") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = showTipoUsuarioDropdown
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                    )
                                    
                                    ExposedDropdownMenu(
                                        expanded = showTipoUsuarioDropdown,
                                        onDismissRequest = { showTipoUsuarioDropdown = false }
                                    ) {
                                        tiposUsuario.forEach { tipo ->
                                            DropdownMenuItem(
                                                text = { Text(tipo) },
                                                onClick = {
                                                    tipoUsuario = tipo
                                                    showTipoUsuarioDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }
                                
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                                    Button(
                            onClick = { currentStep = 2 },
                            modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.outline
                                        )
                        ) {
                            Text("Anterior")
                        }
                        
                        Button(
                            onClick = { registrarPaciente() },
                                        enabled = isFormValid,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Registrar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // DatePicker
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Date(millis)
                            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            fechaNacimiento = format.format(date)
                            calculatedAge = calculateAge(fechaNacimiento)
                        }
                        showDatePickerDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun StepIndicator(
    step: Int,
    currentStep: Int,
    title: String,
    isCompleted: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = when {
                        step < currentStep || isCompleted -> MaterialTheme.colorScheme.primary
                        step == currentStep -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        else -> MaterialTheme.colorScheme.outline
                    },
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (step < currentStep || isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completado",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = step.toString(),
                    color = if (step == currentStep) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = if (step <= currentStep) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
} 