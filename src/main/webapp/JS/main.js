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

/**
 * Alterna la visibilidad del campo de contraseña y cambia el icono.
 * @param {HTMLInputElement} inputEl - El elemento de entrada de texto.
 * @param {HTMLElement} iconEl - El elemento del icono (<i>).
 */
function togglePassword(inputEl, iconEl) {
  const show = inputEl.type === 'password'; 
  
  inputEl.type = show ? 'text' : 'password'; 
  
  iconEl.className = show ? 'fa fa-eye-slash' : 'fa fa-eye';
}


/* Listener para los campos passsword*/
document.addEventListener('DOMContentLoaded', () => {
  const passwordContainers = document.querySelectorAll('.password-field');

  passwordContainers.forEach(container => {
    const input = container.querySelector('input');
    const toggleBtn = container.querySelector('.toggle-pass');

    if (input && toggleBtn) {
      const icon = toggleBtn.querySelector('i');

      toggleBtn.addEventListener('click', (e) => {
        e.preventDefault();
        togglePassword(input, icon);
      });
    }
  });
});

/**
 * Muestra u oculta errores en cualquier tipo de campo.
 * @param {HTMLElement} element - El input, select o checkbox a validar.
 * @param {string} text - El mensaje de error (vacío para limpiar).
 */
function showError(element, text) {
    const container = element.closest('.field');
    
    const small = container ? container.querySelector('small.invalid') : null;

    if (small) {
        small.textContent = text;
        
        small.style.display = text !== "" ? "block" : "none";

        if (text !== "") {
            element.classList.add('is-invalid');
            element.classList.remove('is-valid');
        } else {
            element.classList.remove('is-invalid');
            if (element.value.trim() !== "") {
                element.classList.add('is-valid');
            }
        }
    }
}