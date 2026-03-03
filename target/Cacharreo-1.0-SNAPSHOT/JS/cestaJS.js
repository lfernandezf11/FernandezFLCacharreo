/** * Elementos globales para la actualización del resumen de compra 
 */
const subtotalGlobal = document.getElementById('cart-amount');
const totalIva = document.getElementById('iva-amount');
const totalPedido = document.getElementById('total-final');

/**
 * Actualiza el indicador numérico (badge) del carrito en la interfaz.
 * Controla la visibilidad del elemento basándose en si la cantidad es mayor a cero.
 * 
 * @param {number|string} cantidad - El número total de productos en la cesta.
 * @returns {void}
 */
function actualizarBadge(cantidad) {
    const badge = document.getElementById('cart-badge');
    if (badge) {
        badge.textContent = cantidad;
        // Si la cantidad es mayor a 0, lo mostramos; si no, lo ocultamos
        if (parseInt(cantidad) > 0) {
            badge.classList.remove('d-none');
        } else {
            badge.classList.add('d-none');
        }
    }
}

/**
 * Actualiza las unidades de un producto en la cesta mediante una petición asíncrona.
 * Gestiona la actualización de totales de línea, totales globales y el estado de los botones.
 * 
 * @param {string} idProducto - ID único del producto a modificar.
 * @param {string} accion - Tipo de operación: 'sumar' o 'restar'.
 * @param {HTMLElement} form - Contenedor HTML (formulario) que origina la acción.
 * @returns {Promise<void>}
 */
async function actualizarUnidades(idProducto, accion, form) {
    const data = new URLSearchParams();
    data.append('accion', accion);
    data.append('idProducto', idProducto);

    try {
        let response = await fetch(URL_CESTA, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: data.toString()
        });

        if (response.ok) {
            let resultado = await response.json();

            if (resultado.success) {
                // 1. Actualizar cantidad en la tarjeta, controlando que el botón - esté deshabilitado si hay una unidad del producto
                form.querySelector('.qty-val').textContent = resultado.nuevaCantidad;
                actualizarBadge(resultado.totalUnidadesCesta);

                const btnRestar = form.querySelector('button[value="restar"]');
                if (btnRestar) {
                    btnRestar.disabled = (parseInt(resultado.nuevaCantidad) <= 1);
                }

                // 2. Actualizar el subtotal de la línea (la tarjeta)
                const itemCard = form.closest('.cart-item-card');
                const subtotalLineaEl = itemCard.querySelector('.linea-subtotal');
                if (subtotalLineaEl) {
                    subtotalLineaEl.textContent = resultado.subtotalProd + " €";
                }

                // 3. ACTUALIZAR RESUMEN DE TOTALES (Lado derecho)
                if (subtotalGlobal && resultado.subtotalCesta) {
                    subtotalGlobal.textContent = resultado.subtotalCesta + " €";
                }
                if (totalIva && resultado.ivaCesta) {
                    totalIva.textContent = resultado.ivaCesta + " €";
                }
                if (totalPedido && resultado.totalCesta) {
                    totalPedido.textContent = resultado.totalCesta + " €"; //// Display de nuevo total. Como ya viene formateado con dos decimales desde el controlador, solo añadimos el símbolo
                }
            }
        }
    } catch (error) {
        lanzarToast("Error de conexión al actualizar unidades: " + error, "error");
    }
}

/**
 * Elimina un producto de la cesta y del DOM con un efecto visual de desvanecimiento.
 * Si la cesta queda vacía tras la eliminación, recarga la página.
 * 
 * @param {string} idProducto - Identificador del producto a eliminar.
 * @param {HTMLElement} form - Elemento que contiene la referencia al producto.
 * @returns {Promise<void>}
 */
async function eliminarFila(idProducto, form) {
    const data = new URLSearchParams();
    data.append('accion', 'eliminar');
    data.append('idProducto', idProducto);

    try {
        let response = await fetch(URL_CESTA, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: data.toString()
        });

        if (response.ok) {
            let resultado = await response.json();

            if (resultado.success) {
                const itemCard = form.closest('.cart-item-card'); // Tarjeta asociada al artículo para la animación

                if (itemCard) {
                    // Animación de borrado
                    itemCard.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
                    itemCard.style.opacity = '0';
                    itemCard.style.transform = 'translateX(20px)';

                    // Esperamos a que la animación termine antes de quitar el elemento del DOM
                    setTimeout(() => {
                        itemCard.remove();
                        actualizarBadge(resultado.totalUnidadesCesta);

                        // Actualizamos totales CON PROTECCIÓN (si fallan, no rompen el script)
                        try {
                            if (subtotalGlobal)
                                subtotalGlobal.textContent = (resultado.subtotalCesta || "0.00") + " €";
                            if (totalIva)
                                totalIva.textContent = (resultado.ivaCesta || "0.00") + " €";
                            if (totalPedido)
                                totalPedido.textContent = (resultado.totalCesta || "0.00") + " €"; // Display de nuevo total. Como ya viene formateado con dos decimales desde el controlador, solo añadimos el símbolo
                        } catch (e) {
                            console.warn("Error al actualizar textos de totales:", e);
                        }

                        // Si era el último producto, recargamos para mostrar el estado "Cesta vacía"
                        if (document.querySelectorAll('.cart-item-card').length === 0) {
                            location.reload();
                        }
                    }, 400);
                }
                lanzarToast(resultado.message, "exito");
            } else {
                lanzarToast(resultado.message || "No se pudo eliminar el producto", "error");
            }
        }
    } catch (error) {
        lanzarToast("Error crítico de conexión al eliminar", "error");
    }
}

// MANEJO DE EVENTOS EN CESTA.JSP
document.addEventListener('click', (e) => {
    // Buscamos si el clic fue en el botón o dentro de él
    const btn = e.target.closest('.qty-selector button, button[value="eliminar"]');

    if (btn) {
        e.preventDefault();
        const form = btn.closest('form');

        // Verificación de seguridad
        if (!form) {
            console.error("No se encontró el formulario para este botón");
            return;
        }

        const inputId = form.querySelector('input[name="idProducto"]');
        if (!inputId) {
            console.error("No se encontró el input idProducto en este formulario");
            return;
        }

        const idProd = inputId.value;
        const accion = btn.value;

        if (accion === "eliminar") {
            eliminarFila(idProd, form);
        } else {
            actualizarUnidades(idProd, accion, form);
        }
    }
});



// Manejo del evento Submit del formulario de añadir producto
document.addEventListener('submit', async (e) => {
    if (e.target.classList.contains('addForm')) {
        e.preventDefault();

        const form = e.target;

        const btnSubmit = form.querySelector('.btn-add');
        const idProducto = form.querySelector('input[name="idProducto"]').value;

        const data = new URLSearchParams();
        data.append('accion', 'addCarrito');
        data.append('idProducto', idProducto);

        if (btnSubmit)
            btnSubmit.disabled = true;

        try {
            const response = await fetch(URL_CESTA, {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: data.toString()
            });

            if (response.ok) {
                const resultado = await response.json();

                if (resultado.success) {
                    //Localizamos el modal y lo cerramos
                    const modalEl = form.closest('.modal');
                    const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEl);

                    if (modalInstance) {
                        modalInstance.hide(); //dispara los listeners de cierre de modal
                    }
                    actualizarBadge(resultado.totalUnidadesCesta);
                    lanzarToast(resultado.message, "exito");
                } else {
                    lanzarToast("Error: " + (resultado.message || "No se pudo registrar."), "error");
                }
            }
        } catch (error) {
            lanzarToast("Error crítico de conexión", "error");
        } finally {
            // Rehabilitamos el botón solo si el modal no se cerró (hay error)
            // Si el modal se cerró, el listener global lo reseteará al ocultarse.
            if (btnSubmit)
                btnSubmit.disabled = false;
        }
    }
});


/* FILTROS */

document.addEventListener('DOMContentLoaded', () => {
    const minInput = document.getElementById('priceMin');
    const maxInput = document.getElementById('priceMax');
    const display = document.getElementById('priceDisplay');
    const formulario = document.getElementById('filterForm');
    const checks = document.querySelectorAll('#filterForm .form-check-input');
    const botonLimpiar = document.getElementById('limpiarFiltros');
    const inputTexto = document.getElementById('buscarPalabra');

    const btnAplicar = document.getElementById('btn-apply-filters');

    // Valores iniciales para comparar si el slider se ha movido
    const minOriginal = minInput ? minInput.value : null;
    const maxOriginal = maxInput ? maxInput.value : null;

    /**
     * Valida el estado de los filtros para habilitar o deshabilitar el botón de aplicar.
     * @returns {void}
     */
    const validarFiltros = () => {
        if (!btnAplicar)
            return;

        const hayChecks = Array.from(checks).some(c => c.checked); // checks marcados
        const precioMovido = (minInput.value !== minOriginal) || (maxInput.value !== maxOriginal); //selectores de precio distintos a los originales
        const hayTexto = inputTexto && inputTexto.value.trim().length > 0;

        btnAplicar.disabled = !(hayChecks || precioMovido || hayTexto);
    };

    /**
     * Configura el comportamiento de los deslizadores (sliders) de precio.
     * Asegura que el valor mínimo no supere al máximo y viceversa.
     * @returns {void}
     */
    function setupPriceSlider() {
        if (!minInput || !maxInput || !display)
            return;

        const updateSliders = (e) => {
            let minVal = parseInt(minInput.value);
            let maxVal = parseInt(maxInput.value);

            if (e.target.id === 'priceMin' && minVal > maxVal) {
                minInput.value = maxVal;
                minVal = maxVal;
            } else if (e.target.id === 'priceMax' && maxVal < minVal) {
                maxInput.value = minVal;
                maxVal = minVal;
            }

            display.innerText = `${minVal.toFixed(2)}€ - ${maxVal.toFixed(2)}€`;
            // Validar cada vez que se mueve el slider
            validarFiltros();
        };
        minInput.addEventListener('input', updateSliders);
        maxInput.addEventListener('input', updateSliders);
    }
    ;

    // Inicialización del slider
    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", setupPriceSlider);
    } else {
        setupPriceSlider();
    }

    // Escuchar cambios en los checkboxes
    checks.forEach(check => {
        check.addEventListener('change', validarFiltros);
    });

    if (inputTexto) {
        inputTexto.addEventListener('input', validarFiltros);
    }

    // Gestión del botón de limpiar
    if (botonLimpiar) {
        botonLimpiar.addEventListener('click', () => {
            if (formulario) {
                formulario.reset();
                display.innerText = `${parseFloat(minInput.value).toFixed(2).replace(".", ",")} € - ${parseFloat(maxInput.value).toFixed(2).replace(".", ",")} €`;
                // Al limpiar, el botón debe volver a desactivarse
                validarFiltros();
            }
        });
    }
    // Ejecución inicial para que el botón nazca en el estado correcto
    validarFiltros();
});
