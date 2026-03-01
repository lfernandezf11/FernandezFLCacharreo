<%-- 
    Author     : fdezf
--%>

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="es">
    <head>
        <c:import url="/INC/headTags.jsp"/>
        <title>Cacharreo</title>
        <link rel="stylesheet" type="text/css" href="${style}" /> 
    </head>
    <body> 
        <c:import url="/INC/header.jsp"/>
        <c:import url="/INC/toast.jsp"/>
        
        <main class="page">
            <section id="signUpSection">
                <div class="signUpWrapper col-12 col-lg-10">
                    <div class="section-header">
                        <h4>Date de alta</h4>
                    </div>  
                    <form class="row g-3 signUpForm" enctype="multipart/form-data">
                        <div class="col-md-8">
                            <div class="row g-3">
                                <div class="col-md-6 field">
                                    <label for="nombre" class="form-label">Nombre</label>
                                    <input type="text" class="form-control" id="nombre" name="nombre">
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-md-6 field">
                                    <label for="apellidos" class="form-label">Apellidos</label>
                                    <input type="text" class="form-control" id="apellidos" name="apellidos">
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-12 field">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="text" class="form-control" id="email" name="email">
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-12 field">
                                    <label for="direccion" class="form-label">Direcci&oacute;n</label>
                                    <input type="text" class="form-control" id="direccion" placeholder="1234 Main St" name="direccion">
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-12 field">
                                    <label for="localidad" class="form-label">Localidad</label>
                                    <input type="text" class="form-control" id="localidad" name="localidad">
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-md-8 field">
                                    <label for="provincia" class="form-label">Provincia</label>
                                    <select id="provincia" class="form-select" name="provincia">
                                        <option value="" selected disabled>Selecciona tu provincia...</option>
                                        <option value="Alava">Araba/&Aacute;lava</option>
                                        <option value="Albacete">Albacete</option>
                                        <option value="Alicante">Alicante/Alacant</option>
                                        <option value="Almeria">Almer&iacute;a</option>
                                        <option value="Asturias">Asturias</option>
                                        <option value="Avila">&Aacute;vila</option>
                                        <option value="Badajoz">Badajoz</option>
                                        <option value="Baleares">Balears, Illes</option>
                                        <option value="Barcelona">Barcelona</option>
                                        <option value="Bizkaia">Bizkaia</option>
                                        <option value="Burgos">Burgos</option>
                                        <option value="Caceres">C&aacute;ceres</option>
                                        <option value="Cadiz">C&aacute;diz</option>
                                        <option value="Cantabria">Cantabria</option>
                                        <option value="Castellon">Castell&oacute;n/Castell&oacute;</option>
                                        <option value="Ceuta">Ceuta</option>
                                        <option value="Ciudad Real">Ciudad Real</option>
                                        <option value="Cordoba">C&oacute;rdoba</option>
                                        <option value="Coruña">Coru&ntilde;a, A</option>
                                        <option value="Cuenca">Cuenca</option>
                                        <option value="Gipuzkoa">Gipuzkoa</option>
                                        <option value="Girona">Girona</option>
                                        <option value="Granada">Granada</option>
                                        <option value="Guadalajara">Guadalajara</option>
                                        <option value="Huelva">Huelva</option>
                                        <option value="Huesca">Huesca</option>
                                        <option value="Jaen">Ja&eacute;n</option>
                                        <option value="Leon">Le&oacute;n</option>
                                        <option value="Lleida">Lleida</option>
                                        <option value="Lugo">Lugo</option>
                                        <option value="Madrid">Madrid</option>
                                        <option value="Malaga">M&aacute;laga</option>
                                        <option value="Melilla">Melilla</option>
                                        <option value="Murcia">Murcia</option>
                                        <option value="Navarra">Navarra</option>
                                        <option value="Ourense">Ourense</option>
                                        <option value="Palencia">Palencia</option>
                                        <option value="Palmas">Palmas, Las</option>
                                        <option value="Pontevedra">Pontevedra</option>
                                        <option value="Rioja">Rioja, La</option>
                                        <option value="Salamanca">Salamanca</option>
                                        <option value="Santa Cruz de Tenerife">Santa Cruz de Tenerife</option>
                                        <option value="Segovia">Segovia</option>
                                        <option value="Sevilla">Sevilla</option>
                                        <option value="Soria">Soria</option>
                                        <option value="Tarragona">Tarragona</option>
                                        <option value="Teruel">Teruel</option>
                                        <option value="Toledo">Toledo</option>
                                        <option value="Valencia">Valencia/Val&egrave;ncia</option>
                                        <option value="Valladolid">Valladolid</option>
                                        <option value="Zamora">Zamora</option>
                                        <option value="Zaragoza">Zaragoza</option>
                                    </select>
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-md-4 field">
                                    <label for="codigoPostal" class="form-label cp">C&oacute;digo postal</label>
                                    <input type="text" class="form-control" id="codigoPostal" name="codigoPostal">
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-md-8 field">
                                    <label for="nif" class="form-label">NIF</label>
                                    <input type="text" class="form-control" id="nif" name="nif">
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-md-4 field">
                                    <label for="telefono" class="form-label">Tel&eacute;fono</label>
                                    <input type="tlf" class="form-control" id="telefono" name="telefono">
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-12 field">
                                    <label for="password1" class="form-label">Contrase&ntilde;a</label>
                                    <div class="password-field">
                                        <input type="password" class="form-control" id="password1" name="password">
                                        <button type="button" class="toggle-pass">
                                            <i class="fa fa-eye" aria-hidden="true"></i>
                                        </button>
                                    </div>
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-12 field">
                                    <label for="password2" class="form-label">Repite la contrase&ntilde;a</label>
                                    <div class="password-field">
                                        <input type="password" class="form-control" id="password2">
                                        <button type="button" class="toggle-pass">
                                            <i class="fa fa-eye" aria-hidden="true"></i>
                                        </button>
                                    </div>
                                    <small class="invalid"></small>
                                </div>
                            </div>
                        </div>

                        <!-- Campo imagen -->
                        <div class="col-md-4 d-flex flex-column align-items-center justify-content-start pt-4">
                            <div style="top: 20px; z-index: 1; justify-items:center; ">
                                <div class="mb-3 text-center align-center">
                                    <img id="previa" 
                                         src="${context}/IMG/avatares/${not empty u.avatar ? u.avatar : 'default.png'}" 
                                         class="rounded-circle border shadow-sm" 
                                         style="width: 200px; height: 200px; object-fit: cover; flex-shrink: 0;"
                                         onerror="this.onerror=null;this.src='${context}/IMG/avatares/default.png';">
                                </div>
                                <div class="text-center field">
                                    <label for="avatar" class="form-label">Foto de perfil</label>
                                    <input type="file" class="form-control" id="avatar" name="avatar" accept="image/*">
                                    <div class="mt-2">
                                        <small class="text-muted d-block">JPG, PNG (Máx. 100KB)</small>
                                        <small class="invalid" id="avatarError"></small>
                                    </div>
                                    <br>
                                    <button type="button" class="btn btn-secondary" id="btn-delete-avatarR">
                                        <i class="bi bi-trash"></i> Eliminar foto
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div class="col-12">
                            <div class="form-check field">
                                <input class="form-check-input" type="checkbox" id="gridCheck">
                                <label class="form-check-label" for="gridCheck">
                                    Acepto los 
                                    <button type="button" class="btn btn-link-terms text-primary p-0 mb-1 text-decoration-none" data-bs-toggle="modal" data-bs-target="#modalTerminos">
                                        t&eacute;rminos y condiciones
                                    </button>
                                </label>
                                <br><small class="invalid" id="checkError"></small>
                            </div>
                        </div>

                        <div class="col-12">
                            <button type="submit" class="btn btn-primary">Crear cuenta</button>
                        </div>

                    </form>
                    <br>
                    <form action="${context}/FrontController" method="post">
                        ¿Ya tienes cuenta? Ingresa 
                        <button type="submit" name="accion" value="login" class="btn btn-link-terms text-primary p-0 mb-1 text-decoration-none">aquí</button>
                    </form>
                </div>
            </section>
        </main>


        <!-- Modal de términos y condiciones -->
        <div class="modal fade" id="modalTerminos" tabindex="-1" aria-labelledby="modalTerminosLabel">
            <div class="modal-dialog modal-lg modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTerminosLabel">T&eacute;rminos y Condiciones - Cacharreo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <h6>1. Informaci&oacute;n General</h6>
                        <p>Bienvenido a Cacharreo. Al acceder a nuestro sitio web y realizar una compra, usted acepta estar sujeto a los siguientes t&eacute;rminos y condiciones.</p>

                        <h6>2. Garant&iacute;a de Hardware</h6>
                        <p>Todos nuestros componentes cuentan con la garant&iacute;a legal de 3 a&ntilde;os. La garant&iacute;a no cubre da&ntilde;os por manipulaci&oacute;n indebida, overclocking extremo o montaje incorrecto por parte del usuario.</p>

                        <h6>3. Env&iacute;os y Devoluciones</h6>
                        <p>Los env&iacute;os de componentes en stock suelen tardar 24/48h. Dispone de 14 d&iacute;as naturales para desistir de su compra, siempre que el producto conserve su precinto original y no haya sido instalado.</p>

                        <h6>4. Protecci&oacute;n de Datos</h6>
                        <p>Sus datos ser&aacute;n tratados con la &uacute;nica finalidad de gestionar sus pedidos y mejorar su experiencia de navegaci&oacute;n en Cacharreo.</p>

                        <h6>5. Limitaci&oacute;n de Responsabilidad</h6>
                        <p>Cacharreo no se hace responsable de incompatibilidades t&eacute;cnicas entre componentes si el cliente no ha consultado previamente con nuestro servicio t&eacute;cnico.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primaryAlt" data-bs-dismiss="modal">Entendido</button>
                    </div>
                </div>
            </div>
        </div>


        <c:import url="/INC/footer.jsp"/>
        <script src="${pageContext.request.contextPath}/JS/registroJS.js"></script>
    </body>
</html>
