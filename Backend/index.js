// Backend/index.js

require('dotenv').config();
const express    = require('express');
const cors       = require('cors');
const bcrypt     = require('bcryptjs');
const jwt        = require('jsonwebtoken');
const { Pool }   = require('pg');
const https      = require('https');
const fs         = require('fs');

const app = express();
app.use(cors());
app.use(express.json());

// Configuración de base de datos usando variables de entorno
const pool = new Pool({
    host: process.env.DB_HOST || 'localhost',
    port: parseInt(process.env.DB_PORT) || 5432,
    user: process.env.DB_USER || 'admin', 
    password: process.env.DB_PASSWORD || 'camilo99',
    database: process.env.DB_DATABASE || 'telephases',
    ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false
});

const JWT_SECRET = process.env.JWT_SECRET || 'development_jwt_secret_change_in_production';

// Ruta de prueba
app.get('/', (req, res) => {
  res.json({ 
    message: 'API Telephases funcionando', 
    version: '1.0.1',
    endpoints: [
      'POST /api/auth/login',
      'POST /api/auth/register', 
      'GET /api/examenes',
      'POST /api/examenes',
      'GET /api/examenes/ultimos',
      'POST /api/patients/sync',
      'POST /api/users/bulk-sync',
      'GET /api/users/changes',
      'PUT /api/users/:id/update'
    ]
  });
});

// Genera JWT con userId y email
function generateToken(user) {
  return jwt.sign(
    { userId: user.id, email: user.email, rolId: user.rol_id },
    JWT_SECRET,
    { expiresIn: '2h' }
  );
}

// Registro de usuario - Rutas compatibles con Android
app.post('/api/auth/register', async (req, res) => {
  const {
    username,
    email,
    password,
    primer_nombre,
    segundo_nombre,
    primer_apellido,
    segundo_apellido,
    tipo_documento_id,
    numero_documento,
    telefono,
    direccion,
    ciudad_id,
    fecha_nacimiento,
    genero,
    rol_id,
    entidad_salud_id
  } = req.body;

  // Validación mínima
  if (!username || !email || !password
      || !primer_nombre || !primer_apellido
      || !tipo_documento_id || !numero_documento
      || !primer_apellido || !rol_id) {
    return res.status(400).json({ error: 'Faltan campos obligatorios' });
  }

  try {
    // 1) Hash de contraseña
    const hash = await bcrypt.hash(password, 10);

    // 2) UPSERT SQL (INSERT o UPDATE si ya existe)
    // IMPORTANTE: Si el usuario existe, NO sobreescribir rol_id, password_hash ni entidad_salud_id
    const upsertSQL = `
      INSERT INTO public.usuario
        (username, email, password_hash,
         primer_nombre, segundo_nombre,
         primer_apellido, segundo_apellido,
         tipo_documento_id, numero_documento,
         telefono, direccion, ciudad_id,
         fecha_nacimiento, genero, rol_id, entidad_salud_id)
      VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16)
      ON CONFLICT (email) DO UPDATE SET
        username = EXCLUDED.username,
        primer_nombre = EXCLUDED.primer_nombre,
        segundo_nombre = EXCLUDED.segundo_nombre,
        primer_apellido = EXCLUDED.primer_apellido,
        segundo_apellido = EXCLUDED.segundo_apellido,
        tipo_documento_id = EXCLUDED.tipo_documento_id,
        numero_documento = EXCLUDED.numero_documento,
        telefono = EXCLUDED.telefono,
        direccion = EXCLUDED.direccion,
        ciudad_id = EXCLUDED.ciudad_id,
        fecha_nacimiento = EXCLUDED.fecha_nacimiento,
        genero = EXCLUDED.genero,
        rol_id = EXCLUDED.rol_id,
        entidad_salud_id = EXCLUDED.entidad_salud_id
      RETURNING id, email, rol_id
    `;
      // Validar que entidad_salud_id existe en la tabla entidades_salud
      let validatedEntidadSaludId = entidad_salud_id || null;
      if (validatedEntidadSaludId !== null) {
        try {
          const checkEntidad = await pool.query(
            'SELECT id FROM public.entidades_salud WHERE id = $1',
            [validatedEntidadSaludId]
          );
          if (checkEntidad.rows.length === 0) {
            console.warn(`⚠️ entidad_salud_id ${validatedEntidadSaludId} no existe, usando NULL`);
            validatedEntidadSaludId = null;
          }
        } catch (checkErr) {
          console.error('Error validando entidad_salud_id:', checkErr);
          validatedEntidadSaludId = null;
        }
      }

      const values = [
        username, email, hash,
        primer_nombre, segundo_nombre || null,
        primer_apellido, segundo_apellido || null,
        tipo_documento_id, numero_documento,
        telefono || null, direccion || null,
        ciudad_id || null,
        fecha_nacimiento || null,
        genero || null,
        rol_id,
        validatedEntidadSaludId
      ];

    const { rows } = await pool.query(upsertSQL, values);
    const user = rows[0];

    // 3) Genera y devuelve JWT
    const token = generateToken(user);
    res.status(201).json({ token });

  } catch (err) {
    console.error('Error en /register:', err);
    if (err.code === '23505') {
      // Violación de unique (email, username o documento)
      return res.status(409).json({ error: 'Usuario o datos duplicados' });
    }
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Login de usuario
app.post('/api/auth/login', async (req, res) => {
  // Soportar TANTO credential (Android) como email (Portal Web)
  const { credential, email, password } = req.body;
  const loginIdentifier = credential || email;

  if (!loginIdentifier || !password) {
    return res.status(400).json({ error: 'Credencial/email y contraseña requeridas' });
  }

  try {
    // Buscar usuario por username o email
    const query = `
      SELECT id, username, email, password_hash, rol_id, primer_nombre, primer_apellido, activo
      FROM public.usuario
      WHERE (username = $1 OR email = $1) AND activo = TRUE
    `;
    const { rows } = await pool.query(query, [loginIdentifier]);

    if (rows.length === 0) {
      return res.status(401).json({ error: 'Credenciales inválidas' });
    }

    const user = rows[0];

    // Verificar contraseña
    const validPassword = await bcrypt.compare(password, user.password_hash);
    if (!validPassword) {
      return res.status(401).json({ error: 'Credenciales inválidas' });
    }

    // Generar token
    const token = generateToken(user);
    
    res.json({ 
      token,
      user: {
        id: user.id,
        username: user.username,
        email: user.email,
        rol_id: user.rol_id,
        primer_nombre: user.primer_nombre,
        primer_apellido: user.primer_apellido
      }
    });

  } catch (err) {
    console.error('Error en /login:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// ENDPOINTS PARA SINCRONIZACIÓN MASIVA DE USUARIOS

// Sincronización masiva de pacientes (bulk upload)
app.post('/api/patients/sync', authenticateToken, async (req, res) => {
  const { patients } = req.body;

  if (!Array.isArray(patients) || patients.length === 0) {
    return res.status(400).json({ error: 'Se requiere un array de pacientes' });
  }

  // console.log(`📤 Recibiendo sincronización de ${patients.length} pacientes...`);

  const results = {
    success: [],
    failed: [],
    updated: [],
    created: []
  };

  for (const patient of patients) {
    try {
      // Generar username a partir del email o documento
      const username = patient.email ? patient.email.split('@')[0] : `paciente_${patient.numero_documento}`;
      
      // Contraseña por defecto para pacientes sincronizados
      const defaultPassword = 'paciente123';
      const hash = await bcrypt.hash(defaultPassword, 10);

      // Validar que entidad_salud_id existe
      let validatedEntidadSaludId = patient.entidad_salud_id || null;
      if (validatedEntidadSaludId !== null) {
        try {
          const checkEntidad = await pool.query(
            'SELECT id FROM public.entidades_salud WHERE id = $1',
            [validatedEntidadSaludId]
          );
          if (checkEntidad.rows.length === 0) {
            console.warn(`⚠️ Paciente ${patient.id}: entidad_salud_id ${validatedEntidadSaludId} no existe, usando NULL`);
            validatedEntidadSaludId = null;
          }
        } catch (checkErr) {
          console.error('Error validando entidad_salud_id para paciente:', checkErr);
          validatedEntidadSaludId = null;
        }
      }

      // UPSERT del paciente como usuario con rol_id = 2 (Paciente)
      const upsertSQL = `
        INSERT INTO public.usuario
          (id, username, email, password_hash,
           primer_nombre, segundo_nombre,
           primer_apellido, segundo_apellido,
           tipo_documento_id, numero_documento,
           telefono, direccion, ciudad_id,
           fecha_nacimiento, genero, rol_id, entidad_salud_id)
        VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16,$17)
        ON CONFLICT (email) DO UPDATE SET
          primer_nombre = EXCLUDED.primer_nombre,
          segundo_nombre = EXCLUDED.segundo_nombre,
          primer_apellido = EXCLUDED.primer_apellido,
          segundo_apellido = EXCLUDED.segundo_apellido,
          tipo_documento_id = EXCLUDED.tipo_documento_id,
          numero_documento = EXCLUDED.numero_documento,
          telefono = EXCLUDED.telefono,
          direccion = EXCLUDED.direccion,
          ciudad_id = EXCLUDED.ciudad_id,
          fecha_nacimiento = EXCLUDED.fecha_nacimiento,
          genero = EXCLUDED.genero,
          rol_id = EXCLUDED.rol_id,
          entidad_salud_id = EXCLUDED.entidad_salud_id
        RETURNING id, email
      `;
      
      const values = [
        patient.id || null, // UUID del paciente (si existe)
        username,
        patient.email || `paciente_${patient.numero_documento}@temp.com`,
        hash,
        patient.primer_nombre,
        patient.segundo_nombre || null,
        patient.primer_apellido,
        patient.segundo_apellido || null,
        patient.tipo_documento_id || 1,
        patient.numero_documento,
        patient.telefono || null,
        patient.direccion || null,
        patient.ciudad_id || null,
        patient.fecha_nacimiento || null,
        patient.genero || null,
        2, // rol_id = 2 (Paciente)
        validatedEntidadSaludId || 1 // Usar validado o default a entidad 1
      ];

      const { rows } = await pool.query(upsertSQL, values);
      const syncedPatient = rows[0];

      if (syncedPatient) {
        results.success.push(patient.id);
        results.created.push(syncedPatient.id);
        // console.log(`✅ Paciente sincronizado: ${patient.email || patient.numero_documento}`);
      }

    } catch (err) {
      console.error(`❌ Error sincronizando paciente ${patient.id}:`, err.message);
      results.failed.push({
        id: patient.id,
        email: patient.email,
        error: err.message
      });
    }
  }

  // console.log(`📊 Sincronización completada: ${results.success.length} éxitos, ${results.failed.length} fallos`);

  res.json({
    message: 'Sincronización de pacientes completada',
    results: {
      total: patients.length,
      synced: results.success.length,
      failed: results.failed.length,
      created: results.created,
      errors: results.failed
    }
  });
});

// Sincronización de entidades de salud
app.post('/api/entidades/sync', authenticateToken, async (req, res) => {
  const { entidades } = req.body;

  if (!Array.isArray(entidades) || entidades.length === 0) {
    return res.status(400).json({ error: 'Se requiere un array de entidades' });
  }

  console.log(`📤 Recibiendo sincronización de ${entidades.length} entidades de salud...`);

  const results = {
    success: [],
    failed: [],
    mapping: {} // localId -> serverId
  };

  for (const entidad of entidades) {
    try {
      // Verificar si la entidad ya existe por NIT o nombre
      const checkQuery = `
        SELECT id FROM public.entidades_salud 
        WHERE nit = $1 OR nombre_entidad = $2
      `;
      const { rows: existingRows } = await pool.query(checkQuery, [
        entidad.nit,
        entidad.nombreEntidad
      ]);

      if (existingRows.length > 0) {
        // Entidad ya existe, retornar el ID del servidor
        const serverId = existingRows[0].id;
        results.success.push(entidad.localId);
        results.mapping[entidad.localId] = serverId;
        console.log(`✅ Entidad existe: ${entidad.nombreEntidad} -> Server ID: ${serverId}`);
      } else {
        // Entidad no existe, crear nueva
        const insertQuery = `
          INSERT INTO public.entidades_salud
            (nombre_entidad, nit, contacto_principal_nombre, 
             contacto_principal_email, contacto_principal_telefono, activa)
          VALUES ($1, $2, $3, $4, $5, true)
          RETURNING id
        `;
        
        const { rows } = await pool.query(insertQuery, [
          entidad.nombreEntidad,
          entidad.nit || null,
          entidad.contactoPrincipalNombre || null,
          entidad.contactoPrincipalEmail || null,
          entidad.contactoPrincipalTelefono || null
        ]);

        const serverId = rows[0].id;
        results.success.push(entidad.localId);
        results.mapping[entidad.localId] = serverId;
        console.log(`✅ Entidad creada: ${entidad.nombreEntidad} -> Server ID: ${serverId}`);
      }

    } catch (err) {
      console.error(`❌ Error sincronizando entidad ${entidad.nombreEntidad}:`, err.message);
      results.failed.push({
        localId: entidad.localId,
        nombre: entidad.nombreEntidad,
        error: err.message
      });
    }
  }

  console.log(`📊 Sincronización de entidades completada: ${results.success.length} exitosas, ${results.failed.length} fallidas`);

  res.json({
    success: results.failed.length === 0,
    total: entidades.length,
    synced: results.success.length,
    failed: results.failed.length,
    mapping: results.mapping,
    errors: results.failed
  });
});

// Sincronización masiva de usuarios (bulk upload)
app.post('/api/users/bulk-sync', authenticateToken, async (req, res) => {
  const { users } = req.body;

  if (!Array.isArray(users) || users.length === 0) {
    return res.status(400).json({ error: 'Se requiere un array de usuarios' });
  }

  // console.log(`📤 Recibiendo sincronización masiva de ${users.length} usuarios...`);

  const results = {
    success: [],
    failed: [],
    updated: [],
    created: []
  };

  for (const user of users) {
    try {
      // Verificar si el usuario ya existe por email o username
      const checkQuery = `
        SELECT id, fecha_modificacion FROM public.usuario 
        WHERE email = $1 OR username = $2
      `;
      const { rows: existingRows } = await pool.query(checkQuery, [user.email, user.username]);

      if (existingRows.length > 0) {
        // Usuario existe, actualizar
        const existingUser = existingRows[0];
        
        const updateQuery = `
          UPDATE public.usuario SET
            primer_nombre = $1,
            segundo_nombre = $2,
            primer_apellido = $3,
            segundo_apellido = $4,
            tipo_documento_id = $5,
            numero_documento = $6,
            telefono = $7,
            direccion = $8,
            ciudad_id = $9,
            fecha_nacimiento = $10,
            genero = $11,
            rol_id = $12,
            entidad_salud_id = $13,
            fecha_modificacion = NOW(),
            activo = TRUE
          WHERE id = $14
          RETURNING id
        `;
        
        await pool.query(updateQuery, [
          user.primer_nombre,
          user.segundo_nombre || null,
          user.primer_apellido,
          user.segundo_apellido || null,
          user.tipo_documento_id,
          user.numero_documento,
          user.telefono || null,
          user.direccion || null,
          user.ciudad_id || null,
          user.fecha_nacimiento || null,
          user.genero || null,
          user.rol_id,
          user.entidad_salud_id || null,
          existingUser.id
        ]);

        results.updated.push(user.email);
        results.success.push(user.email);
        // console.log(`✅ Usuario actualizado: ${user.email}`);

      } else {
        // Usuario no existe, crear nuevo
        const hash = await bcrypt.hash(user.password || 'temporal', 10);
        
        const insertQuery = `
          INSERT INTO public.usuario
            (username, email, password_hash,
             primer_nombre, segundo_nombre,
             primer_apellido, segundo_apellido,
             tipo_documento_id, numero_documento,
             telefono, direccion, ciudad_id,
             fecha_nacimiento, genero, rol_id, entidad_salud_id)
          VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16)
          RETURNING id
        `;
        
        await pool.query(insertQuery, [
          user.username,
          user.email,
          hash,
          user.primer_nombre,
          user.segundo_nombre || null,
          user.primer_apellido,
          user.segundo_apellido || null,
          user.tipo_documento_id,
          user.numero_documento,
          user.telefono || null,
          user.direccion || null,
          user.ciudad_id || null,
          user.fecha_nacimiento || null,
          user.genero || null,
          user.rol_id,
          user.entidad_salud_id || null
        ]);

        results.created.push(user.email);
        results.success.push(user.email);
        // console.log(`✅ Usuario creado: ${user.email}`);
      }

    } catch (err) {
      console.error(`❌ Error procesando ${user.email}:`, err.message);
      results.failed.push({ email: user.email, error: err.message });
    }
  }

  // console.log(`📊 Sincronización completada: ${results.success.length} exitosos, ${results.failed.length} fallidos`);

  res.json({
    success: results.failed.length === 0,
    total: users.length,
    created: results.created.length,
    updated: results.updated.length,
    failed: results.failed.length,
    details: results
  });
});

// Obtener cambios de usuarios desde un timestamp
app.get('/api/users/changes', authenticateToken, async (req, res) => {
  try {
    const { since } = req.query;
    
    let query = `
      SELECT id, username, email, primer_nombre, segundo_nombre,
             primer_apellido, segundo_apellido, tipo_documento_id,
             numero_documento, telefono, direccion, ciudad_id,
             fecha_nacimiento, genero, rol_id, entidad_salud_id,
             fecha_registro, fecha_modificacion, activo
      FROM public.usuario
      WHERE activo = TRUE
    `;
    
    const params = [];
    
    if (since) {
      query += ` AND (fecha_modificacion > $1 OR fecha_registro > $1)`;
      params.push(since);
    }
    
    query += ` ORDER BY fecha_modificacion DESC`;
    
    const { rows } = await pool.query(query, params);
    
    // No devolver password_hash
    const users = rows.map(user => {
      const { password_hash, ...userData } = user;
      return userData;
    });
    
    // console.log(`📥 Enviando ${users.length} usuarios modificados desde ${since || 'el inicio'}`);
    
    res.json({ 
      users,
      count: users.length,
      timestamp: new Date().toISOString()
    });

  } catch (err) {
    console.error('Error obteniendo cambios de usuarios:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Actualizar un usuario específico
app.put('/api/users/:id/update', authenticateToken, async (req, res) => {
  const { id } = req.params;
  const userData = req.body;

  try {
    const updateQuery = `
      UPDATE public.usuario SET
        primer_nombre = COALESCE($1, primer_nombre),
        segundo_nombre = $2,
        primer_apellido = COALESCE($3, primer_apellido),
        segundo_apellido = $4,
        telefono = $5,
        direccion = $6,
        ciudad_id = $7,
        fecha_nacimiento = $8,
        genero = $9,
        rol_id = COALESCE($10, rol_id),
        entidad_salud_id = $11,
        fecha_modificacion = NOW()
      WHERE id = $12
      RETURNING id, username, email, rol_id, fecha_modificacion
    `;

    const { rows } = await pool.query(updateQuery, [
      userData.primer_nombre,
      userData.segundo_nombre || null,
      userData.primer_apellido,
      userData.segundo_apellido || null,
      userData.telefono || null,
      userData.direccion || null,
      userData.ciudad_id || null,
      userData.fecha_nacimiento || null,
      userData.genero || null,
      userData.rol_id,
      userData.entidad_salud_id || null,
      id
    ]);

    if (rows.length === 0) {
      return res.status(404).json({ error: 'Usuario no encontrado' });
    }

    // console.log(`✅ Usuario ${id} actualizado`);
    res.json({ 
      success: true,
      user: rows[0]
    });

  } catch (err) {
    console.error('Error actualizando usuario:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Middleware para autenticación
function authenticateToken(req, res, next) {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    return res.status(401).json({ error: 'Token de acceso requerido' });
  }

  jwt.verify(token, JWT_SECRET, (err, user) => {
    if (err) {
      return res.status(403).json({ error: 'Token inválido' });
    }
    req.user = user;
    next();
  });
}

// ENDPOINTS PARA EXÁMENES

// Obtener exámenes del usuario autenticado
app.get('/api/examenes', authenticateToken, async (req, res) => {
  try {
    const { page = 1, limit = 20 } = req.query;
    const offset = (page - 1) * limit;
    
    const query = `
      SELECT e.id, e.titulo, e.valor, e.unidad, 
             e.fecha_creacion, e.fecha_modificacion, e.observaciones,
             te.nombre as tipo_examen_nombre,
             te.descripcion as tipo_examen_descripcion,
             e.datos_adicionales
      FROM examen e
      INNER JOIN tipo_examen te ON e.tipo_examen_id = te.id
      WHERE e.usuario_id = $1 AND e.activo = TRUE
      ORDER BY e.fecha_creacion DESC 
      LIMIT $2 OFFSET $3
    `;
    
    const { rows } = await pool.query(query, [req.user.userId, limit, offset]);
    res.json({ examenes: rows });
  } catch (err) {
    console.error('Error obteniendo exámenes:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Crear nuevo examen
app.post('/api/examenes', authenticateToken, async (req, res) => {
  const { tipo_examen_nombre, titulo, valor, unidad, observaciones, datos_adicionales, patientId, paciente_id } = req.body;
  
  if (!tipo_examen_nombre || !titulo || !valor) {
    return res.status(400).json({ error: 'Campos requeridos: tipo_examen_nombre, titulo, valor' });
  }
  
  try {
    // Obtener o crear el ID del tipo de examen
    let tipoExamenId;
    {
      const tipoQuery = 'SELECT id FROM tipo_examen WHERE nombre = $1';
      const { rows: tipoRows } = await pool.query(tipoQuery, [tipo_examen_nombre]);
      if (tipoRows.length === 0) {
        const insertTipo = `INSERT INTO tipo_examen (nombre, descripcion) VALUES ($1, NULL) RETURNING id`;
        const { rows: inserted } = await pool.query(insertTipo, [tipo_examen_nombre]);
        tipoExamenId = inserted[0].id;
      } else {
        tipoExamenId = tipoRows[0].id;
      }
    }
    
    // Determinar el usuario_id a usar: paciente_id o patientId si se proporciona, sino el usuario del token
    const userIdToUse = paciente_id || patientId || req.user.userId;
    
    // Insertar examen
    const query = `
      INSERT INTO examen (usuario_id, tipo_examen_id, titulo, valor, unidad, observaciones, datos_adicionales, sincronizado)
      VALUES ($1, $2, $3, $4, $5, $6, $7, TRUE)
      RETURNING id, titulo, valor, unidad, fecha_creacion, fecha_modificacion, observaciones
    `;
    
    const values = [
      userIdToUse, 
      tipoExamenId, 
      titulo, 
      valor, 
      unidad || null, 
      observaciones || null,
      datos_adicionales ? JSON.stringify(datos_adicionales) : null
    ];
    
    const { rows } = await pool.query(query, values);
    
    console.log(`✅ Examen creado: ${titulo} = ${valor} ${unidad || ''} para usuario ${userIdToUse} (${patientId ? 'paciente' : 'admin'})`);
    res.status(201).json({ 
      examen: rows[0],
      message: 'Examen guardado exitosamente'
    });
  } catch (err) {
    console.error('Error creando examen:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// =====================
// ENDPOINTS DE ESTUDIOS
// =====================

// Crea un estudio y 1..n exámenes asociados
app.post('/api/estudios', authenticateToken, async (req, res) => {
  try {
    const { paciente_id, fecha_estudio, observaciones, examenes } = req.body;

    if (!paciente_id || !Array.isArray(examenes) || examenes.length === 0) {
      return res.status(400).json({ error: 'Campos requeridos: paciente_id y examenes[ ]' });
    }

    const client = await pool.connect();
    try {
      await client.query('BEGIN');

      // 1) Crear estudio
      const insertEstudioSQL = `
        INSERT INTO estudios (paciente_id, creado_por_usuario_id, fecha_estudio)
        VALUES ($1, $2, COALESCE($3, NOW()))
        RETURNING id
      `;
      const { rows: estudioRows } = await client.query(insertEstudioSQL, [
        paciente_id,
        req.user.userId,
        fecha_estudio || null
      ]);
      const estudioId = estudioRows[0].id;

      // 2) Resolver tipos de examen (crear si no existen)
      const nombreToId = new Map();
      const uniqueNombres = [...new Set(examenes.map(e => e.tipo_examen_nombre).filter(Boolean))];
      for (const nombre of uniqueNombres) {
        const { rows } = await client.query('SELECT id FROM tipo_examen WHERE nombre = $1', [nombre]);
        if (rows.length > 0) {
          nombreToId.set(nombre, rows[0].id);
        } else {
          const { rows: ins } = await client.query(
            'INSERT INTO tipo_examen (nombre, descripcion) VALUES ($1, NULL) RETURNING id',
            [nombre]
          );
          nombreToId.set(nombre, ins[0].id);
        }
      }

      // 3) Insertar exámenes del estudio y recolectar IDs
      const insertedExamIds = [];
      for (const ex of examenes) {
        if (!ex.titulo || !ex.valor || !ex.tipo_examen_nombre) {
          await client.query('ROLLBACK');
          return res.status(400).json({ error: 'Cada examen requiere tipo_examen_nombre, titulo y valor' });
        }
        const tipoId = nombreToId.get(ex.tipo_examen_nombre);
        const insertExSQL = `
          INSERT INTO examen (estudio_id, usuario_id, tipo_examen_id, titulo, valor, unidad, observaciones, datos_adicionales, sincronizado)
          VALUES ($1, $2, $3, $4, $5, $6, $7, $8, TRUE)
          RETURNING id
        `;
        const { rows: exRows } = await client.query(insertExSQL, [
          estudioId,
          paciente_id,
          tipoId,
          ex.titulo,
          ex.valor,
          ex.unidad || null,
          ex.observaciones || null,
          ex.datos_adicionales ? JSON.stringify(ex.datos_adicionales) : null
        ]);
        insertedExamIds.push(exRows[0].id);
      }

      await client.query('COMMIT');
      return res.status(201).json({ estudio_id: estudioId, exam_ids: insertedExamIds, exams_count: insertedExamIds.length, message: 'Estudio creado con exámenes' });
    } catch (txErr) {
      await client.query('ROLLBACK');
      console.error('Error creando estudio:', txErr);
      return res.status(500).json({ error: 'Error interno del servidor' });
    } finally {
      client.release();
    }
  } catch (err) {
    console.error('Error en /api/estudios:', err);
    return res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// =====================
// ENDPOINTS DE CITAS
// =====================

// Crear una nueva cita (Admin y Médicos)
app.post('/api/citas', authenticateToken, async (req, res) => {
  if (req.user.rolId !== 1 && req.user.rolId !== 3) {
    return res.status(403).json({ error: 'Solo administradores y médicos pueden crear citas' });
  }

  const {
    paciente_id,
    estado_cita_id = 1,
    fecha_cita,
    observaciones_admin,
    observaciones_paciente,
    examenes_previstos // array de ids de tipo_examen
  } = req.body;

  if (!paciente_id || !fecha_cita) {
    return res.status(400).json({ error: 'Faltan campos obligatorios' });
  }

  // Usar siempre el usuario del token como creador de la cita
  const creatorId = req.user.userId;

  const client = await pool.connect();
  try {
    await client.query('BEGIN');

    const insertCitaSQL = `
      INSERT INTO citas (
        paciente_id, creado_por_usuario_id, estado_cita_id, fecha_cita,
        observaciones_admin, observaciones_paciente
      ) VALUES ($1,$2,$3,$4,$5,$6)
      RETURNING id, fecha_cita, estado_cita_id
    `;

    const { rows } = await client.query(insertCitaSQL, [
      paciente_id, creatorId, estado_cita_id, fecha_cita,
      observaciones_admin || null, observaciones_paciente || null
    ]);

    const cita = rows[0];

    console.log(`🔍 Creando cita ID: ${cita.id}`);
    console.log(`🔍 Exámenes previstos recibidos:`, examenes_previstos);
    
    if (Array.isArray(examenes_previstos) && examenes_previstos.length > 0) {
      console.log(`🔍 Guardando ${examenes_previstos.length} exámenes previstos...`);
      const values = [];
      const params = [];
      examenes_previstos.forEach((tipoId, idx) => {
        values.push(`($1, $${idx + 2})`);
        params.push(tipoId);
        console.log(`  - Tipo examen ID: ${tipoId}`);
      });
      
      const insertSQL = `INSERT INTO cita_examenes_previstos (cita_id, tipo_examen_id) VALUES ${values.join(',')} ON CONFLICT DO NOTHING`;
      console.log(`🔍 SQL de inserción:`, insertSQL);
      console.log(`🔍 Parámetros:`, [cita.id, ...params]);
      
      await client.query(insertSQL, [cita.id, ...params]);
      console.log(`✅ Exámenes previstos guardados exitosamente`);
    } else {
      console.log(`⚠️ No hay exámenes previstos para guardar`);
    }

    await client.query('COMMIT');
    res.status(201).json({ citaId: cita.id, message: 'Cita creada' });
  } catch (err) {
    await client.query('ROLLBACK');
    console.error('Error creando cita:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  } finally {
    client.release();
  }
});

// Listar citas con filtros opcionales (Admin y Médicos)
app.get('/api/citas', authenticateToken, async (req, res) => {
  if (req.user.rolId !== 1 && req.user.rolId !== 3) {
    return res.status(403).json({ error: 'Solo administradores y médicos pueden listar citas' });
  }
  const { fecha_desde, fecha_hasta } = req.query;
  try {
    const filters = [];
    const params = [];
    let idx = 1;
    if (fecha_desde) { filters.push(`c.fecha_cita >= $${idx++}`); params.push(fecha_desde); }
    if (fecha_hasta) { filters.push(`c.fecha_cita < $${idx++}`); params.push(fecha_hasta); }
    const where = filters.length ? `WHERE ${filters.join(' AND ')}` : '';

    const sql = `
      SELECT c.id, c.fecha_cita, c.estado_cita_id, ec.nombre AS estado_cita,
             c.paciente_id, c.creado_por_usuario_id,
             c.observaciones_admin, c.observaciones_paciente,
             u_p.primer_nombre AS paciente_nombre, u_p.numero_documento AS paciente_cedula,
             u_a.primer_nombre AS admin_nombre
      FROM citas c
      JOIN estado_cita ec ON c.estado_cita_id = ec.id
      JOIN public.usuario u_p ON c.paciente_id = u_p.id
      JOIN public.usuario u_a ON c.creado_por_usuario_id = u_a.id
      ${where}
      ORDER BY c.fecha_cita ASC
    `;
    const { rows } = await pool.query(sql, params);
    res.json({ citas: rows });
  } catch (err) {
    console.error('Error listando citas:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Citas del día para Tablet
app.get('/api/citas/hoy', authenticateToken, async (req, res) => {
  try {
    const sql = `
      SELECT c.id, c.fecha_cita, ec.nombre AS estado_cita,
             u_p.id AS paciente_id, u_p.primer_nombre AS paciente_nombre, u_p.numero_documento AS paciente_cedula
      FROM citas c
      JOIN estado_cita ec ON c.estado_cita_id = ec.id
      JOIN public.usuario u_p ON c.paciente_id = u_p.id
      WHERE DATE(c.fecha_cita AT TIME ZONE 'UTC') = DATE(NOW() AT TIME ZONE 'UTC')
      ORDER BY c.fecha_cita ASC
    `;
    const { rows } = await pool.query(sql);
    
    console.log(`🔍 Citas encontradas: ${rows.length}`);
    
    // Para cada cita, obtener los exámenes previstos
    const citasConExamenes = await Promise.all(rows.map(async (cita) => {
      console.log(`🔍 Consultando exámenes previstos para cita ID: ${cita.id}`);
      
      const examenesSQL = `
        SELECT te.id, te.nombre, te.descripcion
        FROM cita_examenes_previstos cep
        JOIN tipo_examen te ON cep.tipo_examen_id = te.id
        WHERE cep.cita_id = $1
        ORDER BY te.nombre ASC
      `;
      
      try {
        const { rows: examenesRows } = await pool.query(examenesSQL, [cita.id]);
        console.log(`  - Exámenes encontrados para cita ${cita.id}: ${examenesRows.length}`);
        
        if (examenesRows.length > 0) {
          examenesRows.forEach(ex => {
            console.log(`    * Examen ID: ${ex.id}, Nombre: ${ex.nombre}`);
          });
        }
        
        return {
          ...cita,
          examenes_previstos: examenesRows.map(ex => ({
            id: ex.id,
            nombre: ex.nombre,
            descripcion: ex.descripcion
          }))
        };
      } catch (examenesErr) {
        console.error(`❌ Error obteniendo exámenes para cita ${cita.id}:`, examenesErr);
        return {
          ...cita,
          examenes_previstos: []
        };
      }
    }));
    
    res.json({ citas: citasConExamenes });
  } catch (err) {
    console.error('Error obteniendo citas de hoy:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Obtener últimos exámenes de cada tipo
app.get('/api/examenes/ultimos', authenticateToken, async (req, res) => {
  try {
    const query = `
      SELECT DISTINCT ON (te.nombre) 
        e.id, e.titulo, e.valor, e.unidad, e.fecha_creacion, e.observaciones,
        te.nombre as tipo_examen_nombre,
        te.descripcion as tipo_examen_descripcion,
        e.datos_adicionales,
        es.codigo as estado_codigo,
        es.nombre as estado_nombre,
        es.emoji as estado_emoji,
        es.color as estado_color,
        es.descripcion as estado_descripcion,
        es.nivel_urgencia as estado_nivel_urgencia
      FROM examen e
      INNER JOIN tipo_examen te ON e.tipo_examen_id = te.id
      LEFT JOIN estado_salud es ON e.estado_salud_id = es.id
      WHERE e.usuario_id = $1 AND e.activo = TRUE
      ORDER BY te.nombre, e.fecha_creacion DESC
    `;
    
    const { rows } = await pool.query(query, [req.user.userId]);
    res.json({ examenes: rows });
  } catch (err) {
    console.error('Error obteniendo últimos exámenes:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Obtener últimos exámenes de un paciente específico
app.get('/api/examenes/paciente/:patientId', authenticateToken, async (req, res) => {
  try {
    const { patientId } = req.params;
    
    // Solo administradores y médicos pueden ver exámenes de pacientes
    if (req.user.rolId !== 1 && req.user.rolId !== 2) {
      return res.status(403).json({ error: 'Solo administradores y médicos pueden ver exámenes de pacientes' });
    }
    
    const query = `
      SELECT DISTINCT ON (te.nombre) 
        e.id, e.titulo, e.valor, e.unidad, e.fecha_creacion, e.observaciones,
        te.nombre as tipo_examen_nombre,
        te.descripcion as tipo_examen_descripcion,
        e.datos_adicionales,
        es.codigo as estado_codigo,
        es.nombre as estado_nombre,
        es.emoji as estado_emoji,
        es.color as estado_color,
        es.descripcion as estado_descripcion,
        es.nivel_urgencia as estado_nivel_urgencia
      FROM examen e
      INNER JOIN tipo_examen te ON e.tipo_examen_id = te.id
      LEFT JOIN estado_salud es ON e.estado_salud_id = es.id
      WHERE e.usuario_id = $1 AND e.activo = TRUE
      ORDER BY te.nombre, e.fecha_creacion DESC
    `;
    
    const { rows } = await pool.query(query, [patientId]);
    res.json({ examenes: rows });
  } catch (err) {
    console.error('Error obteniendo exámenes del paciente:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Obtener todos los pacientes
app.get('/api/pacientes', authenticateToken, async (req, res) => {
  try {
    // Solo administradores y médicos pueden ver pacientes
    if (req.user.rolId !== 1 && req.user.rolId !== 2 && req.user.rolId !== 3) {
      return res.status(403).json({ error: 'Solo administradores y médicos pueden ver pacientes' });
    }
    
    const query = `
      SELECT id, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido,
             numero_documento, email, telefono, fecha_nacimiento, genero,
             fecha_registro
      FROM public.usuario 
      WHERE rol_id = 2
      ORDER BY fecha_registro DESC
    `;
    
    const { rows } = await pool.query(query);
    
    const pacientes = rows.map(paciente => ({
      ...paciente,
      nombre_completo: `${paciente.primer_nombre} ${paciente.primer_apellido}`.trim()
    }));
    
    res.json({ pacientes });
  } catch (err) {
    console.error('Error obteniendo pacientes:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Buscar paciente por número de documento
app.get('/api/pacientes/buscar/:documento', authenticateToken, async (req, res) => {
  try {
    const { documento } = req.params;
    
    // Solo administradores y médicos pueden buscar pacientes
    if (req.user.rolId !== 1 && req.user.rolId !== 2) {
      return res.status(403).json({ error: 'Solo administradores y médicos pueden buscar pacientes' });
    }
    
    const query = `
      SELECT id, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido,
             numero_documento, email, telefono, fecha_nacimiento, genero
      FROM public.usuario 
      WHERE numero_documento = $1 AND rol_id = 2
    `;
    
    const { rows } = await pool.query(query, [documento]);
    
    if (rows.length === 0) {
      return res.status(404).json({ error: 'Paciente no encontrado' });
    }
    
    const paciente = rows[0];
    const nombreCompleto = `${paciente.primer_nombre} ${paciente.primer_apellido}`.trim();
    
    res.json({ 
      paciente: {
        ...paciente,
        nombre_completo: nombreCompleto
      }
    });
  } catch (err) {
    console.error('Error buscando paciente:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

// Registrar nuevo paciente (Admin y Médicos)
app.post('/api/pacientes/registrar', authenticateToken, async (req, res) => {
  // Solo administradores y médicos pueden registrar pacientes
  if (req.user.rolId !== 1 && req.user.rolId !== 2) {
    return res.status(403).json({ error: 'Solo administradores y médicos pueden registrar pacientes' });
  }
  
  const {
    primer_nombre,
    segundo_nombre,
    primer_apellido,
    segundo_apellido,
    tipo_documento_id,
    numero_documento,
    email,
    telefono,
    direccion,
    ciudad_id,
    fecha_nacimiento,
    genero
  } = req.body;

  // Validación mínima
  if (!primer_nombre || !primer_apellido || !numero_documento || !tipo_documento_id) {
    return res.status(400).json({ error: 'Faltan campos obligatorios' });
  }

  try {
    // Verificar si el paciente ya existe
    const checkQuery = 'SELECT id FROM public.usuario WHERE numero_documento = $1';
    const { rows: existingRows } = await pool.query(checkQuery, [numero_documento]);
    
    if (existingRows.length > 0) {
      return res.status(409).json({ error: 'Ya existe un paciente con este número de documento' });
    }

    // Generar username automático y contraseña temporal
    const username = `paciente_${numero_documento}`;
    const password = numero_documento; // Contraseña temporal = número de documento
    const hash = await bcrypt.hash(password, 10);

    // Insertar paciente con rol_id = 2 (paciente)
    const insertSQL = `
      INSERT INTO public.usuario
        (username, email, password_hash,
         primer_nombre, segundo_nombre,
         primer_apellido, segundo_apellido,
         tipo_documento_id, numero_documento,
         telefono, direccion, ciudad_id,
         fecha_nacimiento, genero, rol_id)
      VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,2)
      RETURNING id, primer_nombre, primer_apellido, numero_documento, email
    `;
    
    const values = [
      username, 
      email || null, 
      hash,
      primer_nombre, 
      segundo_nombre || null,
      primer_apellido, 
      segundo_apellido || null,
      tipo_documento_id, 
      numero_documento,
      telefono || null, 
      direccion || null, 
      ciudad_id || null,
      fecha_nacimiento || null, 
      genero || null
    ];

    const { rows } = await pool.query(insertSQL, values);
    const paciente = rows[0];
    
    console.log(`✅ Paciente registrado: ${paciente.primer_nombre} ${paciente.primer_apellido} (${paciente.numero_documento})`);
    
    res.status(201).json({ 
      paciente: {
        ...paciente,
        nombre_completo: `${paciente.primer_nombre} ${paciente.primer_apellido}`.trim()
      },
      message: 'Paciente registrado exitosamente'
    });
  } catch (err) {
    console.error('Error registrando paciente:', err);
    res.status(500).json({ error: 'Error interno del servidor' });
  }
});

const PORT = process.env.PORT || 3001;

// Para desarrollo: usar HTTP en lugar de HTTPS
app.listen(PORT, '0.0.0.0', () => {
  console.log(`🟢 API escuchando en http://0.0.0.0:${PORT} (accesible desde la red local)`);
  console.log(`📊 Endpoints disponibles:`);
  console.log(`   POST /api/auth/login - Iniciar sesión`);
  console.log(`   POST /api/auth/register - Registrar usuario`);
  console.log(`   GET  /api/examenes - Obtener exámenes del usuario`);
  console.log(`   POST /api/examenes - Crear nuevo examen`);
  console.log(`   GET  /api/examenes/ultimos - Últimos exámenes por tipo`);
  console.log(`   POST /api/citas - Crear cita`);
  console.log(`   GET  /api/citas - Listar citas`);
  console.log(`   GET  /api/citas/hoy - Citas del día`);
});

// ============ ENDPOINT DE LOGIN DUPLICADO ELIMINADO ============
// El endpoint principal de login ya maneja credenciales de Android Y Portal Web (línea 166)