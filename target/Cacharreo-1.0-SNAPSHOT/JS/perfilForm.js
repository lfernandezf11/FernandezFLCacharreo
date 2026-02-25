const nombreEl = document.getElementById('nombrePerfil');
const apellidosEl = document.getElementById('apellidosPerfil');
const localidadEl = document.getElementById('localidadPerfil');
const direccionEl = document.getElementById('direccionPerfil');
const provinciaEl = document.getElementById('provinciaPerfil');
const cpEl = document.getElementById('codigoPostalPerfil');
const telefonoEl = document.getElementById('telefonoPerfil');
const btnSaveProfile = document.getElementById('btn-save-profile');
const editProfileForm = document.querySelector('.editProfileForm');

const passActual = document.getElementById('currentPass');
const passNueva = document.getElementById('newPass');
const confirmPassNueva = document.getElementById('confirmNewPass');
const btnSavePass = document.getElementById('btn-save-pass');
const editPassForm = document.querySelector('.changePasswordForm');

// Configuración del estado inicial
let datosOriginales = almacenarDatosEditables();
validarFormularioPerfil();
btnSaveProfile.disabled = true;
btnSavePass.disabled = true;

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

/*function validarFormularioPerfil() {
    const isNombreOk = REGEX_LETRAS.test(nombreEl.value.trim());
    const isApellidosOk = REGEX_LETRAS.test(apellidosEl.value.trim());
    const isLocalidadOk = REGEX_LETRAS.test(localidadEl.value.trim());
    const isDireccionOk = REGEX_DIRECCION.test(direccionEl.value.trim());
    const isCPOk = REGEX_CP.test(cpEl.value.trim()) && parseInt(cpEl.value.trim(), 10) <= 52080;
    const isProvinciaOk = provinciaEl.value !== "";

    // Teléfono: opcional (vacío ok) o debe cumplir regex
    const tlfValor = telefonoEl.value.trim();
    const isTelefonoOk = tlfValor === "" || REGEX_TLF.test(tlfValor);

    btnSaveProfile.disabled = !(isNombreOk && isApellidosOk && isLocalidadOk && isDireccionOk && isCPOk && isProvinciaOk && isTelefonoOk);
}*/

function validarFormularioPerfil() {
    const tieneErroresVisuales = editProfileForm.querySelectorAll('.is-invalid').length > 0;

    const camposVacios = [nombreEl, apellidosEl, localidadEl, direccionEl, cpEl, provinciaEl]
                         .some(el => el.value.trim() === "");

    btnSaveProfile.disabled = tieneErroresVisuales || camposVacios;
}

function validarFormularioPassword() {
    const isPassActualOk = passActual.value.trim() !== "";
    const isPassNuevaOk = REGEX_PASSWORD.test(passNueva.value);
    const isConfirmOk = passNueva.value === confirmPassNueva.value && confirmPassNueva.value !== "";

    btnSavePass.disabled = !(isPassActualOk && isPassNuevaOk && isConfirmOk);
}



function validatePassword() {
    const password = passNueva.value;

    if (REGEX_PASSWORD.test(password)) {
        showError(passNueva, "");
        if (confirmPassNueva.value !== "")
            validatePasswordsIguales();
        return true;
    } else {
        showError(passNueva, "Mínimo 8 caracteres, una mayúscula y una minúscula.");
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
        showError(confirmPassNueva, "");
        return false;
    }
    
    if (!REGEX_PASSWORD.test(p1) && p2 !== "" && p1 === p2) {
        showError(confirmPassNueva, "Mínimo 8 caracteres, una mayúscula y una minúscula.");
        return false;
    }
    
    if (p1 === p2) {
        showError(confirmPassNueva, "");
        return true;

    } else {
        showError(confirmPassNueva, "Las contraseñas no coinciden.");
        return false;
    }
}

/* LISTENERS */
// Usamos 'input' para que el botón se active en el milisegundo en que la regex sea válida
nombreEl.addEventListener('input', () => {
    validateTexto(nombreEl);
    validarFormularioPerfil();
});

apellidosEl.addEventListener('input', () => {
    validateTexto(apellidosEl);
    validarFormularioPerfil();
});

localidadEl.addEventListener('input', () => {
    validateTexto(localidadEl);
    validarFormularioPerfil();
});

direccionEl.addEventListener('input', () => {
    validateTexto(direccionEl);
    validarFormularioPerfil();
});

cpEl.addEventListener('input', () => {
    validateNumber(cpEl, REGEX_CP, "C&oacute;digo Postal");
    validarFormularioPerfil();
});

telefonoEl.addEventListener('input', () => {
    validateNumber(telefonoEl, REGEX_TLF, "Tel&eacute;fono");
    validarFormularioPerfil();
});

provinciaEl.addEventListener('change', () => {
    validateProvincia();
    validarFormularioPerfil();
});

passActual.addEventListener('input', () => {
    if (passActual.value.trim() !== "") {
        showError(passActual, "");
    }
    validarFormularioPassword();
});

passNueva.addEventListener('input', () => {
    validatePassword();
    validarFormularioPassword();
});

confirmPassNueva.addEventListener('input', () => {
    validatePasswordsIguales(); 
    validarFormularioPassword();
});


// EVENTO SUBMIT DEL FORMULARIO DE PERFIL
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
        let response = await fetch(URL_USUARIO, {
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
                if (btnSaveProfile) btnSaveProfile.disabled = false;
            }
        } else {
             lanzarToast("Error en la respuesta del servidor", "error");
             if (btnSaveProfile) btnSaveProfile.disabled = false;
        }
    } catch (error) {
        lanzarToast("Error crítico de conexión", "error");
        if (btnSaveProfile) btnSaveProfile.disabled = false;
    }
});


// EVENTO SUBMIT DEL FORMULARIO DE CONTRASEÑA
editPassForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    if(btnSavePass) btnSavePass.disabled = true; //bloqueo preventivo

    const data = new URLSearchParams();
    data.append('accion', 'actualizarPassword');
    data.append('datosFormPass', JSON.stringify({
        'password': passActual.value.trim(),
        'nuevaPassword': passNueva.value.trim()
    }));

    try {
        let response = await fetch(URL_USUARIO, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: data.toString()
        });

        if (response.ok) {
            let resultado = await response.json();
            if (resultado.success) {
                //Localizamos el modal padre de este formulario y damos orden de cerrar. Esto dispara el listener global de hidden.bs.modal
                const modalEl = editPassForm.closest('.modal');
                const modalInstance = bootstrap.Modal.getInstance(modalEl);
                
                if (modalInstance) 
                    modalInstance.hide();
                
                lanzarToast(resultado.message, "exito");
            } else {
                lanzarToast(resultado.message || "Error", "error");
                // No cerramos el modal si hay error, solo reseteamos campos
                resetForm(editPassForm);
                if (btnSavePass) btnSavePass.disabled = true;
            }
        }
    } catch (error) {
        lanzarToast("Error crítico de conexión", "error");
        if (btnSavePass) btnSavePass.disabled = false; // Rehabilitar para reintento
    }
});

