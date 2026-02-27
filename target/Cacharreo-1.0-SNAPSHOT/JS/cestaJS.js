const subtotalGlobal = document.getElementById('cart-amount');
const totalIva = document.getElementById('iva-amount');
const totalPedido = document.getElementById('total-final');

/**
 * Función para actualizar las unidades de la cesta de forma asíncrona
 * @param {string} idProducto 
 * @param {string} accion (sumar o restar)
 * @param {HTMLElement} form contenedor del producto
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
 * Elimina un producto de la cesta de forma asíncrona enviando una petición al servidor.
 * Si la eliminación es exitosa en el backend, se aplica un efecto visual de 
 * desvanecimiento a la tarjeta del producto y se elimina del DOM.
 * @param {string} idProducto - El identificador único del producto a eliminar.
 * @param {HTMLElement} form - El elemento <form> que contiene el botón de eliminar.
 * @returns {Promise<void>} - Promesa que se resuelve una vez completada la operación y actualización visual.
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
                    // Aplicamos transición CSS dinámica para un borrado elegante
                    itemCard.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
                    itemCard.style.opacity = '0';
                    itemCard.style.transform = 'translateX(20px)';

                    // Esperamos a que la animación termine antes de quitar el elemento del DOM
                    setTimeout(() => {
                        itemCard.remove();

                        if (subtotalGlobal && resultado.subtotalCesta) {
                            subtotalGlobal.textContent = resultado.subtotalCesta + " €";
                        }
                        if (totalIva && resultado.ivaCesta) {
                            totalIva.textContent = resultado.ivaCesta + " €";
                        }
                        if (totalPedido && resultado.totalCesta) {
                            totalPedido.textContent = resultado.totalCesta + " €"; //// Display de nuevo total. Como ya viene formateado con dos decimales desde el controlador, solo añadimos el símbolo
                        }

                        // Si era el último producto, recargamos para mostrar el estado "Cesta vacía"
                        const restantes = document.querySelectorAll('.cart-item-card').length;
                        if (restantes === 0) {
                            location.reload();
                        }
                    }, 500);
                }

                lanzarToast(resultado.message, "exito");
            } else {
                lanzarToast(resultado.message || "No se pudo eliminar el producto", "error");
            }
        }
    } catch (error) {
        lanzarToast("Error crítico de conexión al eliminar", "error");
        console.log(error);
    }
}

// MANEJO DE EVENTOS EN CESTA.JSP
document.addEventListener('click', (e) => {
    // Botones de línea (Sumar, Restar, Eliminar Producto)
    if (e.target.matches('.qty-selector button, button[value="eliminar"')) {
        e.preventDefault();
        const btn = e.target;
        const form = btn.closest('form');
        const idProd = form.querySelector('input[name="idProducto"]').value; // El selector hace referencia al nombre porque el id es distinto para cada elemento.
        const accion = btn.value; // "sumar", "restar" o "eliminar"

        if (accion === "eliminar") {
            eliminarFila(idProd, form);
        } else {
            actualizarUnidades(idProd, accion, form);
        }
    }

    // Botón VACIAR CESTA (Global)
    if (e.target.id === 'btn-delete') {
        vaciarCestaCompleta();
    }

    // Botón TRAMITAR PEDIDO (Global)
    if (e.target.id === 'btn-buy') {
        tramitarPedido();
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

    const btnAplicar = document.getElementById('btn-apply-filters');

    // Valores iniciales para comparar si el slider se ha movido
    const minOriginal = minInput ? minInput.value : null;
    const maxOriginal = maxInput ? maxInput.value : null;
    btnAplicar.disabled = true;

    // Función para activar/desactivar el botón de filtrar según haya filtros seleccionados o no
    const validarFiltros = () => {
        if (!btnAplicar) return;

        const hayChecks = Array.from(checks).some(c => c.checked); // checks marcados
        const precioMovido = (minInput.value !== minOriginal) || (maxInput.value !== maxOriginal); //selectores de precio distintos a los originales

        btnAplicar.disabled = !(hayChecks || precioMovido);
    };

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

            display.innerText = `${minVal}€ - ${maxVal}€`;
            // Validar cada vez que se mueve el slider
            validarFiltros();
        };
        minInput.addEventListener('input', updateSliders);
        maxInput.addEventListener('input', updateSliders);
    };

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

    // Gestión del botón de limpiar
    if (botonLimpiar) {
        botonLimpiar.addEventListener('click', () => {
            if (formulario) {
                formulario.reset();
                display.innerText = `${minInput.value}€ - ${maxInput.value}€`;
                // Al limpiar, el botón debe volver a desactivarse
                validarFiltros();
            }
        });
    }
    // Ejecución inicial para que el botón nazca en el estado correcto
    validarFiltros();
});
