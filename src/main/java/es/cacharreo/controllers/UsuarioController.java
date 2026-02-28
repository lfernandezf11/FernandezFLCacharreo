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
 *
 * @author fdezf
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
            case "inicio": 
                url= "/index.jsp";
                break; 
                
                
            case "historial":
               url= "/JSP/usuario/historial.jsp";
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
