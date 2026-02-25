<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="css-navbar">


    <form action="${pageContext.request.contextPath}/FrontController" method="post" class="nav-form">
        <button type="submit" name="accion" value="inicio" class="btn-logo">
            <img class="logo" src="${pageContext.request.contextPath}/IMG/logoNavbar.png" alt="Logo de Cacharreo">
        </button>
        <nav class="nav-links">
            <button type="submit" name="accion" value="inicio" class="btn-link">Inicio</button>
            <button type="submit" name="accion" value="quienesSomos" class="btn-link">¿Quiénes somos?</button>
            <button type="submit" name="accion" value="productos" class="btn-link">Productos</button>
            <button type="submit" name="accion" value="contacto" class="btn-link">Contacto</button>
        </nav>


        <c:if test="${not empty usuarioLogueado}">
            <div class="btn-group-logueado d-flex align-content-center justify-items-center gap-0 "> 
                <button type="submit" name="accion" value="perfil" class="p-0 border-0 bg-transparent align-self-center">
                    <img id="imgNavbar" src="${context}/IMG/avatares/${not empty usuarioLogueado.avatar ? usuarioLogueado.avatar : 'default.png'}" 
                         class="rounded-circle border shadow-sm ratio-1x1 ms-2 me-2" 
                         height="50" width="50"
                         style="object-fit: cover; cursor: pointer;"
                         onerror="this.onerror=null;this.src='${context}/IMG/avatares/default.png';">
                </button>
                <div class="d-flex flex-column text-start align-self-center p-0 pb-0">
                    <small class="m-0">${not empty usuarioLogueado.nombre ? usuarioLogueado.nombre : ''}</small>
                    <button type="submit" name="accion" value="logout" class="text-danger fw-semibold p-0 btn-logout-navbar"><small class="mt-0 pt-0">Cerrar sesi&oacute;n</small></button>
                </div>

                <button type="submit" name="accion" value="verCesta" class="btn btn-primaryAlt mb-3 mt-3">
                    <i class="fa-duotone fa-solid fa-cart-shopping"></i>
                </button>
            </div>
        </c:if>

        <c:if test="${empty usuarioLogueado}">
            <div class="btn-group">
                <button type="submit" name="accion" value="registro" class="btn btn-primaryAlt">Registrarse</button>
                <button type="submit" name="accion" value="login" class="btn btn-primaryAlt">Iniciar sesión</button>

                <button type="submit" name="accion" value="verCesta" class="btn btn-primaryAlt">
                    <i class="fa-duotone fa-solid fa-cart-shopping"></i>
                </button>
            </c:if>
        </div>
    </form>
</header>

