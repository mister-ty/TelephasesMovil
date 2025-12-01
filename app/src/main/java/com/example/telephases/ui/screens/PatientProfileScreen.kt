package com.example.telephases.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.telephases.R
import com.example.telephases.data.local.entities.PatientEntity
import com.example.telephases.ui.viewmodel.PatientViewModel
import android.util.Log
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfileScreen(
    navController: NavHostController,
    patientId: String,
    patientViewModel: PatientViewModel = hiltViewModel()
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var patient by remember { mutableStateOf<PatientEntity?>(null) }
    var entidadSalud by remember { mutableStateOf("") }
    
    // Estados para edición
    var primerNombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var primerApellido by remember { mutableStateOf("") }
    var segundoApellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var estadoCivil by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var tipoUsuario by remember { mutableStateOf("") }
    
    // Animaciones
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    val slideIn = slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(800, easing = EaseOutCubic)
    )
    
    val fadeIn = fadeIn(animationSpec = tween(1000, delayMillis = 200))
    
    // Cargar paciente desde la base de datos
    LaunchedEffect(patientId) {
        try {
            patient = patientViewModel.getLocalPatientById(patientId)
            if (patient != null) {
                entidadSalud = patientViewModel.getEntidadSaludName(patient?.entidadSaludId)
            }
            isLoading = false
        } catch (e: Exception) {
            Log.e("PatientProfileScreen", "Error cargando paciente: ${e.message}")
            isLoading = false
        }
    }
    
    // Inicializar campos de edición
    LaunchedEffect(patient) {
        patient?.let { p ->
            primerNombre = p.primerNombre
            segundoNombre = p.segundoNombre ?: ""
            primerApellido = p.primerApellido
            segundoApellido = p.segundoApellido ?: ""
            telefono = p.telefono ?: ""
            direccion = p.direccion ?: ""
            fechaNacimiento = p.fechaNacimiento ?: ""
            genero = p.genero ?: ""
            estadoCivil = p.estadoCivil ?: ""
            pais = p.pais ?: ""
            departamento = p.departamento ?: ""
            municipio = p.municipio ?: ""
            tipoUsuario = p.tipoUsuario ?: ""
        }
    }
    
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Cargando perfil...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }
    
    if (patient == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.error.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PersonOff,
                    contentDescription = "Paciente no encontrado",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Paciente no encontrado",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Perfil del Paciente",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            Icons.Default.Edit, 
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.03f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header con foto de perfil mejorado
                AnimatedVisibility(
                    visible = true,
                    enter = slideIn + fadeIn
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                                        )
                                    )
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Foto de perfil con animación
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                                )
                                            )
                                        )
                                        .scale(scale = pulse),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier.size(60.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(20.dp))
                                
                                // Nombre completo con estilo mejorado
                                patient?.let { p ->
                                    Text(
                                        text = "${p.primerNombre} ${p.segundoNombre ?: ""} ${p.primerApellido} ${p.segundoApellido ?: ""}".trim(),
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    // Email con icono
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "Email",
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = p.email ?: "No especificado",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    // Tipo de usuario con chip mejorado
                                    p.tipoUsuario?.let { tipo ->
                                        Card(
                                            modifier = Modifier.padding(top = 8.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Badge,
                                                    contentDescription = "Tipo de usuario",
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = tipo,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.primary,
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
                
                // Información personal con iconos y mejor diseño
                AnimatedVisibility(
                    visible = true,
                    enter = slideIn + fadeIn
                ) {
                    InfoSectionCard(
                        title = "Información Personal",
                        icon = Icons.Default.Person,
                        content = {
                            patient?.let { p ->
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.Badge,
                                    label = "Nombre Completo",
                                    value = "${p.primerNombre} ${p.segundoNombre ?: ""} ${p.primerApellido} ${p.segundoApellido ?: ""}".trim()
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.CreditCard,
                                    label = "Documento",
                                    value = "${getTipoDocumento(p.tipoDocumentoId)} ${p.numeroDocumento}"
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.Phone,
                                    label = "Teléfono",
                                    value = p.telefono ?: "No especificado"
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.LocationOn,
                                    label = "Dirección",
                                    value = p.direccion ?: "No especificada"
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.Cake,
                                    label = "Fecha de Nacimiento",
                                    value = p.fechaNacimiento?.let {
                                        PatientEntity.convertDateFormatForDisplay(it) ?: it
                                    } ?: "No especificada"
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.Person,
                                    label = "Género",
                                    value = p.genero?.let { 
                                        when (it) {
                                            "M" -> "Masculino"
                                            "F" -> "Femenino"
                                            "O" -> "Otro"
                                            else -> it
                                        }
                                    } ?: "No especificado"
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.Favorite,
                                    label = "Estado Civil",
                                    value = p.estadoCivil ?: "No especificado"
                                )
                            }
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Información de ubicación
                AnimatedVisibility(
                    visible = true,
                    enter = slideIn + fadeIn
                ) {
                    InfoSectionCard(
                        title = "Información de Ubicación",
                        icon = Icons.Default.Place,
                        content = {
                            patient?.let { p ->
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.Public,
                                    label = "País",
                                    value = p.pais ?: "No especificado"
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.LocationCity,
                                    label = "Departamento",
                                    value = p.departamento ?: "No especificado"
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.Home,
                                    label = "Municipio",
                                    value = p.municipio ?: "No especificado"
                                )
                            }
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Información del sistema
                AnimatedVisibility(
                    visible = true,
                    enter = slideIn + fadeIn
                ) {
                    InfoSectionCard(
                        title = "Información del Sistema",
                        icon = Icons.Default.Info,
                        content = {
                            patient?.let { p ->
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.CalendarToday,
                                    label = "Fecha de Registro",
                                    value = formatDate(p.fechaRegistro)
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.LocalHospital,
                                    label = "Entidad de Salud",
                                    value = entidadSalud.ifEmpty { "No especificada" }
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = if (p.sincronizado) Icons.Default.CloudDone else Icons.Default.CloudSync,
                                    label = "Estado",
                                    value = if (p.sincronizado) "Sincronizado" else "Pendiente de sincronización",
                                    valueColor = if (p.sincronizado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                                
                                PatientInfoRowWithIcon(
                                    icon = Icons.Default.Sync,
                                    label = "Última Sincronización",
                                    value = p.fechaUltimaSincronizacion?.let { formatDate(it) } ?: "Nunca"
                                )
                            }
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    
    // Dialog de edición mejorado
    if (showEditDialog) {
        EditPatientDialog(
            onDismiss = { showEditDialog = false },
            onSave = { 
                // Aquí implementarías la lógica de guardado
                showEditDialog = false
            },
            primerNombre = primerNombre,
            segundoNombre = segundoNombre,
            primerApellido = primerApellido,
            segundoApellido = segundoApellido,
            telefono = telefono,
            direccion = direccion,
            fechaNacimiento = fechaNacimiento,
            genero = genero,
            estadoCivil = estadoCivil,
            pais = pais,
            departamento = departamento,
            municipio = municipio,
            tipoUsuario = tipoUsuario,
            onPrimerNombreChange = { primerNombre = it },
            onSegundoNombreChange = { segundoNombre = it },
            onPrimerApellidoChange = { primerApellido = it },
            onSegundoApellidoChange = { segundoApellido = it },
            onTelefonoChange = { telefono = it },
            onDireccionChange = { direccion = it },
            onFechaNacimientoChange = { fechaNacimiento = it },
            onGeneroChange = { genero = it },
            onEstadoCivilChange = { estadoCivil = it },
            onPaisChange = { pais = it },
            onDepartamentoChange = { departamento = it },
            onMunicipioChange = { municipio = it },
            onTipoUsuarioChange = { tipoUsuario = it }
        )
    }
}

@Composable
private fun InfoSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header de la sección con icono
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // Contenido de la sección
            content()
        }
    }
}

@Composable
private fun PatientInfoRowWithIcon(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Contenido
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = valueColor,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun PatientInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(120.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun EditPatientDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    primerNombre: String,
    segundoNombre: String,
    primerApellido: String,
    segundoApellido: String,
    telefono: String,
    direccion: String,
    fechaNacimiento: String,
    genero: String,
    estadoCivil: String,
    pais: String,
    departamento: String,
    municipio: String,
    tipoUsuario: String,
    onPrimerNombreChange: (String) -> Unit,
    onSegundoNombreChange: (String) -> Unit,
    onPrimerApellidoChange: (String) -> Unit,
    onSegundoApellidoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onDireccionChange: (String) -> Unit,
    onFechaNacimientoChange: (String) -> Unit,
    onGeneroChange: (String) -> Unit,
    onEstadoCivilChange: (String) -> Unit,
    onPaisChange: (String) -> Unit,
    onDepartamentoChange: (String) -> Unit,
    onMunicipioChange: (String) -> Unit,
    onTipoUsuarioChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Perfil del Paciente") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = primerNombre,
                    onValueChange = onPrimerNombreChange,
                    label = { Text("Primer Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = segundoNombre,
                    onValueChange = onSegundoNombreChange,
                    label = { Text("Segundo Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = primerApellido,
                    onValueChange = onPrimerApellidoChange,
                    label = { Text("Primer Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = segundoApellido,
                    onValueChange = onSegundoApellidoChange,
                    label = { Text("Segundo Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = telefono,
                    onValueChange = onTelefonoChange,
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = direccion,
                    onValueChange = onDireccionChange,
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = onFechaNacimientoChange,
                    label = { Text("Fecha de Nacimiento (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = genero,
                    onValueChange = onGeneroChange,
                    label = { Text("Género (M/F/O)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = estadoCivil,
                    onValueChange = onEstadoCivilChange,
                    label = { Text("Estado Civil") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = pais,
                    onValueChange = onPaisChange,
                    label = { Text("País") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = departamento,
                    onValueChange = onDepartamentoChange,
                    label = { Text("Departamento") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = municipio,
                    onValueChange = onMunicipioChange,
                    label = { Text("Municipio") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = tipoUsuario,
                    onValueChange = onTipoUsuarioChange,
                    label = { Text("Tipo de Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun getTipoDocumento(tipoId: Int): String {
    return when (tipoId) {
        1 -> "CC"
        2 -> "TI"
        3 -> "CE"
        4 -> "RC"
        5 -> "Pasaporte"
        else -> "Documento"
    }
}

private fun formatDate(dateString: String): String {
    return try {
        // Primero intentar parsear como ISO datetime
        val instant = Instant.parse(dateString)
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        try {
            // Si falla, intentar parsear como fecha simple (yyyy-MM-dd)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = java.time.LocalDate.parse(dateString, formatter)
            val displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            displayFormatter.format(localDate)
        } catch (e2: Exception) {
            dateString // Retorna el original si hay error de parseo
        }
    }
}
