package es.cacharreo.DAO;

import es.cacharreo.beans.Producto;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia para la entidad Producto.
 * <p>
 * Proporciona los métodos necesarios para la consulta del catálogo, la
 * recuperación de muestras aleatorias para la Home y la ejecución de búsquedas
 * avanzadas con múltiples criterios de filtrado.</p>
 *
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public interface IProductoDAO {

    /**
     * Recupera la lista completa de productos del catálogo.
     *
     * @return Una {@link List} con todos los productos disponibles.
     */
    public List<Producto> getProductos();

    /**
     * Obtiene una muestra aleatoria de productos.
     * <p>
     * Ideal para secciones de "Productos destacados" o carruseles en la página
     * de inicio que requieren dinamismo en cada carga.</p>
     *
     * @param limite Número máximo de productos a recuperar.
     * @return Lista de productos seleccionados al azar.
     */
    public List<Producto> getProductosRandom(int limite);

    /**
     * Recupera una colección de productos basados en una lista de
     * identificadores.
     * <p>
     * Utilizado principalmente para reconstruir la cesta de la compra a partir
     * de los IDs almacenados en las cookies del cliente.</p>
     *
     * @param ids Lista de identificadores únicos (Short).
     * @return Lista de objetos {@link Producto} correspondientes a esos IDs.
     */
    public List<Producto> getProductosByIds(List<Short> ids);

    /**
     * Busca un único producto por su identificador.
     *
     * @param idProducto Identificador único del producto.
     * @return El objeto {@link Producto} encontrado, o {@code null} si no
     * existe.
     */
    public Producto getProductoUnicoById(Short idProducto);

    /**
     * Obtiene todas las marcas distintas presentes en el catálogo.
     * <p>
     * Este método es fundamental para poblar los componentes de filtro
     * (Checkboxes o Selects) en la interfaz de usuario.</p>
     *
     * @return Lista de cadenas con los nombres de las marcas.
     */
    public List<String> getMarcas();

    /**
     * Recupera los valores de precio mínimo y máximo registrados en la base de
     * datos.
     *
     * @return Una lista de {@link Float} donde el primer elemento es el precio
     * mínimo y el segundo es el precio máximo (útil para Range Sliders).
     */
    public List<Float> getPreciosLimite();

    /**
     * Realiza una búsqueda avanzada de productos aplicando múltiples filtros
     * simultáneos.
     * <p>
     * El método debe construir una consulta dinámica (normalmente usando
     * {@code WHERE 1=1}) para concatenar los criterios que no sean nulos o
     * vacíos.</p>
     *
     * @param categorias Array de IDs de categorías seleccionadas.
     * @param marcas Array de nombres de marcas seleccionadas.
     * @param min Precio mínimo del rango.
     * @param max Precio máximo del rango.
     * @param texto Cadena de texto para búsqueda por nombre o descripción
     * (LIKE).
     * @return Lista de productos que cumplen con todos los criterios
     * especificados.
     */
    public List<Producto> getProductosFiltrados(String[] categorias, String[] marcas, float min, float max, String texto);

    /**
     * Libera los recursos de conexión vinculados a la implementación del DAO.
     */
    public void closeConnection();
}
