package es.cacharreo.listeners;

import es.cacharreo.DAO.IUsuarioDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Usuario;
import java.sql.Timestamp;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 *
 * @author fdezf
 */
public class ListenersList implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        /*if ("usuarioLogueado".equals(event.getName())) {
            Usuario usuario = (Usuario) event.getValue();
            if (usuario != null) {
                DAOFactory daof = DAOFactory.getDAOFactory();
                IUsuarioDAO uDAO = daof.getUsuarioDAO();
                
                Timestamp ultimoAcceso = uDAO.updateUltimoAcceso(usuario.getIdUsuario()); // Actualiza el campo en la BD y lo devuelve
                usuario.setUltimoAcceso(ultimoAcceso); // Sincroniza el bean
            }
        }*/
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }




}
