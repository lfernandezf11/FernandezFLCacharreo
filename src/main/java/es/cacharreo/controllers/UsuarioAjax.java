package es.cacharreo.controllers;

import com.google.gson.Gson;
import es.cacharreo.DAO.IUsuarioDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Usuario;
import es.cacharreo.models.Utilities;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.json.JSONObject;

/**
 *
 * @author fdezf
 */
@MultipartConfig // Obligatorio para recibir archivos
@WebServlet(name = "UsuarioAjax", urlPatterns = {"/UsuarioAjax"})
public class UsuarioAjax extends HttpServlet {

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
        IUsuarioDAO uDAO = daof.getUsuarioDAO();
        Usuario usuario = null;

        String accion = request.getParameter("accion");
        JSONObject objeto = null;
        Gson g = null;

        if (accion == null) {
            request.setAttribute("error", "Se ha producido un error en el envío de datos del formulario.");
            request.getRequestDispatcher("/JSP/aviso/error.jsp").forward(request, response);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        switch (accion) {
            case "validateEmail":
                String email = request.getParameter("email");
                boolean disponible = !uDAO.getDuplicateEmail(email);

                objeto = new JSONObject();
                objeto.put("disponible", disponible);
                break;

            case "asignarLetraNIF":
                Character letra = Utilities.asignarLetraNif(request.getParameter("nif"));
                objeto = new JSONObject();

                if (letra != null) {
                    objeto.put("letra", String.valueOf(letra));
                } else {
                    objeto.put("error", "DNI no válido");
                }
                break;

            case "registrarUsuario":
                objeto = new JSONObject();
                try {
                    String datosRegistro = request.getParameter("datosRegistro");
                    g = new Gson();
                    usuario = g.fromJson(datosRegistro, Usuario.class);

                    if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                        usuario.setPassword(Utilities.md5(usuario.getPassword()));
                    }

                    // Este método ya asigna el ID generado para la tupla al Usuario referenciado que entra como parámetro, 
                    // y devuelve "ok" o el string de error correspondiente.
                    String resultado = uDAO.addUsuario(usuario);

                    if ("ok".equals(resultado)) {
                        // Aquí podemos recuperar directamente el ID.
                        Short idUsuario = usuario.getIdUsuario();

                        Part filePart = request.getPart("avatar");
                        
                        if (filePart != null && filePart.getSize() > 0) {
                            String contentType = filePart.getContentType(); // Devuelve "image/png", "image/jpg"...
                            String extension = "." + contentType.split("/")[1];
                            String nombreFichero = "avatar-" + idUsuario + extension;
                            
                            String ruta = request.getServletContext().getRealPath("/IMG/avatares/");

                            try {
                                filePart.write(ruta + nombreFichero);
                                usuario.setAvatar(nombreFichero);
                                uDAO.updateAvatar(usuario); // Actualizamos el nombre en la BD
                            } catch (IOException ex) {

                                uDAO.deleteUsuario(idUsuario);  // SI FALLA LA IMAGEN, borramos el usuario por seguridad
                                throw new Exception("Error al escribir el archivo.");
                            }
                        }
                        session.setAttribute("usuarioLogueado", usuario);
                        objeto.put("success", true);
                        objeto.put("mensaje", "¡Bienvenido a Cacharreo, " + usuario.getNombre() + "!");
                        
                    } else {
                        objeto.put("success", false);
                        objeto.put("message", resultado);
                    }
                } catch (Exception e) {
                    objeto.put("success", false);
                    objeto.put("message", "Error al procesar el registro: " + e.getMessage());
                }
                break;

            case "loginUsuario":
                objeto = new JSONObject();
                try {
                    String datosLogin = request.getParameter("datosLogin");
                    g = new Gson();

                    usuario = g.fromJson(datosLogin, Usuario.class);

                    Usuario usuarioValidado = uDAO.login(usuario.getEmail(), Utilities.md5(usuario.getPassword()));

                    if (usuarioValidado != null) { //Login exitoso
                        session.setAttribute("usuarioLogueado", usuarioValidado);
                        objeto.put("success", true);
                    } else {
                        objeto.put("success", false); // El JS maneja el mensaje de error: "email o contraseña incorrectos".
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    objeto.put("success", false);
                    objeto.put("message", "Error en el servidor al autenticar.");
                }
                break;

            case "actualizarUsuario":
                objeto = new JSONObject();
                try {
                    String datosJson = request.getParameter("datosFormPerfil");
                    g = new Gson();

                    usuario = (Usuario) session.getAttribute("usuarioLogueado");

                    Usuario datosEditados = g.fromJson(datosJson, Usuario.class);

                    usuario.setNombre(datosEditados.getNombre());
                    usuario.setApellidos(datosEditados.getApellidos());
                    usuario.setDireccion(datosEditados.getDireccion());
                    usuario.setLocalidad(datosEditados.getLocalidad());
                    usuario.setProvincia(datosEditados.getProvincia());
                    usuario.setCodigoPostal(datosEditados.getCodigoPostal());
                    usuario.setTelefono(datosEditados.getTelefono());

                    boolean actualizado = uDAO.updateUsuario(usuario);

                    if (actualizado) {
                        objeto.put("success", true);
                        session.setAttribute("usuarioLogueado", usuario);
                    } else {
                        objeto.put("success", false);
                        objeto.put("message", "Ocurrió un error durante el guardado.");
                    }
                } catch (Exception e) {
                    objeto.put("success", false);
                    objeto.put("message", "Error interno.");
                }
                break;

            case "actualizarPassword":
                objeto = new JSONObject();
                try {
                    String datosJson = request.getParameter("datosFormPass");
                    usuario = (Usuario) session.getAttribute("usuarioLogueado");

                    JSONObject datosPass = new JSONObject(datosJson); // No es necesario indicar una clase porque no existe bean para estos atributos.
                    String passActualInput = datosPass.getString("password");
                    String passNuevaInput = datosPass.getString("nuevaPassword");

                    // Manejo de contraseñas iguales (comparamos con texto plano)
                    if (passActualInput.equals(passNuevaInput)) {
                        objeto.put("success", false);
                        objeto.put("message", "La nueva contraseña no puede ser idéntica a la actual.");
                    } else {
                        // Ciframos para la operación en BD
                        String passActualCifrada = Utilities.md5(passActualInput);
                        String passNuevaCifrada = Utilities.md5(passNuevaInput);

                        //  Este método valida y actualiza la contraseña en la misma llamada. 
                        // Devuelve false si la contraseña actual no coincide con la almacenada en la bd
                        boolean cambiado = uDAO.updatePassword(usuario.getEmail(), passActualCifrada, passNuevaCifrada);

                        if (cambiado) {
                            objeto.put("success", true);
                            objeto.put("message", "¡Contraseña actualizada con éxito!");
                        } else {
                            // Si llegamos aquí, es porque el UPDATE no encontró la fila (contraseña actual incorrecta)
                            objeto.put("success", false);
                            objeto.put("message", "La contraseña actual no es correcta. Inténtalo de nuevo.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    objeto.put("success", false);
                    objeto.put("message", "Error crítico en el servidor.");
                }
                break;
        }
        response.getWriter().print(objeto);

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
