package es.cacharreo.DAO;

/**
 * Fábrica de conexiones para acceder a la base de datos mediante JNDI. Gestiona
 * dos pools de conexiones: uno para usuarios normales y otro para
 * administradores.
 *
 * @author Lucía Fernández Florencio
 */
import java.sql.Connection;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory {

    /*
     * DataSource y Connection como variables estáticas globales
     * para compartir el pool de conexiones entre métodos
     */
    static DataSource dataSource = null;
    static Connection conexion = null;

    static final String DATASOURCE_NAME = "java:comp/env/jdbc/cacharreo";
    

    public static Connection getConnection() {

        try {
            Context contextoInicial = new InitialContext();
            dataSource = (DataSource) contextoInicial.lookup(DATASOURCE_NAME);
            conexion = dataSource.getConnection();
        } catch (NamingException | SQLException ex) {
            // Existe un error al intentar crear el pool de conexiones. Escribimos el logger y se visualiza error500.jsp
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);

        }
        return conexion; 
    }

   
    /**
     * Cierra la conexión activa del pool (abandona el hilo).
     * Método necesario para liberar recursos del pool.
     */
    public static void closeConnection() {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
