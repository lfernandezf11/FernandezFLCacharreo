//const URL = '/Cacharreo/UsuarioAjax'; // ruta ya declarada en perfilForm, el script ya está en la jsp

// Selectores
const avatarInput = document.getElementById('avatar');
const imgPrevia = document.getElementById('previa');
const avatarError = document.getElementById('avatarError');
const changeAvatarForm = document.getElementById('changeAvatarForm');
const btnDelete = document.getElementById('btn-delete-avatar');
const btnSubmit = document.getElementById('btn-save-avatar'); // Aquí lo tienes

// PREVISUALIZACIÓN Y VALIDACIÓN del formulario ---
if (avatarInput) {
    avatarInput.addEventListener('change', function() {
        const file = this.files[0];
        
        if (file) {
            // Validar que sea imagen
            if (!file.type.startsWith('image/')) {
                marcarErrorAvatar("El archivo seleccionado no es una imagen válida.");
                this.value = ''; // Reseteamos el input
                return;
            }

            // Validar tamaño (100KB)
            if (file.size > 102400) {
                marcarErrorAvatar("La imagen es demasiado grande (Máx. 100KB).");
                this.value = '';
                return;
            }

            // Si todo está bien, leemos el archivo para mostrar la previa
            marcarErrorAvatar(""); // Limpiar errores
            const reader = new FileReader();
            reader.onload = function(e) {
                if (imgPrevia) imgPrevia.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });
}

// MANEJO DE LA SUBIDA(Evento submit del formulario)
if (changeAvatarForm) {
    changeAvatarForm.addEventListener('submit', async (e) => {
        e.preventDefault(); 

        if (!avatarInput.files[0]) {
            marcarErrorAvatar("Por favor, selecciona una foto primero.");
            return;
        }

        // Desactivamos el botón mientras se actualiza la imagen
        btnSubmit.disabled = true;
        btnSubmit.textContent = "Subiendo...";

        const formData = new FormData();
        formData.append('accion', 'actualizarAvatar');
        formData.append('avatar', avatarInput.files[0]);

        try {
            let response = await fetch(URL, { method: 'POST', body: formData });
            let resultado = await response.json();

            if (resultado.success) {
                lanzarToast((resultado.message || "¡Imagen actualizada!"), "exito");
                cerrarModalAvatar();
                setTimeout(() => window.location.reload(), 1200);
            } else {
                lanzarToast(resultado.message, "error");
                btnSubmit.disabled = false;
                btnSubmit.textContent = "Guardar cambios";
            }
        } catch (error) {
            lanzarToast("Error crítico de conexión.", "error");
            btnSubmit.disabled = false;
        }
    });
}

// MANEJO DEL BORRADO (Evento click del botón)
if (btnDelete) {
    btnDelete.addEventListener('click', async () => {
        if (!confirm("¿Estás seguro de que quieres eliminar tu foto?")) return;

        const data = new URLSearchParams();
        data.append('accion', 'eliminarAvatar');

        try {
            let response = await fetch(AVATAR_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: data.toString()
            });

            let resultado = await response.json();
            if (resultado.success) {
                lanzarToast("Foto eliminada.", "exito");
                cerrarModalAvatar();
                setTimeout(() => window.location.reload(), 1200);
            }
        } catch (error) {
            lanzarToast("Error al borrar la foto.", "error");
        }
    });
}

// --- FUNCIONES DE APOYO ---
function marcarErrorAvatar(texto) {
    if (avatarError) avatarError.textContent = texto;
    avatarInput.classList.toggle('is-invalid', texto !== "");
}

function cerrarModalAvatar() {
    const modalEl = document.getElementById('changeAvatarModal');
    const modalInstance = bootstrap.Modal.getInstance(modalEl);
    if (modalInstance) modalInstance.hide();
    document.body.classList.remove('modal-open');
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) backdrop.remove();
}