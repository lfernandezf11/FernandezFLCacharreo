package es.cacharreo.DAO;

import es.cacharreo.beans.Usuario;

/**
 * Interfaz para operaciones CRUD sobre la tabla usuarios.
 *
 * @author fdezf
 */
public interface IUsuarioDAO {

    public Boolean getDuplicateEmail(String email);

    public String addUsuario(Usuario usuario);

    public Usuario login(String email, String passwordHash);

    public Boolean updateUsuario(Usuario usuario);

    /**
     * Actualiza la contraseña de un usuario validando que la contraseña actual
     * sea correcta.
     *
     * @param email Correo del usuario.
     * @param passActualHash Password actual (MD5).
     * @param passNuevaHash Password nueva (MD5).
     * @return true si se actualizó (la actual era correcta), false en caso
     * contrario.
     */
    public boolean updatePassword(String email, String passActualHash, String passNuevaHash);
    
    public boolean updateAvatar(Usuario usuario);
    
    public boolean deleteUsuario(Short idUsuario);

    /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();

}
