package es.cacharreo.DAOFactory;

import es.cacharreo.DAO.ICategoriaDAO;
import es.cacharreo.DAO.ILineaPedidoDAO;
import es.cacharreo.DAO.IPedidoDAO;
import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAO.IUsuarioDAO;

/**
 * Clase abstracta que define la factoría principal para la creación de DAOs.
 * <p>
 * Implementa el patrón de diseño <b>Abstract Factory</b>, permitiendo que la
 * aplicación sea independiente de la implementación específica de la
 * persistencia (por ejemplo, facilitando el cambio entre MySQL, Oracle o
 * servicios en la nube).</p>
 *
 *
 *
 * @author fdezf
 * @version 1.0
 */
public abstract class DAOFactory {

    /**
     * Obtiene la implementación para las operaciones de la tabla usuarios.
     *
     * @return Interfaz {@link IUsuarioDAO}.
     */
    public abstract IUsuarioDAO getUsuarioDAO();

    /**
     * Obtiene la implementación para las operaciones de la tabla productos.
     *
     * @return Interfaz {@link IProductoDAO}.
     */
    public abstract IProductoDAO getProductoDAO();

    /**
     * Obtiene la implementación para las operaciones de la tabla categorías.
     *
     * @return Interfaz {@link ICategoriaDAO}.
     */
    public abstract ICategoriaDAO getCategoriaDAO();

    /**
     * Obtiene la implementación para las operaciones de la tabla pedidos.
     *
     * @return Interfaz {@link IPedidoDAO}.
     */
    public abstract IPedidoDAO getPedidoDAO();

    /**
     * Obtiene la implementación para las operaciones de la tabla líneas de
     * pedido.
     *
     * @return Interfaz {@link ILineaPedidoDAO}.
     */
    public abstract ILineaPedidoDAO getLineaPedidoDAO();

    /**
     * Método estático encargado de instanciar la factoría concreta.
     * <p>
     * Actualmente, devuelve una instancia de {@link MySQLDAOFactory}. Si en el
     * futuro se añadieran otros motores de base de datos, este método
     * centralizaría la lógica de selección de la implementación correcta.</p>
     *
     * @return Instancia concreta de {@code DAOFactory}.
     */
    public static DAOFactory getDAOFactory() {
        DAOFactory daof = null;

        // Instanciación de la implementación concreta para MySQL
        daof = new MySQLDAOFactory();

        return daof;
    }
}
