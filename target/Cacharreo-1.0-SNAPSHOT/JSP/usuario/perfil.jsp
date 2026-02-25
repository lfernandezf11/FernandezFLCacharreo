<%-- 
    Author     : fdezf
--%>

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="u" value="${sessionScope.usuarioLogueado}" />


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

        <main class="page gap-4">
            <section id="profileSection">
                <div class="signUpWrapper col-12 col-lg-10 mt-5">
                    <div class="section-header">
                        <h4>Mi Perfil</h4>
                        <p class="text-muted small">Puedes actualizar tus datos excepto el email y el NIF por motivos de seguridad.</p>
                    </div>  

                    <form class="row g-3 editProfileForm">
                        <div class="col-md-8">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="email" class="form-label text-muted">Email</label>
                                    <input type="email" class="form-control bg-light" id="email" name="email" value="${u.email}" readonly tabindex="-1">
                                </div>
                                <div class="col-md-6">
                                    <label for="nif" class="form-label text-muted">NIF</label>
                                    <input type="text" class="form-control bg-light" id="nif" name="nif" value="${u.nif}" readonly tabindex="-1">
                                </div>

                                <div class="col-md-6">
                                    <label for="nombrePerfil" class="form-label">Nombre</label>
                                    <input type="text" class="form-control" id="nombrePerfil" name="nombre" value="${u.nombre}">
                                    <small class="invalid"></small>
                                </div>
                                <div class="col-md-6">
                                    <label for="apellidosPerfil" class="form-label">Apellidos</label>
                                    <input type="text" class="form-control" id="apellidosPerfil" name="apellidos" value="${u.apellidos}">
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-12">
                                    <label for="direccionPerfil" class="form-label">Direcci&oacute;n</label>
                                    <input type="text" class="form-control" id="direccionPerfil" name="direccion" value="${u.direccion}">
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-md-6">
                                    <label for="localidadPerfil" class="form-label">Localidad</label>
                                    <input type="text" class="form-control" id="localidadPerfil" name="localidad" value="${u.localidad}">
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-md-6">
                                    <label for="provinciaPerfil" class="form-label">Provincia</label>
                                    <select id="provinciaPerfil" class="form-select" name="provincia">
                                        <c:set var="provincias" value="Alava,Albacete,Alicante,Almeria,Asturias,Avila,Badajoz,Baleares,Barcelona,Bizkaia,Burgos,Caceres,Cadiz,Cantabria,Castellon,Ceuta,Ciudad Real,Cordoba,Coruña,Cuenca,Gipuzkoa,Girona,Granada,Guadalajara,Huelva,Huesca,Jaen,Leon,Lleida,Lugo,Madrid,Malaga,Melilla,Murcia,Navarra,Ourense,Palencia,Palmas,Pontevedra,Rioja,Salamanca,Santa Cruz de Tenerife,Segovia,Sevilla,Soria,Tarragona,Teruel,Toledo,Valencia,Valladolid,Zamora,Zaragoza" />

                                        <option value="" disabled>Selecciona tu provincia...</option>
                                        <c:forEach items="${provincias}" var="prov">
                                            <option value="${prov}" ${u.provincia == prov ? 'selected' : ''}>${prov}</option>
                                        </c:forEach>
                                    </select>
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-md-4">
                                    <label for="codigoPostalPerfil" class="form-label">C&oacute;digo postal</label>
                                    <input type="text" class="form-control" id="codigoPostalPerfil" name="codigoPostal" value="${u.codigoPostal}">
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-md-8">
                                    <label for="telefonoPerfil" class="form-label">Tel&eacute;fono</label>
                                    <input type="text" class="form-control" id="telefonoPerfil" name="telefono" value="${u.telefono}">
                                    <small class="invalid"></small>
                                </div>
                            </div>
                        </div> 

                        <!-- Campo imagen -->
                        <div class="col-md-4 d-flex flex-column align-items-center justify-content-start pt-4">
                            <div class="sticky-md-top" style="top: 20px; z-index: 1; justify-items:center; ">
                                <div class="mb-3 text-center align-center">
                                    <img id="imgPerfil" src="${context}/IMG/avatares/${not empty u.avatar ? u.avatar : 'default.png'}" 
                                         class="rounded-circle border shadow-sm ratio-1x1" 
                                         width="200" height="200"
                                         style="object-fit: cover;"
                                         onerror="this.onerror=null;this.src='${context}/IMG/avatares/default.png';">
                                </div>
                                <button type="button" class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#changeAvatarModal">
                                    <i class="fa fa-camera-retro" aria-hidden="true"></i> Cambiar foto de perfil
                                </button>
                            </div>
                        </div> 

                        <div class="col-12 mt-4 d-flex align-items-center gap-3">
                            <button type="submit" class="btn btn-primary" id="btn-save-profile">Actualizar mis datos</button>
                            <button type="button" class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                                <i class="fa fa-key" aria-hidden="true"></i> Cambiar contrase&ntilde;a
                            </button>

                        </div>
                    </form>

                </div>
                <br>
                
            </section>
                                         <!-- Último acceso (solo si no es null) -->               
                <c:if test="${not empty u.ultimoAcceso}">
                    <div class="text-start mt-3">
                        <p class="text-white small fw-bold">
                            <i class="fa fa-history" aria-hidden="true"></i> 
                            &Uacute;ltimo acceso: 
                            <fmt:formatDate value="${u.ultimoAcceso}" pattern="dd/MMM/yyyy HH:mm" />
                        </p>
                    </div>
                </c:if>
        </main>

        <!-- Modal para cambiar contraseña -->
        <div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title ms-3" id="changePasswordModalLabel">Cambiar contrase&ntilde;a</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <form id="changePasswordForm" class="changePasswordForm fw-400">
                        <div class="modal-body m-3">
                            <div class="row g-3">
                                <div class="col-12">
                                    <label for="currentPass" class="form-label">Contrase&ntilde;a actual</label>
                                    <input type="password" class="form-control" id="currentPass" name="password">
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-12">
                                    <label for="newPass" class="form-label">Nueva contrase&ntilde;a</label>
                                    <input type="password" class="form-control" id="newPass" name="nuevaPassword">
                                    <small class="invalid"></small>
                                </div>

                                <div class="col-12">
                                    <label for="confirmNewPass" class="form-label">Confirmar nueva contrase&ntilde;a</label>
                                    <input type="password" class="form-control" id="confirmNewPass">
                                    <small class="invalid"></small>
                                </div>
                            </div>

                            <div class="col-12 mt-4 d-flex align-items-center gap-3">
                                <button type="submit" class="btn btn-primary" id="btn-save-pass">Guardar cambios</button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal para cambiar foto de avatar -->
        <div class="modal fade" id="changeAvatarModal" tabindex="-1" aria-labelledby="changeAvatarModalLabel">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title ms-3" id="changeAvatarModalLabel">Cambiar foto de perfil</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <form id="changeAvatarForm" class="changeAvatarForm fw-400">
                        <div class="modal-body m-3">
                            <div class="col-12 d-flex flex-column pt-1 gap-4">
                                <div class="w-100 text-center" style="justify-items:center;">
                                    <div class="ratio ratio-1x1" style="width: 60%;">
                                        <img id="previa" src="${context}/IMG/avatares/${not empty u.avatar ? u.avatar : 'default.png'}" 
                                             class="rounded-circle border shadow-sm ratio-1x1" 
                                             style="object-fit: cover;"
                                             onerror="this.onerror=null;this.src='${context}/IMG/avatares/default.png';">
                                    </div>
                                </div>
                                <div class="w-100 text-start mt-2">
                                    <input type="file" class="form-control" id="avatar" name="avatar" accept="image/*">
                                    <small class="text-muted">JPG, PNG (Máx. 100KB)</small>
                                    <small class="invalid" id="avatarError"></small>
                                </div>
                            </div>

                            <div class="col-12 mt-4 d-flex align-items-center gap-3 mt-2">
                                <button type="submit" class="btn btn-primary" id="btn-save-avatar">Guardar cambios</button>
                                <button type="button" class="btn btn-secondary" id="btn-delete-avatar">Borrar</button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>


        <c:import url="/INC/footer.jsp"/>
        <script src="${pageContext.request.contextPath}/JS/perfilForm.js"></script>
        <script src="${pageContext.request.contextPath}/JS/cambioAvatar.js"></script>
    </body>
</html>
