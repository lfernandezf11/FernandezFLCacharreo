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
        <title>Nuestros productos</title>
        <link rel="stylesheet" type="text/css" href="${style}" /> 
    </head>
    <body> 
        <c:import url="/INC/header.jsp"/>
        <main>
            <section class="products">
                <div class="section-header">
                    <h3>Nuestros Productos_</h3>
                    <h4>Elige entre una amplia gama de productos de las mejores marcas </h4>
                </div>  
                <div class="productos-grid">
                    <c:forEach items="${catalogo}" var="producto">
                        <article class="product-card">
                            <div class="product-image">
                                <%-- Si no hay imagen, usamos default.jpg --%>
                                <img src="${context}/IMG/productos/${producto.imagen != null ? producto.imagen : 'default.jpg'}.jpg" 
                                     alt="${producto.nombre}">
                            </div>

                            <div class="product-info">
                                <span class="product-brand">${producto.marca}</span>
                                <h3 class="product-name">${producto.nombre}</h3>
                                <p class="product-price">
                                    <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                </p>

                                <%-- Formulario para añadir al carrito --%>
                                <form action="${context}/CestaController" method="post">
                                    <input type="hidden" name="idProducto" value="${producto.idProducto}">
                                    <button type="submit" name="accion" value="addCarrito" class="btn btn-primary">
                                        Añadir al carrito
                                    </button>
                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
  Launch demo modal
</button>
                                </form>
                            </div>
                        </article>
                    </c:forEach>
                </div>
            </section>
        </main>
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">Modal title</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        ...
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>
        <c:import url="/INC/footer.jsp"/>
    </body>
</html>

