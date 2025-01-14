// 화살표로 슬라이드인데 변경예정
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
// 화살표로 슬라이드인데 변경예정


// 하트 클릭시 색 변경
document.addEventListener("DOMContentLoaded", () => {
    // 하트 클릭 이벤트
    document.querySelectorAll(".heart-icon").forEach((heart) => {
        heart.addEventListener("click", () => {
            if (heart.classList.contains("filled")) {
                heart.classList.remove("filled");
                heart.classList.add("unfilled");
            } else {
                heart.classList.remove("unfilled");
                heart.classList.add("filled");
            }
        });
    });
});
// 하트 클릭시 색 변경

