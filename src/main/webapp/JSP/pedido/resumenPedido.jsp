<%-- 
    Author     : Lucía Fernández Florencio
--%>
<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<!DOCTYPE html>
<html lang="es">
<head>
    <c:import url="/INC/headTags.jsp"/>
    <title>¡Gracias por tu compra! - Cacharreo</title>
    <link rel="stylesheet" type="text/css" href="${style}" />
</head>
<body>
    <c:import url="/INC/header.jsp"/>

    <main class="py-5 px-0  col-12">
        <div class="row justify-content-center mt-5">
            <div class="col-12 col-lg-9">
                
                <div class="cart-total-bar overflow-hidden p-0">
                    <div class="card-header text-white text-center py-4" style='background-color: var(--color-lilac);'>
                        <i class="bi bi-check-circle-fill display-4"></i>
                        <h3 class="mt-2 mb-0 text-uppercase">¡Pedido Confirmado!</h3>
                        <p class="mb-0 opacity-75">Gracias por confiar en Cacharreo, ${usuarioLogueado.nombre}</p>
                    </div>

                    <div class="card-body p-4 p-md-5">
                        <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
                            <div>
                                <span class="text-muted d-block small text-uppercase">Nº de Pedido</span>
                                <span class="fw-bold text-primary">#${pedidoFinalizado.idPedido}</span>
                            </div>
                            <div class="text-end">
                                <span class="text-muted d-block small text-uppercase">Fecha de Compra</span>
                                <span class="fw-bold">
                                    <fmt:formatDate value="${pedidoFinalizado.fecha}" pattern="dd/MM/yyyy"/>
                                </span>
                            </div>
                        </div>

                        <h5 class="mb-3">Detalle de artículos</h5>
                        <div class="table-responsive">
                            <table class="table table-borderless align-middle">
                                <thead class="table-light">
                                    <tr class="text-secondary fw-400 text-muted text-uppercase">
                                        <th colspan="2">Producto</th>
                                        <th class="text-center">Cant.</th>
                                        <th class="text-end">Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${pedidoFinalizado.lineas}" var="linea">
                                        <tr class="border-bottom">
                                            <td style="width: 60px;">
                                                <img src="${context}/IMG/productos/${linea.producto.imagen}.jpg" 
                                                     class="rounded" width="50" height="50" style="object-fit: cover;">
                                            </td>
                                            <td>
                                                <span class="d-block fw-semibold">${linea.producto.nombre}</span>
                                                <small class="text-muted">${linea.producto.marca}</small>
                                            </td>
                                            <td class="text-center">x${linea.cantidad}</td>
                                            <td class="text-end fw-bold">
                                                <fmt:formatNumber value="${linea.importe}" type="currency" currencySymbol="€"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="row justify-content-end mt-4" style="font-family: 'FigTree';">
                            <div class="col-md-5">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Subtotal</span>
                                    <span><fmt:formatNumber value="${pedidoFinalizado.importe}" type="currency" currencySymbol="€"/></span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">IVA (21%)</span>
                                    <span><fmt:formatNumber value="${pedidoFinalizado.iva}" type="currency" currencySymbol="€"/></span>
                                </div>
                                <div class="d-flex justify-content-between mt-3 pt-3 border-top">
                                    <span class="h4 fw-bold">TOTAL</span>
                                    <span class="h4 fw-bold" style='color: var(--color-dark-lilac);'>
                                        <fmt:formatNumber value="${pedidoFinalizado.importe + pedidoFinalizado.iva}" type="currency" currencySymbol="€"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card-footer bg-light p-4 text-center border-top">
                        <p class="text-muted small mb-3">Se enviar&aacute; un correo de confirmaci&oacute;n a <strong>${usuarioLogueado.email}</strong></p>
                        <form action="${context}/UsuarioController" method="post">
                            <button type="submit" name="accion" value="inicio" class="btn btn-primary px-5">
                                Seguir Comprando
                            </button>
                            <button type="submit" name="accion" value="historial" class="btn btn-secondary px-5 ms-3">
                                Historial de pedidos
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <c:import url="/INC/footer.jsp"/>
</body>
</html>