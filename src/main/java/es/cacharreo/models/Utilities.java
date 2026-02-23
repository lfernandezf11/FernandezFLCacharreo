package es.cacharreo.models;

import es.cacharreo.beans.Producto;
import es.cacharreo.beans.Usuario;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * Clase de utilidades generales para la aplicación.
 *
 * @author fdezf
 */
public class Utilities {

    /**
     * Valida que todos los campos del formulario estén completos.
     *
     * @param request objeto HttpServletRequest con los valores
     * @return true si todo está correcto, false si hay algún campo vacío
     */
    public static Boolean comprobarCampos(HttpServletRequest request) {
        Enumeration<String> parametros = request.getParameterNames();
        Boolean vacio = false;
        while (parametros.hasMoreElements() && !vacio) {
            String campo = parametros.nextElement();
            String valor = request.getParameter(campo);

            if (valor == null || valor.trim().isEmpty()) {
                vacio = true;
            }
        }
        return !vacio;
    }

    /**
     * Valida que los campos del formulario de login estén completos.
     *
     * @param request objeto HttpServletRequest con los valores
     * @return mensaje de error HTML o null si todo está correcto.
     */
    public static String comprobarCamposLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "Todos los campos son obligatorios";
        }
        return null;
    }

    /**
     * Convierte código de género de BD (H/M) a enum Genero.
     *
     * @param input
     * @param generoChar código de género desde base de datos
     * @return valor del enum Genero correspondiente
     */
    /*public static Usuario.Genero string2Genero(String generoChar) {
        Usuario.Genero genero;
        switch (generoChar) {
            case "H":
                genero = Usuario.Genero.Hombre;
                break;
            case "M":
                genero = Usuario.Genero.Mujer;
                break;
            default:
                genero = Usuario.Genero.Otro;
        }
        return genero;
    }

    /**
     * Convierte enum Genero a código de género para BD (H/M/O).
     *
     * @param genero valor del enum Genero
     * @return código de género para base de datos
     */
 /*public static String genero2String(Usuario.Genero genero) {
        String sexo = null;

        switch (genero) {
            case Hombre:
                sexo = "H";
                break;
            case Mujer:
                sexo = "M";
                break;
            default:
                sexo = "O";
                break;
        }
        return sexo;
    }

    /**
     * Obtiene el valor de utilizar la función MD5 de una cadena
     *
     * @param input Cadena pasada para convertir a MD5
     * @return Cadena tras la conversión
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Capitaliza las palabras de una cadena.
     *
     * @param texto cadena a formatear
     * @return cadena tras la capitalización
     */
    public static String capitalizar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        String formateado = "";

        String[] palabras = texto.split(" ");
        for (String p : palabras) {
            p = p.substring(0, 1).toUpperCase()
                    + p.substring(1).toLowerCase();
            if (!formateado.isEmpty()) {
                formateado += " ";
            }
            formateado += p;
        }
        return formateado;
    }

    public static Character asignarLetraNif(String nifStr) {
        if (nifStr != null && nifStr.length() == 8) {
            int nif = Integer.parseInt(nifStr);
            String charPermitidos = "TRWAGMYFPDXBNJZSQVHLCKE";
            return charPermitidos.charAt(nif % 23);
        }
        return null;
    }
}
