package es.cacharreo.beans;

import es.cacharreo.models.Utilities;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Clase JavaBean que representa a un usuario dentro del sistema.
 * <p>
 * Encapsula la información personal, de contacto y de autenticación, aplicando
 * reglas de normalización automática mediante la clase {@link Utilities} al
 * establecer campos de texto como nombre, apellidos o localidad.</p>
 *
 * 
 * @author fdezf
 * @version 1.0
 */
public class Usuario implements Serializable {

    /**
     * Identificador único del usuario (Primary Key en BD)
     */
    private Short idUsuario;

    /**
     * Correo electrónico único para inicio de sesión
     */
    private String email;

    /**
     * Contraseña del usuario (almacenada generalmente como hash MD5)
     */
    private String password;

    /**
     * Nombre de pila del usuario
     */
    private String nombre;

    /**
     * Apellidos del usuario
     */
    private String apellidos;

    /**
     * Número de Identificación Fiscal único
     */
    private String nif;

    /**
     * * Teléfono de contacto. Se usa {@code String} para coincidir con el tipo
     * {@code CHAR(9)} de la BD y preservar ceros a la izquierda o formatos
     * fijos.
     */
    private String telefono;

    /**
     * Dirección física (calle, número, etc.)
     */
    private String direccion;

    /**
     * Código postal (coincidente con {@code CHAR(5)} en BD)
     */
    private String codigoPostal;

    /**
     * Ciudad o población de residencia
     */
    private String localidad;

    /**
     * Provincia de residencia
     */
    private String provincia;

    /**
     * Marca de tiempo del último inicio de sesión exitoso
     */
    private Timestamp ultimoAcceso;

    /**
     * Nombre del fichero de imagen para el perfil (nullable)
     */
    private String avatar;

    /**
     * Constructor por defecto requerido para la especificación JavaBeans.
     */
    public Usuario() {
    }

    /**
     * @return El ID único del usuario
     */
    public Short getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Short idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return Email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email convirtiéndolo automáticamente a minúsculas para
     * garantizar la consistencia en las búsquedas y login.
     *
     * @param email Dirección de correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    /**
     * @return Contraseña (hash)
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Nombre normalizado
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre aplicando capitalización automática.
     *
     * @param nombre Nombre de pila.
     */
    public void setNombre(String nombre) {
        this.nombre = Utilities.capitalizar(nombre);
    }

    /**
     * @return Apellidos normalizados
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos aplicando capitalización automática.
     *
     * @param apellidos Apellidos del usuario.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = Utilities.capitalizar(apellidos);
    }

    /**
     * @return NIF con letra de control
     */
    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * @return Localidad normalizada
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Establece la localidad aplicando capitalización automática.
     *
     * @param localidad Nombre de la ciudad o pueblo.
     */
    public void setLocalidad(String localidad) {
        this.localidad = Utilities.capitalizar(localidad);
    }

    /**
     * @return Provincia normalizada
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Establece la provincia aplicando capitalización automática.
     *
     * @param provincia Nombre de la provincia.
     */
    public void setProvincia(String provincia) {
        this.provincia = Utilities.capitalizar(provincia);
    }

    /**
     * @return Instancia {@link Timestamp} del último acceso
     */
    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * @return Nombre del archivo de imagen de perfil
     */
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
