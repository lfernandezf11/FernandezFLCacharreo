package es.cacharreo.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa la entidad Categoria en el modelo de negocio.
 * <p>
 * Esta clase se utiliza para clasificar los productos del catálogo y se
 * almacena frecuentemente en el {@code ServletContext} para alimentar los
 * filtros de búsqueda de la aplicación.</p>
 *
 *
 *
 * @author fdezf
 * @version 1.0
 */
public class Categoria implements Serializable {

    /**
     * Identificador único de la categoría en la base de datos
     */
    private byte idCategoria;

    /**
     * Nombre descriptivo de la categoría (ej: Periféricos, Componentes)
     */
    private String nombre;

    /**
     * Ruta o nombre del archivo de imagen asociado. Por defecto: default.jpg
     */
    private String imagen;

    /**
     * Constructor por defecto requerido para la especificación JavaBeans.
     */
    public Categoria() {
    }

    /**
     * Obtiene el identificador de la categoría.
     *
     * @return El ID de la categoría como un objeto Byte.
     */
    public Byte getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador de la categoría.
     *
     * @param idCategoria El nuevo ID de la categoría.
     */
    public void setIdCategoria(Byte idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return El nombre de la categoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * @param nombre El nuevo nombre descriptivo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la imagen representativa de la categoría.
     *
     * @return El nombre del fichero de imagen.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen representativa.
     *
     * @param imagen El nombre del fichero (ej: "graficas.png").
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Genera un código hash basado en los atributos de la categoría.
     *
     * @return El valor del hash calculado.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.idCategoria);
        hash = 89 * hash + Objects.hashCode(this.nombre);
        hash = 89 * hash + Objects.hashCode(this.imagen);
        return hash;
    }

    /**
     * Compara esta categoría con otro objeto para determinar su igualdad.
     * <p>
     * Se consideran iguales si todos sus atributos (ID, nombre e imagen)
     * coinciden.</p>
     *
     * @param obj El objeto a comparar.
     * @return {@code true} si son iguales; {@code false} en caso contrario.
     */
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
