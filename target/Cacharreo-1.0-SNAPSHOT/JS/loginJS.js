const URL = 'UsuarioAjax'; // Endpoint para peticiones ajax de login

const REGEX_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// Campos de texto
const emailEl = document.getElementById('emailLogin');
const passEl = document.getElementById('passwordLogin');
const error = document.querySelector('.signInForm .invalid');

function showErrorLogin(text) {
    if (error) {
        error.textContent = text;
        error.style.display = text !== "" ? "block" : "none";
    }
}

// EVENTO SUBMIT DEL FORMULARIO
const form = document.querySelector('.signInForm');

form.addEventListener('submit', async (e) => {
    e.preventDefault(); // Evita que la página se recargue

    const email = emailEl.value.trim();
    const password = passEl.value;

    if (email === "" || password === "") { // Validación de campos vacíos
        showErrorLogin("Todos los datos son necesarios");
        return;
    }

    if (!REGEX_EMAIL.test(email)) { // Validación de formato
        showErrorLogin("El formato del email no es válido.");
        return;
    }
    showErrorLogin(""); // Campos llenos y con formato correcto: limpiamos errores previos

    const data = new URLSearchParams();
    data.append('accion', 'loginUsuario');

    data.append('datosLogin', JSON.stringify({
        'email': email,
        'password': password
    }));

    try {
        let variable = await fetch(URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: data.toString()
        });

        if (variable.ok) {
            let resultado = await variable.json();

            if (resultado.success) {
                window.location.href = "/Cacharreo/JSP/usuario/perfil.jsp"; // Si el login es correcto, redirigimos al perfil
            } else {
                showErrorLogin("Usuario o contraseña incorrectos.");
            }
        } else {
            lanzarToast(("Error de servidor: " + variable.statusText), "error");
        }
    } catch (error) {
        lanzarToast("Error de conexión con el servidor.", "error");
    }
});

