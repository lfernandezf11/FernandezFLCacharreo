package es.cacharreo.DAO;

import es.cacharreo.beans.LineaPedido;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Usuario;
import java.util.List;

/**
 * Interfaz que define las operaciones de persistencia para Pedidos y sus Líneas
 * asociadas.
 * <p>
 * Proporciona un contrato granular para la gestión de la cesta de la compra,
 * permitiendo la sincronización en tiempo real entre el estado en memoria y la
 * base de datos MySQL.</p>
 *
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public interface IPedidoDAO {

    /**
     * Elimina un pedido de la base de datos y todas sus líneas asociadas.
     * <p>
     * Debido a la restricción de integridad referencial <b>ON DELETE
     * CASCADE</b>
     * definida en el esquema relacional, las filas correspondientes en la tabla
     * 'lineaspedidos' se eliminan automáticamente al borrar la cabecera.</p>
     *
     * @param idPedido El identificador único (SMALLINT) del pedido que se desea
     * eliminar.
     * @return {@code true} si el pedido fue eliminado con éxito; {@code false}
     * en caso contrario.
     */
    public boolean borrarPedido(Short idPedido);

    /**
     * Inserta un pedido completo (cabecera y líneas) en la base de datos.
     * <p>
     * Este método suele utilizarse en la primera persistencia de una cesta que
     * solo existía en sesión o durante el registro de un nuevo usuario.</p>
     *
     * @param pedido El objeto {@link Pedido} a persistir.
     * @return {@code true} si la inserción de la cabecera y sus líneas fue
     * exitosa.
     */
    public boolean insertarCesta(Pedido pedido);

    /**
     * Recupera el pedido activo (estado 'C' - Carrito) de un usuario
     * específico.
     * <p>
     * Realiza una carga profunda (Eager Loading) recuperando tanto la cabecera
     * como todas las líneas y productos asociados.</p>
     *
     * @param usuario El objeto {@link Usuario} del que se desea recuperar la
     * cesta.
     * @return El {@link Pedido} activo encontrado, o {@code null} si el usuario
     * no tiene cesta en BD.
     */
    public Pedido getCestaByUsuario(Usuario usuario);

    /**
     * Actualiza la cantidad de una línea de pedido existente.
     * <p>
     * Este método es crítico para la sincronización AJAX cuando el usuario suma
     * o resta unidades desde la vista del carrito.</p>
     *
     * @param idPedido Identificador único del pedido.
     * @param idProducto Identificador del producto en la línea.
     * @param cantidad Nueva cantidad total para la línea.
     * @return {@code true} si se actualizó correctamente.
     */
    public boolean updateCantidadLinea(short idPedido, short idProducto, int cantidad);

    /**
     * Inserta una nueva línea de pedido de forma individual.
     *
     * @param lp Objeto {@link LineaPedido} que contiene los datos a persistir.
     * @return {@code true} si la inserción fue exitosa.
     */
    public boolean insertarLineaIndividual(LineaPedido lp);

    /**
     * Elimina una línea específica de un pedido basándose en el producto.
     *
     * @param idPedido Identificador del pedido.
     * @param idProducto Identificador del producto cuya línea se desea
     * eliminar.
     * @return {@code true} si la fila fue eliminada físicamente de la BD.
     */
    public boolean deleteLineaIndividual(short idPedido, short idProducto);

    /**
     * Sincroniza los totales económicos de la cabecera (importe neto e IVA).
     * <p>
     * Debe invocarse tras cualquier modificación de las líneas (add, update o
     * delete) para mantener la integridad financiera del pedido en la tabla de
     * cabeceras.</p>
     *
     * @param p Objeto {@link Pedido} con los totales ya recalculados en
     * memoria.
     * @return {@code true} si la actualización fue exitosa.
     */
    public boolean updateTotalesPedido(Pedido p);

    /**
     * Finaliza el proceso de compra de un pedido.
     * <p>
     * Cambia el estado del pedido (normalmente de 'C' a 'F' o 'P'), actualiza
     * la fecha de finalización y sella la transacción.</p>
     *
     * @param pedido El pedido a finalizar.
     * @return {@code true} si la operación se completó correctamente.
     */
    public boolean finalizarPedido(Pedido pedido);

    /**
     * Recupera la relación de todos los pedidos finalizados de un usuario.
     *
     * @param idUsuario Identificador del usuario.
     * @return Una {@link List} de objetos {@link Pedido} históricos.
     */
    public List<Pedido> getHistorialPedidos(Short idUsuario);

    /**
     * Libera los recursos de conexión asociados al DAO.
     * <p>
     * Cierra el hilo del pool de conexiones para evitar fugas de recursos en el
     * servidor de aplicaciones.</p>
     */
    public void closeConnection();
}
