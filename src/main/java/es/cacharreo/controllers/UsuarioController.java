package es.cacharreo.controllers;

import es.cacharreo.DAO.IPedidoDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Lucía Fernández Florencio
 */
@WebServlet(name = "UsuarioController", urlPatterns = {"/UsuarioController"})
public class UsuarioController extends HttpServlet {

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
        IPedidoDAO pDAO = daof.getPedidoDAO();

        Usuario usuario = null;
        String accion = request.getParameter("accion");
        String url = "/JSP/pedido/cesta.jsp";

        if (accion == null) {
            request.setAttribute("error", "Acción no válida o sesión expirada.");
            request.getRequestDispatcher("/JSP/aviso/aviso.jsp").forward(request, response);
            return;
        }

        switch (accion) {
            case "inicio":
                url = "/FrontController";
                break;

            case "historial":
                usuario = (Usuario) session.getAttribute("usuarioLogueado");

                if (usuario != null) {
                    List<Pedido> misPedidos = pDAO.getHistorialPedidos(usuario.getIdUsuario());

                    request.setAttribute("historialPedidos", misPedidos);
                    url = "/JSP/usuario/historial.jsp";
                } else {
                    request.setAttribute("error", "Debes estar identificado para ver tu historial.");
                    url = "/JSP/usuario/login.jsp";
                }
                break;

            default:
                url = "/index.jsp";
                break;
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Retorna una breve descripción del propósito de este Servlet.
     *
     * @return String descriptivo del controlador del historial de usuario.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}