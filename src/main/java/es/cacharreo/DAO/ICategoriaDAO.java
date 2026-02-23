
package es.cacharreo.DAO;

import es.cacharreo.beans.Categoria;
import java.util.List;

/**
 *
 * @author fdezf
 */
public interface ICategoriaDAO {
    public List<Categoria> getCategorias();
     public void closeConnection();
}
