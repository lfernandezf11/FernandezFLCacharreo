<%-- 
    Author     : Lucía Fernández Florencio
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
        <c:import url="/INC/scrollToTopButton.jsp"/>

        <section class="hero-minimalist mb-0">
            <div class="hero-container">
                <h1 class="hero-title-main">
                    TU TIENDA DE<br>
                    INGENIERIA Y COMPONENTES.
                </h1>
                <button type="button" class="btn btn-primaryAlt mt-3">
                    <a href="#Productos">Empieza a explorar<i class="bi bi-arrow-right ms-2"></i></a>
                </button>
            </div>
            <div class="hero-divider-image">
                <img src="${pageContext.request.contextPath}/IMG/hero.jpg" alt="Detalle de componentes">
            </div>
        </section>

        <main>
            <section class="products" id="Productos">
                <div class="section-header">
                    <h3>Nuestros Productos</h3>
                </div>  
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-12 col-lg-4 mb-4">
                            <aside class="filter-sidebar">
                                <div class="cart-total-bar">
                                    <h4 class="mb-4">Filtros</h4>

                                    <form action="${context}/CestaController" method="post" id="filterForm">

                                        <div class="filter-group mb-4">
                                            <div class="filter-group mb-4">
                                                <div class="input-group">
                                                    <input type="text" class="form-control" id="buscarPalabra" name="fTexto" 
                                                           placeholder="Escribe palabras clave...">
                                                    <span class="input-group-text"><i class="fa-solid fa-magnifying-glass"></i></span>
                                                </div>
                                            </div>
                                            <label class="form-label concept-text">Categor&iacute;as</label>
                                            <div class="dropdown">
                                                <button class="btn btn-white border w-100 text-start dropdown-toggle d-flex justify-content-between align-items-center" 
                                                        type="button" id="dropCat" data-bs-toggle="dropdown" data-bs-auto-close="outside" aria-expanded="false">
                                                    Seleccionar
                                                </button>
                                                <ul class="dropdown-menu w-100 shadow-sm p-2 scrollable-menu" aria-labelledby="dropCat">
                                                    <c:forEach var="cat" items="${applicationScope.categorias}">
                                                        <li class="px-2 py-1">
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="checkbox" name="fCategoria" value="${cat.idCategoria}" id="cat${cat.idCategoria}">
                                                                <label class="form-check-label w-100" for="cat${cat.idCategoria}">
                                                                    ${cat.nombre}
                                                                </label>
                                                            </div>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>

                                        <div class="filter-group mb-4">
                                            <label class="form-label concept-text">Marcas</label>
                                            <div class="dropdown">
                                                <button class="btn btn-white border w-100 text-start dropdown-toggle d-flex justify-content-between align-items-center" 
                                                        type="button" id="dropMarca" data-bs-toggle="dropdown" data-bs-auto-close="outside" aria-expanded="false">
                                                    Seleccionar
                                                </button>
                                                <ul class="dropdown-menu w-100 shadow-sm p-2 scrollable-menu" aria-labelledby="dropMarca">
                                                    <c:forEach var="marca" items="${applicationScope.marcas}">
                                                        <li class="px-2 py-1">
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="checkbox" name="fMarca" value="${marca}" id="marca${marca}">
                                                                <label class="form-check-label w-100" for="marca${marca}">
                                                                    ${marca}
                                                                </label>
                                                            </div>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>

                                        <div class="filter-group mb-4">
                                            <label class="form-label concept-text d-flex justify-content-between">
                                                Rango de Precio:
                                                <span id="priceDisplay" class="text-primary fw-bold">
                                                    <fmt:formatNumber value="${applicationScope.minPrecio}" type="currency" currencySymbol="€" /> - 
                                                    <fmt:formatNumber value="${applicationScope.maxPrecio}" type="currency" currencySymbol="€" />
                                                </span>
                                            </label>

                                            <div class="range-slider-container position-relative" style="height: 35px;">
                                                <input type="range" class="form-range position-absolute top-0 start-0" 
                                                       id="priceMin" name="fPrecioMin"
                                                       min="${applicationScope.minPrecio}" 
                                                       max="${applicationScope.maxPrecio}" 
                                                       value="${applicationScope.minPrecio}">

                                                <input type="range" class="form-range position-absolute top-0 start-0" 
                                                       id="priceMax" name="fPrecioMax"
                                                       min="${applicationScope.minPrecio}" 
                                                       max="${applicationScope.maxPrecio}" 
                                                       value="${applicationScope.maxPrecio}">
                                            </div>
                                        </div>

                                        <div class="filter-btns mt-3">
                                            <button type="submit" name="accion" value="filtrarProductos" 
                                                    class="btn btn-primary w-100" 
                                                    id="btn-apply-filters">
                                                Aplicar Filtros
                                            </button>

                                            <button type="button" id="limpiarFiltros" 
                                                    class="btn-clear-link fw-semibold mt-2">
                                                Limpiar filtros
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </aside>
                        </div>

                        <div class="col-12 col-lg-8">
                            <div class="row g-4 ms-2 pt-3">
                                <c:choose>
                                    <c:when test="${not empty productosFiltrados}">
                                        <c:forEach var="producto" items="${productosFiltrados}">
                                            <div class="col-12 col-md-6 col-lg-4 mb-3">
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

                                                            <h4 class="price-tag" style="font-size: 40px;">
                                                                <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                                            </h4>
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
                                                            <div class="product-image mb-2">
                                                                <img src="${context}/IMG/productos/${producto.imagen != null ? producto.imagen : 'default'}.jpg" 
                                                                     alt="${producto.nombre}"> 
                                                            </div>

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
                                    </c:when>
                                    <c:otherwise>
                                        <div class="col-12 text-start py-4">   
                                            <h4 class="text-secondary">No hay resultados para esta b&uacute;squeda.</h4>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <section id="trending">
                <div class="section-header">
                    <h3>Los más populares</h3>
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

                                                                <h4 class="price-tag" style="font-size: 40px;">
                                                                    <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                                                </h4>
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
                                                                <div class="product-image mb-2">
                                                                    <img src="${context}/IMG/productos/${producto.imagen != null ? producto.imagen : 'default'}.jpg" 
                                                                         alt="${producto.nombre}"> 
                                                                </div>

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
            <section class="position-relative overflow-hidden testimonial-carousel-section py-5" id="Opiniones">
                <c:import url="/INC/reviews.jsp"/>
            </section>
        </main>

        <c:import url="/INC/footer.jsp"/>
        <script src="${pageContext.request.contextPath}/JS/cestaJS.js"></script>
        <script src="${pageContext.request.contextPath}/JS/reviewsLogic.js"></script>
    </body>
</html>