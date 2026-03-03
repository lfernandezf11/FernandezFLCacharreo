package es.cacharreo.DAO;

import es.cacharreo.beans.Usuario;

/**
 * Interfaz que define el contrato para las operaciones de persistencia de la
 * entidad Usuario.
 * <p>
 * Proporciona los métodos necesarios para gestionar el ciclo de vida de los
 * usuarios, incluyendo el registro, la autenticación segura, la gestión del
 * perfil y la validación de integridad de datos (emails y NIFs únicos).</p>
 *
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public interface IUsuarioDAO {

    /**
     * Comprueba si un correo electrónico ya existe en la base de datos.
     * <p>
     * Utilizado durante el proceso de registro y edición de perfil para
     * garantizar la unicidad del identificador de acceso.</p>
     *
     * @param email Dirección de correo a validar.
     * @return {@code true} si el email ya está registrado, {@code false} en
     * caso contrario.
     */
    public Boolean getDuplicateEmail(String email);

    /**
     * Comprueba si un NIF ya existe en el sistema.
     * <p>
     * Garantiza que no existan dos cuentas vinculadas al mismo documento de
     * identidad legal.</p>
     *
     * @param nifString Cadena con el NIF completo (8 números y letra).
     * @return {@code true} si el NIF está duplicado.
     */
    public Boolean getDuplicateNif(String nifString);

    /**
     * Inserta un nuevo usuario en la base de datos.
     * <p>
     * Este método debe asignar al objeto {@link Usuario} el ID generado
     * automáticamente por la base de datos tras una inserción exitosa.</p>
     *
     * @param usuario Objeto con los datos del nuevo registro.
     * @return Una cadena con el estado de la operación (ej: "ok" o el mensaje
     * de error SQL).
     */
    public String addUsuario(Usuario usuario);

    /**
     * Valida las credenciales de acceso de un usuario.
     *
     * @param email Correo electrónico del usuario.
     * @param passwordHash Contraseña ya cifrada (MD5) para comparar con la
     * almacenada.
     * @return El objeto {@link Usuario} completo si las credenciales son
     * válidas, o {@code null} en caso de error o datos incorrectos.
     */
    public Usuario login(String email, String passwordHash);

    /**
     * Actualiza la información personal de un usuario (nombre, dirección,
     * etc.).
     *
     * @param usuario Objeto con los datos actualizados.
     * @return {@code true} si la actualización fue exitosa.
     */
    public Boolean updateUsuario(Usuario usuario);

    /**
     * Actualiza la contraseña de un usuario validando que la contraseña actual
     * sea correcta.
     * <p>
     * Esta operación suele realizarse mediante una sentencia {@code UPDATE} que
     * incluya la contraseña antigua en la cláusula {@code WHERE} para asegurar
     * la identidad del solicitante.</p>
     *
     * @param email Correo del usuario.
     * @param passActualHash Hash de la contraseña actual.
     * @param passNuevaHash Hash de la nueva contraseña.
     * @return {@code true} si se actualizó (indica que la contraseña actual era
     * correcta), {@code false} en caso contrario.
     */
    public boolean updatePassword(String email, String passActualHash, String passNuevaHash);

    /**
     * Actualiza la referencia al archivo de imagen (avatar) del usuario.
     *
     * @param usuario Objeto usuario con la nueva ruta de avatar establecida.
     * @return {@code true} si se actualizó el registro en BD.
     */
    public boolean updateAvatar(Usuario usuario);

    /**
     * Registra la marca de tiempo del acceso actual del usuario.
     *
     * @param idUsuario Identificador único del usuario.
     * @return {@code true} si el registro fue exitoso.
     */
    public boolean updateUltimoAcceso(short idUsuario);

    /**
     * Elimina físicamente un usuario del sistema.
     *
     * @param idUsuario Identificador del usuario a eliminar.
     * @return {@code true} si se afectó a la fila correspondiente.
     */
    public boolean deleteUsuario(Short idUsuario);

    /**
     * Libera la conexión asociada al pool de conexiones.
     * <p>
     * Es esencial llamar a este método para evitar el agotamiento de recursos
     * en el servidor de aplicaciones.</p>
     */
    public void closeConnection();
}
