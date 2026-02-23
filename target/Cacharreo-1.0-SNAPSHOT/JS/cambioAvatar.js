/**
 * Lógica exclusiva para la gestión del Avatar en Mi Perfil
 */
const AVATAR_URL = '/Cacharreo/UsuarioAjax'; // Endpoint centralizado

// Selectores específicos del modal de perfil
const avatarInput = document.getElementById('avatar');
const imgPrevia = document.getElementById('previa');
const avatarError = document.getElementById('avatarError');
const changeAvatarForm = document.getElementById('changeAvatarForm');
const btnDeleteAvatar = document.getElementById('btn-delete-avatar');
const modalAvatarEl = document.getElementById('changeAvatarModal');

// 1. VISTA PREVIA Y VALIDACIÓN EN TIEMPO REAL
if (avatarInput) {
    avatarInput.addEventListener('change', function () {
        const file = this.files[0];

        if (file) {
            // Validación de tipo: debe ser imagen
            if (!file.type.startsWith('image/')) {
                marcarErrorAvatar("El archivo debe ser una imagen válida.");
                this.value = '';
                return;
            }

            // Validación de tamaño: Máximo 100KB
            if (file.size > 102400) {
                marcarErrorAvatar("La imagen es demasiado grande (Máx. 100KB).");
                this.value = '';
                return;
            }

            // Si es válida, generamos la previa
            marcarErrorAvatar("");
            const reader = new FileReader();
            reader.onload = (e) => {
                if (imgPrevia) imgPrevia.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });
}

// 2. ENVIAR NUEVA FOTO (SUBIDA)
if (changeAvatarForm) {
    changeAvatarForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        if (!avatarInput.files[0]) {
            lanzarToast("Por favor, selecciona una imagen.", "warning");
            return;
        }

        const formData = new FormData();
        formData.append('accion', 'actualizarAvatar');
        formData.append('avatar', avatarInput.files[0]); // El archivo binario

        try {
            let response = await fetch(AVATAR_URL, {
                method: 'POST',
                body: formData // El navegador configura el Content-Type automáticamente
            });

            if (response.ok) {
                let resultado = await response.json();
                if (resultado.success) {
                    lanzarToast("Foto de perfil actualizada con éxito.", "exito");
                    cerrarModalAvatar();
                    // Recargamos para refrescar la imagen en todo el sitio
                    setTimeout(() => window.location.reload(), 1200);
                } else {
                    lanzarToast(resultado.message, "error");
                }
            }
        } catch (error) {
            lanzarToast("Error al conectar con el servidor.", "error");
        }
    });
}

// 3. BORRAR FOTO (VOLVER A DEFAULT)
if (btnDeleteAvatar) {
    btnDeleteAvatar.addEventListener('click', async () => {
        if (!confirm("¿Seguro que quieres eliminar tu foto actual?")) return;

        const data = new URLSearchParams();
        data.append('accion', 'eliminarAvatar');

        try {
            let response = await fetch(AVATAR_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: data.toString()
            });

            if (response.ok) {
                let resultado = await response.json();
                if (resultado.success) {
                    lanzarToast("Foto eliminada.", "exito");
                    cerrarModalAvatar();
                    setTimeout(() => window.location.reload(), 1200);
                }
            }
        } catch (error) {
            lanzarToast("Error al procesar la solicitud.", "error");
        }
    });
}

// FUNCIONES DE APOYO
function marcarErrorAvatar(texto) {
    if (avatarError) avatarError.textContent = texto;
    if (avatarInput) {
        if (texto !== "") {
            avatarInput.classList.add('is-invalid');
            avatarInput.classList.remove('is-valid');
        } else {
            avatarInput.classList.remove('is-invalid');
            avatarInput.classList.add('is-valid');
        }
    }
}

function cerrarModalAvatar() {
    const modalInstance = bootstrap.Modal.getInstance(modalAvatarEl);
    if (modalInstance) modalInstance.hide();
    
    // Limpieza manual para evitar el error de "botón bloqueado"
    document.body.classList.remove('modal-open');
    document.body.style.overflow = '';
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) backdrop.remove();
}