package com.example.telephases.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telephases.R
import com.example.telephases.network.Credentials
import com.example.telephases.ui.viewmodel.AuthState
import com.example.telephases.ui.viewmodel.AuthViewModel
import com.example.telephases.ui.viewmodel.MaletaViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(),
    maletaViewModel: MaletaViewModel = hiltViewModel()
) {
    // Local input state
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Snackbar host for error messages
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado de autenticación
    val currentAuthState = viewModel.state.collectAsState(initial = AuthState.Idle).value

    // React to auth state changes
    LaunchedEffect(currentAuthState) {
        when (currentAuthState) {
            AuthState.Loading -> Unit // handled in UI
            is AuthState.Error -> {
                val errorState = currentAuthState as AuthState.Error
                snackbarHostState.showSnackbar(errorState.msg)
            }
            is AuthState.Success -> {
                val token = (currentAuthState as AuthState.Success).token
                Log.d("AUTH_SUCCESS", "Token recibido: $token")
                // Forzar recarga de configuración antes de decidir navegación
                maletaViewModel.reloadMaletaConfig()
                // Verificar configuración de maleta y navegar automáticamente
                try {
                    val hasMaleta = maletaViewModel.checkMaletaSetup()
                    if (hasMaleta) {
                        navController.navigate("admin_dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("maleta_setup") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                } catch (e: Exception) {
                    // En caso de error, ir al setup como fallback
                    navController.navigate("maleta_setup") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Fondo y logo en la parte superior
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Logo de la aplicación
                Image(
                    painter = painterResource(id = R.drawable.ic_tele_vertical),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "TeleMonitoreo",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Monitoreo de Salud a Distancia",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Tarjeta de inicio de sesión
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = "Iniciar Sesión",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario o Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            visualTransformation = PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Opción para recuperar contraseña
                        Text(
                            text = "¿Olvidó su contraseña?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(vertical = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (username.isNotBlank() && password.isNotBlank()) {
                                    viewModel.login(Credentials(username.trim(), password))
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = username.isNotBlank() && password.isNotBlank() && currentAuthState != AuthState.Loading
                        ) {
                            if (currentAuthState == AuthState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(text = "Entrar")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Opción para registrarse
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes cuenta?",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    TextButton(
                        onClick = { navController.navigate("register") }
                    ) {
                        Text(text = "Regístrate", color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Mostrar indicador de carga si estamos en estado de carga
                if (currentAuthState == AuthState.Loading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }

                // Spacer para empujar el contenido del footer hacia abajo
                Spacer(modifier = Modifier.weight(1f))

                // Footer con información de la app
                Text(
                    text = "TeleMonitoreo v1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}