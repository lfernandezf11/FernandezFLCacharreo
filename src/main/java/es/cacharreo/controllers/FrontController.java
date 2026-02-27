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
 * Controlador principal. Constituye el punto de entrada a la aplicación y
 * redirige el flujo de la misma.
 *
 * Cuando se inicia la aplicación, carga la vista inicial (index.jsp), o el menú
 * correspondiente al usuario (menuNormal o menuAdmin) si éste seleccionó
 * 'Recordar usuario' y existe una cookie con su username.
 *
 * @author fdezf
 */
@WebServlet(name = "FrontController", urlPatterns = {"", "/FrontController"})
public class FrontController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
            request.getRequestDispatcher("/JSP/aviso/aviso.jsp").forward(request, response);
            return;
        }

        switch (accion) {
            case "inicio":
                ProductoUtils.prepararSubcatalogo(request); // Maneja la recarga de 8 productos aleatorios.
                url = "/index.jsp";
                break;

            case "perfil":
                url = "/JSP/usuario/perfil.jsp";
                break;

            case "productos":
                url = "/JSP/pedido/productos.jsp";
                break;

            case "registro":
                url = "/JSP/usuario/registro.jsp";
                break;

            case "login":
                url = "/JSP/usuario/login.jsp";
                break;

            case "logout":
                usuario = (Usuario) session.getAttribute("usuarioLogueado");
                if (usuario != null) {
                    uDAO.updateUltimoAcceso(usuario.getIdUsuario());
                }
                session.invalidate(); // imnpia todo, con lo que antes de redirigir hay que resetear la home

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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
