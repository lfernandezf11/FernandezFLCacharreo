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
 * Controlador AJAX encargado de gestionar las operaciones de la cesta de la compra.
 * <p>Este Servlet procesa peticiones asíncronas para añadir, modificar o eliminar productos 
 * de la cesta sin recargar la página. Devuelve respuestas en formato JSON.</p>
 * 
 * <p><strong>Estrategia de Persistencia:</strong></p>
 * <ul>
 * <li>Si el usuario es anónimo: Los cambios se guardan en la {@link HttpSession} y se 
 * replican en una {@link Cookie} persistente.</li>
 * <li>Si el usuario está logueado: Los cambios se sincronizan en tiempo real con la 
 * base de datos a través de {@link CestaService}.</li>
 * </ul>
 * 
 * @author fdezf
 * @version 1.0
 */
@WebServlet(name = "CestaAjax", urlPatterns = {"/CestaAjax"})
public class CestaAjax extends HttpServlet {

    private static final String COOKIE_NAME = "cestaCookie";
    private static final int COOKIE_DURATION = 2 * 24 * 60 * 60; // Dos días en segundos
    
    /**
     * Procesa las peticiones POST enviadas por los scripts de la interfaz (JS).
     * <p>Dependiendo del parámetro {@code accion}, delega la lógica en el servicio correspondiente
     * y genera un objeto JSON con los totales actualizados de la cesta.</p>
     * 
     * 
     * 
     * @param request  Contiene la acción a realizar y el ID del producto.
     * @param response Respuesta en formato {@code application/json}.
     * @throws ServletException Si ocurre un error en la gestión del servlet.
     * @throws IOException      Si ocurre un error en la escritura de la respuesta.
     */
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
            objeto.put("totalUnidadesCesta", cesta.getCantidadTotal());
            
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

    /**
     * Retorna una breve descripción del propósito de este Servlet.
     * @return String con la descripción del controlador de cesta.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
