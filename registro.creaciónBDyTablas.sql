-- Desde adminRegistro, creamos la BD registro:
CREATE DATABASE registro CHARSET utf8 COLLATE utf8_spanish_ci;

-- Creamos la tabla usuarios:
CREATE TABLE usuarios (
    idUsuario SMALLINT(6) NOT NULL AUTO_INCREMENT, -- Primary key: idUsuario autoincremental
    nombre VARCHAR(15) NOT NULL,
    apellidos VARCHAR(30) NOT NULL,
    genero CHAR(1) NOT NULL, -- H, M, O (Hombre, Mujer, Otro).
    username VARCHAR(20) NOT NULL, -- Nombre de usuario único
    password VARCHAR(100) NOT NULL, -- La encriptaremos con MD5.
    ultimoAcceso DATETIME,
    rol VARCHAR(6) NOT NULL DEFAULT 'normal', -- rol alternativo: 'admin', que insertaremos desde el Workbench.
    
    CONSTRAINT pk_usuarios PRIMARY KEY (idUsuario),
    CONSTRAINT uq_usuarios_username UNIQUE (username),
    CONSTRAINT chk_usuarios_genero CHECK (genero IN ('M', 'H', 'O')) 
);

DESC usuarios;

-- Creamos la tabla auditoria:
CREATE TABLE auditoria (
	id SMALLINT(6) NOT NULL AUTO_INCREMENT, -- Primary key: id autoincremental
    idUsuario SMALLINT(6) NOT NULL, -- Foreign key: referencia la tabla usuarios
    fechaEntrada DATE NOT NULL,
    horaEntrada TIME NOT NULL,
    horaSalida TIME,
    
    CONSTRAINT pk_auditoria PRIMARY KEY (id),
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario)
		ON DELETE CASCADE -- Asegura que cuando se borra un registro de usuarios, los accesos asociados al mismo también se borran
);

DESC auditoria;

-- Insertamos a mano el usuario administrador
INSERT INTO usuarios (
    nombre, 
    apellidos, 
    genero, 
    username, 
    password, 
    rol, 
    ultimoAcceso
) VALUES (
    'Administrador', 
    'Sistema', 
    'O', 
    'admin', 
    MD5('admin'), 
    'admin', 
    NOW()
);

DELETE FROM usuarios 
WHERE username = 'usuarioPrueba';

SELECT username, password, HEX(password) as md5_hex 
FROM usuarios WHERE username = 'admin';

SELECT * FROM usuarios;

-- usuario de prueba
INSERT INTO usuarios (
    idUsuario,
    nombre,
    apellidos,
    genero,
    username,
    password,
    ultimoAcceso,
    rol
) VALUES (
    NULL,                 -- AUTO_INCREMENT
    'Usuario',
    'Prueba', 
    'M',                  -- CHAR(1): M (Mujer)
    'user',
    MD5('user'),     -- password: Java*2026
    NULL,
    'normal'              -- DEFAULT pero lo especificamos
);

select * from usuarios;


