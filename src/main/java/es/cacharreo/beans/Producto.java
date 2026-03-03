package es.cacharreo.beans;

import java.io.Serializable;

/**
 * Representa un artículo individual del catálogo de la tienda.
 * <p>Esta clase encapsula toda la información técnica y comercial de un producto,
 * incluyendo su categorización, precio y metadatos de visualización. Además, 
 * incorpora un campo de cantidad para facilitar la transferencia de datos entre 
 * la base de datos y la cesta de la compra.</p>
 * 
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public class Producto implements Serializable {

    /** Identificador único del producto en la base de datos */
    private Short idProducto;
    
    /** Nombre comercial del producto */
    private String nombre;
    
    /** Descripción detallada del artículo (admite nulos) */
    private String descripcion; 
    
    /** Precio de venta al público (con precisión de 2 decimales) */
    private Float precio; 
    
    /** Nombre del fabricante o marca del producto */
    private String marca;
    
    /** Ruta o nombre del archivo de imagen asociado (por defecto: default.png) */
    private String imagen; 
    
     
    /** 
     * Categoría a la que pertenece el producto.
     * <p>Representa una clave foránea (FK) hacia la entidad {@link Categoria}. 
     * Puede ser nulo si el producto no está categorizado.</p>
     */
    private Categoria categoria; 

    /**
     * Constructor por defecto requerido para la especificación JavaBeans.
     */
    public Producto() {
    }

    /** @return El ID único del producto */
    public Short getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Short idProducto) {
        this.idProducto = idProducto;
    }

    /** @return Objeto {@link Categoria} asociado */
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /** @return Nombre del producto */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return Descripción del producto o null si no existe */
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** @return Precio unitario */
    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    /** @return Marca del fabricante */
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    /** @return Nombre del archivo de imagen */
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}