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

        <div class="btn-group">
            <button type="submit" name="accion" value="registro" class="btn btn-primaryAlt">Registrarse</button>
            <button type="submit" name="accion" value="login" class="btn btn-primaryAlt">Iniciar sesión</button>
            <button type="submit" name="accion" value="verCesta" class="btn btn-primaryAlt">
                <i class="fa-duotone fa-solid fa-cart-shopping"></i>
            </button>
        </div>
    </form>
</header>

