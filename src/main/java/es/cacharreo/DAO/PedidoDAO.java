package es.cacharreo.DAO;

import es.cacharreo.beans.LineaPedido;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fdezf
 */
public class PedidoDAO implements IPedidoDAO {

    @Override
    public boolean insertarCesta(Pedido pedido) {
        boolean exito = false;
        Connection connection = null;
        PreparedStatement psPedido = null;
        PreparedStatement psLinea = null;
        ResultSet rs = null;

        String sqlPedido = "INSERT INTO pedidos (fecha, idUsuario, importe, iva) VALUES (?, ?, ?, ?)";
        String sqlLinea = "INSERT INTO lineaspedidos (idPedido, idProducto, cantidad, importe) VALUES (?, ?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            // Sentencia para la tabla pedidos, con recuperación del idPedido generado para asignarlo a la cesta en sesión.
            psPedido = connection.prepareStatement(sqlPedido, PreparedStatement.RETURN_GENERATED_KEYS);

            // Conversión de fecha de java.util.Date a java.sql.Date
            if (pedido.getFecha() != null) {
                psPedido.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            } else {
                psPedido.setDate(1, new java.sql.Date(System.currentTimeMillis())); // Por si acaso viene nula
            }
            psPedido.setShort(2, pedido.getUsuario().getIdUsuario());
            psPedido.setFloat(3, pedido.getImporte());
            psPedido.setFloat(4, pedido.getIva());

            int filasPedido = psPedido.executeUpdate();

            if (filasPedido > 0) {
                rs = psPedido.getGeneratedKeys();
                if (rs.next()) {
                    // Seteamos el ID generado al objeto Pedido (idPedido)
                    short idPedidoGenerado = rs.getShort(1);
                    pedido.setIdPedido(idPedidoGenerado);

                    // Sentencias para la tabla lineaspedidos
                    psLinea = connection.prepareStatement(sqlLinea);

                    for (LineaPedido lp : pedido.getLineas()) {
                        psLinea.setShort(1, idPedidoGenerado);
                        psLinea.setShort(2, lp.getProducto().getIdProducto());
                        psLinea.setInt(3, lp.getCantidad());
                        psLinea.setFloat(4, lp.getImporte());

                        psLinea.addBatch(); // Añadimos a un lote para procesar todas juntas
                    }
                    try {
                        psLinea.executeBatch();
                        connection.commit(); // Si llega aquí, todo se guarda
                        exito = true;
                    } catch (SQLException eBatch) {
                        connection.rollback(); // Si falla la inserción de las líneas, el pedido se borra por seguridad.
                        Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error en el lote de líneas", eBatch);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error general en insertarCesta", e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.getStackTrace();
            }
        } finally {
            this.closeConnection();
        }
        return exito;
    }

    @Override
    public Pedido getCestaByIdUsuario(short idUsuario) {
        Pedido cesta = null;
        List<LineaPedido> lineas = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM pedidos WHERE idusuario = ? AND estado = 'c'";

        try {
            connection = ConnectionFactory.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setShort(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                cesta = mapearPedido(rs);

                // IMPORTANTE: Ahora necesitamos cargar sus líneas a través del idPedido, y después asignarlas antes de devolverlo
                lineas = getLineasPedido(cesta.getIdPedido(), connection);
                cesta.setLineas(lineas);
            }
        } catch (SQLException e) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error al recuperar cesta de BD", e);
        } finally {
            this.closeConnection();
        }
        return cesta;
    }

// Método auxiliar privado para no repetir código
    private List<LineaPedido> getLineasPedido(short idPedido, Connection connection) throws SQLException {
        List<LineaPedido> lineas = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // Hacemos JOIN con productos para tener los datos del objeto Producto (nombre, precio, etc.)
        String sql = "SELECT lp.*, p.nombre, p.precio, p.imagen FROM lineaspedidos lp "
                + "JOIN productos p ON lp.idproducto = p.idproducto WHERE lp.idpedido = ?";

        try {
            connection = ConnectionFactory.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setShort(1, idPedido);
            rs = ps.executeQuery();
                       
                while (rs.next()) {
                    LineaPedido lp = new LineaPedido();
                    lp.setIdLinea(rs.getShort("idlinea"));
                    lp.setIdPedido(idPedido);
                    lp.setCantidad(rs.getInt("cantidad"));

                    // Hidratamos el objeto Producto dentro de la línea
                    Producto prod = new Producto();
                    prod.setIdProducto(rs.getShort("idproducto"));
                    prod.setNombre(rs.getString("nombre"));
                    prod.setPrecio(rs.getFloat("precio"));
                    prod.setImagen(rs.getString("imagen"));

                    lp.setProducto(prod);
                    lineas.add(lp);
                }
            }
        return lineas;
        }
        
    

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

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();

        pedido.setIdPedido(rs.getShort("idpedido"));
        pedido.setFecha(rs.getDate("fecha"));
        pedido.setImporte(rs.getFloat("importe"));
        pedido.setIva(rs.getFloat("iva"));
        // El estado ya sabemos que es 'c'
        return pedido;
    }
}
