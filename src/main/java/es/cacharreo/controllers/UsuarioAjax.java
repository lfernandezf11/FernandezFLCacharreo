package es.cacharreo.controllers;

import com.google.gson.Gson;
import es.cacharreo.DAO.IPedidoDAO;
import es.cacharreo.DAO.IUsuarioDAO;
import es.cacharreo.DAOFactory.DAOFactory;
import es.cacharreo.beans.Pedido;
import es.cacharreo.beans.Usuario;
import es.cacharreo.models.Cookies;
import es.cacharreo.models.Utilities;
import java.io.IOException;
import java.util.Date;
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
        IPedidoDAO pDAO = daof.getPedidoDAO();
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
                String numNif = request.getParameter("nif");
                Character letra = Utilities.asignarLetraNif(numNif);
                objeto = new JSONObject();

                if (letra != null) {
                    boolean noRepetido = !uDAO.getDuplicateNif(numNif + letra);
                    if (noRepetido) {
                        objeto.put("letra", String.valueOf(letra));
                    } else {
                        objeto.put("error", "Este NIF ya está registrado.");
                    }
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

                        // Migración de cesta en el primer logueo (desde la sesión)
                        Pedido cesta = (Pedido) session.getAttribute("cesta");
                        
                        if (cesta.getLineas().isEmpty()) { 
                            cesta = new Pedido();
                        }
                        cesta.setUsuario(usuario); // 'usuario' ya tiene el ID asignado por addUsuario
                        // Si hay cesta y tiene líneas, la guardamos. El estado por defecto será 'c'
                        if (cesta != null && cesta.getLineas() != null && !cesta.getLineas().isEmpty()) {
                            cesta.setFecha(new Date());

                            boolean guardado = pDAO.insertarCesta(cesta); // Este método inserta el pedido y sus líneas en bd, y también actualiza el id del pedido en cesta
                            if (guardado) { // Éxito: pasamos a operar únicamente con sesión y bd   
                                response.addCookie(Cookies.generarCookie("cestaCookie", "", 0, request));

                            }
                        }

                        // SIEMPRE actualizamos la cesta en sesión, tenga líneas o no.
                        // Así, si añade un producto después, CestaService ya tendrá el usuario disponible.
                        session.setAttribute("cesta", cesta);
                        session.setAttribute("usuarioLogueado", usuario);

                        objeto.put("success", true);
                        objeto.put("message", "¡BIENVENIDA/O A CACHARREO, " + usuario.getNombre().toUpperCase() + "! Registro completado.");

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
                        // En primer lugar, recuperamos su cesta de BD si existiera (el método ya devuelve el usuario asignado)
                        Pedido cestaBD = pDAO.getCestaByUsuario(usuarioValidado);

                        if (cestaBD != null) {
                            // CASO A: Ya existe una cesta persistente. La cargamos en la sesión (el usuario viene asignado del método)
                            session.setAttribute("cesta", cestaBD);

                        } else if (usuarioValidado.getUltimoAcceso() == null) {
                            // CASO B: No hay cesta en BD y es la "primera vez" que entra (no tiene último acceso asociado).
                            // Migramos la cesta de la sesión (la anónima) a la base de datos.
                            Pedido cesta = (Pedido) session.getAttribute("cesta");

                            if (cesta != null && cesta.getLineas() != null && !cesta.getLineas().isEmpty()) {
                                cesta.setUsuario(usuarioValidado);
                                cesta.setFecha(new Date());

                                boolean guardado = pDAO.insertarCesta(cesta);
                                if (guardado) {
                                    // Sincronizamos el objeto en sesión por si acaso el DAO hizo cambios
                                    session.setAttribute("cesta", cesta);
                                }
                            }
                        } else {
                            // CASO C: Usuario que ya ha accedido antes pero no tiene cesta en BD
                            // Sobreescribimos la cesta de la sesión con una totalmente nueva y vacía
                            Pedido nuevaCesta = new Pedido();
                            nuevaCesta.setUsuario(usuarioValidado); // Vinculamos el usuario a este nuevo objeto

                            // Actualizamos la sesión con el objeto limpio
                            session.setAttribute("cesta", nuevaCesta);

                            // Nota: Al ser una cesta vacía, no llamamos a pDAO.insertarCesta aún.
                            // Se insertará automáticamente en la BD cuando añada el primer producto 
                            // gracias a la lógica de CestaService.gestionarAddProducto.
                        }
                        // En todos los login exitosos, la cookie muere
                        response.addCookie(Cookies.generarCookie("cestaCookie", "", 0, request));
                        session.setAttribute("usuarioLogueado", usuarioValidado);
                        objeto.put("success", true);
                        objeto.put("message", "¡HOLA, " + usuarioValidado.getNombre().toUpperCase() + "! Nos encanta tenerte de vuelta.");

                    } else {
                        // Credenciales incorrectas. 
                        objeto.put("success", false);
                        objeto.put("message", "Email o contraseña incorrectos");
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

            case "actualizarAvatar":
                objeto = new JSONObject();

                try {
                    usuario = (Usuario) session.getAttribute("usuarioLogueado");

                    Part filePart = request.getPart("avatar");

                    if (filePart != null && filePart.getSize() > 0) {
                        String contentType = filePart.getContentType();
                        String extension = "." + contentType.split("/")[1];
                        String nombreFichero = "avatar-" + usuario.getIdUsuario() + extension;

                        String ruta = request.getServletContext().getRealPath("/IMG/avatares/");

                        filePart.write(ruta + nombreFichero); // Escribimos el archivo en el servidor (sobrescribe si ya existe)
                        usuario.setAvatar(nombreFichero);
                        boolean ok = uDAO.updateAvatar(usuario);

                        if (ok) {
                            session.setAttribute("usuarioLogueado", usuario); // Refrescamos sesión
                            objeto.put("success", true);
                            objeto.put("message", "¡Foto de perfil actualizada!");
                        } else {
                            objeto.put("success", false);
                            objeto.put("message", "Error al guardar en la base de datos.");
                        }
                    } else {
                        objeto.put("success", false);
                        objeto.put("message", "No se ha seleccionado ningún archivo válido.");
                    }
                } catch (Exception e) {
                    objeto.put("success", false);
                    objeto.put("message", "Error interno: " + e.getMessage());
                }
                break;

            case "eliminarAvatar":
                objeto = new JSONObject();
                try {
                    usuario = (Usuario) session.getAttribute("usuarioLogueado");

                    usuario.setAvatar(null); //el JSP mostrará default.png automáticamente
                    boolean ok = uDAO.updateAvatar(usuario);

                    if (ok) {
                        session.setAttribute("usuarioLogueado", usuario);
                        objeto.put("success", true);
                        objeto.put("message", "Imagen de perfil eliminada.");
                    } else {
                        objeto.put("success", false);
                        objeto.put("message", "No se pudo eliminar la imagen.");
                    }
                } catch (Exception e) {
                    objeto.put("success", false);
                    objeto.put("message", "Error al procesar la solicitud.");
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
