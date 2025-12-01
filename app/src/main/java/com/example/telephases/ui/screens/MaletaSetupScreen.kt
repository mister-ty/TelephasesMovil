package com.example.telephases.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.telephases.ui.viewmodel.MaletaViewModel
import com.example.telephases.ui.viewmodel.AuthViewModel
import com.example.telephases.ui.viewmodel.AuthState
import com.example.telephases.ui.viewmodel.AuthUiState
import kotlinx.coroutines.launch
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaletaSetupScreen(
    navController: NavHostController,
    maletaViewModel: MaletaViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onSetupComplete: (() -> Unit)? = null
) {
    val turquoiseColor = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()
    
    // Observar estados
    val maletaUiState by maletaViewModel.uiState.collectAsState()
    val authUiState by authViewModel.uiState.collectAsState(
        initial = AuthUiState(
            authState = AuthState.Idle,
            currentUser = null,
            isOnline = false,
            hasValidSession = false
        )
    )
    
    // Estados del formulario
    var nombreMaleta by remember { mutableStateOf("") }
    var identificadorInvima by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var currentStep by remember { mutableStateOf(1) }
    
    // Snackbar para mensajes
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Validaciones
    val isStep1Valid = nombreMaleta.isNotBlank() && identificadorInvima.isNotBlank()
    val isFormValid = isStep1Valid
    
    // Función para configurar maleta
    fun configurarMaleta() {
        if (!isFormValid) {
            scope.launch {
                snackbarHostState.showSnackbar("Por favor complete todos los campos obligatorios")
            }
            return
        }
        
        maletaViewModel.configurarMaleta(
            nombreMaleta = nombreMaleta.trim(),
            identificadorInvima = identificadorInvima.trim(),
            descripcion = descripcion.trim().takeIf { it.isNotBlank() }
        )
    }
    
    // Efectos para manejar estados
    LaunchedEffect(maletaUiState.error) {
        maletaUiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            maletaViewModel.clearError()
        }
    }
    
    LaunchedEffect(maletaUiState.hasMaletaConfigured) {
        if (maletaUiState.hasMaletaConfigured && !maletaUiState.isLoading) {
            showSuccess = true
            // Delay para mostrar éxito antes de navegar
            kotlinx.coroutines.delay(2000)
            onSetupComplete?.invoke()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuración de Maleta",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = turquoiseColor
                )
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
            if (!showSuccess) {
                // Header con información del usuario
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🎒 Configuración Inicial",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = turquoiseColor
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Configure su maleta médica para comenzar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Información del usuario actual
                        authUiState.currentUser?.let { user ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Usuario",
                                    tint = turquoiseColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Admin: ${user.username}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            user.entidadSaludId?.let { entidadId ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Business,
                                        contentDescription = "Entidad",
                                        tint = turquoiseColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Entidad ID: $entidadId",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
                }
                
                // Formulario de configuración
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = "Maleta",
                                    tint = turquoiseColor,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Información de la Maleta",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = turquoiseColor
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            // Nombre de la maleta
                            OutlinedTextField(
                                value = nombreMaleta,
                                onValueChange = { nombreMaleta = it },
                                label = { Text("Nombre de la Maleta *") },
                                placeholder = { Text("Ej: Maleta Principal - Sede Central") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Identificador INVIMA
                            OutlinedTextField(
                                value = identificadorInvima,
                                onValueChange = { identificadorInvima = it },
                                label = { Text("Identificador INVIMA *") },
                                placeholder = { Text("Ej: INV-2024-001") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Descripción
                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                label = { Text("Descripción (Opcional)") },
                                placeholder = { Text("Descripción adicional de la maleta") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3,
                                shape = RoundedCornerShape(8.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Texto informativo
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "💡 Esta configuración se realizará una sola vez. La maleta quedará asociada a su usuario y entidad de salud.",
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botón de configurar
                    Button(
                        onClick = { configurarMaleta() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(12.dp),
                        enabled = isFormValid && !maletaUiState.isLoading
                    ) {
                        if (maletaUiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Configurando...")
                        } else {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Configurar",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Configurar Maleta",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            } else {
                // Pantalla de éxito
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Éxito",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(80.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "🎉 ¡Configuración Completada!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Su maleta \"$nombreMaleta\" ha sido configurada exitosamente.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF2E7D32),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Identificador: $identificadorInvima",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF2E7D32),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Redirigiendo al sistema...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                }
            }
        }
    }
}
