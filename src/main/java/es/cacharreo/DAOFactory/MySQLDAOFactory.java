package es.cacharreo.DAOFactory;

import es.cacharreo.DAO.CategoriaDAO;
import es.cacharreo.DAO.ICategoriaDAO;
import es.cacharreo.DAO.ILineaPedidoDAO;
import es.cacharreo.DAO.IPedidoDAO;
import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAO.UsuarioDAO;
import es.cacharreo.DAO.IUsuarioDAO;
import es.cacharreo.DAO.LineaPedidoDAO;
import es.cacharreo.DAO.PedidoDAO;
import es.cacharreo.DAO.ProductoDAO;

/**
 * Fábrica concreta encargada de instanciar los DAOs para una base de datos MySQL.
 * <p>Esta clase extiende {@link DAOFactory} y cumple con el contrato de la factoría 
 * abstracta devolviendo las implementaciones específicas (JDBC/SQL) para cada entidad 
 * del sistema.</p>
 * 
 * <p>Al centralizar aquí las instanciaciones de {@code UsuarioDAO}, {@code ProductoDAO}, 
 * etc., se permite que el resto de la aplicación trabaje únicamente con interfaces, 
 * siguiendo el principio de inversión de dependencia.</p>
 * 
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public class MySQLDAOFactory extends DAOFactory {

    /**
     * Instancia y devuelve un objeto para la gestión de usuarios en MySQL.
     * @return Implementación concreta {@link UsuarioDAO}.
     */
    @Override
    public IUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }

    /**
     * Instancia y devuelve un objeto para la gestión de productos en MySQL.
     * @return Implementación concreta {@link ProductoDAO}.
     */
    @Override
    public IProductoDAO getProductoDAO() {
        return new ProductoDAO();
    }

    /**
     * Instancia y devuelve un objeto para la gestión de categorías en MySQL.
     * @return Implementación concreta {@link CategoriaDAO}.
     */
    @Override
    public ICategoriaDAO getCategoriaDAO() {
       return new CategoriaDAO();
    }

    /**
     * Instancia y devuelve un objeto para la gestión de pedidos en MySQL.
     * @return Implementación concreta {@link PedidoDAO}.
     */
    @Override
    public IPedidoDAO getPedidoDAO() {
        return new PedidoDAO();
    }

    /**
     * Instancia y devuelve un objeto para la gestión de líneas de pedido en MySQL.
     * @return Implementación concreta {@link LineaPedidoDAO}.
     */
    @Override
    public ILineaPedidoDAO getLineaPedidoDAO() {
        return new LineaPedidoDAO();
    }
}