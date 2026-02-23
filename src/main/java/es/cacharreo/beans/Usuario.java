package es.cacharreo.beans;

import es.cacharreo.models.Utilities;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Clase bean que representa un usuario del sistema de registro.
 *
 * @author fdezf
 */
public class Usuario implements Serializable {
    private Short idUsuario;
    private String email; //unique
    private String password;
    private String nombre;
    private String apellidos;
    private String nif; //unique
    private String telefono; //nullable. Tipo String porque es un char con longitud fija en bd (char(9)), para que no haya problemas de conversión.
    private String direccion;
    private String codigoPostal; // char(5) en bd.
    private String localidad;
    private String provincia;
    private Timestamp ultimoAcceso;
    private String avatar; //nullable
    

    public Usuario(){
    }

    public Short getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Short idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Utilities.capitalizar(nombre);
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = Utilities.capitalizar(apellidos);
    }

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

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = Utilities.capitalizar(localidad);
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = Utilities.capitalizar(provincia);
    }

    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    
}
