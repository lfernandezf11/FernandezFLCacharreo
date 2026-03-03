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
 * Clase de utilidad para la gestión de la cesta de la compra persistida en cookies.
 * Proporciona métodos estáticos para la conversión entre el formato de texto plano 
 * de la cookie y los objetos de negocio (Pedido, LineaPedido, Producto).
 * 
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public class CestaUtils {

    /**
     * Reconstruye un objeto {@link Pedido} a partir de una cadena de texto serializada 
     * proveniente de una cookie.
     * 
     * <p>El método realiza las siguientes operaciones:</p>
     * <ul>
     * <li>Parsea el string con formato {@code id1:cant1,id2:cant2}.</li>
     * <li>Consulta la base de datos para obtener la información actualizada de los productos.</li>
     * <li>Crea las líneas de pedido vinculando productos y cantidades.</li>
     * <li>Sincroniza los totales económicos del pedido.</li>
     * </ul>
     * 
     * @param cestaStr Cadena recuperada de la cookie (ej: "12:2,5:1").
     * @return Un objeto {@link Pedido} inicializado. Si la cadena es nula o vacía, 
     * devuelve un pedido sin líneas pero no nulo.
     */
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
     * Serializa un objeto {@link Pedido} en una cadena de texto apta para ser 
     * almacenada en una cookie de navegador.
     * 
     * <p>El formato resultante es una lista separada por comas donde cada elemento 
     * representa un producto y su cantidad separados por dos puntos ({@code ID:CANTIDAD}).</p>
     * 
     * @param pedido El objeto pedido que contiene la lista de {@link LineaPedido}.
     * @return Una cadena formateada (ej: "1:2,4:5"). Si el pedido es nulo o no tiene líneas, 
     * devuelve una cadena vacía.
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
}
