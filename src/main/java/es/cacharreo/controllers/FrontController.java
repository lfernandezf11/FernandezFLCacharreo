package es.cacharreo.controllers;

import es.cacharreo.DAO.ICategoriaDAO;
import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAO.IUsuarioDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Categoria;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Usuario;
import es.cacharreo.models.CestaUtils;
import es.cacharreo.models.Cookies;
import es.cacharreo.models.ProductoUtils;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador Principal (Front Controller) de la aplicación.
 * <p>
 * Actúa como el punto de entrada unificado para todas las peticiones,
 * gestionando el flujo de navegación, la inicialización de recursos críticos y
 * el mantenimiento del estado de la sesión (como la cesta de la compra).</p>
 *
 * <p>
 * <strong>Responsabilidades clave:</strong></p>
 * <ul>
 * <li>Carga del catálogo inicial y datos de filtrado en el contexto de
 * aplicación.</li>
 * <li>Recuperación de la cesta persistida mediante cookies para usuarios
 * anónimos.</li>
 * <li>Gestión del ciclo de vida del usuario (navegación a registro, login y
 * logout).</li>
 * </ul>
 *
 *
 *
 * @author fdezf
 * @version 1.0
 */
@WebServlet(name = "FrontController", urlPatterns = {"", "/FrontController"})
public class FrontController extends HttpServlet {

    /**
     * Procesa las peticiones iniciales (GET) al acceder a la aplicación.
     * <p>
     * Realiza la hidratación de datos necesaria para la Home:</p>
     * <ol>
     * <li>Carga una subselección aleatoria de productos.</li>
     * <li>Sincroniza la cesta de la compra con la cookie del cliente.</li>
     * <li>Carga en el {@link ServletContext} los metadatos de filtrado
     * (categorías, marcas y rangos de precio) solo si no están presentes,
     * optimizando el acceso a la base de datos.</li>
     * </ol>
     *
     * @param request Petición HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException Si ocurre un error en el despacho del Servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /* 
            Al iniciar la aplicación:
            1. Recuperamos 8 productos aleatorios para mostrarlos en la landingPage. 
         */
        DAOFactory daof = DAOFactory.getDAOFactory();
        IProductoDAO pDAO = daof.getProductoDAO();

        ProductoUtils.prepararSubcatalogo(request); // Maneja la carga en request de 8 productos aleatorios para refrescar la Home.

        /* 
         2. Comprobamos si existe una cesta previa almacenada como cookie y recuperamos su valor.
         */
        HttpSession session = request.getSession();
        String cestaStr = Cookies.recuperarCookieValue(request, "cestaCookie");

        /* 
         3. Construimos el pedido a partir del valor recuperado. 
            Este método ya controla cestaStr nulo o vacío, devolviendo un Pedido inicializado.*/
        Pedido carritoActual = CestaUtils.cargarPedidoDesdeCookieStr(cestaStr);
        session.setAttribute("cesta", carritoActual);

        /* 
        3. Almacenamos en contexto de aplicación, de cara a implementar los filtros, tanto las categorías como las marcas.
         */
        ServletContext ctx = request.getServletContext();

        List<Categoria> categorias = (List<Categoria>) ctx.getAttribute("categorias");
        List<String> marcas = (List<String>) ctx.getAttribute("marcas");
        Float maxPrecio = (Float) ctx.getAttribute("maxPrecio");
        Float minPrecio = (Float) ctx.getAttribute("minPrecio");

        if (categorias == null) {
            ICategoriaDAO cDAO = daof.getCategoriaDAO();
            categorias = cDAO.getCategorias();
            ctx.setAttribute("categorias", categorias);
        }

        if (marcas == null) {
            marcas = pDAO.getMarcas();
            ctx.setAttribute("marcas", marcas);
        }

        if (maxPrecio == null || minPrecio == null) {
            List<Float> limites = pDAO.getPreciosLimite();
            if (limites.size() == 2) {
                ctx.setAttribute("minPrecio", limites.get(0));
                ctx.setAttribute("maxPrecio", limites.get(1));
            }
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    /**
     * Gestiona las peticiones de navegación y acciones de usuario (POST).
     * <p>
     * Utiliza un sistema de encaminamiento basado en el parámetro
     * {@code accion} para redirigir al usuario a las vistas de registro, login,
     * perfil o para gestionar el cierre de sesión.</p>
     *
     * @param request Petición HTTP con el parámetro 'accion'.
     * @param response Respuesta HTTP.
     * @throws ServletException Si ocurre un error en el flujo de control.
     * @throws IOException Si ocurre un error de escritura.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        DAOFactory daof = DAOFactory.getDAOFactory();
        IUsuarioDAO uDAO = daof.getUsuarioDAO();
        Usuario usuario = null;

        String url = null;
        String accion = request.getParameter("accion");

        if (accion == null) {
            request.setAttribute("error", "Acción no válida o sesión expirada.");
            request.getRequestDispatcher("/JSP/aviso/error404.jsp").forward(request, response);
            return;
        }

        switch (accion) {
            case "inicio":
                ProductoUtils.prepararSubcatalogo(request); // Maneja la recarga de 8 productos aleatorios.
                url = "/index.jsp";
                break;

            case "registro":
                url = "/JSP/usuario/registro.jsp";
                break;

            case "login":
                url = "/JSP/usuario/login.jsp";
                break;

            case "perfil":
                url = "/JSP/usuario/perfil.jsp";
                break;

            case "logout":
                usuario = (Usuario) session.getAttribute("usuarioLogueado");
                if (usuario != null) {
                    uDAO.updateUltimoAcceso(usuario.getIdUsuario());
                }
                session.invalidate(); // limpia todo, con lo que antes de redirigir hay que resetear la home

                // "Entorno de invitado"
                HttpSession nuevaSesion = request.getSession(true);
                nuevaSesion.setAttribute("cesta", new Pedido()); // El constructor por defecto inicializa sin nulos y con importes a 0.
                ProductoUtils.prepararSubcatalogo(request);

                url = "/index.jsp";
                break;

            case "verCesta":
                url = "/JSP/pedido/cesta.jsp";
                break;

            default:
                ProductoUtils.prepararSubcatalogo(request);
                url = "/index.jsp";
                break;
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Retorna una breve descripción del propósito de este Front Controller.
     *
     * @return String descriptivo del Servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
