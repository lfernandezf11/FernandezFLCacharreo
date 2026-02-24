package es.cacharreo.DAO;

import es.cacharreo.beans.Categoria;
import es.cacharreo.beans.Producto;
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
public class ProductoDAO implements IProductoDAO {

    @Override
    public List<Producto> getProductos() {
        List<Producto> catalogo = null;
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparada = null;
        String sql = "SELECT p.idProducto, p.nombre as prodNombre, p.descripcion, p.precio, p.marca, p.imagen AS prodImagen, "
                + "c.idCategoria, c.nombre AS catNombre, c.imagen AS catImagen "
                + "FROM productos p "
                + "LEFT JOIN categorias c ON p.idCategoria = c.idCategoria"; // LEFT JOIN por si el producto no tiene categoría (es nullable)

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            rs = preparada.executeQuery();

            catalogo = new ArrayList<>();

            while (rs.next()) {
                catalogo.add(mapearProducto(rs)); // Este método controla la construcción de un objeto Producto a partir de una tupla del ResultSet.
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            this.closeConnection();
        }
        return catalogo;
    }

    @Override
    public List<Producto> getProductosRandom(int limite) {
        List<Producto> subCatalogo = null;
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparada = null;

        // SQL con ordenación aleatoria y límite de 8 registros
        String sql = "SELECT p.idProducto, p.nombre as prodNombre, p.descripcion, p.precio, p.marca, p.imagen AS prodImagen, "
                + "c.idCategoria, c.nombre AS catNombre, c.imagen AS catImagen "
                + "FROM productos p "
                + "LEFT JOIN categorias c ON p.idCategoria = c.idCategoria "
                + "ORDER BY RAND() "
                + "LIMIT ?";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setInt(1, limite);
            rs = preparada.executeQuery();

            subCatalogo = new ArrayList<>();

            while (rs.next()) {
                subCatalogo.add(mapearProducto(rs)); // Este método controla la construcción de un objeto Producto a partir de una tupla del ResultSet.
            }

        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, "Error al obtener productos al azar", e);
        } finally {
            this.closeConnection();
        }
        return subCatalogo;
    }

    @Override
    public Producto getProductoUnicoById(Short idProducto) {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparada = null;
        Producto prod = null;
        String sql = "SELECT p.idProducto, p.nombre AS prodNombre, p.descripcion, p.precio, p.marca, p.imagen AS prodImagen, "
                + "c.idCategoria, c.nombre AS catNombre, c.imagen AS catImagen "
                + "FROM productos p "
                + "LEFT JOIN categorias c ON p.idCategoria = c.idCategoria "
                + "WHERE p.idProducto = ?";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idProducto);
            rs = preparada.executeQuery();

            if (rs.next()) {
                prod = mapearProducto(rs); // Este método controla la construcción de un objeto Producto a partir de una tupla del ResultSet.
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, "Error al buscar producto por ID: " + idProducto, e);
        }
        return prod;
    }

    @Override
    public List<Producto> getProductosByIds(List<Short> ids) {
        List<Producto> lista = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return lista;
        }

        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparada = null;

        // Generamos los placeholders (?, ?, ?) a partir de ids.size()
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            sb.append("?");
            if (i < ids.size() - 1) {
                sb.append(",");
            }
        }

        String params = sb.toString();

        String sql = "SELECT p.idProducto, p.nombre AS prodNombre, p.descripcion, p.precio, p.marca, p.imagen AS prodImagen, "
                + "c.idCategoria, c.nombre AS catNombre, c.imagen AS catImagen "
                + "FROM productos p "
                + "LEFT JOIN categorias c ON p.idCategoria = c.idCategoria "
                + "WHERE p.idProducto IN (" + params + ")";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);

            // Recorremos la sentencia asignando el correspondiente id de la lista que entra por parámetro
            for (int i = 0; i < ids.size(); i++) {
                preparada.setShort(i + 1, ids.get(i));
            }

            rs = preparada.executeQuery();

            while (rs.next()) {
                lista.add(mapearProducto(rs)); // Este método controla la construcción de un objeto Producto a partir de una tupla del ResultSet.
            }

        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, "Error al recuperar productos por lista de IDs", e);
        } finally {
            this.closeConnection();
        }
        return lista;
    }

    @Override
    public List<String> getMarcas() {
        List<String> listaMarcas = new ArrayList<>();
        String sql = "SELECT DISTINCT marca FROM productos WHERE marca IS NOT NULL ORDER BY marca ASC";
        // Recuperamos las marcas ordenadas alfabéticamente para que el filtro en el jsp esté ordenado.

        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet rs = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            rs = preparada.executeQuery();

            while (rs.next()) {
                listaMarcas.add(rs.getString("marca"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, "Error al obtener marcas", e);
        } finally {
            this.closeConnection();
        }

        return listaMarcas;
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

    public static Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto prod = new Producto();
        prod.setIdProducto(rs.getShort("idProducto"));
        prod.setNombre(rs.getString("prodNombre"));
        prod.setDescripcion(rs.getString("descripcion"));
        prod.setPrecio(rs.getFloat("precio"));
        prod.setMarca(rs.getString("marca"));
        prod.setImagen(rs.getString("prodImagen"));

        /* 
                Comprobamos el valor del idCategoria para instanciar o no Categoria para el producto.
                Lo hacemos porque si el valor null en la base de datos, idCategoria adoptaría el valor por defecto de byte: 0.
                no null.
         */
        byte idCat = rs.getByte("idCategoria");
        if (!rs.wasNull()) {
            Categoria cat = new Categoria();
            cat.setIdCategoria(idCat);
            cat.setNombre(rs.getString("catNombre"));
            cat.setImagen(rs.getString("catImagen"));
            prod.setCategoria(cat);
        }
        return prod;
    }

}
