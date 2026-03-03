package es.cacharreo.models;

import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * Clase de utilidad para la gestión y preparación de datos del catálogo en la capa de vista.
 * <p>Centraliza procesos de lógica de negocio que transforman las listas planas de la 
 * base de datos en estructuras complejas requeridas por los componentes visuales de la interfaz.</p>
 * 
 * @author fdezf
 * @version 1.0
 */
public class ProductoUtils {

   /**
     * Prepara y estructura el subcatálogo de productos para la página de inicio (Home).
     * 
     * <p>Este método realiza las siguientes acciones:</p>
     * <ol>
     * <li>Recupera una selección aleatoria de 8 productos desde la base de datos.</li>
     * <li>Organiza los productos en una estructura de lista de listas (grupos de 4), 
     * optimizada para carruseles de Bootstrap u otros componentes de rejilla.</li>
     * <li>Inyecta los datos en el scope de la {@code request} para su consumo en las JSPs.</li>
     * </ol>
     * 
     * 
     * 
     * @param request El objeto {@link HttpServletRequest} donde se almacenarán los atributos 
     * "subCatalogo" (lista de grupos) y "productosFiltrados" (lista plana).
     */
    public static void prepararSubcatalogo(HttpServletRequest request) {
        IProductoDAO pDAO = DAOFactory.getDAOFactory().getProductoDAO();
        List<Producto> subCatalogo = pDAO.getProductosRandom(8);

        if (subCatalogo != null && !subCatalogo.isEmpty()) {
            List<List<Producto>> grupos = new ArrayList<>();
            List<Producto> grupoActual = new ArrayList<>();

            for (Producto p : subCatalogo) {
                grupoActual.add(p);

                // Si ya tenemos 4, lo añadimos a los grupos y vaciamos el "cubo"
                if (grupoActual.size() == 4) {
                    grupos.add(grupoActual);
                    grupoActual = new ArrayList<>(); // Nuevo grupo vacío para los siguientes
                }
            }

            // Por si acaso la lista no fuera múltiplo de 4 (ej. 10 productos), 
            // añadimos el último grupo que quedó a medias.
            if (!grupoActual.isEmpty()) {
                grupos.add(grupoActual);
            }

            request.setAttribute("subCatalogo", grupos);
            request.setAttribute("productosFiltrados", subCatalogo);
        // Por defecto, utilizamos los mismos productos como placeholder para los filtros.
        } else {
            request.setAttribute("mensaje", "No hay productos para mostrar");
        }
    }
}
