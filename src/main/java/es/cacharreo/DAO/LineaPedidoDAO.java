package es.cacharreo.DAO;

import es.cacharreo.beans.LineaPedido;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fdezf
 */
public class LineaPedidoDAO implements ILineaPedidoDAO {

    public static LineaPedido mapearLineaPedido(ResultSet rs) throws SQLException {
        LineaPedido lp = new LineaPedido(); 
        
        lp.setIdLinea(rs.getShort("idlinea"));
        lp.setIdPedido(rs.getShort("idpedido"));
        lp.setCantidad(rs.getInt("cantidad"));
        
        lp.setProducto(ProductoDAO.mapearProducto(rs));
        // El usuario se asigna en el controller
        
        return lp;
    }

}
