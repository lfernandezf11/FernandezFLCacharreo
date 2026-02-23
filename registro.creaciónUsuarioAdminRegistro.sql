-- Desde el usuario raíz, creamos el usuario administrador, y le garantizamos todos los permisos sobre la BD registro.
CREATE USER adminRegistro@localhost IDENTIFIED BY 'Java*2025';

GRANT ALL PRIVILEGES ON registro.* TO adminRegistro@localhost;

SHOW GRANTS FOR adminRegistro@localhost;

-- Puesto que adminRegistro no posee el privilegio adicional de otorgar permisos a otros usuarios, 
-- lo hacemos también desde aquí:
GRANT SELECT, INSERT, UPDATE ON registro.usuarios TO java2026@localhost;