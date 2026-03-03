package es.cacharreo.DAO;

import es.cacharreo.beans.Producto;
import java.util.List;

/**
 *
 * @author fdezf
 */
public interface IProductoDAO {
    public List<Producto> getProductos(); // no 
    public List<Producto> getProductosRandom(int limite);
    public List<Producto> getProductosByIds(List<Short>ids);
    public Producto getProductoUnicoById(Short idProducto); 
    
    public List<String> getMarcas();
public List<Float> getPreciosLimite();
public List<Producto> getProductosFiltrados(String[] categorias, String[] marcas, float min, float max, String texto);
    public void closeConnection();
}

