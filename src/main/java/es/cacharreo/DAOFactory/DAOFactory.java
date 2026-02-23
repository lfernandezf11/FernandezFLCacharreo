package es.cacharreo.DAOFactory;

import es.cacharreo.DAO.ICategoriaDAO;
import es.cacharreo.DAO.ILineaPedidoDAO;
import es.cacharreo.DAO.IPedidoDAO;
import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAO.IUsuarioDAO;

/**
 *
 * @author fdezf
 */
public abstract class DAOFactory {

    // Una clase abstracta por cada tabla de la base de datos
    /**
     * @return Interface de las operaciones a realizar con la tabla usuarios
     */
    public abstract IUsuarioDAO getUsuarioDAO();

    public abstract IProductoDAO getProductoDAO();

    public abstract ICategoriaDAO getCategoriaDAO();

    public abstract IPedidoDAO getPedidoDAO();

    public abstract ILineaPedidoDAO getLineaPedidoDAO();


    public static DAOFactory getDAOFactory() {

        DAOFactory daof = null;

        daof = new MySQLDAOFactory();

        return daof;

    }

}
