package es.cacharreo.DAO;

/**
 * Interfaz que define el contrato para las operaciones de persistencia de las
 * líneas de pedido.
 * <p>
 * Actualmente, esta interfaz actúa como un <b>marcador de arquitectura</b>
 * (Placeholder). La lógica de persistencia de las líneas de pedido se encuentra
 * integrada de forma transaccional dentro de {@link IPedidoDAO} para garantizar
 * la integridad de los datos en las operaciones de compra.</p>
 *
 * <p>
 * <b>Nota de mantenimiento:</b> Se reserva para implementaciones futuras donde
 * se requiera una gestión granular e independiente de las líneas (por ejemplo,
 * para sistemas de inventario avanzado, devoluciones parciales o auditorías de
 * cambios línea a línea).</p>
 *
 * @author fdezf
 * @version 1.0
 * @see IPedidoDAO
 */
public interface ILineaPedidoDAO {

    /**
     * Reservado para futuras implementaciones de operaciones CRUD específicas
     * sobre la tabla 'lineaspedidos'.
     */
}
