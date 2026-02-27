const URL_USUARIO = '/Cacharreo/UsuarioAjax'; // Endpoint para peticiones ajax 
const URL_CESTA = '/Cacharreo/CestaAjax';

//const REGEX_LETRAS = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
const REGEX_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const REGEX_CP = /^\d{5}$/;
const REGEX_TLF = /^[679]\d{8}$/;
const REGEX_PASSWORD = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
const REGEX_NUMDNI = /^\d{8}$/;
//const REGEX_DIRECCION = /^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s,\/\-º°ª.]+$/;

/**
 * Lanza una notificación Toast de Bootstrap
 * @param {string} mensaje - Texto a mostrar
 * @param {string} tipo - exito para verde, error para rojo, warning para naranja
 */
function lanzarToast(mensaje, tipo) {
    const toastEl = document.getElementById('liveToast');
    const toastBody = document.getElementById('toastBody');

    if (!toastEl || !toastBody)
        return;

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

    const bsToast = bootstrap.Toast.getOrCreateInstance(toastEl, {
        delay: 2000});
    bsToast.show();

    const cerrarAlClicar = (e) => {
        if (!toastEl.contains(e.target)) {
            bsToast.hide();
            document.removeEventListener('click', cerrarAlClicar);
        }
    };

    setTimeout(() => {
        document.addEventListener('click', cerrarAlClicar);
    }, 100);

    toastEl.addEventListener('hidden.bs.toast', () => {
        document.removeEventListener('click', cerrarAlClicar);
    }, {once: true});
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

/**
 * Resetea completamente un formulario: valores, clases de validación y mensajes de error.
 * @param {HTMLFormElement} formEl - El elemento del formulario a resetear.
 */
function resetForm(formEl) {
    if (!formEl)
        return;

    formEl.reset();

    const inputs = formEl.querySelectorAll('.is-invalid, .is-valid');
    inputs.forEach(input => {
        input.classList.remove('is-invalid', 'is-valid');
    });

    const errorMessages = formEl.querySelectorAll('small.invalid');
    errorMessages.forEach(small => {
        small.textContent = "";
        small.style.display = "none";
    });
}

///////////////// VALIDACIONES
// Validación genérica para campos de texto (Nombre, Apellidos, Localidad)
function validateTexto(element) {
    if (element.value.trim() === "") {
        showError(element, `El campo es obligatorio.`);
        return false;
    }
    showError(element, "");
    return true;
}

// Validación para campos numéricos (cp, tlf)
function validateNumber(element, regex, fieldName) {
    const valor = element.value.trim();

    if (fieldName === "Tel&eacute;fono" && valor === "") { // Campo nullable vacío, correcto
        showError(element, "");
        return true;
    }

    if (valor === "") {
        showError(element, `El campo es obligatorio.`);
        return false;
    }

    if (!regex.test(valor)) {
        const msg = fieldName === "C&oacute;digo Postal"
                ? "Debe tener exactamente 5 dígitos."
                : "Debe empezar por 6, 7 o 9 y tener 9 dígitos.";
        showError(element, msg);
        return false;
    }

    // Validación Lógica Extra para CP (Máximo España: 52080)
    if (fieldName === "C&oacute;digo Postal") {
        const cpNumerico = parseInt(valor, 10);
        if (cpNumerico > 52080) {
            showError(element, "El código postal no es válido en España.");
            return false;
        }
    }

    showError(element, "");
    return true;
}

/*function validateDireccion() {
 const valor = direccionEl.value.trim();
 if (valor === "") {
 showError(direccionEl, "La dirección es obligatoria.");
 return false;
 }
 if (!REGEX_DIRECCION.test(valor)) {
 showError(direccionEl, "La dirección contiene caracteres no permitidos.");
 return false;
 }
 showError(direccionEl, "");
 return true;
 }
 ;*/

function validateProvincia() {
    const provincia = provinciaEl.value; //Viene de un select, no hace falta trim()
    if (provincia === "") {
        showError(provinciaEl, "Selecciona una provincia de la lista.");
        return false;
    }
    showError(provinciaEl, "");
    return true;
}
;


/// LISTENERS DE CIERRE DE MODALES
// Para evitar la colisión de las animaciones de cierre de modal de bootstrap y las acciones programadas 
// con las respuestas de ajax, es necesario controlar manualmente la desaparición de estilos residuales
// y el elemento enfocado.

// 1. Cuando el modal empieza a cerrarse
document.addEventListener('hide.bs.modal', function (event) {
    const modalEl = event.target; // El modal que se está cerrando

    // Para detectar el botón que abrió este modal específico, recuperamos la instancia de bootstrap
    const modalInstance = bootstrap.Modal.getInstance(modalEl);
    const btnAbrir = modalInstance._element.querySelector('[data-bs-toggle="modal"]')
            || document.querySelector(`[data-bs-target="#${modalEl.id}"]`);


    modalEl.setAttribute('inert', '');  // Fuerza el bloqueo de accesibilidad (Inert)
    // Manejo del foco
    if (document.activeElement)
        document.activeElement.blur();

    if (btnAbrir)
        setTimeout(() => btnAbrir.focus(), 0);
});


// 2. Cuando el modal se ha cerrado
document.addEventListener('hidden.bs.modal', function (event) {
    const modalEl = event.target;

    modalEl.removeAttribute('inert');  // Restaurar interactividad para la próxima apertura

    // Limpieza de seguridad del body (evita el cuelgue visual)
    setTimeout(() => {
        const modalesAbiertos = document.querySelectorAll('.modal.show').length;
        if (modalesAbiertos === 0) {
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            document.body.style.paddingRight = '';
            document.querySelectorAll('.modal-backdrop').forEach(b => b.remove());
        }
    }, 100);

    // Reset de formularios contenidos en el modal
    const form = modalEl.querySelector('form');
    if (form)
        resetForm(form);
});


