const URL = 'UsuarioAjax'; // Endpoint para peticiones ajax

const REGEX_LETRAS = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
const REGEX_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const REGEX_CP = /^\d{5}$/;
const REGEX_TLF = /^\d{9}$/;
const REGEX_PASSWORD = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
const REGEX_NUMDNI = /^\d{8}$/;
const REGEX_DIRECCION = /^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s,\/\-º°ª.]+$/;

let imagenInput; // Variable para almacenar la imagen previa del avatar



const nombreEl = document.getElementById('nombre'); // Campos de texto
const apellidosEl = document.getElementById('apellidos');
const localidadEl = document.getElementById('localidad');

const direccionEl = document.getElementById('direccion'); // Dirección
const provinciaEl = document.getElementById('provincia'); // Provincia

const cpEl = document.getElementById('codigoPostal'); // Campos numéricos
const telefonoEl = document.getElementById('telefono');

const pass1El = document.getElementById('password1'); // Contraseñas
const pass2El = document.getElementById('password2');

const emailEl = document.getElementById('email'); // Campos de comprobación asíncrona
const nifEl = document.getElementById('nif');

const checkEl = document.getElementById('gridCheck'); // Términos y condiciones

const avatarEl = document.getElementById('avatar'); // Campos de imagen
const previaEl = document.getElementById('previa');


////////////////// VALIDACIONES
// Validación genérica para campos de texto (Nombre, Apellidos, Localidad)
function validateTexto(element) {
const valor = element.value.trim();
if (valor === "") {
    showError(element, `El campo es obligatorio.`);
    return false;
}
if (!REGEX_LETRAS.test(valor)) {
    showError(element, `El campo solo admite letras.`);
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
    // Personalizamos el mensaje según el campo
    const msg = fieldName === "C&oacute;digo Postal"
            ? "Debe tener exactamente 5 dígitos."
            : "Debe tener exactamente 9 dígitos.";

    showError(element, msg);
    return false;
}

showError(element, "");
return true;
}

function validateDireccion() {
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
;

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

function validatePassword() {
const password = pass1El.value;

if (REGEX_PASSWORD.test(password)) {
    showError(pass1El, "");
    if (pass2El.value !== "")
        validatePasswordsIguales();
    return true;
} else {
    showError(pass1El, "Mínimo 8 caracteres, una mayúscula y una minúscula.");
    return false;
}

if (pass2El.value !== "")
    validatePasswordsIguales(); // Si ya había algo en el segundo campo, validamos coincidencia
}
;

function validatePasswordsIguales() {
const p1 = pass1El.value;
const p2 = pass2El.value;

// Si el segundo campo está vacío, no mostramos error aún (mejora la experiencia de usuario)
if (p2 === "") {
    showError(pass2El, "");
    return false;
}

if (p1 === p2) {
    showError(pass2El, "");
    return true;

} else {
    showError(pass2El, "Las contraseñas no coinciden.");
    return false;
}
}

// Gestiona la vista previa de la imagen de avatar
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader(); // Objeto JS lector de archivos del host
        reader.onload = function (e) {
            imagenInput = e.target.result; 
            if (previaEl) previaEl.src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]); // Convierte la foto en una cadena de texto Base64
    }
}

///////////////////////// VALIDACIONES ASÍNCRONAS: email y nif

/* EMAIL: formato correcto, y en caso de serlo, comprobación de duplicidad en bd.*/
async function validateEmail() {
const email = emailEl.value.trim();

// Validación de formato (síncrona)
if (email === "") {
    showError(emailEl, "El email es obligatorio.");
    return false;
}
if (!REGEX_EMAIL.test(email)) {
    showError(emailEl, "El formato del email no es válido.");
    return false;
}

/* Validación de disponibilidad (asíncrona) */
/* Convertimos el objeto a formato URLSearchParams (clave=valor&clave2=valor2),
 * de modo que para recuperar el dato en el Servlet podemos hacerlo con request.getParameter("accion").
 */
const data = new URLSearchParams();
data.append('accion', 'validateEmail');
data.append('email', email);

try {
    let response = await fetch(URL, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}, // Indicamos al Servlet que enviamos datos de formulario
        body: data.toString()
    });

    if (response.ok) {
        let resultado = await response.json();
        if (resultado.disponible) {
            showError(emailEl, "");
        } else {
            showError(emailEl, "Este correo ya está registrado.");
        }
    } else {
        showError(emailEl, "Error al verificar el correo.");
    }
} catch (error) {
    lanzarToast("Error de conexión con el servidor.", "error");
}
}
;

async function validateNif() {
let valorActual = nifEl.value.trim();

if (valorActual === "") { // Campo vacío
    showError(nifEl, "El NIF es obligatorio.");
    return;
}
/* 
 * Si el usuario hace cambios después de la asignación de la letra (por ejemplo, borrar números o añadir más),
 * podría enviar el formulario con el NIF incorrecto. 
 * Para evitarlo, implementamos una validación robusta del campo, extrayendo sólo los números y comprobando su 
 * longitud en cada 'blur'.
 */
const soloNumeros = valorActual.replace(/\D/g, "");

if (soloNumeros.length === 8) {
    const data = new URLSearchParams();
    data.append('accion', 'asignarLetraNIF');
    data.append('nif', soloNumeros);

    try {
        let response = await fetch(URL, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: data.toString()
        });
        if (response.ok) {
            let resultado = await response.json();

            if (resultado.letra) {
                nifEl.value = soloNumeros + resultado.letra;
                showError(nifEl, "");
                nifEl.classList.add('is-valid');
            } else if (resultado.error) {
                showError(nifEl, resultado.error);
            }
        }
    } catch (error) {
        showError(nifEl, "Error al verificar NIF.");
    }
} else {
    showError(nifEl, "El NIF debe tener exactamente 8 números.");

}
}
;



/* LISTENERS */
// Para que no se ejecuten en cuanto carga la página, las funciones con parámetros se envuelven en una función anónima
nombreEl.addEventListener('blur', () => validateTexto(nombreEl));
apellidosEl.addEventListener('blur', () => validateTexto(apellidosEl));
localidadEl.addEventListener('blur', () => validateTexto(localidadEl));
direccionEl.addEventListener('blur', validateDireccion);
cpEl.addEventListener('blur', () => validateNumber(cpEl, REGEX_CP, "C&oacute;digo Postal"));
telefonoEl.addEventListener('blur', () => validateNumber(telefonoEl, REGEX_TLF, "Tel&eacute;fono"));
provinciaEl.addEventListener('blur', validateProvincia);
pass1El.addEventListener('input', validatePassword); // En tiempo real, conforme el usuario va escribiendo se comprueba el contenido.
pass2El.addEventListener('blur', validatePasswordsIguales);
emailEl.addEventListener('blur', validateEmail);
nifEl.addEventListener('blur', validateNif);

avatarEl.addEventListener('change', function() {
    const file = this.files[0];
    if (file) {
        // Validación de formato
        if (!file.type.startsWith('image/')) {
            showError(this, "El archivo seleccionado no es una imagen válida.");
            this.value = ''; 
            if (previaEl) previaEl.src = "/Cacharreo/IMG/avatares/default.png"; 
            return;
        }

        // Validación de tamaño(100KB)
        if (file.size > 102400) { 
            
            showError(this, "La imagen es demasiado grande (Máx. 100KB).");
            this.value = '';
            if (previaEl) previaEl.src = "/Cacharreo/IMG/avatares/default.png";
        } else {
            showError(this, "");
            readURL(this); // Mostramos la previa
        }
    }
});

// EVENTO SUBMIT DEL FORMULARIO
const form = document.querySelector('.signUpForm');

form.addEventListener('submit', async (e) => {
e.preventDefault(); // Evita que la página se recargue

// Validamos el checkbox antes de reevaluar los campos
const isTermsOk = checkEl.checked;
if (!isTermsOk) {
    document.getElementById('checkError').innerHTML = "Debes aceptar los términos.";
    return;
} else {
    document.getElementById('checkError').innerHTML = "";
}

// Validamos los campos síncronos
const validaciones = [
    validateTexto(nombreEl),
    validateTexto(apellidosEl),
    validateTexto(localidadEl),
    validateDireccion(),
    validateProvincia(),
    validateNumber(cpEl, REGEX_CP, "C&oacute;digo Postal"),
    validateNumber(telefonoEl, REGEX_TLF, "Tel&eacute;fono"),
    validatePassword(),
    validatePasswordsIguales(),
    isTermsOk
];

// Validamos los campos asíncronos. Para evitar hacer peticiones de más, comprobamos la validez por clases css.
// Si el email o el nif tienen la clase 'is-invalid', la validación falla.
const asyncOk = !emailEl.classList.contains('is-invalid') &&
        !nifEl.classList.contains('is-invalid') &&
        emailEl.value !== "" && nifEl.value !== "";


if (validaciones.every(valido => valido === true) && asyncOk) { // Validación exitosa
    
    // Necesario formdata en lugar de URLParams para poder transferir la imagen al controller (formData maneja archivos binarios)
    const formData = new FormData(); 
    formData.append('accion', 'registrarUsuario');
    
    // Añadimos el archivo físico (clave para el @MultipartConfig del Servlet)
        if (avatarEl.files[0]) {
            formData.append('avatar', avatarEl.files[0]);
        }

    // Construimos el objeto con todos los campos y lo añadimos como datosRegistro
    formData.append('datosRegistro', JSON.stringify({
        'nombre': nombreEl.value.trim(),
        'apellidos': apellidosEl.value.trim(),
        'email': emailEl.value.trim(),
        'direccion': direccionEl.value.trim(),
        'localidad': localidadEl.value.trim(),
        'provincia': provinciaEl.value,
        'codigoPostal': cpEl.value.trim(),
        'nif': nifEl.value.trim(),
        'password': pass1El.value,
        // Manejo condicional del teléfono
        ...(telefonoEl.value.trim() !== "" && {'telefono': telefonoEl.value.trim()})
        }));

        try {
            // Con FormData, no es necesario poner headers de 'Content-Type'. El navegador lo configura solo.
            let variable = await fetch(URL, {
                method: 'POST',
                body: formData 
            });

            if (variable.ok) {
                let resultado = await variable.json();

                if (resultado.success) {
                    lanzarToast((resultado.message ||"¡Bienvenido a Cacharreo! Registro completado."), "exito");
                    window.location.href = "/Cacharreo/JSP/usuario/perfil.jsp"; // Redirige el JS para no perder las validaciones asíncronas, una vez ha dado el ok el controller.
                } else {
                    lanzarToast(("Error: " + (resultado.message || "No se pudo registrar.")), "error");
                }
            } else {
                lanzarToast(("Error de servidor: " + variable.statusText), "error");
            }
        } catch (error) {
            lanzarToast("Error crítico de conexión", "error");
        }

    } else { // Validación errónea
        lanzarToast("Por favor, corrige los errores antes de continuar.", "warning");
    }
});