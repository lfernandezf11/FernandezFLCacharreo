package es.cacharreo.DAO;

import es.cacharreo.beans.LineaPedido;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase encargada de la persistencia y manipulación de las líneas de pedido en
 * la base de datos MySQL.
 * <p>
 * Esta clase implementa el contrato {@link ILineaPedidoDAO} y proporciona los
 * métodos necesarios para transformar los registros de la base de datos en
 * objetos {@link LineaPedido}.</p>
 *
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public class LineaPedidoDAO implements ILineaPedidoDAO {

    /**
     * Transforma un registro actual de un {@link ResultSet} en un objeto
     * {@link LineaPedido}.
     * <p>
     * Este método realiza una <b>hidratación compuesta</b>: no solo recupera
     * los datos propios de la línea (id, cantidad), sino que también delega en
     * {@link ProductoDAO#mapearProducto(ResultSet)} para instanciar y asociar
     * el objeto {@link es.cacharreo.beans.Producto} correspondiente.</p>
     *
     *
     *
     * @param rs El {@link ResultSet} posicionado en la fila que se desea
     * mapear.
     * @return Un objeto {@link LineaPedido} con sus datos y su producto
     * vinculados.
     * @throws SQLException Si ocurre un error al acceder a las columnas de la
     * base de datos.
     * @see ProductoDAO#mapearProducto(ResultSet)
     */
    public static LineaPedido mapearLineaPedido(ResultSet rs) throws SQLException {
        LineaPedido lp = new LineaPedido();

        lp.setIdLinea(rs.getShort("idlinea"));
        lp.setIdPedido(rs.getShort("idpedido"));
        lp.setCantidad(rs.getInt("cantidad"));

        // Composición: la línea de pedido contiene un objeto Producto completo
        lp.setProducto(ProductoDAO.mapearProducto(rs));

        // Nota: La vinculación del usuario con el pedido se gestiona a nivel de controlador
        // para mantener la integridad de la sesión.
        return lp;
    }

}
