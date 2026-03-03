package es.cacharreo.controllers;

import es.cacharreo.DAO.IPedidoDAO;
import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Producto;
import es.cacharreo.beans.Usuario;
import es.cacharreo.models.Cookies;
import es.cacharreo.models.ProductoUtils;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador principal para la gestión síncrona de la cesta de la compra y el
 * catálogo.
 * <p>
 * Este Servlet centraliza las operaciones de alto nivel que requieren
 * navegación entre páginas, como el vaciado total del carrito, el filtrado
 * avanzado de productos y la finalización del proceso de compra (tramitación
 * del pedido).</p>
 *
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
@WebServlet(name = "CestaController", urlPatterns = {"/CestaController"})
public class CestaController extends HttpServlet {

    /**
     * Gestiona las peticiones GET redirigiendo a la vista de la cesta.
     *
     * @param request La petición HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el despacho de la vista.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/pedido/error404.jsp").forward(request, response);

    }

    /**
     * Orquestador de acciones de post-formulario.
     * <p>
     * Implementa un switch para manejar tres casos de uso:</p>
     * <ul>
     * <li><strong>eliminarCarrito</strong>: Limpia sesión, cookies y base de
     * datos (si procede).</li>
     * <li><strong>filtrarProductos</strong>: Procesa filtros de búsqueda y
     * devuelve el catálogo actualizado.</li>
     * <li><strong>tramitarPedido</strong>: Convierte la cesta temporal en un
     * pedido finalizado en el sistema.</li>
     * </ul>
     *
     *
     *
     * @param request La petición HTTP con los parámetros de acción y datos.
     * @param response La respuesta HTTP para redirecciones o forwards.
     * @throws ServletException Si ocurre un error en el flujo del servlet.
     * @throws IOException Si ocurre un error de escritura.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String accion = request.getParameter("accion");
        String url = "/JSP/pedido/cesta.jsp";

        if (accion == null) {
            request.setAttribute("error", "Acción no válida o sesión expirada.");
            request.getRequestDispatcher("/JSP/aviso/aviso.jsp").forward(request, response);
            return;
        }

        Pedido cesta = (Pedido) session.getAttribute("cesta");
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        switch (accion) {
            case "eliminarCarrito":
                if (cesta != null) {
                    // Determina si la sesión tiene que limpiarse. Verdadero por defecto para los usuarios anónimos
                    boolean limpiarSesion = true;

                    /* Manejo de USUARIO LOGUEADO */
                    if (usuario != null) {
                        // Si no hay ID, el pedido solo está en sesión, no hay nada que borrar en BD

                        if (cesta.getIdPedido() == null) {
                            limpiarSesion = true;
                        } else {
                            try {
                                DAOFactory daof = DAOFactory.getDAOFactory();
                                IPedidoDAO pDAO = daof.getPedidoDAO();

                                // Solo limpiamos la sesión si el borrado en BD fue efectivo (es decir, el pedido existía y estaba en estado 'c')
                                limpiarSesion = pDAO.borrarPedido(cesta.getIdPedido());

                                if (!limpiarSesion) {
                                    request.setAttribute("error", "No se pudo vaciar la base de datos: el pedido ya no es un carrito activo.");
                                }
                            } catch (Exception e) {
                                limpiarSesion = false;
                                request.setAttribute("error", "Error técnico al intentar limpiar la base de datos.");
                            }
                        }
                    }
                    /* LIMPIEZA DE CESTA (sesión y cookies) */
                    if (limpiarSesion) {
                        // Eliminamos la cookie independientemente de si hay usuario o no (limpieza total)
                        Cookie cBorrar = Cookies.generarCookie("cestaCookie", "", 0, request);
                        response.addCookie(cBorrar);

                        // Reseteamos el objeto en sesión como cesta vacía pero inicializada
                        cesta.getLineas().clear();
                        cesta.setIdPedido(null);
                        cesta.calcularTotales(); // importes a cero

                        request.setAttribute("exito", "La cesta se ha vaciado correctamente.");
                    }
                } else {
                    request.setAttribute("warning", "La cesta ya estaba vacía.");
                }
                break;

            case "filtrarProductos":
                String[] categorias = request.getParameterValues("fCategoria");
                String[] marcas = request.getParameterValues("fMarca");
                float min = Float.parseFloat(request.getParameter("fPrecioMin"));
                float max = Float.parseFloat(request.getParameter("fPrecioMax"));
                String texto = request.getParameter("fTexto");

                try {
                    DAOFactory daof = DAOFactory.getDAOFactory();
                    IProductoDAO pDAO = daof.getProductoDAO();

                    // Refrescamos la página inicial antes de filtrar los productos. ¿Por qué? 
                    // Porque ProductoUtils.prepararSubcatalogo() establece un atributo de sesión "productosFiltrados"
                    // por defecto (con los 8 productos aleatorios), y tenemos que sobreescribirlo para devolver el 
                    // resultado real del filtrado.
                    ProductoUtils.prepararSubcatalogo(request);
                    List<Producto> filtrados = pDAO.getProductosFiltrados(categorias, marcas, min, max, texto);

                    request.setAttribute("productosFiltrados", filtrados);
                    url = "/index.jsp";
                } catch (Exception e) {
                    request.setAttribute("error", "Error al procesar los filtros.");
                }
                break;

            case "tramitarPedido":
                if (usuario == null) {
                    request.setAttribute("warning", "Tienes que iniciar sesión para completar tu pedido.");
                    url = "/JSP/usuario/login.jsp";
                } else {
                    // Verificamos que exista el objeto cesta en sesión y tenga líneas
                    if (cesta != null && !cesta.getLineas().isEmpty()) {
                        IPedidoDAO pDAO = DAOFactory.getDAOFactory().getPedidoDAO();

                        // Si el pedido no tiene ID pero el usuario está logueado,
                        // significa que es una cesta de sesión que aún no se ha persistido.
                        if (cesta.getIdPedido() == null) {
                            cesta.setFecha(new java.util.Date());
                            pDAO.insertarCesta(cesta); // Esto le asignará el ID generado por la BD
                        }

                        // Ahora que estamos seguros de que tiene ID, finalizamos
                        if (cesta.getIdPedido() != null) {
                            // El método actualiza la fecha y estado en bbdd y sesión
                            // Necesario actualizar en sesión para poder pasarlo a la request para el resumen en resumenPedido.jsp
                            boolean compraExitosa = pDAO.finalizarPedido(cesta);

                            if (compraExitosa) {
                                // Pasamos el pedido a la request antes de limpiar la sesión
                                request.setAttribute("pedidoFinalizado", cesta);
                                session.removeAttribute("cesta");

                                // Seteamos una nueva cesta vacía para futuras compras en la misma sesión
                                Pedido nuevaCesta = new Pedido();
                                nuevaCesta.setUsuario(usuario);
                                session.setAttribute("cesta", nuevaCesta);

                                url = "/JSP/pedido/resumenPedido.jsp";
                            } else {
                                request.setAttribute("error", "No se pudo finalizar la compra en la base de datos.");
                                url = "/JSP/pedido/cesta.jsp";
                            }
                        } else {
                            request.setAttribute("error", "Error al registrar la cesta en el sistema.");
                        }
                    } else {
                        request.setAttribute("warning", "Tu cesta está vacía.");
                        url = "/JSP/pedido/cesta.jsp";
                    }
                }
                break;
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Retorna una breve descripción del propósito de este Servlet.
     *
     * @return String descriptivo del controlador de flujo de cesta.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
