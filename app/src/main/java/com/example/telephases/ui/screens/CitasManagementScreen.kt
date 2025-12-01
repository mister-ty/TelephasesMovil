package com.example.telephases.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.telephases.ui.viewmodel.AuthViewModel
import com.example.telephases.ui.viewmodel.CitaViewModel
import com.example.telephases.ui.viewmodel.CitaUiState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.telephases.R
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.telephases.di.RepositoryModule
import com.example.telephases.di.DatabaseModule
import com.example.telephases.ui.viewmodel.CitaViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CitasManagementScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    
    val citaViewModel = remember {
        val database = DatabaseModule.provideAppDatabaseForFactory(context)
        val citaDao = DatabaseModule.provideCitaDao(database)
        val patientDao = DatabaseModule.providePatientDao(database)
        val syncMetadataDao = DatabaseModule.provideSyncMetadataDao(database)
        val examDao = DatabaseModule.provideExamDao(database)
        val tipoExamenDao = DatabaseModule.provideTipoExamenDao(database)
        val apiService = RepositoryModule.provideApiService()
        RepositoryModule.provideCitaViewModelForFactory(
            database, citaDao, apiService, patientDao, syncMetadataDao, examDao, tipoExamenDao, context
        )
    }
    val uiState by citaViewModel.uiState.collectAsState()
    var showCreateCitaDialog by remember { mutableStateOf(false) }
    
    // Animaciones
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
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
    
    LaunchedEffect(Unit) {
        citaViewModel.loadCitasHoy()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Icono de citas
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Citas",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        
                        Column {
                            Text(
                                text = "Gestión de Citas",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = "Telemonitoreo Inteligente",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { citaViewModel.syncCitasFromServer() }) {
                        Icon(
                            imageVector = Icons.Filled.Sync,
                            contentDescription = "Sincronizar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
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
                    .padding(16.dp)
            ) {
                // Card de sincronización mejorada
                AnimatedVisibility(
                    visible = true,
                    enter = slideIn + fadeIn
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
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
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Icono de sincronización con animación
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.radialGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                )
                                            )
                                        )
                                        .scale(pulse),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Sync,
                                        contentDescription = "Sincronización",
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Text(
                                    text = "Sincronización de Citas",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Button(
                                    onClick = { citaViewModel.syncCitasFromServer() },
                                    enabled = !uiState.isLoading,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    if (uiState.isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Filled.CloudSync,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (uiState.isLoading) "Sincronizando..." else "Sincronizar desde la Nube",
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                
                                if (uiState.lastSyncTime != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Schedule,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Última sincronización: ${uiState.lastSyncTime}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Botón para crear nueva cita mejorado
                AnimatedVisibility(
                    visible = true,
                    enter = slideIn + fadeIn
                ) {
                    Button(
                        onClick = { showCreateCitaDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        // Icono de agregar
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Crear Nueva Cita",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Título de citas del día mejorado
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
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Citas de hoy",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "Citas de Hoy",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Contador de citas
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "${uiState.citasHoy.size}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                
                // Lista de citas
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
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
                                    text = "Cargando citas...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    uiState.citasHoy.isEmpty() -> {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideIn + fadeIn
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                    modifier = Modifier.padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(
                                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.EventBusy,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Text(
                                        text = "No hay citas programadas para hoy",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = "Las citas aparecerán aquí cuando estén programadas",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        // Resumen de citas
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Citas de hoy",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${uiState.citasHoy.size} programadas",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                AssistChip(
                                    onClick = { citaViewModel.loadCitasHoy() },
                                    label = { Text("Actualizar") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Refresh,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.citasHoy) { cita ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = slideIn + fadeIn
                                ) {
                                    CitaCard(
                                        cita = cita,
                                        onCardClick = { 
                                            navController.navigate("cita_detalle/${cita.id}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Dialog de creación de cita
    if (showCreateCitaDialog) {
        CreateCitaDialog(
            onDismiss = { showCreateCitaDialog = false },
            onCitaCreated = { 
                showCreateCitaDialog = false
                // Recargar citas después de crear una nueva
                citaViewModel.loadCitasHoy()
            },
            citaViewModel = citaViewModel,
            authViewModel = authViewModel
        )
    }
}

@Composable
fun CitaCard(
    cita: com.example.telephases.network.CitaResumen,
    onCardClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .shadow(
                elevation = if (isPressed) 2.dp else 6.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .scale(scale = if (isPressed) 0.98f else 1f),
        shape = RoundedCornerShape(16.dp),
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
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header con avatar del paciente y estado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Avatar del paciente con logo de la empresa
                        Box(
                            modifier = Modifier
                                .size(50.dp)
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
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Paciente",
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = cita.paciente_nombre ?: "Paciente Desconocido",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Estado de la cita con chip mejorado
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = when (cita.estado_cita) {
                                        "Programada" -> Color(0xFFFF9800)
                                        "Confirmada" -> MaterialTheme.colorScheme.tertiary
                                        "Cancelada por Paciente", "Cancelada por Admin" -> Color(0xFFF44336)
                                        "Completada" -> MaterialTheme.colorScheme.primary
                                        else -> Color.Gray
                                    }
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when (cita.estado_cita) {
                                            "Programada" -> Icons.Filled.Schedule
                                            "Confirmada" -> Icons.Filled.CheckCircle
                                            "Cancelada por Paciente", "Cancelada por Admin" -> Icons.Filled.Cancel
                                            "Completada" -> Icons.Filled.Done
                                            else -> Icons.Filled.Info
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = cita.estado_cita ?: "Desconocido",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                    
                    // Indicador de acción (flecha)
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
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Ver detalles",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Información de la cita con iconos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Fecha y hora
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Schedule,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatDateTime(cita.fecha_cita),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Cédula del paciente
                        if (!cita.paciente_cedula.isNullOrEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Badge,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Cédula: ${cita.paciente_cedula}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    // Indicador de prioridad mejorado
                    if (cita.estado_cita == "Programada") {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFF9800)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PriorityHigh,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "PENDIENTE",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Footer con indicador de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Toca para ver detalles",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCitaDialog(
    onDismiss: () -> Unit,
    onCitaCreated: () -> Unit,
    citaViewModel: CitaViewModel,
    authViewModel: AuthViewModel
) {
    var selectedPatientId by remember { mutableStateOf("") }
    var selectedPatientName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var selectedExamenes by remember { mutableStateOf<List<Int>>(emptyList()) }
    var showPatientDropdown by remember { mutableStateOf(false) }
    var showExamenesDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // Estados para los datos
    var pacientes by remember { mutableStateOf<List<com.example.telephases.data.local.entities.PatientEntity>>(emptyList()) }
    var tiposExamen by remember { mutableStateOf<List<com.example.telephases.data.local.entities.TipoExamenEntity>>(emptyList()) }
    var currentUserId by remember { mutableStateOf<String?>(null) }
    
    // Cargar datos reales de la base de datos
    LaunchedEffect(Unit) {
        try {
            // Obtener usuario actual
            val currentUser = authViewModel.getCurrentUser()
            currentUserId = currentUser?.id
            println("🔍 Usuario actual: ${currentUser?.username} (ID: ${currentUser?.id})")
            
            if (currentUserId == null) {
                errorMessage = "No hay usuario logueado. Por favor inicia sesión primero."
                return@LaunchedEffect
            }
            
            // Cargar pacientes reales (solo los registrados en esta tablet)
            val allPatients = citaViewModel.loadPatients()
            // Filtrar solo pacientes activos y sincronizados (registrados en esta tablet)
            pacientes = allPatients.filter { patient ->
                patient.activo && patient.sincronizado
            }
            println("🔍 Pacientes registrados en esta tablet: ${pacientes.size}")
            println("🔍 Total de pacientes en BD: ${allPatients.size}")
            
            // Cargar tipos de examen reales
            tiposExamen = citaViewModel.loadExamTypes()
            println("🔍 Tipos de examen cargados: ${tiposExamen.size}")
            
            if (pacientes.isEmpty()) {
                errorMessage = "No hay pacientes registrados en esta tablet. Por favor registra un paciente primero."
            }
            
            if (tiposExamen.isEmpty()) {
                errorMessage = "No hay tipos de examen disponibles. Contacta al administrador."
            }
        } catch (e: Exception) {
            println("❌ Error cargando datos: ${e.message}")
            e.printStackTrace()
            errorMessage = "Error cargando datos: ${e.message}"
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Crear Nueva Cita",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Selección de paciente
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Paciente",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        ExposedDropdownMenuBox(
                            expanded = showPatientDropdown,
                            onExpandedChange = { showPatientDropdown = !showPatientDropdown }
                        ) {
                            OutlinedTextField(
                                value = selectedPatientName,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Seleccionar paciente") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (showPatientDropdown) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            
                            androidx.compose.material3.DropdownMenu(
                                expanded = showPatientDropdown,
                                onDismissRequest = { showPatientDropdown = false }
                            ) {
                                pacientes.forEach { paciente ->
                                    DropdownMenuItem(
                                        text = { 
                                            Text("${paciente.primerNombre} ${paciente.primerApellido} - ${paciente.numeroDocumento}")
                                        },
                                        onClick = {
                                            selectedPatientId = paciente.id
                                            selectedPatientName = "${paciente.primerNombre} ${paciente.primerApellido}"
                                            showPatientDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Fecha con DatePicker
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Fecha de la Cita",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // DatePicker con ExposedDropdownMenuBox
                            ExposedDropdownMenuBox(
                                expanded = showDatePicker,
                                onExpandedChange = { showDatePicker = !showDatePicker },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedDate,
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Seleccionar fecha") },
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
                                    placeholder = { Text("DD/MM/YYYY") }
                                )
                                
                                ExposedDropdownMenu(
                                    expanded = showDatePicker,
                                    onDismissRequest = { showDatePicker = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Seleccionar fecha") },
                                        onClick = {
                                            showDatePicker = false
                                            showDatePickerDialog = true
                                        }
                                    )
                                }
                            }
                            
                            // Botón HOY
                            Button(
                                onClick = {
                                    val today = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                    selectedDate = today.format(java.util.Date())
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "HOY",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
                
                // Hora con formato colombiano
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Hora de la Cita",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = selectedTime,
                            onValueChange = { newTime ->
                                // Validar formato de hora colombiano (HH:MM AM/PM)
                                if (newTime.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) || 
                                    newTime.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]\\s?(AM|PM|am|pm)$"))) {
                                    selectedTime = newTime
                                } else if (newTime.isEmpty()) {
                                    selectedTime = newTime
                                }
                            },
                            label = { Text("Hora (HH:MM AM/PM)") },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("02:30 PM") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Hora"
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Botones de hora rápida
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("08:00 AM", "10:00 AM", "02:00 PM", "04:00 PM").forEach { time ->
                                Button(
                                    onClick = { selectedTime = time },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = time,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Exámenes previstos
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Exámenes Previstos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        ExposedDropdownMenuBox(
                            expanded = showExamenesDropdown,
                            onExpandedChange = { showExamenesDropdown = !showExamenesDropdown }
                        ) {
                            OutlinedTextField(
                                value = if (selectedExamenes.isEmpty()) "Seleccionar exámenes" else "${selectedExamenes.size} exámenes seleccionados",
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Exámenes") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (showExamenesDropdown) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            
                            androidx.compose.material3.DropdownMenu(
                                expanded = showExamenesDropdown,
                                onDismissRequest = { showExamenesDropdown = false }
                            ) {
                                tiposExamen.forEach { tipoExamen ->
                                    DropdownMenuItem(
                                        text = { 
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = selectedExamenes.contains(tipoExamen.id),
                                                    onCheckedChange = { isChecked ->
                                                        selectedExamenes = if (isChecked) {
                                                            selectedExamenes + tipoExamen.id
                                                        } else {
                                                            selectedExamenes - tipoExamen.id
                                                        }
                                                    }
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(tipoExamen.descripcion)
                                            }
                                        },
                                        onClick = {
                                            selectedExamenes = if (selectedExamenes.contains(tipoExamen.id)) {
                                                selectedExamenes - tipoExamen.id
                                            } else {
                                                selectedExamenes + tipoExamen.id
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        
                        // Mostrar exámenes seleccionados
                        if (selectedExamenes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyColumn(
                                modifier = Modifier.height(100.dp)
                            ) {
                                items(selectedExamenes) { examenId ->
                                    val tipoExamen = tiposExamen.find { it.id == examenId }
                                    tipoExamen?.let {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            )
                                        ) {
                                            Text(
                                                text = it.descripcion,
                                                modifier = Modifier.padding(8.dp),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Observaciones
                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    label = { Text("Observaciones (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
                
                // Mensaje de error
                if (errorMessage.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = errorMessage,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedPatientId.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
                        errorMessage = "Por favor completa todos los campos obligatorios"
                        return@Button
                    }
                    
                    isLoading = true
                    errorMessage = ""
                    
                    // Crear la cita con formato correcto
                    val fechaCompleta = formatDateTimeForAPI(selectedDate, selectedTime)
                    
                    // Logging de exámenes seleccionados
                    println("🔍 Exámenes seleccionados:")
                    selectedExamenes.forEach { examenId ->
                        val tipoExamen = tiposExamen.find { it.id == examenId }
                        println("  - ID: $examenId, Nombre: ${tipoExamen?.nombre}, Descripción: ${tipoExamen?.descripcion}")
                    }
                    
                    val request = com.example.telephases.network.CitaCreateRequest(
                        paciente_id = selectedPatientId,
                        creado_por_usuario_id = currentUserId ?: "unknown-user",
                        estado_cita_id = 1, // Programada
                        fecha_cita = fechaCompleta,
                        observaciones_admin = observaciones.takeIf { it.isNotEmpty() },
                        observaciones_paciente = null,
                        examenes_previstos = selectedExamenes.takeIf { it.isNotEmpty() }
                    )
                    
                    // Llamar al ViewModel para crear la cita
                    citaViewModel.createCita(request)
                    onCitaCreated()
                },
                enabled = !isLoading && selectedPatientId.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isLoading) "Creando..." else "Crear Cita")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
    
    // DatePickerDialog
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
                            selectedDate = format.format(date)
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

// Función para formatear la fecha y hora para la API
private fun formatDateTimeForAPI(date: String, time: String): String {
    return try {
        println("🔍 Formateando fecha para API:")
        println("  - Fecha recibida: $date")
        println("  - Hora recibida: $time")
        
        // Convertir fecha de DD/MM/YYYY a YYYY-MM-DD
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parsedDate = dateFormat.parse(date)
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate!!)
        
        // Convertir hora de formato colombiano a 24 horas
        val timeIn24Hours = convertTo24HourFormat(time)
        println("  - Hora convertida a 24h: $timeIn24Hours")
        
        // Crear fecha completa en formato ISO 8601
        val isoDateTime = "${dateString}T${timeIn24Hours}:00.000Z"
        println("  - Fecha ISO final: $isoDateTime")
        
        isoDateTime
    } catch (e: Exception) {
        println("❌ Error formateando fecha: ${e.message}")
        e.printStackTrace()
        // Fallback: usar formato simple
        "$date $time"
    }
}

// Función para convertir hora de 12 horas a 24 horas
private fun convertTo24HourFormat(time12Hour: String): String {
    return try {
        println("🔍 Convirtiendo hora: $time12Hour")
        val time = time12Hour.trim().uppercase()
        val isPM = time.contains("PM")
        val isAM = time.contains("AM")
        
        println("  - Es PM: $isPM")
        println("  - Es AM: $isAM")
        
        val timeOnly = time.replace(Regex("\\s?(AM|PM)"), "")
        val parts = timeOnly.split(":")
        
        println("  - Partes: $parts")
        
        if (parts.size == 2) {
            var hour = parts[0].toInt()
            val minute = parts[1]
            
            println("  - Hora original: $hour")
            println("  - Minuto: $minute")
            
            if (isPM && hour != 12) {
                hour += 12
            } else if (isAM && hour == 12) {
                hour = 0
            }
            
            val result = String.format("%02d:%s", hour, minute)
            println("  - Resultado: $result")
            result
        } else {
            println("  - Error: formato no válido")
            time12Hour // Retornar original si no se puede parsear
        }
    } catch (e: Exception) {
        println("  - Error en conversión: ${e.message}")
        time12Hour // Retornar original si hay error
    }
}

// Función para formatear la fecha y hora
private fun formatDateTime(dateTimeString: String): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTimeString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateTimeString // Retornar el string original si falla el parsing
    }
}
