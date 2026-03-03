package es.cacharreo.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Clase de utilidades para la gestión segura y estandarizada de cookies HTTP.
 *
 * <p>
 * Esta clase centraliza la lógica de manipulación de cookies, forzando el uso
 * de codificación UTF-8 para evitar errores de sintaxis en el protocolo HTTP
 * cuando el contenido incluye caracteres especiales (espacios, eñes, símbolos,
 * etc.).</p>
 *
 * <p>
 * Implementa funcionalidades clave para:</p>
 * <ul>
 * <li>Persistencia de sesiones de usuario ("Recordar usuario").</li>
 * <li>Serialización de estados temporales (Cesta de la compra).</li>
 * <li>Configuración de rutas de contexto automáticas para evitar colisiones
 * entre apps.</li>
 * </ul>
 *
 * @author fdezf
 * @version 1.0
 */
public class Cookies {

    /**
     * Genera una nueva instancia de {@link Cookie} configurada y codificada.
     *
     * <p>
     * El contenido se somete a {@link URLEncoder} para asegurar que sea
     * compatible con los estándares de cabeceras HTTP, eliminando conflictos
     * con caracteres reservados.</p>
     *
     * @param nombre El identificador único de la cookie.
     * @param contenido El valor textual a almacenar. Se codificará en UTF-8.
     * @param duracionSegundos Tiempo de vida útil de la cookie. (Ejemplo:
     * {@code 60*60*24} para un día).
     * @param request La petición actual, utilizada para definir el {@code path}
     * basado en el contexto de la aplicación.
     * @return Una {@link Cookie} lista para ser añadida a la respuesta
     * (Response).
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
     * Busca y recupera el valor decodificado de una cookie presente en la
     * petición.
     *
     * <p>
     * Este método itera sobre el array de cookies del cliente, localiza la
     * coincidencia por nombre y revierte la codificación URL aplicada durante
     * la creación.</p>
     *
     * @param request El objeto {@link HttpServletRequest} que contiene las
     * cookies enviadas por el navegador.
     * @param nombreCookie El nombre de la cookie que se desea leer.
     * @return El valor original decodificado, o {@code null} si la cookie no
     * existe en la petición.
     */
    public static String recuperarCookieValue(HttpServletRequest request, String nombreCookie) {
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
