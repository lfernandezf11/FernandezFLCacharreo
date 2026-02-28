package es.cacharreo.controllers;

import es.cacharreo.beans.LineaPedido;
import es.cacharreo.beans.Pedido;
import es.cacharreo.models.CestaService;
import es.cacharreo.models.CestaUtils;
import es.cacharreo.models.Cookies;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author fdezf
 */
@WebServlet(name = "CestaAjax", urlPatterns = {"/CestaAjax"})
public class CestaAjax extends HttpServlet {

    private static final String COOKIE_NAME = "cestaCookie";
    private static final int COOKIE_DURATION = 2 * 24 * 60 * 60; // Dos días en segundos

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String accion = request.getParameter("accion");
        String idProdStr = request.getParameter("idProducto");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject objeto = new JSONObject();

        // Manejo de acción nula
        if (accion == null || idProdStr == null) {
            objeto.put("success", false);
            objeto.put("message", "Petición incompleta.");
            response.getWriter().print(objeto);
            return;
        }

        short idProducto = Short.parseShort(idProdStr);
        Pedido cesta = (Pedido) session.getAttribute("cesta");

        if (cesta == null) {
            objeto.put("success", false);
            objeto.put("message", "Tu sesión ha expirado.");
            response.getWriter().print(objeto);
            return;
        }

        try {
            switch (accion) {
                case "addCarrito":
                    String pNombre = CestaService.gestionarAddProducto(cesta, idProducto);
                    objeto.put("success", true);
                    objeto.put("message", "¡" + pNombre + " añadido al carrito!");
                    break;

                case "sumar":
                case "restar":
                    if ("sumar".equals(accion)) {
                        CestaService.sumarUnidad(cesta, idProducto);
                    } else {
                        CestaService.restarUnidad(cesta, idProducto);
                    }

                    // Recuperamos los nuevos valores de la línea modificada
                    for (LineaPedido lp : cesta.getLineas()) {
                        if (lp.getProducto().getIdProducto() == idProducto) {
                            objeto.put("nuevaCantidad", lp.getCantidad());
                            // Cantidad formateada a la salida
                            objeto.put("subtotalProd", String.format("%.2f", lp.getImporte()).replace(",", "."));
                            break;
                        }
                    }
                    objeto.put("success", true);
                    break;

                case "eliminar":
                    CestaService.eliminarProducto(cesta, idProducto);
                    objeto.put("success", true);
                    objeto.put("message", "Producto eliminado de la cesta.");
                    break;
            }

            //// Persistencia y totales (común a todos los case)
            float totalCesta = cesta.getImporte() + cesta.getIva();
            objeto.put("subtotalCesta", String.format("%.2f", cesta.getImporte()).replace(",", "."));
            objeto.put("ivaCesta", String.format("%.2f", cesta.getIva()).replace(",", "."));
            objeto.put("totalCesta", String.format("%.2f", totalCesta).replace(",", "."));
            
            if(cesta.getUsuario() == null){ //Persistencia en cookie sólo en cesta anónima
            String cestaStr = CestaUtils.parserPedidoAString(cesta);
            Cookie c = Cookies.generarCookie(COOKIE_NAME, cestaStr, COOKIE_DURATION, request);
            response.addCookie(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objeto.put("success", false);
            objeto.put("message", "Error procesando la operación.");
        } finally {
            response.getWriter().print(objeto);
        }

    }

    /* Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
