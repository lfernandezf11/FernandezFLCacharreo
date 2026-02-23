package es.cacharreo.beans;

import java.io.Serializable;

/**
 *
 * @author fdezf
 */
public class Producto implements Serializable {

    private Short idProducto;
    private String nombre;
    private String descripcion; //nullable
    private Float precio; //2decimales
    private String marca;
    private String imagen; // default
    
    private int cantidad; // Necesario para construir y recuperar la cesta
    
    private Categoria categoria; //A través de FK idCategoria, nullable

    public Producto() {
    }

    public Short getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Short idProducto) {
        this.idProducto = idProducto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    
}
