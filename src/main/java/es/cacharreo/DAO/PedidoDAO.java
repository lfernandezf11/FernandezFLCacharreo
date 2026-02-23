package es.cacharreo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fdezf
 */
public class PedidoDAO implements IPedidoDAO {

    @Override
    public boolean borrarPedido(short idPedido) {
        boolean exito = false;
        Connection connection = null;
        PreparedStatement preparada = null;
        String sql = "DELETE FROM pedidos WHERE idpedido = ? AND estado = 'c'";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idPedido);

            int filasAfectadas = preparada.executeUpdate();

            // Si se ha borrado al menos una fila, la operación es exitosa
            if (filasAfectadas > 0) {
                exito = true;
            }

        } catch (SQLException e) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error al borrar el pedido: " + idPedido, e);
        } finally {
            this.closeConnection();
        }

        return exito;
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
}
