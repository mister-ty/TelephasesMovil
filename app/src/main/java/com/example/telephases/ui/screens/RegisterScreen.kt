package com.example.telephases.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.telephases.network.RegisterRequest
import com.example.telephases.ui.viewmodel.AuthState
import com.example.telephases.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState(initial = AuthState.Idle)
    val scrollState = rememberScrollState()

    // Campos de UI con validación
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var primerNombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var primerApellido by remember { mutableStateOf("") }
    var segundoApellido by remember { mutableStateOf("") }
    var tipoDoc by remember { mutableStateOf("") }
    var numeroDoc by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var ciudadId by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var rolId by remember { mutableStateOf("2") }

    // Estado de validación
    var formHasErrors by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Función para validar el formulario
    fun validateForm(): Boolean {
        when {
            username.isBlank() -> {
                errorMessage = "El nombre de usuario es obligatorio"
                return false
            }
            email.isBlank() || !email.contains("@") -> {
                errorMessage = "Por favor ingresa un email válido"
                return false
            }
            password.length < 6 -> {
                errorMessage = "La contraseña debe tener al menos 6 caracteres"
                return false
            }
            primerNombre.isBlank() -> {
                errorMessage = "El primer nombre es obligatorio"
                return false
            }
            primerApellido.isBlank() -> {
                errorMessage = "El primer apellido es obligatorio"
                return false
            }
            tipoDoc.isBlank() || tipoDoc.toIntOrNull() == null -> {
                errorMessage = "El tipo de documento es obligatorio"
                return false
            }
            numeroDoc.isBlank() -> {
                errorMessage = "El número de documento es obligatorio"
                return false
            }
            fechaNacimiento.isNotBlank() && !fechaNacimiento.matches("""\d{4}-\d{2}-\d{2}""".toRegex()) -> {
                errorMessage = "El formato de fecha debe ser YYYY-MM-DD"
                return false
            }
            else -> return true
        }
    }

    fun submit() {
        formHasErrors = !validateForm()
        if (!formHasErrors) {
            viewModel.register(
                RegisterRequest(
                    username = username.trim(),
                    email = email.trim(),
                    password = password,
                    primer_nombre = primerNombre.trim(),
                    segundo_nombre = segundoNombre.trim().ifBlank { null },
                    primer_apellido = primerApellido.trim(),
                    segundo_apellido = segundoApellido.trim().ifBlank { null },
                    tipo_documento_id = tipoDoc.toIntOrNull() ?: 0,
                    numero_documento = numeroDoc.trim(),
                    telefono = telefono.trim().ifBlank { null },
                    direccion = direccion.trim().ifBlank { null },
                    ciudad_id = ciudadId.toIntOrNull(),
                    fecha_nacimiento = fechaNacimiento.trim().ifBlank { null },
                    genero = genero.trim().ifBlank { null },
                    rol_id = rolId.toInt()
                )
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sección de información de cuenta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Información de cuenta",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        singleLine = true,
                        isError = formHasErrors && username.isBlank(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        isError = formHasErrors && (email.isBlank() || !email.contains("@")),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        isError = formHasErrors && password.length < 6,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Sección de datos personales
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Datos personales",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = primerNombre,
                            onValueChange = { primerNombre = it },
                            label = { Text("Primer nombre") },
                            singleLine = true,
                            isError = formHasErrors && primerNombre.isBlank(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        )

                        OutlinedTextField(
                            value = segundoNombre,
                            onValueChange = { segundoNombre = it },
                            label = { Text("Segundo nombre") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = primerApellido,
                            onValueChange = { primerApellido = it },
                            label = { Text("Primer apellido") },
                            singleLine = true,
                            isError = formHasErrors && primerApellido.isBlank(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        )

                        OutlinedTextField(
                            value = segundoApellido,
                            onValueChange = { segundoApellido = it },
                            label = { Text("Segundo apellido") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    // Documento
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = tipoDoc,
                            onValueChange = { tipoDoc = it },
                            label = { Text("Tipo doc.") },
                            singleLine = true,
                            isError = formHasErrors && (tipoDoc.isBlank() || tipoDoc.toIntOrNull() == null),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(0.4f)
                                .padding(end = 4.dp)
                        )

                        OutlinedTextField(
                            value = numeroDoc,
                            onValueChange = { numeroDoc = it },
                            label = { Text("Número documento") },
                            singleLine = true,
                            isError = formHasErrors && numeroDoc.isBlank(),
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(start = 4.dp)
                        )
                    }
                }
            }

            // Sección de datos adicionales
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Datos adicionales",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = ciudadId,
                        onValueChange = { ciudadId = it },
                        label = { Text("Ciudad (ID)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = fechaNacimiento,
                        onValueChange = { fechaNacimiento = it },
                        label = { Text("Fecha nacimiento (YYYY-MM-DD)") },
                        singleLine = true,
                        isError = formHasErrors && fechaNacimiento.isNotBlank() &&
                                !fechaNacimiento.matches("""\d{4}-\d{2}-\d{2}""".toRegex()),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = genero,
                        onValueChange = { genero = it },
                        label = { Text("Género") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Mensajes de error y estado
            if (formHasErrors) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            when (state) {
                AuthState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is AuthState.Error -> {
                    Text(
                        text = (state as AuthState.Error).msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                is AuthState.Success -> {
                    LaunchedEffect(state) {
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                }
                else -> { /* Nada que mostrar */ }
            }

            // Botón de registro
            Button(
                onClick = { submit() },
                enabled = state !is AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Registrarme",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Botón para cancelar/volver
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Cancelar")
            }
        }
    }
}