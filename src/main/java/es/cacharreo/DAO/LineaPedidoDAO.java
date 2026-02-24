package es.cacharreo.DAO;

import es.cacharreo.beans.LineaPedido;
import es.cacharreo.beans.Pedido;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fdezf
 */
public class LineaPedidoDAO implements ILineaPedidoDAO {

    private LineaPedido mapearLineaPedido(ResultSet rs) throws SQLException {
        LineaPedido lp = new LineaPedido(); 
        
        lp.setIdLinea(rs.getShort("idlinea"));
        lp.setIdPedido(rs.getShort("idpedido"));
        lp.setCantidad(rs.getInt("cantidad"));
        
        return lp;
    }

}
