const URL = '/Cacharreo/CestaAjax';

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
        let response = await fetch(URL, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: data.toString()
        });
        if (response.ok) {
            let resultado = await response.json();
            if (resultado.success) {
                form.querySelector('.qty-val').textContent = resultado.nuevaCantidad; //Display de nueva cantidad

                const totalPedidoEl = document.querySelector('.cart-total-bar h3');
                if (totalPedidoEl) {
                    // Display de nuevo total. Como ya viene formateado con dos decimales desde el controlador, solo añadimos el símbolo
                    totalPedidoEl.innerHTML = `Total pedido: ${resultado.totalCesta} €`;
                }

                const itemCard = form.closest('.cart-item-card'); //Display de nuevo subtotal
                const subtotalEl = itemCard.querySelector('.price-group p.mb-0 strong');
                if (subtotalEl) {
                    subtotalEl.textContent = resultado.subtotalProd + " €";
                }
            }
        }
    } catch (error) {
        lanzarToast(("Error de conexión al actualizar unidades:" + error), "error");
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
        let response = await fetch(URL, {
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

                        // Actualizamos el total general del pedido 
                        const totalPedidoEl = document.querySelector('.cart-total-bar h3');
                        if (totalPedidoEl && resultado.totalCesta) {
                            totalPedidoEl.innerHTML = `Total pedido: ${resultado.totalCesta} €`;
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
        const btnSubmit = form.querySelector('#btn-add'); // Captura respecto al modal actual (en document.getElementById, devuelve el primer id, y todos los modales tienen el mismo).
        const idProducto = form.querySelector('input[name="idProducto"]').value;
        const data = new URLSearchParams();
        data.append('accion', 'addCarrito');
        data.append('idProducto', idProducto);
        btnSubmit.disabled = true; // Evita el pulsado de añadir mientras la petición se lleva a cabo.

        try {
            const response = await fetch(URL, {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: data.toString()
            });
            if (response.ok) {
                const resultado = await response.json();
                if (resultado.success) {
                    const modalEl = form.closest('.modal');

                    // getOrCreateInstance es mucho más fiable que getInstance
                    const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEl);

                    if (modalInstance) {
                        modalInstance.hide();
                    }

                    // SEGURO DE VIDA: Si después de 350ms (lo que dura la animación) 
                    // el backdrop sigue ahí, lo fulminamos sin bloquear el scroll.
                    setTimeout(() => {
                        const backdrop = document.querySelector('.modal-backdrop');
                        if (backdrop) {
                            console.warn("Forzando limpieza de backdrop 'zombie'...");
                            backdrop.remove();
                            document.body.classList.remove('modal-open');
                            document.body.style.overflow = '';
                            document.body.style.paddingRight = '';
                        }
                    }, 400);

                    lanzarToast(resultado.message, "exito"); // "Producto talycual añadido al carrito"
                }

            } else {
                lanzarToast(("Error: " + (resultado.message || "No se pudo registrar.")), "error");
            }

        } catch (error) {
            lanzarToast("Error crítico de conexión", "error");
        } finally {
            btnSubmit.disabled = false;
        }
    }
});