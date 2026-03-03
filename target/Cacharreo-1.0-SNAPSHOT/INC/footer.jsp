<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<footer class="text-center text-lg-start text-white shadow-lg" style="background-color: var(--color-dark-lilac)">

    <div class="p-1" style="background-color: var(--color-lilac)">
        <div class="container d-flex justify-content-end align-items-center mt-0">
            <section class="mt-0 mb-0">
                <a class="btn btn-outline-light rounded-circle  p-0 d-inline-flex align-items-center justify-content-center m-1" 
                   href="https://www.facebook.com/" role="button" target="blank">
                    <i class="fab fa-facebook-f"></i></a>
                <a class="btn btn-outline-light rounded-circle p-0 d-inline-flex align-items-center justify-content-center m-1" 
                   href="https://x.com/" role="button" target="blank">
                    <i class="fab fa-twitter"></i></a>
                <a class="btn btn-outline-light rounded-circle p-0 d-inline-flex align-items-center justify-content-center m-1" 
                   href="https://www.google.com/" role="button" target="blank">
                    <i class="fab fa-google"></i></a>
                <a class="btn btn-outline-light rounded-circle p-0 d-inline-flex align-items-center justify-content-center m-1" 
                   href="https://www.instagram.com/" role="button" target="blank">
                    <i class="fab fa-instagram"></i></a>
                <a class="btn btn-outline-light rounded-circle p-0 d-inline-flex align-items-center justify-content-center m-1" 
                   href="https://es.linkedin.com/" role="button" target="blank">
                    <i class="fab fa-linkedin-in"></i></a>
                <a class="btn btn-outline-light rounded-circle p-0 d-inline-flex align-items-center justify-content-center m-1" 
                   href="https://github.com/" role="button" target="blank">
                    <i class="fab fa-github"></i></a>
            </section>
        </div>
    </div>

    <div class="container text-center text-md-start mt-4 pb-2">
        <div class="row mt-3">

            <div class="col-md-3 col-lg-4 col-xl-4 mx-auto mb-4">
                <h6 class="text-uppercase fw-bold">CACHARREO</h6>
                <hr class="mb-4 mt-0 d-inline-block mx-auto" style="width: 60px; background-color: #7c4dff; height: 2px" />
                <p style="line-height: 1.6;">
                    Más que una tienda, somos el lugar donde los amantes de los "cacharros" se sienten como en casa.
                    <br><br>
                    Nos encanta explorar, probar y traerte lo último en gadgets y componentes. Si te apasiona trastear con la tecnología tanto como a nosotros, estás en el lugar correcto.
                </p>
            </div>

            <div class="col-md-2 col-lg-2 col-xl-2 mx-auto mb-4">
                <h6 class="text-uppercase fw-bold">Enlaces</h6>
                <hr class="mb-4 mt-0 d-inline-block mx-auto" style="width: 60px; background-color: #7c4dff; height: 2px" />
                <p><a href="#Productos" class="text-white text-decoration-none">Nuestros productos</a></p>
                <p><a class="text-white text-decoration-none" disabled>¿Quiénes somos?</a></p>
                <p><a class="text-white text-decoration-none" disabled>Política de privacidad</a></p>
            </div>

            <div class="col-md-4 col-lg-3 col-xl-3 mx-auto mb-md-0 mb-4">
                <h6 class="text-uppercase fw-bold">Contacto</h6>
                <hr class="mb-4 mt-0 d-inline-block mx-auto" style="width: 60px; background-color: #7c4dff; height: 2px" />
                <p><i class="fas fa-map-marker-alt me-3 text-white"></i> Mérida, Extremadura, ES</p>
                <p><i class="fas fa-envelope me-3 text-white"></i> cacharreoinfo@gmail.com</p>
                <p><i class="fas fa-phone me-3 text-white"></i> + 34 665 997 452</p>
            </div>
        </div>
    </div>

    <c:if test="${empty usuarioLogueado}">
        <form method="post" action="${context}/FrontController">
            <div class="container-fluid py-3" style="border-top: 1px solid rgba(255,255,255,0.1);">
                <div class="d-flex justify-content-center align-items-center">
                    <span class="me-3">¡Regístrate gratis!</span>
                    <button type="submit" class="btn btn-outline-light rounded-pill px-4"
                            name="accion" value="registro">
                        REGISTRARSE
                    </button>

                </div>
            </div>
        </form>
    </c:if>

    <div class="text-center p-3 fw-light" style="background-color: var(--color-darkest-lilac); font-size: 0.9rem;">
        © 2026 Cacharreo. Todos los derechos reservados.
    </div>

</footer>


<script src="${pageContext.request.contextPath}/JS/main.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@floating-ui/core@1.7.4"></script>
<script src="https://cdn.jsdelivr.net/npm/@floating-ui/dom@1.7.5"></script>