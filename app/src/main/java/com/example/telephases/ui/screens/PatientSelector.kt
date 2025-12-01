package com.example.telephases.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientSelector(
    navController: NavHostController
) {
    val turquoiseColor = MaterialTheme.colorScheme.primary
    
    // Estado para la búsqueda
    var searchQuery by remember { mutableStateOf("") }
    
    // Lista de pacientes de ejemplo (en el futuro vendrá de la base de datos)
    val patients = remember {
        listOf(
            Patient("1", "Juan Pérez", "juan.perez@email.com", "1234567890"),
            Patient("2", "María García", "maria.garcia@email.com", "0987654321"),
            Patient("3", "Carlos López", "carlos.lopez@email.com", "1122334455"),
            Patient("4", "Ana Rodríguez", "ana.rodriguez@email.com", "5566778899"),
            Patient("5", "Luis Martínez", "luis.martinez@email.com", "9988776655")
        )
    }
    
    // Filtrar pacientes basado en la búsqueda
    val filteredPatients = patients.filter { patient ->
        patient.name.contains(searchQuery, ignoreCase = true) ||
        patient.email.contains(searchQuery, ignoreCase = true) ||
        patient.phone.contains(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Seleccionar Paciente",
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
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Campo de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar paciente...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de pacientes
            if (filteredPatients.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No se encontraron pacientes",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Intente con otro término de búsqueda",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredPatients) { patient ->
                        PatientCard(
                            patient = patient,
                            onClick = {
                                // Navegar a la pantalla de exámenes con el paciente seleccionado
                                navController.navigate("home?patientId=${patient.id}&patientName=${patient.name}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PatientCard(
    patient: Patient,
    onClick: () -> Unit
) {
    val turquoiseColor = MaterialTheme.colorScheme.primary
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = turquoiseColor
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = patient.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Tel: ${patient.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Tocar para realizar examen →",
                    style = MaterialTheme.typography.bodySmall,
                    color = turquoiseColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Data class para representar un paciente
data class Patient(
    val id: String,
    val name: String,
    val email: String,
    val phone: String
) 