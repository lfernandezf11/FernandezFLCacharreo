const URL = '/Cacharreo/UsuarioAjax'; // Endpoint para peticiones ajax

const REGEX_PASSWORD = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/;

const passActual = document.getElementById('currentPass');
const passNueva = document.getElementById('newPass');
const confirmPassNueva = document.getElementById('confirmNewPass');
const btnSave = document.getElementById('btn-save-pass');


// Configuración del estado inicial
let datosOriginales = almacenarDatosEditables();
validarFormularioCompleto();
btnSave.disabled = true;


function showErrorPassword(element, text) {
    const msgSpan = document.getElementById('save-msg');
    if (msgSpan)
        msgSpan.textContent = "";

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
    validarFormularioCompleto();
}



/**
 * Función que comprueba la validez total llamando a tus validaciones
 */
function validarFormularioCompleto() {
    const isNombreOk = REGEX_LETRAS.test(nombreEl.value.trim());
    const isApellidosOk = REGEX_LETRAS.test(apellidosEl.value.trim());
    const isLocalidadOk = REGEX_LETRAS.test(localidadEl.value.trim());
    const isDireccionOk = REGEX_DIRECCION.test(direccionEl.value.trim());
    const isCPOk = REGEX_CP.test(cpEl.value.trim());
    const isProvinciaOk = provinciaEl.value !== "";

    // Teléfono: opcional (vacío ok) o debe cumplir regex
    const tlfValor = telefonoEl.value.trim();
    const isTelefonoOk = tlfValor === "" || REGEX_TLF.test(tlfValor);

    btnSave.disabled = !(isNombreOk && isApellidosOk && isLocalidadOk && isDireccionOk && isCPOk && isProvinciaOk && isTelefonoOk);
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


/* LISTENERS */
// Usamos 'input' para que el botón se active en el milisegundo en que la regex sea válida
nombreEl.addEventListener('input', () => {
    validateTexto(nombreEl); // Muestra error si es necesario
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



// EVENTO SUBMIT DEL FORMULARIO
const editForm = document.querySelector('.editProfileForm');

editForm.addEventListener('submit', async (e) => {
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

