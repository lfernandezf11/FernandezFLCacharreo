package es.cacharreo.filters;

/**
 *
 * @author Lucía Fernández Florencio
 */
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Filtro de interceptación para forzar la codificación de caracteres en UTF-8.
 * <p>
 * Este componente actúa en la capa de entrada de la aplicación para asegurar
 * que todos los parámetros de las peticiones HTTP (POST/GET) sean interpretados
 * correctamente, evitando problemas de visualización con caracteres especiales
 * propios del castellano (acentos, eñes, etc.).</p>
 *
 *
 *
 * @author fdezf
 * @version 1.0
 */
@WebFilter(filterName = "UTF8Filter", urlPatterns = {"/*"})
public class UTF8Filter implements Filter {

    private String encoding;

    /**
     * Inicializa el filtro configurando el encoding deseado.
     * <p>
     * Busca un parámetro inicial de configuración; de lo contrario, establece
     * por defecto el estándar UTF-8.</p>
     *
     * @param filterConfig Configuración del filtro obtenida del descriptor de
     * despliegue.
     * @throws ServletException Si ocurre un error durante la inicialización.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /*
        * Preguntamos si existe el parámetro inicial "requestEncoding" para, en caso contrario, asignarle el valor UTF-8
         */
        encoding = filterConfig.getInitParameter("requestEncoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    /**
     * Limpieza de recursos cuando el contenedor de servlets destruye el filtro.
     * Actualmente no requiere liberación de recursos específicos.
     */
    @Override
    public void destroy() {
        // Método implementado por contrato de interfaz
    }

    /**
     * Proceso principal de filtrado.
     * <p>
     * Establece la codificación de caracteres en la petición antes de que esta
     * llegue a los Servlets o JSPs de destino. Una vez configurada, cede el
     * control al siguiente eslabón de la cadena (Filter Chain).</p>
     *
     * @param request La petición del cliente.
     * @param response La respuesta del servidor.
     * @param chain Objeto para invocar al siguiente filtro o recurso destino.
     * @throws IOException Si ocurre un error de entrada/salida.
     * @throws ServletException Si la petición no puede ser procesada.
     * @see FilterChain#doFilter(ServletRequest, ServletResponse)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);

        /*
        * Reenviamos la petción al componente web destino que estabamos filtrando 
         */
        chain.doFilter(request, response);
    }

}
