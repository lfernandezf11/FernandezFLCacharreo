<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="css-navbar">


    <form action="${pageContext.request.contextPath}/FrontController" method="post" class="nav-form">
        <button type="submit" name="accion" value="inicio" class="btn-logo">
            <img class="logo" src="${pageContext.request.contextPath}/IMG/logo.png" alt="Logo de Cacharreo">
        </button>
        <nav class="nav-links">
            <button type="submit" name="accion" value="inicio" class="btn-link">Inicio</button>
            <button  class="btn-link"><a href="#Opiniones">Opiniones</a></button>
            <button  class="btn-link"><a href="#Productos">Productos</a></button>

            <!--<button type="submit" name="accion" value="contacto" class="btn-link">Contacto</button>-->
        </nav>


        <c:if test="${not empty usuarioLogueado}">
            <div class="btn-group-logueado d-flex align-content-center justify-items-center gap-0"> 
                <button type="submit" name="accion" value="perfil" class="p-0 border-0 bg-transparent align-self-center">
                    <img id="imgNavbar" src="${context}/IMG/avatares/${not empty usuarioLogueado.avatar ? usuarioLogueado.avatar : 'default.png'}" 
                         class="rounded-circle border-2 shadow-sm ratio-1x1 ms-2 me-2" 
                         height="55" width="55"
                         style="object-fit: cover; cursor: pointer;"
                         onerror="this.onerror=null;this.src='${context}/IMG/avatares/default.png';">
                </button>
                <div class="d-flex flex-column text-start align-self-center p-0 pb-0">
                    <small class="m-0 fw-semibold text-uppercase text-white">${not empty usuarioLogueado.nombre ? usuarioLogueado.nombre : ''}</small>
                    <button type="submit" name="accion" value="logout" class="text-danger fw-semibold p-0 btn-logout-navbar"><small class="mt-0 pt-0">Cerrar sesi&oacute;n</small></button>
                </div>

                <button type="submit" name="accion" value="verCesta" class="btn btn-primaryAlt position-relative align-self-center"
                        style="max-height: 44px;">
                    <i class="fa-duotone fa-solid fa-cart-shopping"></i>
                    <span>Carrito</span>

                    <c:set var="totalArticulos" value="0" />
                    <c:forEach var="linea" items="${sessionScope.cesta.lineas}">
                        <c:set var="totalArticulos" value="${totalArticulos + linea.cantidad}" />
                    </c:forEach>

                    <%-- El badge siempre existe en el DOM para el JS. Usamos una clase para ocultarlo si es 0 --%>
                    <span id="cart-badge" 
                          class="position-absolute top mt-1 start-100 translate-middle badge rounded-pill bg-danger ${totalArticulos == 0 ? 'd-none' : ''}" 
                          style="font-size: 0.7rem; padding: 0.35em 0.6em; border: 2px solid white; z-index: 10;">
                        ${totalArticulos}
                    </span>
                </button>
            </div>
        </c:if>

        <c:if test="${empty usuarioLogueado}">
            <div class="btn-group">
                <!--<button type="submit" name="accion" value="registro" class="btn btn-primaryAlt">Registrarse</button>-->
                <button type="submit" name="accion" value="login" class="btn btn-primaryAlt">Acceder</button>

                <button type="submit" name="accion" value="verCesta" class="btn btn-primaryAlt position-relative">
                    <i class="fa-duotone fa-solid fa-cart-shopping"></i>
                    <span>Carrito</span>

                    <c:set var="totalArticulos" value="0" />
                    <c:forEach var="linea" items="${sessionScope.cesta.lineas}">
                        <c:set var="totalArticulos" value="${totalArticulos + linea.cantidad}" />
                    </c:forEach>

                    <span id="cart-badge" 
                          class="position-absolute top mt-1 start-100 translate-middle badge rounded-pill bg-danger ${totalArticulos == 0 ? 'd-none' : ''}" 
                          style="font-size: 0.7rem; padding: 0.35em 0.6em; border: 2px solid white; z-index: 10;">
                        ${totalArticulos}
                    </span>
                </button>
            </div>
        </c:if>
    </form>
</header>

