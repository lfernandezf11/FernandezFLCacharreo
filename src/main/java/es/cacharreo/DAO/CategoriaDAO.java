package es.cacharreo.DAO;

import es.cacharreo.beans.Categoria;
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
public class CategoriaDAO implements ICategoriaDAO {

    @Override
    public List<Categoria> getCategorias() {
        List<Categoria> listaCategorias = null;
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparada = null;
        String sql = "SELECT idCategoria, nombre, imagen FROM categorias ORDER BY nombre ASC";
        // Recuperamos con orden alfabético para mostrar en jsp

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            rs = preparada.executeQuery();

            listaCategorias = new ArrayList<>();

            while (rs.next()) {
                Categoria categoria = new Categoria();

                // Mapeo de columnas de la tabla a los atributos del Bean
                categoria.setIdCategoria(rs.getByte("idCategoria"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setImagen(rs.getString("imagen"));

                listaCategorias.add(categoria);
            }
        } catch (SQLException e) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            this.closeConnection();
        }

        return listaCategorias;
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

}
