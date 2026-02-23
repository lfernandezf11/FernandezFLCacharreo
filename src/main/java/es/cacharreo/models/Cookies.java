package es.cacharreo.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Clase de utilidades para la gestión segura de cookies HTTP en aplicaciones web.
 * Proporciona métodos estáticos para crear cookies codificadas y recuperar
 * su contenido decodificado, garantizando compatibilidad UTF-8 y manejo robusto de errores.
 * 
 * Útil para implementar "Recordar usuario".
 * 
 * @author fdezf
 */
public class Cookies {
    
    /**
     * Genera una cookie HTTP codificada y configurada para la aplicación.
     * 
     * @param nombre nombre único de la cookie
     * @param contenido valor a almacenar (codificado automáticamente)
     * @param duracionSegundos tiempo de vida en segundos (ej: 7*24*60*60 = 7 días)
     * @param request request HTTP para obtener el contexto de la aplicación
     * @return Cookie configurada
     */
    public static Cookie generarCookie(String nombre, String contenido, int duracionSegundos, HttpServletRequest request) {
        String valorCodificado;
        // Intentamos codificar el contenido de la cookie y, de no ser posible, utilizamos el string tal cual.
        try {
            valorCodificado = URLEncoder.encode(contenido, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            valorCodificado = contenido;
        }

        Cookie cookie = new Cookie(nombre, valorCodificado);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(duracionSegundos);
        return cookie;
    }
    
    /**
     * Recupera el valor decodificado de una cookie específica por su nombre.
     * 
     * @param request request HTTP con las cookies del cliente
     * @param nombreCookie nombre de la cookie a recuperar
     * @return valor decodificado de la cookie o null si no existe
     */
    public static String recuperarCookieValue(HttpServletRequest request, String nombreCookie){     
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(nombreCookie)) {
                    // Intentamos decodificar el contenido de la cookie y, de no ser posible, recuperamos el string tal cual.
                    try {
                        return URLDecoder.decode(c.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException | IllegalArgumentException e) {
                        return c.getValue();
                    }
                }
            }
        }
        return null;
    }
}
