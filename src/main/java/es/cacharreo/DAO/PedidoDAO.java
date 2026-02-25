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
        System.out.println("Llamada a insertarCesta");
        boolean exito = false;
        Connection connection = null;
        PreparedStatement psPedido = null;
        PreparedStatement psLinea = null;
        ResultSet rs = null;

        String sqlPedido = "INSERT INTO pedidos (fecha, idUsuario, importe, iva) VALUES (?, ?, ?, ?)";
        String sqlLinea = "INSERT INTO lineaspedidos (idPedido, idProducto, cantidad) VALUES (?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            // Sentencia para la tabla pedidos, con recuperación del idPedido generado para asignarlo a la cesta en sesión.
            psPedido = connection.prepareStatement(sqlPedido, PreparedStatement.RETURN_GENERATED_KEYS);

            // Conversión de fecha de java.util.Date a java.sql.Date
            java.util.Date fechaUtil = (pedido.getFecha() != null) ? pedido.getFecha() : new java.util.Date();
psPedido.setDate(1, new java.sql.Date(fechaUtil.getTime()));
            psPedido.setShort(2, pedido.getUsuario().getIdUsuario());
            psPedido.setFloat(3, (pedido.getImporte() != null) ? pedido.getImporte() : 0.0f);
            psPedido.setFloat(4, (pedido.getIva() != null) ? pedido.getIva() : 0.0f);

            int filasPedido = psPedido.executeUpdate();
            System.out.println("Insertando Pedido: Usuario=" + pedido.getUsuario().getIdUsuario() + " Importe=" + pedido.getImporte() + " iva= " + pedido.getIva());

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
    public Pedido getCestaByUsuario(Usuario usuario) {
        Pedido cesta = null;
        List<LineaPedido> lineas = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM pedidos WHERE idusuario = ? AND estado = 'c'";

        try {
            connection = ConnectionFactory.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setShort(1, usuario.getIdUsuario());
            rs = ps.executeQuery();

            if (rs.next()) {
                cesta = mapearPedido(rs);
                cesta.setUsuario(usuario);

                // IMPORTANTE: Ahora necesitamos cargar sus líneas a través del idPedido, y después asignarlas antes de devolverlo
                lineas = getLineasPedido(cesta.getIdPedido(), connection);
                cesta.setLineas(lineas);
                cesta.calcularTotales(); // Sincronizamos totales antes de devolver la cesta
            }
        } catch (SQLException e) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error al recuperar cesta de BD", e);
        } finally {
            this.closeConnection();
        }
        return cesta;
    }

    @Override
    public boolean updateCantidadLinea(short idPedido, short idProducto, int cantidad) {
        String sql = "UPDATE lineaspedidos SET cantidad = ? WHERE idpedido = ? AND idproducto = ?";
        try (Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, cantidad);
            ps.setShort(2, idPedido);
            ps.setShort(3, idProducto);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error al actualizar cantidad de línea", e);
            return false;
        }
    }

    @Override
    public boolean insertarLineaIndividual(LineaPedido lp) {
        String sql = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setShort(1, lp.getIdPedido());
            ps.setShort(2, lp.getProducto().getIdProducto());
            ps.setInt(3, lp.getCantidad());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error al insertar línea individual", e);
            return false;
        }
    }

    @Override
public boolean deleteLineaIndividual(short idPedido, short idProducto) {
    String sqlBorrarLp = "DELETE FROM lineaspedidos WHERE idpedido = ? AND idproducto = ?";
    String sqlCountLineas = "SELECT COUNT(*) FROM lineaspedidos WHERE idpedido = ?";
    String sqlBorrarP = "DELETE FROM pedidos WHERE idpedido = ?";

    try (Connection con = ConnectionFactory.getConnection();
         PreparedStatement psDelete = con.prepareStatement(sqlBorrarLp)) {
        
        // Intentamos borrar la línea
        psDelete.setShort(1, idPedido);
        psDelete.setShort(2, idProducto);
        int filasBorradas = psDelete.executeUpdate();

        if (filasBorradas > 0) {
            // Comprobamos cuántas líneas le quedan al pedido
            try (PreparedStatement psCount = con.prepareStatement(sqlCountLineas)) {
                psCount.setShort(1, idPedido);
                try (ResultSet rs = psCount.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // Si no quedan líneas, borramos el pedido (la cabecera)
                        try (PreparedStatement psDelPed = con.prepareStatement(sqlBorrarP)) {
                            psDelPed.setShort(1, idPedido);
                            psDelPed.executeUpdate();       
                        }
                    }
                }
            }
        }
        return filasBorradas > 0;

    } catch (SQLException e) {
        Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error al eliminar línea o pedido vacío", e);
        return false;
    }
}

    @Override
    public boolean updateTotalesPedido(Pedido p) {
        String sql = "UPDATE pedidos SET importe = ?, iva = ? WHERE idpedido = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setFloat(1, p.getImporte());
            ps.setFloat(2, p.getIva());
            ps.setShort(3, p.getIdPedido());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error al actualizar totales del pedido", e);
            return false;
        }
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
        // El usuario se asigna con el parámetro de entrada del método padre.
        return pedido;
    }
    
    // Método auxiliar privado para no repetir código
    private List<LineaPedido> getLineasPedido(short idPedido, Connection connection) throws SQLException {
        // Hereda la conexión de la consulta de la cesta por id, para reutilizar el hilo
        List<LineaPedido> lineas = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        // La consulta es un join entre las tablas lineaspedidos, productos (para asignar el Producto de cada línea)
        // y categorias (para asignar la Categoría a cada Producto).
        String sql = "SELECT lp.*, "
                + "p.nombre AS prodNombre, p.descripcion, p.precio, p.marca, p.imagen AS prodImagen, "
                + "c.idCategoria, c.nombre AS catNombre, c.imagen AS catImagen "
                + "FROM lineaspedidos lp "
                + "JOIN productos p ON lp.idproducto = p.idproducto "
                + "LEFT JOIN categorias c ON p.idCategoria = c.idCategoria "
                + "WHERE lp.idpedido = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setShort(1, idPedido);
            rs = ps.executeQuery();

            while (rs.next()) {
                lineas.add(LineaPedidoDAO.mapearLineaPedido(rs));
                // Este método ya asigna internamente el Producto a cada línea con el método de mapearProducto de ProductoDAO
            }
        } catch (SQLException e) {
            Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, "Error al recuperar cesta de BD", e);
        } // Aquí no se cierra la conexión, sino en el método padre, al terminar la consulta.
        return lineas;
    }
}
