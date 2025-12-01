package com.example.telephases.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.telephases.R

@Composable
fun WelcomeScreen(
    navController: NavHostController
) {
    // Log para debug
    LaunchedEffect(Unit) {
        Log.d("WelcomeScreen", "WelcomeScreen iniciada")
    }
    
    // Color principal de la aplicación
    val turquoiseColor = MaterialTheme.colorScheme.primary
    
    // Animaciones
    val infiniteTransition = rememberInfiniteTransition()
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        turquoiseColor.copy(alpha = 0.05f),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo y título con animación
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000)) + scaleIn(animationSpec = tween(1000))
            ) {
                Box(
                    modifier = Modifier
                        .scale(logoScale)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(50.dp),
                            spotColor = turquoiseColor.copy(alpha = 0.3f)
                        )
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White,
                                    turquoiseColor.copy(alpha = 0.1f)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
            Image(
                        painter = painterResource(id = R.drawable.ic_tele_vertical),
                contentDescription = "Logo TeleMonitoreo",
                modifier = Modifier.size(100.dp)
            )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título principal con animación
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 200)) + slideInVertically(animationSpec = tween(1000, delayMillis = 200))
            ) {
                Text(
                    text = "TeleMonitoreo",
                    style = MaterialTheme.typography.headlineLarge,
                    color = turquoiseColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo con animación
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 400)) + slideInVertically(animationSpec = tween(1000, delayMillis = 400))
            ) {
            Text(
                text = "Monitoreo de Salud a Distancia",
                style = MaterialTheme.typography.bodyLarge,
                    color = turquoiseColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Tarjeta de bienvenida con animación
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 600)) + slideInVertically(animationSpec = tween(1000, delayMillis = 600))
            ) {
            Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = turquoiseColor.copy(alpha = 0.2f)
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                    modifier = Modifier
                        .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White,
                                        turquoiseColor.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                                text = "Bienvenido a TeleMonitoreo INTELICARE",
                                style = MaterialTheme.typography.titleLarge,
                        color = turquoiseColor,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                            Spacer(modifier = Modifier.height(12.dp))

                    Text(
                                text = "Plataforma líder en telemedicina y monitoreo remoto de pacientes en Colombia. Solución integral para profesionales de la salud que permite realizar seguimiento médico a distancia con tecnología de vanguardia.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Sección de funcionalidades principales
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 800))
            ) {
                Column {
                    Text(
                        text = "Soluciones INTELICARE",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = turquoiseColor,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    // Gestión de Pacientes - DETALLADA
                    DetailedFeatureCard(
                        icon = Icons.Filled.PersonAdd,
                        title = "Gestión Integral de Pacientes",
                        description = "Sistema robusto conforme a normativas colombianas de salud (Resolución 3100/2019)",
                        features = listOf(
                            "Registro completo con validación RIPS (Registro Individual de Prestación de Servicios)",
                            "Tipos de identificación colombianos (CC, TI, CE, RC, Pasaporte)",
                            "Vinculación automática con EPS, ARS y entidades de salud autorizadas",
                            "Búsqueda inteligente por documento, nombre o número de historia clínica",
                            "Historia clínica digital cumpliendo Resolución 1995/1999"
                        ),
                        color = turquoiseColor,
                        delayMillis = 900
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Exámenes Médicos - DETALLADA
                    DetailedFeatureCard(
                        icon = Icons.Filled.MedicalServices,
                        title = "Telemonitoreo Clínico Avanzado",
                        description = "Dispositivos médicos certificados INVIMA para monitoreo remoto profesional",
                        features = listOf(
                            "Termometría digital infrarroja sin contacto (FDA/CE aprobado)",
                            "Tensiómetros automáticos con validación ESH/BHS para hipertensión", 
                            "Pulsioximetría SpO2 para monitoreo respiratorio y COVID-19",
                            "Glucómetros con conectividad para diabetes mellitus",
                            "Básculas biomédicas con análisis de composición corporal",
                            "Transmisión automática a plataforma médica en tiempo real"
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        delayMillis = 1100
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Gestión de Maletas - NUEVA
                    DetailedFeatureCard(
                        icon = Icons.Filled.Inventory,
                        title = "Kits Médicos Móviles INTELICARE",
                        description = "Maletas médicas certificadas para atención domiciliaria y rural",
                        features = listOf(
                            "Kits certificados INVIMA para telemedicina ambulatoria",
                            "Dispositivos IoT integrados con conectividad 4G/WiFi",
                            "Asignación por médico especialista o auxiliar de enfermería",
                            "Trazabilidad completa vinculada a IPS y entidades territoriales",
                            "Mantenimiento preventivo según protocolos técnicos",
                            "Inventario digital con geolocalización GPS en tiempo real"
                        ),
                        color = MaterialTheme.colorScheme.tertiary,
                        delayMillis = 1300
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Historial y Reportes - DETALLADA
                    DetailedFeatureCard(
                        icon = Icons.Filled.Analytics,
                        title = "Reportes y Análisis Médico",
                        description = "Sistema de reportes clínicos y análisis de datos para seguimiento médico",
                        features = listOf(
                            "Análisis longitudinal de signos vitales y tendencias",
                            "Reportes automatizados para auditorías médicas y entes de control",
                            "Timeline clínico interactivo con marcadores de eventos críticos",
                            "Alertas tempranas para detección de deterioro del paciente",
                            "Exportación a formatos HL7 FHIR para interoperabilidad",
                            "Integración con SISPRO y sistemas nacionales de salud"
                        ),
                        color = Color(0xFF9C27B0),
                        delayMillis = 1500
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Conectividad y Sincronización - NUEVA
                    DetailedFeatureCard(
                        icon = Icons.Filled.CloudSync,
                        title = "Infraestructura Cloud y Ciberseguridad",
                        description = "Plataforma en nube con estándares internacionales de seguridad médica",
                        features = listOf(
                            "Arquitectura cloud híbrida con disponibilidad 99.9% SLA",
                            "Conectividad multi-protocolo: Bluetooth LE, WiFi, 4G, LoRaWAN",
                            "Encriptación AES-256 conforme a HIPAA y Ley 1581/2012 de datos",
                            "Backup automático en AWS con redundancia geográfica",
                            "Apps nativas iOS/Android con sincronización offline-first",
                            "APIs REST con autenticación OAuth 2.0 para integración hospitalaria"
                        ),
                        color = Color(0xFF00BCD4),
                        delayMillis = 1700
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de iniciar sesión con animación
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 1200)) + scaleIn(animationSpec = tween(1000, delayMillis = 1200))
            ) {
            Button(
                onClick = { 
                    Log.d("WelcomeScreen", "Botón login presionado - navegando a login")
                    navController.navigate("login") 
                },
                modifier = Modifier
                    .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(28.dp),
                            spotColor = turquoiseColor.copy(alpha = 0.3f)
                        ),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = turquoiseColor)
            ) {
                Text(
                    text = "Iniciar Sesión (Admin)",
                    fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            
            // Sección de información adicional
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 1900))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = turquoiseColor.copy(alpha = 0.2f)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = turquoiseColor.copy(alpha = 0.05f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Security,
                            contentDescription = "Seguridad",
                            tint = turquoiseColor,
                            modifier = Modifier.size(32.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Certificaciones y Cumplimiento Normativo",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = turquoiseColor,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "INTELICARE TeleMonitoreo cuenta con certificaciones internacionales ISO 27001, ISO 13485 y cumple íntegramente con la normativa colombiana de salud digital (Resolución 2654/2019), INVIMA y protección de datos personales, garantizando la máxima seguridad y confidencialidad en el manejo de información clínica.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DetailedFeatureCard(
    icon: ImageVector,
    title: String,
    description: String,
    features: List<String>,
    color: Color,
    delayMillis: Int
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(1000, delayMillis = delayMillis)) + 
                slideInVertically(animationSpec = tween(1000, delayMillis = delayMillis))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = color.copy(alpha = 0.3f)
                ),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
) {
    Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header con icono y título
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
        Box(
            modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        color.copy(alpha = 0.1f),
                                        color.copy(alpha = 0.2f)
                                    )
                                )
                            ),
            contentAlignment = Alignment.Center
        ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = color,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = title,
                            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
                            color = color
        )

        Text(
            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            lineHeight = 20.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Lista de características
                features.forEach { feature ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = feature,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    if (feature != features.last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

