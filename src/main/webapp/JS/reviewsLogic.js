(function () {
    /**
     * Inicializa los carruseles de testimonios encontrados en el DOM.
     * Soporta múltiples instancias en una misma página.
     */
    const init = () => {
        const sections = document.querySelectorAll('.testimonial-carousel-section');

        sections.forEach(section => {
            const dots = section.querySelectorAll('.carousel-dot');
            const images = section.querySelectorAll('.carousel-image');
            const contents = section.querySelectorAll('.carousel-item-content');
            let currentIndex = 0;
            let interval;
            const autoPlayDelay = 5000;

            /**
             * Actualiza el estado visual del carrusel (puntos, imágenes y textos).
             * @param {number} index - El índice del slide al que se desea cambiar.
             */
            const updateCarousel = (index) => {
                
                dots.forEach((dot, i) => {
                    if (i === index) {
                        dot.classList.remove('btn-light', 'bg-secondary-subtle');
                        dot.classList.add('btn-primary');
                    } else {
                        dot.classList.remove('btn-primary');
                        dot.classList.add('btn-light', 'bg-secondary-subtle');
                    }
                });

                /**
                 * Cambia la visibilidad de un grupo de elementos (imágenes o contenidos).
                 * Gestiona las clases de Bootstrap para transiciones de opacidad y visualización.
                 * @param {NodeListOf<HTMLElement>} elements - Colección de elementos a procesar.
                 * @param {number} targetIndex - Índice del elemento que debe mostrarse.
                 */
                const switchActive = (elements, targetIndex) => {
                    elements.forEach(el => {
                        const elIndex = parseInt(el.getAttribute('data-slide-index'));
                        if (elIndex === targetIndex) {
                            el.classList.remove('d-none');
                            
                            setTimeout(() => {
                                el.classList.add('show');
                            }, 10);
                        } else {
                            el.classList.remove('show');
                            
                            setTimeout(() => {
                                if (!el.classList.contains('show')) {
                                    el.classList.add('d-none');
                                }
                            }, 150);
                        }
                    });
                };

                switchActive(images, index);
                switchActive(contents, index);

                currentIndex = index;
            };
            
            /**
             * Calcula el siguiente índice de forma circular y actualiza el carrusel.
             */
            const nextSlide = () => {
                const nextIndex = (currentIndex + 1) % dots.length;
                updateCarousel(nextIndex);
            };
            
            /**
             * Inicia el temporizador de reproducción automática.
             */
            const startAutoPlay = () => {
                interval = setInterval(nextSlide, autoPlayDelay);
            };
            
            /**
             * Detiene el temporizador de reproducción automática.
             */
            const stopAutoPlay = () => {
                clearInterval(interval);
            };

            
            dots.forEach(dot => {
                dot.addEventListener('click', (e) => {
                    const targetIndex = parseInt(e.target.getAttribute('data-target'));
                    stopAutoPlay();
                    updateCarousel(targetIndex);
                    startAutoPlay();
                });
            });
            startAutoPlay();

            // Pausa on hover
            section.addEventListener('mouseenter', stopAutoPlay);
            section.addEventListener('mouseleave', startAutoPlay);
        });
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", init);
    } else {
        init();
    }
})();