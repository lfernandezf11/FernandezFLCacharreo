<%-- 
    Author     : fdezf
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <c:import url="/INC/headTags.jsp"/>
    <title>Mi Historial de Pedidos - Cacharreo</title>
    <link rel="stylesheet" type="text/css" href="${style}" />
</head>
<body>
    <c:import url="/INC/header.jsp"/>

    <main class="py-5">
        <div class="row justify-content-center">
            <div class="col-12 col-lg-10">
                
                <c:choose>
                    <c:when test="${empty pedidos}">
                        <div class="text-center p-5 cart-total-bar text-muted mt-5">
                            <i class="bi bi-info-circle display-4 d-block mb-3"></i>
                            <h4>Aún no has realizado ningún pedido</h4>
                            <p>¡Explora nuestro catálogo y empieza a "cacharrear"!</p>
                            <form action="${context}/FrontController" method="post">
                            <button name="accion" value="inicio" class="btn btn-primary mt-3">Ir a la tienda</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="section-header mb-4 d-flex align-items-start">
                    <i class="bi bi-clock-history fs-2 me-3"></i>
                    <h3 class="mb-0">Historial de Pedidos</h3>
                </div>
                        <div class="accordion shadow-sm" id="accordionHistorial">
                            <c:forEach items="${pedidos}" var="pedido" varStatus="status">
                                <div class="accordion-item border-0 mb-3 shadow-sm rounded overflow-hidden">
                                    
                                    <h2 class="accordion-header" id="heading${pedido.idPedido}">
                                        <button class="accordion-button collapsed py-3" type="button" data-bs-toggle="collapse" 
                                                data-bs-target="#collapse${pedido.idPedido}" aria-expanded="false" 
                                                aria-controls="collapse${pedido.idPedido}">
                                            <div class="d-flex w-100 justify-content-between align-items-center me-3">
                                                <div>
                                                    <span class="badge bg-primary me-2">#${pedido.idPedido}</span>
                                                    <span class="fw-bold text-dark">
                                                        <fmt:formatDate value="${pedido.fecha}" pattern="dd 'de' MMMM, yyyy"/>
                                                    </span>
                                                </div>
                                                <div class="text-end">
                                                    <span class="text-muted small d-block">Total Pagado</span>
                                                    <span class="fw-bold text-dark">
                                                        <fmt:formatNumber value="${pedido.importe + pedido.iva}" type="currency" currencySymbol="?"/>
                                                    </span>
                                                </div>
                                            </div>
                                        </button>
                                    </h2>

                                    <div id="collapse${pedido.idPedido}" class="accordion-collapse collapse" 
                                         aria-labelledby="heading${pedido.idPedido}" data-bs-parent="#accordionHistorial">
                                        <div class="accordion-body p-0">
                                            
                                            <div class="table-responsive px-4 pt-4">
                                                <table class="table table-hover align-middle">
                                                    <thead class="table-light">
                                                        <tr class="text-muted small text-uppercase">
                                                            <th colspan="2">Producto</th>
                                                            <th class="text-center">Cantidad</th>
                                                            <th class="text-end">Precio</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${pedido.lineas}" var="linea">
                                                            <tr>
                                                                <td style="width: 60px;">
                                                                    <img src="${context}/IMG/productos/${linea.producto.imagen}.jpg" 
                                                                         class="rounded shadow-sm" width="45" height="45" style="object-fit: cover;">
                                                                </td>
                                                                <td>
                                                                    <span class="fw-semibold d-block">${linea.producto.nombre}</span>
                                                                    <small class="text-muted">${linea.producto.marca}</small>
                                                                </td>
                                                                <td class="text-center">x${linea.cantidad}</td>
                                                                <td class="text-end fw-bold">
                                                                    <fmt:formatNumber value="${linea.importe}" type="currency" currencySymbol="?"/>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <div class="bg-light p-4 border-top">
                                                <div class="row">
                                                    <div class="col-md-7 border-end border-2">
                                                        <div class="row small text-muted">
                                                            <div class="col-6">
                                                                <h6 class="text-uppercase fw-bold text-dark mb-2" style="font-size: 0.75rem;">Empresa</h6>
                                                                <p class="mb-0"><strong>Cacharreo, S.L.U</strong></p>
                                                                <p class="mb-0">NIF: 08890909Y</p>
                                                                <p class="mb-0">06800 - Mérida (Badajoz)</p>
                                                                <p class="mb-0">España</p>
                                                                <p class="mb-0">Tlf: 665 997 452</p>
                                                            </div>
                                                            <div class="col-6">
                                                                <h6 class="text-uppercase fw-bold text-dark mb-2" style="font-size: 0.75rem;">Cliente</h6>
                                                                <p class="mb-0"><strong>${usuarioLogueado.nombre} ${usuarioLogueado.apellidos}</strong></p>
                                                                <p class="mb-0">${usuarioLogueado.direccion}</p>
                                                                <p class="mb-0">${usuarioLogueado.cp} - ${usuarioLogueado.localidad} (${usuarioLogueado.provincia})</p>
                                                                <p class="mb-0">NIF: ${usuarioLogueado.nif}</p>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="col-md-5 ps-md-4 mt-3 mt-md-0">
                                                        <div class="d-flex justify-content-between mb-1">
                                                            <span class="text-muted">Subtotal:</span>
                                                            <span><fmt:formatNumber value="${pedido.importe}" type="currency" currencySymbol="?"/></span>
                                                        </div>
                                                        <div class="d-flex justify-content-between mb-2">
                                                            <span class="text-muted">IVA (21%):</span>
                                                            <span><fmt:formatNumber value="${pedido.iva}" type="currency" currencySymbol="?"/></span>
                                                        </div>
                                                        <div class="d-flex justify-content-between border-top pt-2">
                                                            <span class="fw-bold h5">TOTAL:</span>
                                                            <span class="fw-bold h5 text-primary">
                                                                <fmt:formatNumber value="${pedido.importe + pedido.iva}" type="currency" currencySymbol="?"/>
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>

    <c:import url="/INC/footer.jsp"/>
</body>
</html>