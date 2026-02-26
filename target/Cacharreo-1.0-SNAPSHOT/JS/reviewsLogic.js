(function() {
    const init = () => {
        const sections = document.querySelectorAll('.testimonial-carousel-section');

        sections.forEach(section => {
            const dots = section.querySelectorAll('.carousel-dot');
            const images = section.querySelectorAll('.carousel-image');
            const contents = section.querySelectorAll('.carousel-item-content');
            let currentIndex = 0;
            let interval;
            const autoPlayDelay = 5000;

            const updateCarousel = (index) => {
                // Update dots
                dots.forEach((dot, i) => {
                    if (i === index) {
                        dot.classList.remove('btn-light', 'bg-secondary-subtle');
                        dot.classList.add('btn-primary');
                    } else {
                        dot.classList.remove('btn-primary');
                        dot.classList.add('btn-light', 'bg-secondary-subtle');
                    }
                });

                // Helper to switch visibility
                const switchActive = (elements, targetIndex) => {
                    elements.forEach(el => {
                        const elIndex = parseInt(el.getAttribute('data-slide-index'));
                        if (elIndex === targetIndex) {
                            el.classList.remove('d-none');
                            // Small timeout to allow d-block to apply before adding opacity class for transition
                            setTimeout(() => {
                                el.classList.add('show');
                            }, 10);
                        } else {
                            el.classList.remove('show');
                            // Wait for transition to finish before hiding
                            setTimeout(() => {
                                if (!el.classList.contains('show')) {
                                    el.classList.add('d-none');
                                }
                            }, 150); // Matches standard bootstrap fade duration roughly
                        }
                    });
                };

                switchActive(images, index);
                switchActive(contents, index);

                currentIndex = index;
            };

            const nextSlide = () => {
                const nextIndex = (currentIndex + 1) % dots.length;
                updateCarousel(nextIndex);
            };

            const startAutoPlay = () => {
                interval = setInterval(nextSlide, autoPlayDelay);
            };

            const stopAutoPlay = () => {
                clearInterval(interval);
            };

            // Event Listeners for Dots
            dots.forEach(dot => {
                dot.addEventListener('click', (e) => {
                    const targetIndex = parseInt(e.target.getAttribute('data-target'));
                    stopAutoPlay();
                    updateCarousel(targetIndex);
                    startAutoPlay();
                });
            });

            // Start loop
            startAutoPlay();

            // Optional: Pause on hover
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