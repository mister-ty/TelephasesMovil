# 🏥 Telephases - Sistema de Telemonitoreo Médico

> Sistema integral de telemonitoreo médico con tecnología Bluetooth Low Energy (BLE) para el seguimiento de signos vitales en tiempo real.

[![Android](https://img.shields.io/badge/Android-7.0+-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple.svg)](https://kotlinlang.org/)
[![Node.js](https://img.shields.io/badge/Node.js-18+-green.svg)](https://nodejs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)](LICENSE)

---

## 📋 Descripción

**Telephases** es una solución completa de telemonitoreo médico que integra dispositivos médicos especializados mediante Bluetooth Low Energy (BLE) para realizar exámenes de salud en tiempo real. El sistema está diseñado para entidades de salud (EPS, IPS, ARL) que necesitan monitorear pacientes de forma remota con precisión hospitalaria.

### 🎯 Características Principales

- 📱 **Aplicación móvil Android** con interfaz intuitiva
- 🔗 **Conectividad BLE** con dispositivos médicos certificados
- 📊 **Portal web administrativo** para gestión de pacientes y exámenes
- 🔄 **Sincronización en tiempo real** con servidor en la nube
- 🏥 **Multi-entidad** - Soporte para múltiples instituciones de salud
- 📈 **Análisis y reportes** de datos médicos históricos
- 🔐 **Seguridad de grado médico** con encriptación y autenticación

---

## 🩺 Tipos de Exámenes Soportados

| Tipo | Dispositivo | Mediciones | Unidades |
|------|-------------|------------|----------|
| 📊 **Presión Arterial** | Tensiómetro BLE | Sistólica/Diastólica, Pulso | mmHg, bpm |
| 🌡️ **Temperatura** | Termómetro IR | Temperatura corporal | °C |
| 🍭 **Glucosa** | Glucómetro BLE | Glucemia capilar | mg/dL |
| ⚖️ **Peso/Bioimpedancia** | Báscula BLE | Peso, IMC, % Grasa, Masa muscular | kg, kg/m² |
| 💓 **Oximetría** | Oxímetro BLE | SpO₂, Frecuencia cardíaca, PI | %, bpm |

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                    APLICACIÓN ANDROID                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Glucosa  │  │ Presión  │  │   Peso   │  │ Oxímetro │   │
│  │   BLE    │  │  Arterial│  │   BLE    │  │   BLE    │   │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └────┬─────┘   │
│       └─────────────┴─────────────┴─────────────┘          │
│                      BLE Manager                             │
│                           │                                  │
└───────────────────────────┼──────────────────────────────────┘
                            │ HTTPS/REST
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    BACKEND API (Node.js)                     │
│  ┌────────────────┐  ┌────────────────┐  ┌──────────────┐  │
│  │  Autenticación │  │   Exámenes     │  │   Usuarios   │  │
│  │   JWT/Roles    │  │  CRUD + Sync   │  │  Multi-Rol   │  │
│  └────────────────┘  └────────────────┘  └──────────────┘  │
│                           │                                  │
└───────────────────────────┼──────────────────────────────────┘
                            │ SQL
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  BASE DE DATOS PostgreSQL                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Usuarios │  │ Exámenes │  │ Entidades│  │  Rangos  │   │
│  │  + Roles │  │+ Estados │  │  Salud   │  │Referencia│   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ▲
                            │ Web UI
┌─────────────────────────────────────────────────────────────┐
│              PORTAL WEB ADMINISTRATIVO (React)               │
│  Dashboard │ Pacientes │ Exámenes │ Reportes │ Configuración│
└─────────────────────────────────────────────────────────────┘
```

---

## 🚀 Tecnologías Utilizadas

### 📱 **Aplicación Móvil**
- **Lenguaje:** Kotlin
- **Framework:** Jetpack Compose (UI declarativa)
- **Arquitectura:** MVVM + Clean Architecture
- **Bluetooth:** Nordic Semiconductor BLE Library
- **Base de datos local:** Room Database
- **DI:** Hilt (Dagger)
- **Networking:** Retrofit + OkHttp
- **Coroutines:** Para operaciones asíncronas

### 🖥️ **Backend API**
- **Runtime:** Node.js 18+
- **Framework:** Express.js
- **Base de datos:** PostgreSQL 17
- **Autenticación:** JWT (JSON Web Tokens)
- **Seguridad:** bcrypt, helmet, cors
- **ORM:** node-postgres (pg)

### 🌐 **Portal Web (Opcional)**
- **Framework:** React.js
- **UI:** Material-UI
- **Estado:** Redux Toolkit
- **Build:** Webpack/Vite

---

## 📦 Instalación y Configuración

### 📋 **Requisitos Previos**

- **Android Studio** Hedgehog o superior
- **JDK** 17+
- **Node.js** 18+ y npm
- **PostgreSQL** 17+
- **Git**

### 🔧 **Configuración del Backend**

1. **Clonar el repositorio:**
```bash
git clone https://github.com/tu-usuario/telephases.git
cd telephases
```

2. **Configurar base de datos PostgreSQL:**
```sql
CREATE DATABASE telephases;
CREATE USER admin WITH PASSWORD 'tu_password';
GRANT ALL PRIVILEGES ON DATABASE telephases TO admin;
```

3. **Configurar variables de entorno:**
```bash
cd Backend
cp .env.example .env
nano .env
```

Contenido de `.env`:
```env
DB_HOST=localhost
DB_PORT=5432
DB_USERNAME=admin
DB_PASSWORD=tu_password
DB_NAME=telephases
DATABASE_URL=postgresql://admin:tu_password@localhost:5432/telephases
JWT_SECRET=tu_secret_super_seguro_aqui
PORT=3001
NODE_ENV=development
```

4. **Instalar dependencias y ejecutar:**
```bash
npm install
npm run migrate  # Ejecutar migraciones de base de datos
npm start        # Iniciar servidor
```

El servidor estará disponible en `http://localhost:3001`

### 📱 **Configuración de la App Android**

1. **Abrir el proyecto en Android Studio:**
```bash
cd telephases
# Abrir Android Studio y seleccionar el directorio del proyecto
```

2. **Configurar `local.properties`:**
```properties
sdk.dir=C\:\\Users\\TuUsuario\\AppData\\Local\\Android\\Sdk
```

3. **Configurar URL del servidor:**

En modo **Debug** (desarrollo):
- Edita `app/src/main/java/com/example/telephases/network/ApiConfig.kt`
- La URL por defecto es tu IP local: `http://192.168.1.X:3001/`

En modo **Release** (producción):
- La URL apunta automáticamente a tu servidor VPS

4. **Compilar y ejecutar:**
```bash
./gradlew assembleDebug    # Para desarrollo
./gradlew assembleRelease  # Para producción
```

O desde Android Studio: `Run > Run 'app'`

---

## 🔌 Dispositivos BLE Compatibles

| Fabricante | Modelo | Tipo | Protocolo BLE |
|------------|--------|------|---------------|
| GlucoLeader | Enhance 2 | Glucómetro | Glucose Profile (GLP) |
| Generic | BLE BP Monitor | Tensiómetro | Blood Pressure Profile (BLP) |
| Generic | BLE Oximeter | Oxímetro | Health Device Profile (HDP) |
| Generic | BLE Scale | Báscula | Weight Scale Profile (WSP) |
| Generic | IR Thermometer | Termómetro | Health Thermometer Profile (HTP) |

### 🔗 **Proceso de Conexión BLE**

1. **Escaneo:** La app busca dispositivos BLE cercanos
2. **Emparejamiento:** El usuario selecciona el dispositivo
3. **Autenticación:** Algunos dispositivos requieren PIN
4. **Conexión:** Se establece la conexión BLE
5. **Descubrimiento:** Se identifican los servicios y características
6. **Medición:** Se realiza el examen automáticamente
7. **Sincronización:** Los datos se envían al servidor

---

## 👥 Sistema de Roles y Permisos

### 🔐 **Roles Disponibles**

| Rol | ID | Descripción | Permisos |
|-----|-----|-------------|----------|
| 🔧 **Super Admin** | 1 | Administrador del sistema | Acceso total |
| 👨‍⚕️ **Admin Entidad** | 2 | Administrador de entidad de salud | Gestión de pacientes de su entidad |
| 👤 **Paciente** | 3 | Usuario final | Ver sus propios exámenes |

### 🏥 **Entidades de Salud Soportadas**

El sistema soporta **49 entidades de salud** del sistema colombiano:
- 10 EPS (Entidades Promotoras de Salud)
- 5 ARL (Administradoras de Riesgos Laborales)
- 6 SOAT (Seguros Obligatorios)
- 4 Entidades Gubernamentales
- 6 IPS (Instituciones Prestadoras de Salud)
- 18 Otras entidades

---

## 🔒 Seguridad y Privacidad

### 🛡️ **Medidas de Seguridad Implementadas**

- ✅ **Encriptación de contraseñas** con bcrypt (10 rounds)
- ✅ **Autenticación JWT** con tokens de expiración
- ✅ **HTTPS/TLS** para comunicaciones
- ✅ **SQL Injection protection** con consultas parametrizadas
- ✅ **CORS configurado** para dominios autorizados
- ✅ **Rate limiting** para prevenir ataques de fuerza bruta
- ✅ **Validación de datos** en frontend y backend
- ✅ **Logs de auditoría** para trazabilidad
- ✅ **Base de datos encriptada** en Android (SQLCipher)

### 📜 **Cumplimiento Normativo**

- ✅ **HIPAA Compliant** - Protección de datos de salud
- ✅ **Ley 1581 de 2012** - Protección de datos personales (Colombia)
- ✅ **ISO 27001** - Gestión de seguridad de la información
- ✅ **Decreto 1377 de 2013** - Habeas Data

---

## 📊 API Endpoints

### 🔐 **Autenticación**

```http
POST /api/auth/register
POST /api/auth/login
POST /api/auth/logout
POST /api/auth/refresh-token
```

### 👥 **Usuarios**

```http
GET    /api/usuarios
GET    /api/usuarios/:id
POST   /api/usuarios
PUT    /api/usuarios/:id
DELETE /api/usuarios/:id
GET    /api/usuarios/entidad/:entidadId
```

### 📊 **Exámenes**

```http
GET    /api/examenes
GET    /api/examenes/:id
POST   /api/examenes
PUT    /api/examenes/:id
DELETE /api/examenes/:id
GET    /api/examenes/usuario/:userId
GET    /api/examenes/ultimos/:userId
GET    /api/examenes/tipo/:tipoId
```

### 🏥 **Entidades de Salud**

```http
GET    /api/entidades
GET    /api/entidades/:id
POST   /api/entidades
PUT    /api/entidades/:id
```

### 📈 **Estadísticas**

```http
GET    /api/estadisticas/usuario/:userId
GET    /api/estadisticas/entidad/:entidadId
GET    /api/estadisticas/examen/:tipoId
```

**Documentación completa:** Ver Postman Collection en `/docs/api/`

---

## 🗄️ Estructura de la Base de Datos

### 📋 **Tablas Principales**

```sql
usuario
├── id (UUID)
├── username (VARCHAR)
├── email (VARCHAR)
├── password_hash (VARCHAR)
├── rol_id (INTEGER)
├── entidad_salud_id (INTEGER)
└── fecha_registro (TIMESTAMP)

examen
├── id (SERIAL)
├── usuario_id (UUID FK)
├── tipo_examen_id (INTEGER FK)
├── valor (VARCHAR)
├── unidad (VARCHAR)
├── estado_salud_id (INTEGER FK)
├── datos_adicionales (JSONB)
└── fecha_creacion (TIMESTAMP)

tipo_examen
├── id (SERIAL)
├── nombre (VARCHAR)
├── descripcion (TEXT)
└── unidad (VARCHAR)

estado_salud
├── id (SERIAL)
├── codigo (VARCHAR)
├── nombre (VARCHAR)
├── emoji (VARCHAR)
├── color (VARCHAR)
└── nivel_urgencia (INTEGER)

rango_referencia
├── id (SERIAL)
├── tipo_examen_id (INTEGER FK)
├── estado_salud_id (INTEGER FK)
├── valor_minimo (DECIMAL)
├── valor_maximo (DECIMAL)
└── unidad (VARCHAR)
```

---

## 🧪 Pruebas

### 🔬 **Backend**
```bash
cd Backend
npm test              # Ejecutar todas las pruebas
npm run test:unit     # Solo pruebas unitarias
npm run test:integration  # Pruebas de integración
npm run test:coverage # Generar reporte de cobertura
```

### 📱 **Android**
```bash
./gradlew test                    # Pruebas unitarias
./gradlew connectedAndroidTest    # Pruebas instrumentadas
./gradlew jacocoTestReport        # Reporte de cobertura
```

---

## 🚀 Deployment

### 🖥️ **Servidor (VPS/Cloud)**

**Requisitos:**
- Ubuntu 20.04+ / Debian 11+
- 2 GB RAM mínimo
- 20 GB disco
- PostgreSQL 13+
- Node.js 18+
- Nginx (opcional, como proxy reverso)

**Instalación:**
```bash
# Instalar dependencias
sudo apt update && sudo apt install -y postgresql nodejs npm

# Clonar repositorio
git clone https://github.com/tu-usuario/telephases.git
cd telephases/Backend

# Configurar base de datos
sudo -u postgres createdb telephases
sudo -u postgres createuser admin

# Instalar dependencias
npm install --production

# Ejecutar con PM2
npm install -g pm2
pm2 start index.js --name telephases-api
pm2 startup
pm2 save

# Configurar firewall
sudo ufw allow 3001/tcp
```

### 📱 **App Android (Google Play Store)**

1. **Generar keystore:**
```bash
keytool -genkey -v -keystore telephases.jks -keyalg RSA -keysize 2048 -validity 10000 -alias telephases
```

2. **Configurar `keystore.properties`:**
```properties
storePassword=tu_password
keyPassword=tu_password
keyAlias=telephases
storeFile=telephases.jks
```

3. **Compilar release:**
```bash
./gradlew assembleRelease
```

4. **Firmar APK:**
```bash
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore telephases.jks app/build/outputs/apk/release/app-release-unsigned.apk telephases
```

5. **Optimizar con zipalign:**
```bash
zipalign -v 4 app-release-unsigned.apk telephases.apk
```

---

## 📁 Estructura del Proyecto

```
telephases/
├── app/                          # Aplicación Android
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/telephases/
│   │   │   │   ├── data/        # Capa de datos
│   │   │   │   ├── domain/      # Lógica de negocio
│   │   │   │   ├── ui/          # Interfaces de usuario
│   │   │   │   ├── network/     # API y networking
│   │   │   │   └── bluetooth/   # Managers BLE
│   │   │   └── res/             # Recursos
│   │   └── test/                # Tests
│   └── build.gradle.kts
├── Backend/                      # API REST
│   ├── src/
│   │   ├── controllers/         # Controladores
│   │   ├── models/              # Modelos de datos
│   │   ├── routes/              # Rutas API
│   │   ├── middlewares/         # Middlewares
│   │   └── config/              # Configuración
│   ├── index.js                 # Entry point
│   └── package.json
├── docs/                         # Documentación
│   ├── api/                     # Postman collections
│   ├── diagrams/                # Diagramas de arquitectura
│   └── manual-usuario.pdf       # Manual de usuario
├── scripts/                      # Scripts de utilidad
├── .gitignore
└── README.md
```

---

## 🐛 Solución de Problemas

### ❌ **Problema: No se conecta el dispositivo BLE**

**Solución:**
1. Verificar que Bluetooth esté activado
2. Verificar permisos de ubicación (Android 10+)
3. Reiniciar el dispositivo BLE
4. Verificar que la batería del dispositivo esté cargada
5. Acercarse más al dispositivo (< 5 metros)

### ❌ **Problema: Error de sincronización con servidor**

**Solución:**
1. Verificar conexión a Internet
2. Verificar que el servidor esté activo
3. Verificar que el token JWT no haya expirado
4. Revisar logs en: `Backend/logs/error.log`

### ❌ **Problema: La app se cierra al tomar un examen**

**Solución:**
1. Verificar permisos de la app
2. Limpiar caché: `Settings > Apps > Telephases > Clear Cache`
3. Verificar logs en Android Studio Logcat
4. Reinstalar la aplicación

---

## 📚 Documentación Adicional

- 📖 [Manual de Usuario](docs/manual-usuario.pdf)
- 🔧 [Guía de Desarrollo](docs/desarrollo.md)
- 🏥 [Protocolo Médico](docs/protocolo-medico.md)
- 🔐 [Política de Seguridad](docs/seguridad.md)
- 📊 [API Documentation](docs/api/README.md)

---

## 🤝 Contribución

Este es un proyecto propietario. Las contribuciones externas no están permitidas actualmente.

---

## 📞 Soporte y Contacto

### 🆘 **Soporte Técnico**
- **Email:** soporte@telephases.com
- **WhatsApp:** +57 300 123 4567
- **Horario:** Lunes a Viernes 8:00 AM - 6:00 PM (GMT-5)

### 🏥 **Emergencias Médicas**
> ⚠️ **Importante:** Esta aplicación es para monitoreo, NO reemplaza atención médica urgente. En emergencias, contacte servicios de salud inmediatamente: **123** (Colombia)

---

## 📄 Licencia

© 2025 Telephases. Todos los derechos reservados.

Este software es propietario y está protegido por derechos de autor. El uso no autorizado, reproducción o distribución está estrictamente prohibido.

---

## ✨ Créditos

Desarrollado con ❤️ para mejorar el acceso a servicios de salud de calidad.

**Equipo de Desarrollo:**
- Arquitectura del Sistema-Cam
- Desarrollo Android-Cam
- Desarrollo Backend-Jose/Cam
- Integración BLE-Cam
- UI/UX Design-Jose

---

## 🔄 Changelog

### Version 2.0.0 (2025-12-01)
- ✅ Implementación completa del sistema multi-entidad
- ✅ Portal web administrativo
- ✅ Sincronización en tiempo real
- ✅ Soporte para 5 tipos de dispositivos BLE
- ✅ Sistema de estados de salud automático
- ✅ Mejoras de seguridad y encriptación

### Version 1.0.0 (2025-06-01)
- 🎉 Lanzamiento inicial
- ✅ App Android con soporte para glucómetros
- ✅ Backend REST API
- ✅ Base de datos PostgreSQL

---

<div align="center">

**🏥 Telephases - Salud al alcance de tu mano**

[🌐 Sitio Web](https://telephases.com) • [📱 Google Play](https://play.google.com/store) • [📧 Contacto](mailto:contacto@telephases.com)

</div>
