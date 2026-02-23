package es.cacharreo.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* 
* @author: fdezf
*/
@WebServlet(name = "EndController", urlPatterns = {"/EndController"})
public class EndController extends HttpServlet {

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
        
        // Eliminamos todos los atributos de sesión (usuario);
        if(request.getSession() != null) request.getSession().invalidate();

        // Dirigimos el flujo hacia el menú pricipal de la aplicación
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}