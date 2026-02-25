/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.cacharreo.controllers;

import es.cacharreo.DAO.IPedidoDAO;
import es.cacharreo.DAO.IProductoDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Usuario;
import es.cacharreo.models.Cookies;
import es.cacharreo.models.ProductoUtils;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "CestaController", urlPatterns = {"/CestaController"})
public class CestaController extends HttpServlet {

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
            case "eliminarCarrito":
                if (cesta != null) {
                    // Determina si la sesión tiene que limpiarse. Verdadero por defecto para los usuarios anónimos
                    boolean limpiarSesion = true;

                    /* Manejo de USUARIO LOGUEADO */
                    if (usuario != null) {
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

                    /* LIMPIEZA DE CESTA (sesión y cookies) */
                    if (limpiarSesion) {
                        // Eliminamos la cookie independientemente de si hay usuario o no (limpieza total)
                        Cookie cBorrar = Cookies.generarCookie("cestaCookie", "", 0, request);
                        response.addCookie(cBorrar);

                        // Reseteamos el objeto en sesión como cesta vacía pero inicializada
                        cesta.getLineas().clear();
                        cesta.calcularTotales(); // importes a cero

                        request.setAttribute("exito", "La cesta se ha vaciado correctamente.");
                    }
                } else {
                    request.setAttribute("aviso", "La cesta ya estaba vacía.");
                }
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
