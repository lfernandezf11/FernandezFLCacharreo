package es.cacharreo.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author fdezf
 */
public class Categoria implements Serializable{
    private byte idCategoria;
    private String nombre;
    private String imagen; //default.jpg si no hay imagen
    
    public Categoria(){
    }

    public Byte getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Byte idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.idCategoria);
        hash = 89 * hash + Objects.hashCode(this.nombre);
        hash = 89 * hash + Objects.hashCode(this.imagen);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Categoria other = (Categoria) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.imagen, other.imagen)) {
            return false;
        }
        return Objects.equals(this.idCategoria, other.idCategoria);
    }
    
    
}
