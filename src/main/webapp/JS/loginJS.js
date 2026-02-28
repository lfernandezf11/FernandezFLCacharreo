// Campos de texto
const emailEl = document.getElementById('emailLogin');
const passEl = document.getElementById('passwordLogin');
const error = document.querySelector('.signInForm .invalid');
error.textContent = "";


// EVENTO SUBMIT DEL FORMULARIO
const form = document.querySelector('.signInForm');

form.addEventListener('submit', async (e) => {
    e.preventDefault(); // Evita que la página se recargue

    const email = emailEl.value.trim();
    const password = passEl.value;

    if (email === "" || password === "") { // Validación de campos vacíos
        error.textContent = "Todos los datos son obligatorios";
        return;
    }

    emailEl.classList.remove('is-invalid');
    passEl.classList.remove('is-invalid');
    error.textContent = "";

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
                window.location.href = "/Cacharreo/JSP/usuario/perfil.jsp"; // Si el login es correcto, redirigimos al perfil
            } else {
                error.textContent = "Usuario o contraseña incorrectos.";
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



