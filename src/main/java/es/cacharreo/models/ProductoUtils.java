package es.cacharreo.models;

import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fdezf
 */
public class ProductoUtils {

    /**
     * Prepara los datos necesarios para la página de inicio. Carga 8 productos
     * aleatorios en una lista PLANA para el carrusel infinito. Va en
     * ProductoUtils porque no pertenece a la capa de acceso a datos
     * (ProductoDAO), sino a la lógica de negocio. Es dependiente de una request
     * (si fuera en el DAO, lo haría dependiente del navegador).
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
        } else {
            request.setAttribute("mensaje", "No hay productos para mostrar");
        }
    }


/**
     * Crea una copia de un producto del catálogo para usarlo en la cesta.
     *
     * @param base Producto original del catálogo
     * @param cantidad Cantidad que se desea asignar
     * @return Nueva instancia de Producto
     */
    public static Producto clonarProducto(Producto base, int cantidad) {
        Producto nuevo = new Producto();
        nuevo.setIdProducto(base.getIdProducto());
        nuevo.setNombre(base.getNombre());
        nuevo.setPrecio(base.getPrecio());
        nuevo.setImagen(base.getImagen());
        nuevo.setMarca(base.getMarca());
        nuevo.setCantidad(cantidad);
        return nuevo;
    }
}