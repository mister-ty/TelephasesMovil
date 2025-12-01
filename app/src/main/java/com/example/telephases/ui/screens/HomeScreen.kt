package com.example.telephases.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telephases.ui.components.HealthIndicatorCard
import com.example.telephases.ui.components.HealthSummaryCard
import com.example.telephases.R
import com.example.telephases.ui.viewmodel.AuthViewModel
import com.example.telephases.ui.viewmodel.AuthState
import com.example.telephases.ui.viewmodel.ExamViewModel
import com.example.telephases.data.local.entities.ExamEntity
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel

// Modelo de datos para los exámenes
data class ExamResult(
    val id: String,
    val type: ExamType,
    val title: String,
    val value: String,
    val unit: String,
    val timestamp: String,
    val lastModified: String,
    val icon: ImageVector
)

enum class ExamType {
    BLOOD_PRESSURE,    // Presión Arterial
    HEART_RATE,        // Frecuencia Cardíaca  
    TEMPERATURE,       // Temperatura
    GLUCOSE,           // Glucosa
    WEIGHT,            // Peso/Báscula
    OXYGEN_SATURATION  // Saturación de Oxígeno
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    patientId: String? = null,
    patientName: String? = null,
    onLogoutClick: () -> Unit = {
        navController.navigate("admin_dashboard") {
            popUpTo("home") { inclusive = true }
        }
    }
) {
    var selectedBottomNavItem by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    
    // ViewModels
    val examViewModel: ExamViewModel = hiltViewModel()
    
    // Estados desde ViewModels
    val examUiState by examViewModel.uiState.collectAsState()
    val allExams by examViewModel.exams.collectAsState()
    val patientExams by examViewModel.patientExams.collectAsState()
    
    // Estado local para exámenes procesados
    var examResults by remember { mutableStateOf<List<ExamResult>>(emptyList()) }
    
    // Animaciones
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAnimation by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // Función para obtener el ícono según el tipo de examen
    fun getExamIcon(tipo: String): ImageVector {
        return when (tipo.uppercase()) {
            "BLOOD_PRESSURE" -> Icons.Outlined.MonitorHeart
            "TEMPERATURE" -> Icons.Outlined.Thermostat
            "GLUCOSE" -> Icons.Outlined.WaterDrop
            "OXYGEN_SATURATION" -> Icons.Outlined.FavoriteBorder
            "WEIGHT" -> Icons.Outlined.Scale
            else -> Icons.Outlined.Assignment
        }
    }
    
    // Función para obtener el tipo de examen
    fun mapExamType(tipo: String): ExamType {
        return when (tipo.uppercase()) {
            "BLOOD_PRESSURE" -> ExamType.BLOOD_PRESSURE
            "TEMPERATURE" -> ExamType.TEMPERATURE
            "GLUCOSE" -> ExamType.GLUCOSE
            "OXYGEN_SATURATION" -> ExamType.OXYGEN_SATURATION
            "WEIGHT" -> ExamType.WEIGHT
            else -> ExamType.HEART_RATE
        }
    }
    
    // Función para cargar los últimos exámenes (offline-first)
    fun loadLatestExams() {
        Log.d("HomeScreen", "🔄 Iniciando carga de exámenes offline-first")
        
        // Obtener token del usuario autenticado
        val authState = authViewModel.state.value
        if (authState is AuthState.Success) {
            val token = authState.token
            
            if (patientId != null) {
                Log.d("HomeScreen", "🔍 Cargando exámenes del paciente: $patientId")
                examViewModel.loadPatientExams(patientId)
            } else {
                Log.d("HomeScreen", "🔍 Cargando exámenes del admin")
                examViewModel.loadRecentExams()
            }
        } else {
            Log.e("HomeScreen", "❌ Usuario no autenticado")
        }
    }
    
    // Procesar exámenes cuando cambien
    LaunchedEffect(allExams, patientExams, patientId) {
        val examsToProcess = if (patientId != null) patientExams else allExams
        Log.d("HomeScreen", "🔄 Procesando ${examsToProcess.size} exámenes ${if (patientId != null) "del paciente" else "del admin"}")
        
        examResults = examsToProcess.map { examEntity ->
            ExamResult(
                id = examEntity.id,
                type = mapExamType(examEntity.tipoExamenNombre),
                title = examEntity.titulo,
                value = examEntity.valor,
                unit = examEntity.unidad ?: "",
                timestamp = examEntity.fechaCreacion,
                lastModified = examEntity.fechaModificacion ?: examEntity.fechaCreacion,
                icon = getExamIcon(examEntity.tipoExamenNombre)
            )
        }
        
        Log.d("HomeScreen", "✅ Procesados ${examResults.size} exámenes")
    }
    
    // Función para sincronizar exámenes
    fun sincronizarExamenes() {
        Log.d("HomeScreen", "🔄 Iniciando sincronización manual de exámenes")
        examViewModel.syncExamsManually()
    }
    
    // Cargar exámenes al inicio
    LaunchedEffect(Unit) {
        loadLatestExams()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        // Logo de intelicare responsive con animación
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(800)) + expandVertically(animationSpec = tween(800))
                        ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_tele_horizontal),
                            contentDescription = "Logo TeleMonitoreo",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(200.dp),
                            contentScale = ContentScale.Fit
                        )
                        }
                        
                        // Mostrar información del paciente si está disponible
                        AnimatedVisibility(
                            visible = patientName != null,
                            enter = fadeIn(animationSpec = tween(600, delayMillis = 400)) + slideInHorizontally(animationSpec = tween(600, delayMillis = 400))
                        ) {
                        patientName?.let { name ->
                            Text(
                                text = "Paciente: $name",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                ),
                actions = {
                    // Botón de sincronización con animación
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) + scaleIn(animationSpec = tween(800, delayMillis = 200))
                    ) {
                    IconButton(
                        onClick = { sincronizarExamenes() },
                        enabled = !examUiState.isSyncing
                    ) {
                        if (examUiState.isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Sincronizar Exámenes",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    // Botón de logout con animación
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(800, delayMillis = 400)) + scaleIn(animationSpec = tween(800, delayMillis = 400))
                    ) {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Volver al panel",
                            tint = Color.Gray
                        )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Casa") },
                    label = { Text("Casa") },
                    selected = selectedBottomNavItem == 0,
                    onClick = { selectedBottomNavItem = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Assignment, contentDescription = "Exámenes") },
                    label = { Text("Exámenes") },
                    selected = selectedBottomNavItem == 1,
                    onClick = { 
                        selectedBottomNavItem = 1
                        val route = if (patientId != null) "medical_exam?patientId=$patientId" else "medical_exam"
                        navController.navigate(route)
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedBottomNavItem == 2,
                    onClick = { 
                        selectedBottomNavItem = 2
                        if (patientId != null) {
                            navController.navigate("patient_profile/$patientId")
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            Color.White
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
                // Header con estadísticas rápidas
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 100)) + slideInVertically(animationSpec = tween(800, delayMillis = 100))
                ) {
                    QuickStatsCard(
                        examCount = examResults.size,
                        isSyncing = examUiState.isSyncing,
                        pulseAnimation = pulseAnimation
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Sección Acciones rápidas (Hero Card)
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) + slideInVertically(animationSpec = tween(800, delayMillis = 200))
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 140.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Acciones rápidas",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (patientName != null) "Gestiona los exámenes de $patientName" else "Crea, sincroniza y revisa exámenes",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Button(
                                            onClick = {
                                                val route = if (patientId != null) "medical_exam?patientId=$patientId" else "medical_exam"
                                                navController.navigate(route)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Home,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                text = "Nuevo examen",
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = { sincronizarExamenes() },
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Sync,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                text = "Sincronizar",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    // Micro-métricas
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // Total de exámenes
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.18f)),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = "Total exámenes: ${examResults.size}",
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelLarge,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }
                                        // Estado de sincronización
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.18f)),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = if (examUiState.isSyncing) "Sincronizando" else "Sincronización al día",
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelLarge,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }
                                // Icono decorativo
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(alpha = 0.18f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.MonitorHeart,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            
            Spacer(modifier = Modifier.height(32.dp))
            
                // Sección Overview con animación
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 600)) + slideInVertically(animationSpec = tween(800, delayMillis = 600))
                ) {
            Text(
                        text = "Overview de Exámenes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
                
                // Mostrar exámenes, estado de carga o error con animaciones
                AnimatedContent(
                    targetState = examUiState,
                    transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
                    }
                ) { state ->
                    when {
                        state.isLoading -> LoadingOverviewState()
                        state.error != null -> ErrorOverviewState(
                            message = state.error!!,
                            onRetry = { loadLatestExams() }
                        )
                        examResults.isEmpty() -> EmptyOverviewState()
                        else -> {
                            // Convertir examResults a ExamEntity para usar con HealthIndicator
                            val examEntities = examResults.map { examResult ->
                                ExamEntity(
                                    id = examResult.id,
                                    patientId = patientId ?: "",
                                    tipoExamenNombre = examResult.type.name,
                                    titulo = examResult.title,
                                    valor = examResult.value,
                                    unidad = examResult.unit,
                                    fechaCreacion = examResult.timestamp,
                                    fechaModificacion = examResult.lastModified,
                                    activo = true,
                                    sincronizado = true
                                )
                            }

                            // Contenedor principal con Card para darle estructura y jerarquía visual
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    LazyColumn(
                                        contentPadding = PaddingValues(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Resumen de salud general
                                        item {
                                            AnimatedVisibility(
                                                visible = true,
                                                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(animationSpec = tween(600))
                                            ) {
                                                HealthSummaryCard(
                                                    exams = examEntities,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        // Separador visual
                                        item {
                                            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                                        }

                                        // Indicadores individuales de cada examen
                                        items(examEntities.size) { index ->
                                            AnimatedVisibility(
                                                visible = true,
                                                enter = fadeIn(animationSpec = tween(600, delayMillis = (index + 1) * 100)) +
                                                        slideInHorizontally(animationSpec = tween(600, delayMillis = (index + 1) * 100))
                                            ) {
                                                HealthIndicatorCard(
                                                    exam = examEntities[index],
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .shadow(
                                                            elevation = 2.dp,
                                                            shape = RoundedCornerShape(12.dp),
                                                            ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                                            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                                        )
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
        }
    }
}

@Composable
fun QuickStatsCard(
    examCount: Int,
    isSyncing: Boolean,
    pulseAnimation: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Exámenes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$examCount",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .scale(pulseAnimation)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Assignment,
                        contentDescription = "Exámenes",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    gradient: Brush,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        enabled = !isLoading
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = text,
                    color = Color.White,  // Blanco puro para máximo contraste
                    fontWeight = FontWeight.Bold,  // Peso bold (no black) para formato normal
                    fontSize = 24.sp,  // Tamaño grande pero no extremo
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.5.sp  // Espaciado normal de letras
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EmptyOverviewState() {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                imageVector = Icons.Outlined.Assignment,
                contentDescription = "Sin exámenes",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF9E9E9E)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No hay exámenes recientes",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

                    Text(
                text = "Realiza tu primer examen para ver los resultados aquí",
                        style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
                    )
        }
    }
}

@Composable
fun ExamItem(
    title: String,
    value: String,
    lastModified: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono circular
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del examen
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = lastModified,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            // Icono de favorito/like
            IconButton(onClick = { /* TODO: Implementar favorito */ }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun LoadingOverviewState() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(12.dp)
) {
    Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 4.dp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Cargando exámenes...",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorOverviewState(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Error",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            
            Spacer(modifier = Modifier.height(16.dp))

        Text(
                text = "Error cargando exámenes",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Reintentar",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
