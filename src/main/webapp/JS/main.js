/**
 * Lanza una notificación Toast de Bootstrap
 * @param {string} mensaje - Texto a mostrar
 * @param {string} tipo - exito para verde, error para rojo, warning para naranja
 */
function lanzarToast(mensaje, tipo) {
    const toastEl = document.getElementById('liveToast');
    const toastBody = document.getElementById('toastBody');
    
    if (!toastEl || !toastBody) return;

    toastBody.textContent = mensaje;

    // Limpiamos todas las clases posibles antes de añadir la nueva
    toastEl.classList.remove('toast-success', 'toast-error', 'toast-warning');

    // Gestionamos clases de CSS según el tipo
    if (tipo === "exito") {
        toastEl.classList.add('toast-success');
    } else if (tipo === "error") {
        toastEl.classList.add('toast-error');
    } else if (tipo === "warning") {
        toastEl.classList.add('toast-warning');
    }

    const bsToast = bootstrap.Toast.getOrCreateInstance(toastEl);
    bsToast.show();
}
