package es.cacharreo.DAO;

import es.cacharreo.beans.Categoria;
import java.util.List;

/**
 * Interfaz que define el contrato para las operaciones de persistencia de Categorías.
 * <p>Establece los métodos necesarios para recuperar la estructura de clasificación 
 * del catálogo, permitiendo que la aplicación obtenga las categorías disponibles 
 * independientemente del motor de base de datos utilizado.</p>
 * 
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public interface ICategoriaDAO {

    /**
     * Recupera la lista completa de categorías almacenadas en el sistema.
     * <p>Este método es utilizado habitualmente durante la inicialización de la 
     * aplicación para cargar el menú de navegación y los filtros de búsqueda.</p>
     * 
     * @return Una {@link List} de objetos {@link Categoria}. Si no hay registros, 
     * devuelve una lista vacía.
     */
    public List<Categoria> getCategorias();

    /**
     * Cierra de forma segura la conexión con la fuente de datos.
     * <p>Debe ser invocado tras finalizar las operaciones de persistencia para 
     * liberar los recursos del sistema (Connection, Statement, ResultSet) y 
     * evitar fugas de memoria (memory leaks).</p>
     */
    public void closeConnection();
}