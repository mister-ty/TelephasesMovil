# 🤝 Guía de Contribución

## 📋 Sobre este Proyecto

**Telephases** es actualmente un proyecto propietario desarrollado para entidades de salud en Colombia. Las contribuciones externas no están abiertas al público en este momento.

## 👥 Equipo Interno

Si eres parte del equipo de desarrollo, sigue estas pautas:

### 🔧 Configuración del Entorno

1. **Clona el repositorio:**
```bash
git clone https://github.com/tu-org/telephases.git
cd telephases
```

2. **Configura tu entorno local:**
- Sigue las instrucciones en [README.md](README.md)
- Configura las variables de entorno
- Instala todas las dependencias

### 📝 Convenciones de Código

#### **Commits**
Usa [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: agregar soporte para nuevo dispositivo BLE
fix: corregir error de sincronización
docs: actualizar documentación de API
style: formatear código según estándar
refactor: reorganizar estructura de managers BLE
test: agregar tests para autenticación
chore: actualizar dependencias
```

#### **Branches**
- `main` - Rama principal (producción)
- `develop` - Rama de desarrollo
- `feature/nombre-feature` - Nuevas funcionalidades
- `fix/nombre-bug` - Correcciones de bugs
- `hotfix/nombre-hotfix` - Correcciones urgentes

#### **Pull Requests**
1. Crea una rama desde `develop`
2. Implementa tus cambios
3. Escribe tests
4. Actualiza documentación
5. Crea PR hacia `develop`
6. Solicita code review

### 🧪 Testing

**Backend:**
```bash
cd Backend
npm test
npm run test:coverage
```

**Android:**
```bash
./gradlew test
./gradlew connectedAndroidTest
```

### 📚 Documentación

- Documenta todas las funciones públicas
- Actualiza README.md si cambias funcionalidades
- Agrega ejemplos de uso
- Documenta endpoints nuevos

### 🔒 Seguridad

- **NO** subas credenciales al repositorio
- **NO** subas archivos `.env`
- Usa `.env.example` como referencia
- Reporta vulnerabilidades a: security@telephases.com

### 🐛 Reportar Bugs

Si encuentras un bug:
1. Verifica que no esté reportado
2. Crea un issue detallado
3. Incluye pasos para reproducir
4. Adjunta logs si es posible

## 📞 Contacto

Para consultas sobre contribución:
- Email: dev@telephases.com
- Slack: #telephases-dev

## 📄 Licencia

Este proyecto es propietario. Ver [LICENSE](LICENSE) para más detalles.

