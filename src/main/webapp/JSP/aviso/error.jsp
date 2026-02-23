<%-- 
    Author     : fdezf
--%>
<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <c:import url="/INC/headTags.jsp"/>
        <title>P&aacute;gina de registro</title>
        <link rel="stylesheet" type="text/css" href="${style}" />
    </head>
    <body> 
        <main>
            <h1>Error</h1>
            <div class="form">
                <c:if test="${not empty requestScope.error}">
                    <h3>${requestScope.error}</h3>
                </c:if>

                <!-- Cancelar también pasa al FrontController, que es el que redirige la acción al EndController.
                    El EndController borra todos los atributos de sesión y redirige al índice. -->
                <form action="${context}/FrontController">
                    <button type="submit" name="accion" value="salir" class="reset">Volver al inicio</button>
                </form>
            </div>
        </main>
        <c:import url="/INC/footer.inc"/>
    </body>
</html>
