package es.cacharreo.DAO;

/**
 *
 * @author fdezf
 */
public interface IPedidoDAO {

    /**
     * Elimina un pedido de la base de datos y todas sus líneas asociadas.
     * Debido a la restricción de integridad referencial 'ON DELETE CASCADE'
     * definida en la base de datos, las filas correspondientes en la tabla
     * 'lineaspedidos' se eliminan de forma automática.
     *
     * @param idPedido El identificador único (SMALLINT) del pedido que se desea
     * eliminar.
     * @return {@code true} si el pedido fue eliminado con éxito (se afectó al
     * menos una fila), false en caso contrario.
     */
    public boolean borrarPedido(short idPedido);
    
     /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
}
