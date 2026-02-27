<%-- 
    Author     : fdezf
--%>

<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <c:import url="/INC/headTags.jsp"/>
        <title>Nuestros productos</title>
        <link rel="stylesheet" type="text/css" href="${style}" /> 
    </head>
    <body> 
        <c:import url="/INC/header.jsp"/>
        <c:import url="/INC/toast.jsp"/>
        <main>
            <section class="products">
                <div class="section-header">
                    <h3>Nuestros Productos</h3>
                    <h4>Elige entre una amplia gama de productos de las mejores marcas </h4>
                </div>  


                <div class="container-fluid">
                    <div class="row">
                        <div class="col-12 col-lg-3 mb-4">
                            <aside class="filters-sidebar">
                                <div class="cart-total-bar">
                                    <h4 class="mb-4">Filtros</h4>

                                    <form action="${context}/CestaController" method="post" id="filterForm">

                                        <div class="filter-group mb-4">
                                            <label class="form-label concept-text">Categor&iacute;as</label>
                                            <div class="dropdown">
                                                <button class="btn btn-white border w-100 text-start dropdown-toggle d-flex justify-content-between align-items-center" 
                                                        type="button" id="dropCat" data-bs-toggle="dropdown" data-bs-auto-close="outside" aria-expanded="false">
                                                    Seleccionar
                                                </button>
                                                <ul class="dropdown-menu w-100 shadow-sm p-2" aria-labelledby="dropCat">
                                                    <c:forEach var="cat" items="${applicationScope.categorias}">
                                                        <li class="px-2 py-1">
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="checkbox" name="f_categoria" value="${cat.idCategoria}" id="cat${cat.idCategoria}">
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
                                                <ul class="dropdown-menu w-100 shadow-sm p-2" aria-labelledby="dropMarca">
                                                    <c:forEach var="marca" items="${applicationScope.marcas}">
                                                        <li class="px-2 py-1">
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="checkbox" name="f_marca" value="${marca}" id="marca${marca}">
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
                                                    ${applicationScope.minPrecio}€ - ${applicationScope.maxPrecio}€
                                                </span>
                                            </label>

                                            <div class="range-slider-container position-relative" style="height: 35px;">
                                                <input type="range" class="form-range position-absolute top-0 start-0" 
                                                       id="priceMin" name="f_precio_min"
                                                       min="${applicationScope.minPrecio}" 
                                                       max="${applicationScope.maxPrecio}" 
                                                       value="${applicationScope.minPrecio}">

                                                <input type="range" class="form-range position-absolute top-0 start-0" 
                                                       id="priceMax" name="f_precio_max"
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

                        <div class="col-12 col-lg-9">
                            <div class="row g-4 ms-2">
                                <c:choose>
                                    <c:when test="${not empty productosFiltrados}">
                                        <c:forEach var="producto" items="${productosFiltrados}">
                                            <div class="col-12 col-md-6 col-xl-4">
                                                <article class="product-card h-100" 
                                                         data-bs-toggle="modal" 
                                                         data-bs-target="#modal${producto.idProducto}"
                                                         style="cursor: pointer;">
                                                    <div class="product-image">
                                                        <img src="${context}/IMG/productos/${producto.imagen != null ? producto.imagen : 'default'}.jpg" 
                                                             class="img-fluid" alt="${producto.nombre}"> 
                                                    </div>
                                                    <div class="product-info p-3">
                                                        <span class="product-brand badge bg-secondary mb-2">${producto.marca}</span>
                                                        <div class="text-start">
                                                            <h3 class="product-name fs-5 fw-bold">${producto.nombre}</h3>
                                                            <h4 class="price-tag text-primary h3">
                                                                <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                                            </h4>
                                                        </div>
                                                    </div>
                                                </article>
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
        </main>

        <c:import url="/INC/footer.jsp"/>
        <script src="${pageContext.request.contextPath}/JS/cestaJS.js"></script>
    </body>
</html>

