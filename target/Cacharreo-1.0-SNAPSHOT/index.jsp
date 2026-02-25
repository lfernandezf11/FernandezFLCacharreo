<%-- 
    Author     : fdezf
--%>

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:url var="style" value="/CSS/styles.css" scope="application" />
<c:set var="context" value="${pageContext.request.contextPath}" scope="application" />

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

        <section class="hero" id="hero">
            <img src="${context}/IMG/logoHero.png" alt="Logo Cacharreo">
        </section>
            
            

        <main>
            <section id="trending">
                <div class="section-header">
                    <h4>Los más populares_</h4>
                </div>  
                <div class="carousel-container-custom">
                    <button class="btn btn-primaryAlt custom-nav-btn prev-btn" type="button" data-bs-target="#multiItemCarousel" data-bs-slide="prev">
                        <i class="fa-solid fa-angles-left"></i>
                    </button>
                    <div id="multiItemCarousel" class="carousel slide" data-bs-interval="false">
                        <div class="carousel-inner">
                            <c:forEach items="${subCatalogo}" var="grupo" varStatus="status">
                                <div class="carousel-item ${status.first ? 'active' : ''}">
                                    <div class="container">
                                        <div class="row">
                                            <c:forEach items="${grupo}" var="producto">

                                                <div class="col-12 col-md-6 col-lg-3">
                                                    <article class="product-card h-100" 
                                                             data-bs-toggle="modal" 
                                                             data-bs-target="#modal${producto.idProducto}"
                                                             style="cursor: pointer;">

                                                        <div class="product-image">
                                                            <img src="${context}/IMG/productos/${producto.imagen != null ? producto.imagen : 'default'}.jpg" 
                                                                 alt="${producto.nombre}"> 
                                                        </div>

                                                        <div class="product-info">
                                                            <span class="product-brand badge bg-secondary mb-0">${producto.marca}</span>
                                                            <div class="text-start">
                                                                <h3 class="product-name">${producto.nombre}</h3>

                                                                <p class="product-price price-ribbon-flat fw-bold">
                                                                    <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                                                </p>
                                                            </div>
                                                        </div>
                                                    </article>
                                                </div>

                                                <!-- Modal para añadir el producto -->
                                                <div class="modal fade" id="modal${producto.idProducto}" tabindex="-1" aria-hidden="true">
                                                    <div class="modal-dialog modal-dialog-centered">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title fw-bold">${producto.nombre}</h5>
                                                                <button type="button" class="btn-close custom-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                                                            </div>

                                                            <div class="modal-body">
                                                                <div class="product-details">
                                                                    <p class="description-text">
                                                                        ${producto.descripcion}
                                                                    </p>
                                                                    <h4 class="price-tag">
                                                                        <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                                                    </h4>
                                                                </div>
                                                            </div>

                                                            <form class="modal-footer addForm">
                                                                <input type="hidden" name="idProducto" value="${producto.idProducto}">
                                                                <button type="submit" name="accion" value="addCarrito" class="btn btn-primary btn-add">
                                                                    Añadir a la cesta
                                                                </button>
                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                                                    Seguir mirando
                                                                </button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>

                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <button class="custom-nav-btn next-btn btn btn-primaryAlt" type="button" data-bs-target="#multiItemCarousel" data-bs-slide="next">
                            <i class="fa-solid fa-angles-right"></i>
                        </button>

                    </div>
            </section>
        </main>

        <c:import url="/INC/footer.jsp"/>
        <script src="${pageContext.request.contextPath}/JS/cestaJS.js"></script>
    </body>
</html>