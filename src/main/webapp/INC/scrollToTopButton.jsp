<a href="#" class="btn shadow-sm" id="btn-back-to-top">
    <i class="bi bi-chevron-double-up custom-bold-icon"></i>
</a>

<style>
    a#btn-back-to-top {
        /* Estado inicial: Invisible */
        opacity: 0;
        visibility: hidden;

        position: fixed;
        bottom: 20px;
        right: 30px;
        z-index: 1000;
        width: 50px;
        height: 50px;
        align-items: center;
        justify-content: center;
        display: flex;

        background-color: white;
        border: 3px solid #62457B;
        border-radius: 12px;
        transition: all 0.3s ease-in-out;
        padding: 0;
        text-decoration: none;
    }

    a#btn-back-to-top.show {
        opacity: 1;
        visibility: visible;
    }

    .custom-bold-icon {
        font-size: 1.5rem;
        color: #62457B;
        -webkit-text-stroke: 1.2px #62457B;
        transition: all 0.3s ease;
    }

    /* Selector corregido para el hover */
    a#btn-back-to-top:hover {
        transform: translateY(-5px);
        background-color: #62457B;
        border-color: #62457B;
    }

    a#btn-back-to-top:hover .custom-bold-icon {
        color: white;
        -webkit-text-stroke: 1.2px white;
    }
</style>


<script>
    document.addEventListener("DOMContentLoaded", () => {
        const backToTopBtn = document.getElementById("btn-back-to-top");

        if (backToTopBtn) {
            document.addEventListener("scroll", (event) => {
                const top = event.target.scrollTop || window.pageYOffset;
                if (top > 300) {
                    backToTopBtn.classList.add("show");
                } else {
                    backToTopBtn.classList.remove("show");
                }
                // Este true le dice al evento de scroll (cuando tiene lugar en un contenedor, 
                // no en la propia ventana) que burbujee hacia los elementos padre para ser capturado.
            }, true);

            // Listener de clic (Subida suave)
            backToTopBtn.addEventListener("click", (e) => {
                e.preventDefault(); // Evita que la URL cambie a #

                // Detecta el elemento que tiene el scroll
                const scrollTarget = document.documentElement.scrollTop > 0 ? document.documentElement :
                        (document.body.scrollTop > 0 ? document.body : window);

                scrollTarget.scrollTo({
                    top: 0,
                    behavior: "smooth"
                });
            });
        }
    });
</script>