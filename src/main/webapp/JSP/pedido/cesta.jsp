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
        <title>Mi Cesta - Cacharreo</title>
        <link rel="stylesheet" type="text/css" href="${style}" /> 
    </head>
    <body> 

        <c:import url="/INC/header.jsp"/>
        <c:import url="/INC/toast.jsp"/>

        <main>
            <section class="cart-section">
                <div class="section-header">
                    <h3 class="">Tu cesta</h3>
                </div>

                <div class="cart-layout mt-1">
                    <!-- Lado izquierdo: tarjetas con scroll o tarjeta de cesta vacía -->
                    <div class="cart-items-container">
                        <c:choose>
                            <c:when test="${empty cesta.lineas}">
                                <article class="empty-cart-card d-flex justify-content-center align-items-center">
                                    <div class="empty-cart-content d-flex flex-column align-items-center text-center">
                                        <img src="${context}/IMG/empty-cart.png" alt="Cesta vacía" class="img-empty-cart mb-3"
                                             height="200" width="200">

                                        <h4>Tu cesta está vacía</h4>
                                        <p>Parece que aún no has añadido nada al carrito.</p>

                                        <form method="post" action="${context}/FrontController">
                                            <button type="submit" name="accion" value="inicio" class="btn btn-primaryAlt">Volver a la tienda</button>
                                        </form> 
                                    </div>
                                </article>
                            </c:when>

                            <c:otherwise>
                                <c:forEach items="${cesta.lineas}" var="linea">
                                    <article class="cart-item-card">

                                        <div class="cart-item-image">
                                            <img src="${context}/IMG/productos/${linea.producto.imagen != null ? linea.producto.imagen : 'default'}.jpg" 
                                                 alt="${linea.producto.nombre}">
                                        </div>

                                        <div class="cart-item-content">
                                            <div class="item-info-top">
                                                <h4 class="item-name fw-semibold">${linea.producto.nombre}</h4>
                                                <span class="item-id">C&Oacute;DIGO: ${linea.producto.idProducto}</span>
                                            </div>

                                            <div class="item-info-bottom">
                                                <!-- Es necesario mantener el formateo en la línea para que la primera vez que cargue el carrito 
                                                         los precios no estén vacíos. -->
                                                <div class="price-group">
                                                    <p>Precio unidad: 
                                                        <strong><fmt:formatNumber value="${linea.producto.precio}" type="currency" currencySymbol="€"/></strong>
                                                    </p>

                                                    <p class="mb-0">Subtotal: 
                                                        <strong class="linea-subtotal" >
                                                            <fmt:formatNumber value="${linea.importe}" type="currency" currencySymbol="€"/>
                                                        </strong>
                                                    </p>
                                                </div>

                                                <form class="item-controls">
                                                    <input type="hidden" name="idProducto" value="${linea.producto.idProducto}">

                                                    <div class="qty-selector">
                                                        <button type="button" name="accion" value="restar" class="btn btn-secondary pt-1 pb-1 fw-semibold" 
                                                                <c:if test="${linea.cantidad <= 1}">disabled</c:if>>-</button>
                                                        <span class="qty-val fw-semibold">${linea.cantidad}</span>
                                                        <button type="button" name="accion" value="sumar" class="btn btn-secondary pt-1 pb-1 fw-semibold">+</button>
                                                    </div>

                                                    <button type="button" name="accion" value="eliminar" class="btn btn-secondary">Eliminar</button>
                                                </form>
                                            </div>
                                        </div>
                                    </article>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <!-- Lado derecho: totales fijos -->
                    <aside class="cart-summary-aside">
                        <div class="cart-total-bar">

                            <div class="summary-table">
                                <h4 class="mb-4">Resumen</h4>
                                <div class="summary-row">
                                    <span class="concept-text">Subtotal</span>
                                    <span class="value-text" id="cart-amount">
                                        <fmt:formatNumber value="${cesta.importe}" type="currency" currencySymbol="€"/>
                                    </span>
                                </div>

                                <div class="summary-row">
                                    <span class="concept-text">Envío</span>
                                    <span class="value-text shipping-fee">GRATIS</span>
                                </div>

                                <div class="summary-row">
                                    <span class="concept-text">IVA (21%)</span>
                                    <span class="value-text" id="iva-amount">
                                        <fmt:formatNumber value="${cesta.iva}" type="currency" currencySymbol="€"/>
                                    </span>
                                </div>

                                <div class="summary-row total-final-row">
                                    <span class="total-label">TOTAL PEDIDO</span>
                                    <span class="total-value" id="total-final">
                                        <fmt:formatNumber value="${cesta.importe + cesta.iva}" type="currency" currencySymbol="€"/>
                                    </span>
                                </div>
                            </div>

                            <div class="summary-btns mt-3">
                                <button type="button" name="accion" value="tramitarPedido" 
                                        class="btn btn-primary w-100 ${empty cesta.lineas ? 'disabled' : ''}" 
                                        id="btn-buy" ${empty cesta.lineas ? 'disabled' : ''}>
                                    Tramitar pedido
                                </button>

                                <c:if test="${not empty cesta.lineas}">
                                    <button type="button" 
                                            class="btn-clear-link btn-del-cart fw-semibold" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#confirmVaciarModal">
                                        Vaciar cesta
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>
        </main>

        <!-- Modal para confirmar el vaciado de la cesta completa -->
        <div class="modal fade" id="confirmVaciarModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">¿Vaciar cesta?</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Se eliminarán todos los productos de tu carrito. Esta acción no se puede deshacer.
                    </div>
                    <div class="modal-footer bg-transparent border-0 pt-0 align-start">
                        <form action="${context}/CestaController" method="post">
                            <button type="submit" name="accion" value="eliminarCarrito" class="btn btn-primary">Vaciar ahora</button>
                        </form>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
        </div>


        <c:import url="/INC/footer.jsp"/>
        <script src="${pageContext.request.contextPath}/JS/cestaJS.js"></script>

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