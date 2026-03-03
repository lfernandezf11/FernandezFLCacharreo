package es.cacharreo.models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Clase de utilidades generales para la aplicación.
 * <p>Proporciona métodos de soporte para la validación de formularios,
 * seguridad de contraseñas, formateo de cadenas y lógica de identificación oficial.</p>
 * 
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public class Utilities {

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

    /**
     * Calcula la letra de control correspondiente a un número de DNI español (NIF).
     * <p>Aplica el algoritmo oficial de la Dirección General de la Policía, 
     * basado en el resto de la división del número del DNI entre 23 (operación módulo).</p>
     * 
     * 
     * 
     * @param nifStr Cadena con los 8 dígitos numéricos del NIF.
     * @return El carácter (letra) de control correspondiente, o {@code null} 
     * si la entrada no tiene el formato correcto.
     */
    public static Character asignarLetraNif(String nifStr) {
        if (nifStr != null && nifStr.length() == 8) {
            int nif = Integer.parseInt(nifStr);
            String charPermitidos = "TRWAGMYFPDXBNJZSQVHLCKE";
            return charPermitidos.charAt(nif % 23);
        }
        return null;
    }
}
