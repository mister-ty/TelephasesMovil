-- Script para actualizar usuarios existentes con tokens válidos
-- Este script convierte los usuarios plantilla en usuarios funcionales

-- Actualizar todos los usuarios existentes con tokens válidos
UPDATE users 
SET 
    token_actual = 'OFFLINE_TOKEN_' || id,
    fecha_expiracion_token = 'NEVER_EXPIRES',
    ultimo_login = datetime('now'),
    sincronizado = 1,
    fecha_ultima_sincronizacion = datetime('now'),
    modificado_localmente = 0,
    fecha_modificacion_local = NULL
WHERE 
    activo = 1 
    AND (token_actual IS NULL OR token_actual = '');

-- Verificar que se actualizaron correctamente
SELECT 
    id, username, email, token_actual, fecha_expiracion_token, 
    activo, ultimo_login, sincronizado
FROM users 
WHERE activo = 1
ORDER BY ultimo_login DESC
LIMIT 10;
