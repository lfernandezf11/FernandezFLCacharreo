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
            <section id="signInSection">
                <div class="signUpWrapper col-12 col-md-10 col-lg-7">
                    <div class="section-header">
                        <h4>Inicia sesi&oacute;n</h4>
                    </div>  
                    <form class="row signInForm">  

                        <div class="col-12 field">
                            <label for="emailLogin" class="form-label">Email</label>
                            <input type="email" class="form-control" id="emailLogin" name="email">

                        </div>
                        <div class="col-12 field">
                            <label for="passwordLogin" class="form-label">Contrase&ntilde;a</label>
                            <div class="password-field">
                                <input type="password" class="form-control" id="passwordLogin" name="password">
                                <button type="button" class="toggle-pass">
                                    <i class="fa fa-eye" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                        <small class="invalid is-invalid"></small>
                        <div class="col-12">
                            <button type="submit" class="btn btn-primary mt-1 mb-2" id="btn-login">Acceder</button>
                        </div>

                    </form>
                    
                    <form action="${context}/FrontController" method="post">
                        ¿Eres nuevo? Date de alta 
                        <button type="submit" name="accion" value="registro" class="btn btn-link-terms text-primary p-0 mb-1 text-decoration-none">aquí</button>
                    </form>
                </div>
            </section>
        </main>


        <c:import url="/INC/footer.jsp"/>
        <script src="${pageContext.request.contextPath}/JS/loginJS.js"></script>
        <script>
            window.addEventListener('DOMContentLoaded', (event) => {
                const mensajeExito = "${exito}";
                const mensajeError = "${error}";
                const mensajeAviso = "${aviso}";

                if (mensajeExito) {
                    lanzarToast(mensajeExito, "exito");
                }
                if (mensajeError) {
                    lanzarToast(mensajeError, "error");
                }
                if (mensajeAviso) {
                    lanzarToast(mensajeAviso, "aviso");
                }
            });
        </script>
    </body>
</html>
