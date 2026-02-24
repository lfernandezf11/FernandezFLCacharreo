package es.cacharreo.models;

import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.LineaPedido;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Producto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fdezf
 */
public class CestaUtils {

    public static Pedido cargarPedidoDesdeCookieStr(String cestaStr) {
        Pedido pedido = new Pedido();
        pedido.setLineas(new ArrayList<LineaPedido>()); // Inicializamos la lista de líneas dentro del pedido para evitar nulos

        // Si no hay cookie, devolvemos el pedido vacío pero inicializado para evitar NullPointerException en JSPs
        if (cestaStr == null || cestaStr.trim().isEmpty()) {
            return pedido;
        }

        // Mapeamos IDs y cantidades desde el String de la cookie (formato id:cant) para poder agrupar después 
        // de la consulta cada producto con su cantidad.
        Map<Short, Integer> cantidades = new HashMap<>();
        String[] items = cestaStr.split(",");
        for (String item : items) {
            String[] parte = item.split(":");
            if (parte.length == 2) {
                cantidades.put(Short.valueOf(parte[0]), Integer.valueOf(parte[1]));
            }
        }

        // Recuperamos los objetos Producto desde la base de datos
        DAOFactory daof = DAOFactory.getDAOFactory();
        IProductoDAO pDAO = daof.getProductoDAO();
        List<Short> ids = new ArrayList<>(cantidades.keySet());
        List<Producto> productosDB = pDAO.getProductosByIds(ids);

        // Transformamos cada Producto en una LineaPedido y lo añadimos al pedido
        for (Producto p : productosDB) {
            LineaPedido linea = new LineaPedido();
            linea.setProducto(p);
            linea.setCantidad(cantidades.get(p.getIdProducto()));

            // El importe de la línea es Precio * Cantidad (base imponible)
            linea.setImporte(p.getPrecio() * linea.getCantidad());
            pedido.getLineas().add(linea);
        }
        pedido.calcularTotales(); // Sincronizamos los totales del objeto Pedido (importe (base imponible) e iva).

        return pedido;
    }

    
    /**
     * Convierte las líneas del pedido en un String para almacenar en la cookie.
     * Formato resultante: "id1:cant1,id2:cant2,id3:cant3"
     *
     * @param pedido El objeto pedido que contiene las líneas
     * @return String formateado para la cookie
     */
    public static String parserPedidoAString(Pedido pedido) {
        StringBuilder sb = new StringBuilder();

        // Verificamos que el pedido tenga líneas para evitar errores
        if (pedido != null && pedido.getLineas() != null) {
            for (LineaPedido lp : pedido.getLineas()) {
                // Si ya hay contenido, añadimos la coma separadora
                if (sb.length() > 0) {
                    sb.append(",");
                }

                // Accedemos al ID a través del producto de la línea
                sb.append(lp.getProducto().getIdProducto())
                        .append(":")
                        .append(lp.getCantidad());
            }
        }

        return sb.toString();
    }


    /*public static Producto buscarEnCesta(List<Producto> cesta, Short idProducto) {
        if (cesta != null) {
            for (Producto p : cesta) {
                if (Objects.equals(p.getIdProducto(), idProducto)) {
                    return p;
                }
            }
        }
        return null;
    }*/
}
