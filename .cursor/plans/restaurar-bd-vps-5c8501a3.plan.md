<!-- 5c8501a3-44df-419c-8d97-f8828230260c 00e25cd7-7905-4e37-b27b-62db04a206da -->
# Plan: Restaurar Base de Datos en VPS y Reclonar Portal

## 1. Backup de la base de datos actual del VPS

Conectarse al VPS y crear un backup completo de la base de datos actual:

```bash
ssh -p 22 ares@198.46.189.221
PGPASSWORD=camilo99 pg_dump -h localhost -U admin -d telephases > ~/telephases_backup_current_$(date +%Y%m%d_%H%M%S).sql
exit
```

Descargar el backup al equipo local:

```powershell
scp -P 22 ares@198.46.189.221:~/telephases_backup_current_*.sql C:\Users\CamiloRv\AndroidStudioProjects\telephases2\
```

## 2. Subir el nuevo archivo SQL al VPS

Transferir el archivo de restauración al VPS:

```powershell
scp -P 22 C:\Users\CamiloRv\Downloads\telephases_backup_17102025.sql ares@198.46.189.221:~/
```

## 3. Eliminar y restaurar la base de datos en el VPS

Conectarse al VPS y ejecutar:

```bash
ssh -p 22 ares@198.46.189.221

# Detener el servicio PM2
pm2 stop telephases-api

# Eliminar la base de datos actual
PGPASSWORD=camilo99 psql -h localhost -U admin -d postgres -c "DROP DATABASE IF EXISTS telephases;"

# Recrear la base de datos
PGPASSWORD=camilo99 psql -h localhost -U admin -d postgres -c "CREATE DATABASE telephases OWNER admin;"

# Restaurar desde el backup
PGPASSWORD=camilo99 psql -h localhost -U admin -d telephases -f ~/telephases_backup_17102025.sql

# Reiniciar el servicio
pm2 restart telephases-api

exit
```

## 4. Eliminar carpeta telephases-produccion local

```powershell
cd C:\Users\CamiloRv\AndroidStudioProjects\telephases2
Remove-Item -Recurse -Force telephases-produccion
```

## 5. Clonar repositorio desde GitHub

```powershell
git clone https://github.com/JoseVeraTRX/telephases-produccion.git
```

## 6. Instrucciones para restaurar el backup creado

Para restaurar el backup que creamos en el paso 1:

```bash
# Desde el equipo local, subir el backup al VPS
scp -P 22 C:\Users\CamiloRv\AndroidStudioProjects\telephases2\telephases_backup_current_*.sql ares@198.46.189.221:~/

# Conectarse al VPS
ssh -p 22 ares@198.46.189.221

# Detener servicio
pm2 stop telephases-api

# Eliminar BD actual
PGPASSWORD=camilo99 psql -h localhost -U admin -d postgres -c "DROP DATABASE IF EXISTS telephases;"

# Recrear BD
PGPASSWORD=camilo99 psql -h localhost -U admin -d postgres -c "CREATE DATABASE telephases OWNER admin;"

# Restaurar backup
PGPASSWORD=camilo99 psql -h localhost -U admin -d telephases -f ~/telephases_backup_current_YYYYMMDD_HHMMSS.sql

# Reiniciar servicio
pm2 restart telephases-api
```

**Nota**: La contraseña del VPS es `yeti22` cuando se solicite durante las conexiones SSH/SCP.

### To-dos

- [ ] Crear backup de la base de datos actual del VPS y descargarla localmente
- [ ] Subir el archivo telephases_backup_17102025.sql al VPS
- [ ] Eliminar BD actual y restaurar desde telephases_backup_17102025.sql
- [ ] Eliminar carpeta telephases-produccion local
- [ ] Clonar repositorio telephases-produccion desde GitHub