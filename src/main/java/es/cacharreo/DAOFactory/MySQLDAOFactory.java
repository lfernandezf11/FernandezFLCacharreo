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
 * Fábrica concreta para la fuente de datos MySQL
 * @author fdezf
 */
public class MySQLDAOFactory extends DAOFactory{

    @Override
    public IUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }

    @Override
    public IProductoDAO getProductoDAO() {
        return new ProductoDAO();
    }

    @Override
    public ICategoriaDAO getCategoriaDAO() {
       return new CategoriaDAO();
    }

    @Override
    public IPedidoDAO getPedidoDAO() {
        return new PedidoDAO();
    }

    @Override
    public ILineaPedidoDAO getLineaPedidoDAO() {
        return new LineaPedidoDAO();
    }
   
}