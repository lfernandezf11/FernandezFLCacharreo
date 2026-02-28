package es.cacharreo.DAO;

import es.cacharreo.beans.LineaPedido;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Usuario;

/**
 * Definición de las operaciones de persistencia para Pedidos y sus Líneas.
 * Proporciona métodos granulares para la sincronización en tiempo real de la cesta.
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
    
    public boolean insertarCesta(Pedido pedido);
    
    public Pedido getCestaByUsuario(Usuario usuario);
    
    /**
     * Actualiza la cantidad y el importe acumulado de una línea de pedido existente.
     * 
     * @param idPedido Identificador único del pedido.
     * @param idProducto Identificador del producto en la línea.
     * @param cantidad Nueva cantidad total para la línea.
     * @return true si se actualizó correctamente, false en caso contrario.
     */
    public boolean updateCantidadLinea(short idPedido, short idProducto, int cantidad);

    /**
     * Inserta una nueva línea de pedido en la base de datos.
     * 
     * @param lp Objeto LineaPedido que contiene los datos a persistir.
     * @return true si la inserción fue exitosa.
     */
    public boolean insertarLineaIndividual(LineaPedido lp);

    /**
     * Elimina una línea específica de un pedido.
     * 
     * @param idPedido Identificador del pedido.
     * @param idProducto Identificador del producto cuya línea se desea eliminar.
     * @return true si la fila fue eliminada.
     */
    public boolean deleteLineaIndividual(short idPedido, short idProducto);

    /**
     * Sincroniza los totales de la cabecera del pedido (importe neto e IVA) 
     * tras modificar sus líneas.
     * 
     * @param p Objeto Pedido que contiene los totales actualizados.
     * @return true si la cabecera se actualizó correctamente.
     */
    public boolean updateTotalesPedido(Pedido p);
    
    
    public boolean finalizarPedido(Pedido pedido);
    
     /**
     * Abandona el hilo del pool de conexiones
     */
    public void closeConnection();
}
