package es.cacharreo.models;

import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.LineaPedido;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Producto;
import java.util.List;
import java.util.Objects;

public class CestaService {

    /**
     * Gestiona la lógica de añadir un producto al pedido. Si ya existe en las
     * líneas, incrementa su cantidad. Si no, crea una nueva línea.
     *
     * @param pedido El objeto pedido (la cesta)
     * @param idProd ID del producto a añadir
     * @return Nombre del producto añadido para feedback al usuario, o cadena
     * vacía si falla.
     */
    public static String gestionarAddProducto(Pedido pedido, Short idProd) {
        DAOFactory daof = DAOFactory.getDAOFactory();
        IProductoDAO pDAO = daof.getProductoDAO();

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
            // Si ya existe, incrementamos cantidad
            lineaPreexistente.setCantidad(lineaPreexistente.getCantidad() + 1);
        } else {
            // Si es nuevo, creamos la LineaPedido
            LineaPedido nuevaLinea = new LineaPedido();
            // Línea necesaria para mantener la coherencia del carrito. Para el usuario anónimo, se queda a null, 
            // pero para el registrado tiene que asignarse el id del pedido correspondiente.
            nuevaLinea.setIdPedido(pedido.getIdPedido()); 
            nuevaLinea.setProducto(productoBD);
            nuevaLinea.setCantidad(1);

            pedido.getLineas().add(nuevaLinea);
        }

        // IMPORTANTE: después de cada cambio, recalculamos importes de líneas, IVA e Importe Total
        pedido.calcularTotales();

        return productoBD.getNombre();
    }

    /**
     * Incrementa en 1 la cantidad del producto dentro de la línea
     * correspondiente. Llama a calcularTotales para sincronizar importes e IVA.
     */
    public static void sumarUnidad(Pedido pedido, Short idProd) {
        for (LineaPedido lp : pedido.getLineas()) {
            if (Objects.equals(lp.getProducto().getIdProducto(), idProd)) {
                lp.setCantidad(lp.getCantidad() + 1);
                break;
            }
        }
        // Sincronizamos importes de líneas y totales de cabecera
        pedido.calcularTotales();
    }

    /**
     * Resta una unidad siempre que la cantidad actual sea mayor que 1.
     */
    public static void restarUnidad(Pedido pedido, Short idProd) {
        for (LineaPedido lp : pedido.getLineas()) {
            if (Objects.equals(lp.getProducto().getIdProducto(), idProd)) {
                if (lp.getCantidad() > 1) {
                    lp.setCantidad(lp.getCantidad() - 1);
                }
                break;
            }
        }
        pedido.calcularTotales();
    }

    
    /**
     * Elimina la línea completa del pedido basándose en el ID del producto.
     */
    public static void eliminarProducto(Pedido pedido, Short idProd) {
        List<LineaPedido> lineas = pedido.getLineas();

        for (int i = 0; i < lineas.size(); i++) { // forEach es inseguro para una iteración interrumpida.
            // Accedemos al ID a través del producto de la línea
            if (Objects.equals(lineas.get(i).getProducto().getIdProducto(), idProd)) {
                lineas.remove(i);
                break; 
            }
        }
        pedido.calcularTotales();
    }

}
