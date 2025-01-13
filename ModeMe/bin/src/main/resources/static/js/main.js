// main.js

// Select necessary elements
const carousel = document.querySelector('.carousel');
const prevBtn = document.querySelector('.prev-btn');
const nextBtn = document.querySelector('.next-btn');

// Define slide step size
const slideStep = 220; // Image width + gap

// Event listeners for buttons
prevBtn.addEventListener('click', () => {
    carousel.scrollBy({
        left: -slideStep,
        behavior: 'smooth',
    });
});

nextBtn.addEventListener('click', () => {
    carousel.scrollBy({
        left: slideStep,
        behavior: 'smooth',
    });
});
