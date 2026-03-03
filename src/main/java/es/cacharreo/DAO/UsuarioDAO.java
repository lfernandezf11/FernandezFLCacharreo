package es.cacharreo.DAO;

import es.cacharreo.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lucía Fernández Florencio
 */
public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public String addUsuario(Usuario usuario) {
        String resultado = "ok";
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet rs = null; // Necesario para asignar el id al objeto que entra por parámetro cuando se recupera, y así añadir la imagen en el controlador.

        // Campos: email, password, nombre, apellidos, nif, telefono, direccion, codigoPostal, localidad, provincia
        String sql = "INSERT INTO usuarios (email, password, nombre, apellidos, nif, "
                + "telefono, direccion, codigo_postal, localidad, provincia) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection(); // O getConnectionNorm() según tu clase
            connection.setAutoCommit(false);

            // Return_generated_keys devuelve las claves generadas en esta inserción concreta
            preparada = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparada.setString(1, usuario.getEmail());
            preparada.setString(2, usuario.getPassword());// Viene encriptada del controller
            preparada.setString(3, usuario.getNombre());
            preparada.setString(4, usuario.getApellidos());
            preparada.setString(5, usuario.getNif());

            // Manejo de teléfono como campo nullable.
            if (usuario.getTelefono() == null || "".equals(usuario.getTelefono())) {
                preparada.setNull(6, java.sql.Types.CHAR);
            } else {
                preparada.setString(6, usuario.getTelefono());
            }

            preparada.setString(7, usuario.getDireccion());
            preparada.setString(8, usuario.getCodigoPostal());
            preparada.setString(9, usuario.getLocalidad());
            preparada.setString(10, usuario.getProvincia());

            int filas = preparada.executeUpdate();

            if (filas > 0) {
                rs = preparada.getGeneratedKeys(); //ID generado
                if (rs.next()) {
                    // Seteamos el ID al objeto. Como el objeto está referenciado, el controller verá este cambio.
                    usuario.setIdUsuario(rs.getShort(1));
                }
            }
            connection.commit();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                // MySQL error 1062: Duplicate entry
                if (e.getMessage().toLowerCase().contains("email")) {
                    resultado = "El correo electrónico ya está registrado.";
                } else if (e.getMessage().toLowerCase().contains("nif")) {
                    resultado = "El NIF introducido ya pertenece a otra cuenta.";
                } else {
                    resultado = "Error: Datos duplicados en el sistema.";
                }
            } else {
                resultado = "Se ha producido un error interno en el servidor.";
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error en addUsuario", e);
            }

            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error en rollback", ex);
            }
        } finally {
            this.closeConnection();
        }
        return resultado;
    }

    @Override
    public Usuario login(String emailString, String passwordHashString) {
        Usuario usuario = null;
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparada = null;

        String sql = "SELECT idUsuario, email, password, nombre, apellidos, "
                + "nif, telefono, direccion, codigo_postal, localidad, provincia, ultimo_acceso, avatar "
                + "FROM usuarios "
                + "WHERE email = ? "
                + "AND password = ?";

        //String sqlUpdate = "UPDATE usuarios SET ultimo_acceso = NOW() WHERE idUsuario = ?"; // Para actualizar el último acceso en cada logueo.

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setString(1, emailString);
            preparada.setString(2, passwordHashString);
            rs = preparada.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
                // Una vez lleno el bean de sesión, actualizamos el último acceso en BD 
                /*try (PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate)) {
                    psUpdate.setShort(1, usuario.getIdUsuario());
                    psUpdate.executeUpdate();
                }*/
            }
        } catch (SQLException e) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            this.closeConnection();
        }
        return usuario;
    }

    @Override
    public Boolean getDuplicateEmail(String email) {
        Boolean existe = false;
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparada = null;

        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?"; // Para saber si existe el email, es más eficiente simplemente contar.

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setString(1, email);
            rs = preparada.executeQuery();

            if (rs.next()) {
                existe = rs.getInt(1) > 0; // La tupla devuelve un número mayor que cero, existe el correo.
            }
        } catch (SQLException e) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error comprobando duplicidad de email", e);
        } finally {
            this.closeConnection();
        }
        return existe;
    }

    @Override
    public Boolean getDuplicateNif(String nifString) {
        Boolean existe = false;
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparada = null;

        String sql = "SELECT COUNT(*) FROM usuarios WHERE nif = ?"; // Para saber si existe el email, es más eficiente simplemente contar.

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setString(1, nifString);
            rs = preparada.executeQuery();

            if (rs.next()) {
                existe = rs.getInt(1) > 0; // La tupla devuelve un número mayor que cero, existe el nif
            }
        } catch (SQLException e) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error comprobando duplicidad de nif", e);
        } finally {
            this.closeConnection();
        }
        return existe;
    }
    
    @Override
    public Boolean updateUsuario(Usuario usuario) {
        boolean actualizado = false;
        Connection connection = null;
        PreparedStatement preparada = null;

        String sql = "UPDATE usuarios SET nombre = ?, apellidos = ?, direccion = ?, "
                + "localidad = ?, provincia = ?, codigo_postal = ?, telefono = ? "
                + "WHERE idUsuario = ?";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);

            preparada.setString(1, usuario.getNombre());
            preparada.setString(2, usuario.getApellidos());
            preparada.setString(3, usuario.getDireccion());
            preparada.setString(4, usuario.getLocalidad());
            preparada.setString(5, usuario.getProvincia());
            preparada.setString(6, usuario.getCodigoPostal());

            // Manejo de nulos para el teléfono
            if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty()) {
                preparada.setNull(7, java.sql.Types.VARCHAR);
            } else {
                preparada.setString(7, usuario.getTelefono());
            }

            preparada.setShort(8, usuario.getIdUsuario());

            // Si executeUpdate devuelve > 0, es que se ha actualizado correctamente
            actualizado = preparada.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error en updateUsuario", e);
            actualizado = false;
        } finally {
            this.closeConnection();
        }
        return actualizado;
    }

    @Override
    public boolean updatePassword(String email, String passActualCifrada, String passNuevaCifrada) {
        boolean exito = false;
        Connection connection = null;
        PreparedStatement preparada = null;

        // La clave está en el WHERE: solo actualiza si coinciden email Y password antigua
        String sql = "UPDATE usuarios SET password = ? WHERE email = ? AND password = ?";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setString(1, passNuevaCifrada);
            preparada.setString(2, email);
            preparada.setString(3, passActualCifrada);

            // Si devuelve > 0, es que el email y la pass antigua coincidían
            if (preparada.executeUpdate() > 0) {
                exito = true;
            }
        } catch (SQLException e) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error al cambiar password de " + email, e);
        } finally {
            this.closeConnection();
        }
        return exito;
    }

    @Override
    public boolean updateAvatar(Usuario usuario) {
        boolean actualizado = false;
        Connection connection = null;
        PreparedStatement preparada = null;

        String sql = "UPDATE usuarios SET avatar = ? WHERE idUsuario = ?";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);

            preparada.setString(1, usuario.getAvatar());
            preparada.setShort(2, usuario.getIdUsuario());

            actualizado = preparada.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error al actualizar el avatar del usuario con ID: " + usuario.getIdUsuario(), e);
        } finally {
            this.closeConnection();
        }
        return actualizado;
    }

    
    @Override
public boolean updateUltimoAcceso(short idUsuario) {
    boolean ok = false;
    Connection connection = null;
    PreparedStatement preparada = null;
    String sql = "UPDATE usuarios SET ultimo_acceso = NOW() WHERE idusuario = ?";

    try {
        connection = ConnectionFactory.getConnection();
        preparada = connection.prepareStatement(sql);
        preparada.setShort(1, idUsuario);

        if (preparada.executeUpdate() > 0) {
            ok = true;
        }
    } catch (SQLException e) {
        Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error al actualizar ultimoAcceso", e);
    } finally {
        this.closeConnection();
    }
    return ok;
}

    @Override
    public boolean deleteUsuario(Short idUsuario) { // Únicamente para un rollback si falla el registro.
        boolean eliminado = false;
        Connection connection = null;
        PreparedStatement preparada = null;

        String sql = "DELETE FROM usuarios WHERE idUsuario = ?";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);

            preparada.setShort(1, idUsuario);

            eliminado = preparada.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, "Error en el borrado preventivo (rollback manual) del usuario ID: " + idUsuario, e);
        } finally {
            this.closeConnection();
        }
        return eliminado;
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setIdUsuario(rs.getShort("idUsuario"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password")); // Ya viene encriptada de la bd en el ResultSet
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setNif(rs.getString("nif"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setDireccion(rs.getString("direccion"));
        usuario.setCodigoPostal(rs.getString("codigo_postal"));
        usuario.setLocalidad(rs.getString("localidad"));
        usuario.setProvincia(rs.getString("provincia"));
        usuario.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
        usuario.setAvatar(rs.getString("avatar"));

        return usuario;
    }
}