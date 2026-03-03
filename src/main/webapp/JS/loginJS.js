// Campos de texto
const emailEl = document.getElementById('emailLogin');
const passEl = document.getElementById('passwordLogin');
const error = document.querySelector('.signInForm .invalid');
error.textContent = "";


// EVENTO SUBMIT DEL FORMULARIO
const form = document.querySelector('.signInForm');

/**
 * Gestiona el envío asíncrono del formulario de inicio de sesión.
 * Realiza validaciones previas en el cliente, envía las credenciales mediante Fetch API
 * y gestiona la respuesta del servidor (redirección o mensajes de error).
 *  
 * @param {SubmitEvent} e - El evento de envío del formulario.
 * @returns {Promise<void>}
 */
form.addEventListener('submit', async (e) => {
    e.preventDefault(); // Evita que la página se recargue

    const email = emailEl.value.trim();
    const password = passEl.value;

    if (email === "" || password === "") { // Validación de campos vacíos
        error.textContent = "Todos los datos son obligatorios";
        return;
    }
    
    // Limpieza de estados visuales previos
    emailEl.classList.remove('is-invalid');
    passEl.classList.remove('is-invalid');
    error.textContent = "";
    
    /** 
     * Preparación de los datos para el envío 
     * Se utiliza URLSearchParams para compatibilidad con el backend Java/JSP (form-urlencoded)
     */
    const data = new URLSearchParams();
    data.append('accion', 'loginUsuario');

    data.append('datosLogin', JSON.stringify({
        'email': email,
        'password': password
    }));

    try {
        let variable = await fetch(URL_USUARIO, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: data.toString()
        });

        if (variable.ok) {
            let resultado = await variable.json();
             if (resultado.success) {
                lanzarToast((resultado.message || "¡HOLA! Nos encanta tenerte de vuelta."), "exito");
                // Esperamos antes de redirigir para que el toast pueda lanzarse
                    setTimeout(() => {
                        window.location.href = "/Cacharreo/JSP/usuario/perfil.jsp"; // Si el login es correcto, redirigimos al perfil para no perder las validaciones 
                    }, 1500);
            } else {
                error.textContent = resultado.message || "Usuario o contraseña incorrectos.";
                emailEl.classList.add('is-invalid');
                passEl.classList.add('is-invalid');
            }
        } else {
            lanzarToast(("Error de servidor: " + variable.statusText), "error");
        }
    } catch (error) {
        lanzarToast("Error de conexión con el servidor.", "error");
    }
});



