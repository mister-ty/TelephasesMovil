# Script para probar la API de Telephases
Write-Host "🔐 Haciendo login..."

# Login
$loginBody = @{
    username = "camilo.rendonvi@amigo.edu.co"
    password = "123456789"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "http://198.46.189.221:3001/api/auth/login" -Method POST -ContentType "application/json" -Body $loginBody
    $token = $loginResponse.token
    Write-Host "✅ Login exitoso! Token obtenido."
    
    # Crear examen de temperatura
    Write-Host "🌡️ Creando examen de temperatura..."
    
    $examBody = @{
        tipo_examen_nombre = "temperatura"
        titulo = "Temperatura Corporal"
        valor = "36.8"
        unidad = "°C"
        observaciones = "Temperatura normal, paciente en reposo"
        datos_adicionales = @{
            metodo_medicion = "timpano"
            dispositivo = "Termómetro digital"
            fecha_medicion = (Get-Date).ToString("yyyy-MM-dd HH:mm:ss")
        }
    } | ConvertTo-Json -Depth 3
    
    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }
    
    $examResponse = Invoke-RestMethod -Uri "http://198.46.189.221:3001/api/examenes" -Method POST -Headers $headers -Body $examBody
    Write-Host "✅ Examen creado exitosamente!"
    Write-Host "📊 ID: $($examResponse.examen.id)"
    Write-Host "🌡️ Valor: $($examResponse.examen.valor) $($examResponse.examen.unidad)"
    Write-Host "💬 Mensaje: $($examResponse.message)"
    
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)"
    Write-Host "Detalles: $($_.ErrorDetails.Message)"
} 