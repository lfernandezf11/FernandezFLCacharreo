package es.cacharreo.models;

import es.cacharreo.DAO.IPedidoDAO;
import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.LineaPedido;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Producto;
import java.util.List;
import java.util.Objects;

/**
 * Servicio encargado de la lógica de negocio de la cesta de la compra. Gestiona
 * tanto la manipulación de objetos en memoria (sesión) como la persistencia en
 * base de datos en tiempo real si el usuario está logueado.
 *
 * @author fdezf
 */
public class CestaService {

    /**
     * Gestiona la lógica de añadir un producto al pedido. Si ya existe en las
     * líneas, incrementa su cantidad. Si no, crea una nueva línea. Si el pedido
     * es persistente (usuario logueado), impacta los cambios en BD.
     *
     * @param pedido El objeto pedido (la cesta)
     * @param idProd ID del producto a añadir
     * @return Nombre del producto añadido para feedback al usuario, o cadena
     * vacía si falla.
     */
    public static String gestionarAddProducto(Pedido pedido, Short idProd) {
        DAOFactory daof = DAOFactory.getDAOFactory();
        IProductoDAO pDAO = daof.getProductoDAO();
        IPedidoDAO pedidoDAO = daof.getPedidoDAO();

        Producto productoBD = pDAO.getProductoUnicoById(idProd); // Buscamos si el producto existe en la BD

        if (productoBD == null) {
            return "";
        }

        // Buscamos si ya existe una línea para este producto en el pedido
        LineaPedido lineaPreexistente = null;
        for (LineaPedido lp : pedido.getLineas()) {
            if (Objects.equals(lp.getProducto().getIdProducto(), idProd)) {
                lineaPreexistente = lp;
                break;
            }
        }

        if (lineaPreexistente != null) {

            lineaPreexistente.setCantidad(lineaPreexistente.getCantidad() + 1); // Si ya existe, incrementamos cantidad
            pedido.calcularTotales(); // IMPORTANTE: después de cada cambio, recalculamos importes de líneas, IVA e Importe Total

            // Si el pedido ya existe en BD, actualizamos la línea existente
            if (pedido.getIdPedido() != null) {
                pedidoDAO.updateCantidadLinea(pedido.getIdPedido(), idProd,
                        lineaPreexistente.getCantidad(),
                        lineaPreexistente.getImporte());
                pedidoDAO.updateTotalesPedido(pedido);
            }
        } else {
            // Si es nuevo, creamos la LineaPedido
            LineaPedido nuevaLinea = new LineaPedido();
            // Línea necesaria para mantener la coherencia del carrito. Para el usuario anónimo, se queda a null, 
            // pero para el registrado tiene que asignarse el id del pedido correspondiente.
            nuevaLinea.setIdPedido(pedido.getIdPedido());
            nuevaLinea.setProducto(productoBD);
            nuevaLinea.setCantidad(1);

            pedido.getLineas().add(nuevaLinea);
            pedido.calcularTotales();

            // Si el pedido ya existe en BD, insertamos la nueva línea
            if (pedido.getIdPedido() != null) {
                pedidoDAO.insertarLineaIndividual(nuevaLinea);
                pedidoDAO.updateTotalesPedido(pedido);
            }
        }
        return productoBD.getNombre();
    }

    /**
     * Incrementa en 1 la cantidad del producto dentro de la línea
     * correspondiente. Si el pedido es persistente, sincroniza el cambio en la
     * base de datos.
     *
     * * @param pedido El objeto pedido actual.
     * @param idProd ID del producto a incrementar.
     */
    public static void sumarUnidad(Pedido pedido, Short idProd) {
        for (LineaPedido lp : pedido.getLineas()) {
            if (Objects.equals(lp.getProducto().getIdProducto(), idProd)) {
                lp.setCantidad(lp.getCantidad() + 1);

                // Sincronizamos importes de líneas y totales de cabecera
                pedido.calcularTotales();

                // PERSISTENCIA: Si el usuario está logueado
                if (pedido.getIdPedido() != null) {
                    IPedidoDAO pedidoDAO = DAOFactory.getDAOFactory().getPedidoDAO();
                    pedidoDAO.updateCantidadLinea(pedido.getIdPedido(), idProd,
                            lp.getCantidad(), lp.getImporte());
                    pedidoDAO.updateTotalesPedido(pedido);
                }
                break;
            }
        }
    }

    /**
     * Resta una unidad siempre que la cantidad actual sea mayor que 1. Si el
     * pedido es persistente, sincroniza el cambio en la base de datos.
     *
     * * @param pedido El objeto pedido actual.
     * @param idProd ID del producto a decrementar.
     */
    public static void restarUnidad(Pedido pedido, Short idProd) {
        for (LineaPedido lp : pedido.getLineas()) {
            if (Objects.equals(lp.getProducto().getIdProducto(), idProd)) {
                if (lp.getCantidad() > 1) {
                    lp.setCantidad(lp.getCantidad() - 1);

                    // Sincronizamos importes de líneas y totales de cabecera
                    pedido.calcularTotales();

                    // PERSISTENCIA: Si el usuario está logueado
                    if (pedido.getIdPedido() != null) {
                        IPedidoDAO pedidoDAO = DAOFactory.getDAOFactory().getPedidoDAO();
                        pedidoDAO.updateCantidadLinea(pedido.getIdPedido(), idProd,
                                lp.getCantidad(), lp.getImporte());
                        pedidoDAO.updateTotalesPedido(pedido);
                    }
                }
                break;
            }
        }
    }

    /**
     * Elimina la línea completa del pedido basándose en el ID del producto. Si
     * el pedido es persistente, elimina la fila correspondiente en la BD.
     *
     * 
     * @param pedido El objeto pedido actual.
     * @param idProd ID del producto a eliminar.
     */
    public static void eliminarProducto(Pedido pedido, Short idProd) {
        List<LineaPedido> lineas = pedido.getLineas();

        for (int i = 0; i < lineas.size(); i++) { // forEach es inseguro para una iteración interrumpida.
            // Accedemos al ID a través del producto de la línea
            if (Objects.equals(lineas.get(i).getProducto().getIdProducto(), idProd)) {

                // PERSISTENCIA: Borrado físico en BD antes de eliminar de la lista en memoria
                if (pedido.getIdPedido() != null) {
                    DAOFactory.getDAOFactory().getPedidoDAO().deleteLineaIndividual(pedido.getIdPedido(), idProd);
                }

                lineas.remove(i);
                break;
            }
        }

        // Sincronizamos importes de líneas y totales de cabecera
        pedido.calcularTotales();

        // PERSISTENCIA: Actualizamos los totales de la cabecera en BD tras borrar la línea
        if (pedido.getIdPedido() != null) {
            DAOFactory.getDAOFactory().getPedidoDAO().updateTotalesPedido(pedido);
        }
    }
}
