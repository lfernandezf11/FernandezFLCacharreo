const URL = '/Cacharreo/UsuarioAjax'; // Endpoint para peticiones ajax

const REGEX_LETRAS = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
const REGEX_CP = /^\d{5}$/;
const REGEX_TLF = /^\d{9}$/;
const REGEX_DIRECCION = /^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s,\/\-º°ª.]+$/;
const REGEX_PASSWORD = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/;

const nombreEl = document.getElementById('nombrePerfil');
const apellidosEl = document.getElementById('apellidosPerfil');
const localidadEl = document.getElementById('localidadPerfil');
const direccionEl = document.getElementById('direccionPerfil');
const provinciaEl = document.getElementById('provinciaPerfil');
const cpEl = document.getElementById('codigoPostalPerfil');
const telefonoEl = document.getElementById('telefonoPerfil');
const btnSaveProfile = document.getElementById('btn-save-profile');

const passActual = document.getElementById('currentPass');
const passNueva = document.getElementById('newPass');
const confirmPassNueva = document.getElementById('confirmNewPass');
const btnSavePass = document.getElementById('btn-save-pass');

// Configuración del estado inicial
let datosOriginales = almacenarDatosEditables();
validarFormularioPerfil();
btnSaveProfile.disabled = true;

// Función para almacenar los valores de los campos editables (no actualizamos si no hay datos nuevos)
function almacenarDatosEditables() {
    return JSON.stringify({
        'nombre': nombreEl.value.trim(),
        'apellidos': apellidosEl.value.trim(),
        'direccion': direccionEl.value.trim(),
        'localidad': localidadEl.value.trim(),
        'provincia': provinciaEl.value,
        'codigoPostal': cpEl.value.trim(),
        'telefono': telefonoEl.value.trim()
    });
}

function showErrorPerfil(element, text) {

    const small = element.parentNode.querySelector('small.invalid');
    if (small) {
        small.textContent = text;
        if (text !== "") {
            element.classList.add('is-invalid');
            element.classList.remove('is-valid');
        } else {
            element.classList.remove('is-invalid');
            if (element.value.trim() !== "")
                element.classList.add('is-valid');
        }
    }
    // Cada vez que modificamos un mensaje, comprobamos el estado del formulario
    validarFormularioPerfil();
}



function validarFormularioPerfil() {
    const isNombreOk = REGEX_LETRAS.test(nombreEl.value.trim());
    const isApellidosOk = REGEX_LETRAS.test(apellidosEl.value.trim());
    const isLocalidadOk = REGEX_LETRAS.test(localidadEl.value.trim());
    const isDireccionOk = REGEX_DIRECCION.test(direccionEl.value.trim());
    const isCPOk = REGEX_CP.test(cpEl.value.trim());
    const isProvinciaOk = provinciaEl.value !== "";

    // Teléfono: opcional (vacío ok) o debe cumplir regex
    const tlfValor = telefonoEl.value.trim();
    const isTelefonoOk = tlfValor === "" || REGEX_TLF.test(tlfValor);

    btnSaveProfile.disabled = !(isNombreOk && isApellidosOk && isLocalidadOk && isDireccionOk && isCPOk && isProvinciaOk && isTelefonoOk);
}

////////////////// VALIDACIONES
// Validación genérica para campos de texto (Nombre, Apellidos, Localidad)
function validateTexto(element) {
    const valor = element.value.trim();
    if (valor === "") {
        showErrorPerfil(element, `El campo es obligatorio.`);
        return false;
    }
    if (!REGEX_LETRAS.test(valor)) {
        showErrorPerfil(element, `El campo solo admite letras.`);
        return false;
    }
    showErrorPerfil(element, "");
    return true;
}

// Validación para campos numéricos (cp, tlf)
function validateNumber(element, regex, fieldName) {
    const valor = element.value.trim();

    if (fieldName === "Tel&eacute;fono" && valor === "") { // Campo nullable vacío, correcto
        showErrorPerfil(element, "");
        return true;
    }

    if (valor === "") {
        showErrorPerfil(element, `El campo es obligatorio.`);
        return false;
    }

    if (!regex.test(valor)) {
        // Personalizamos el mensaje según el campo
        const msg = fieldName === "C&oacute;digo Postal"
                ? "Debe tener exactamente 5 dígitos."
                : "Debe tener exactamente 9 dígitos.";
        showErrorPerfil(element, msg);
        return false;
    }

    showErrorPerfil(element, "");
    return true;
}

function validateDireccion() {
    const valor = direccionEl.value.trim();
    if (valor === "") {
        showErrorPerfil(direccionEl, "La dirección es obligatoria.");
        return false;
    }
    if (!REGEX_DIRECCION.test(valor)) {
        showErrorPerfil(direccionEl, "La dirección contiene caracteres no permitidos.");
        return false;
    }
    showErrorPerfil(direccionEl, "");
    return true;
}
;

function validateProvincia() {
    const provincia = provinciaEl.value; //Viene de un select, no hace falta trim()
    if (provincia === "") {
        showErrorPerfil(provinciaEl, "Selecciona una provincia de la lista.");
        return false;
    }
    showErrorPerfil(provinciaEl, "");
    return true;
}
;

function validatePassword() {
    const password = passNueva.value;

    if (REGEX_PASSWORD.test(password)) {
        showErrorPerfil(passNueva, "");
        if (confirmPassNueva.value !== "")
            validatePasswordsIguales();
        return true;
    } else {
        showErrorPerfil(passNueva, "Mínimo 8 caracteres, una mayúscula y una minúscula.");
        return false;
    }

    if (confirmPassNueva.value !== "")
        validatePasswordsIguales(); // Si ya había algo en el segundo campo, validamos coincidencia
}
;

function validatePasswordsIguales() {
    const p1 = passNueva.value;
    const p2 = confirmPassNueva.value;

// Si el segundo campo está vacío, no mostramos error aún (mejora la experiencia de usuario)
    if (p2 === "") {
        showErrorPerfil(confirmPassNueva, "");
        return false;
    }

    if (p1 === p2) {
        showErrorPerfil(confirmPassNueva, "");
        return true;

    } else {
        showErrorPerfil(confirmPassNueva, "Las contraseñas no coinciden.");
        return false;
    }
}

/* LISTENERS */
// Usamos 'input' para que el botón se active en el milisegundo en que la regex sea válida
nombreEl.addEventListener('input', () => {
    validateTexto(nombreEl);
});

apellidosEl.addEventListener('input', () => {
    validateTexto(apellidosEl);
});

localidadEl.addEventListener('input', () => {
    validateTexto(localidadEl);
});

direccionEl.addEventListener('input', () => {
    validateDireccion();
});

cpEl.addEventListener('input', () => {
    validateNumber(cpEl, REGEX_CP, "C&oacute;digo Postal");
});

telefonoEl.addEventListener('input', () => {
    validateNumber(telefonoEl, REGEX_TLF, "Tel&eacute;fono");
});

provinciaEl.addEventListener('change', () => {
    validateProvincia();
});

passNueva.addEventListener('input', validatePassword); // En tiempo real, conforme el usuario va escribiendo se comprueba el contenido.
confirmPassNueva.addEventListener('input', validatePasswordsIguales);


// EVENTO SUBMIT DEL FORMULARIO DE PERFIL
const editProfileForm = document.querySelector('.editProfileForm');

editProfileForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const datosActuales = almacenarDatosEditables();

    // COMPROBACIÓN DE CAMBIOS
    if (datosActuales === datosOriginales) {
        lanzarToast("No has modificado nada todavía.", "warning");
        return;
    }

    // El botón ya está habilitado, así que los datos son válidos localmente.
    const data = new URLSearchParams();
    data.append('accion', 'actualizarUsuario');

    data.append('datosFormPerfil', JSON.stringify({
        'nombre': nombreEl.value.trim(),
        'apellidos': apellidosEl.value.trim(),
        'direccion': direccionEl.value.trim(),
        'localidad': localidadEl.value.trim(),
        'provincia': provinciaEl.value,
        'codigoPostal': cpEl.value.trim(),
        'telefono': telefonoEl.value.trim()
    }));

    try {
        let response = await fetch(URL, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: data.toString()
        });

        if (response.ok) {
            let resultado = await response.json();

            if (resultado.success) {
                lanzarToast("¡Perfil actualizado con éxito!", "exito");
                datosOriginales = datosActuales;
                setTimeout(() => window.location.reload(), 2000);
            } else {
                lanzarToast(resultado.message || "Error al guardar los cambios", "error");
            }
        }
    } catch (error) {
        lanzarToast(resultado.message || "Error crítico de conexión", "error");
    }
});

// ESCUCHADOR DEL MODAL DE CONTRASEÑA PARA LIMPIEZA AUTOMÁTICA
// Este evento se dispara cuando el modal ha terminado de ocultarse (animación de bootstrap incluida)
const btnAbrirModal = document.querySelector('[data-bs-target="#changePasswordModal"]');
const modalEl = document.getElementById('changePasswordModal');

// 2. Evento que se dispara cuando el modal termina de cerrarse
modalEl.addEventListener('hidden.bs.modal', function () {
    // Quitamos el foco del botón de cerrar (que hacía que el botón trigger del modal desapareciera) y lo devolvemos al botón original.
    if (document.activeElement instanceof HTMLElement) {
        document.activeElement.blur();
    }

    if (btnAbrirModal) {
        btnAbrirModal.focus();
    }

    // Limpieza de clases de Bootstrap por si acaso hay restos que impidan recargar bien la página
    document.body.classList.remove('modal-open');
    document.body.style.overflow = '';
    document.body.style.paddingRight = '';
    const backdrops = document.querySelectorAll('.modal-backdrop');
    backdrops.forEach(b => b.remove());
});


// EVENTO SUBMIT DEL FORMULARIO DE CONTRASEÑA
const editPassForm = document.querySelector('.changePasswordForm');

editPassForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Validamos campos antes de enviar
    const isPassActualOk = passActual.value.trim() !== "";
    const isPassNuevaOk = validatePassword();
    const isConfirmOk = validatePasswordsIguales();

    if (!isPassActualOk) {
        showErrorPerfil(passActual, "Debes introducir tu contraseña actual.");
    }

    if (isPassActualOk && isPassNuevaOk && isConfirmOk) {
        const data = new URLSearchParams();
        data.append('accion', 'actualizarPassword');
        data.append('datosFormPass', JSON.stringify({
            'password': passActual.value.trim(),
            'nuevaPassword': passNueva.value.trim()
        }));

        try {
            let response = await fetch(URL, {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: data.toString()
            });

            if (response.ok) {
                let resultado = await response.json();

                if (resultado.success) {
                    const modalInstance = bootstrap.Modal.getInstance(modalEl);
                    if (modalInstance) {
                        modalInstance.hide(); // dispara el evento del listener
                    }
                    lanzarToast(resultado.message, "exito");

                } else {
                    lanzarToast(resultado.message || "La contraseña actual es incorrecta", "error");
                    showErrorPerfil(passActual, "Contraseña incorrecta");
                }
            }
        } catch (error) {
            lanzarToast("Error crítico de conexión", "error");
        }
    }
});

