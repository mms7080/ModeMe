document.addEventListener("DOMContentLoaded", () => {
    // 품절 버튼과 상품 수량 입력 필드
    const stockInput = document.getElementById("stock");
    const soldOutButton = document.getElementById("sold-out-btn");

    // 품절 버튼 클릭 시 수량을 0으로 설정하고 입력 필드 비활성화
    soldOutButton.addEventListener("click", () => {
        stockInput.value = 0;
        stockInput.disabled = true; // 입력 필드 비활성화
    });

    const colorInfoContainer = document.querySelector(".product-color-information");

    // Function to add a new color section
    const addColorSection = () => {
        const colorSection = document.createElement("div");
        colorSection.className = "color-section";
        colorSection.innerHTML = `
            <span class="color-info">
                <label for="color">상품 색상</label>
                <input class="color" type="color" value="#000000">
            </span>
            <div class="color-output">R: 0, G: 0, B: 0</div>
            <input class="color-name" type="text" placeholder="색상 이름 입력">
            <button class="add-color">+</button>
        `;
        colorInfoContainer.appendChild(colorSection);

        // Add event listener to new color input
        const colorInput = colorSection.querySelector(".color");
        const colorOutput = colorSection.querySelector(".color-output");
        const addButton = colorSection.querySelector(".add-color");

        colorInput.addEventListener("input", () => {
            const rgb = hexToRgb(colorInput.value);
            colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;
        });

        addButton.addEventListener("click", addColorSection);
    };

    // Function to convert HEX to RGB
    const hexToRgb = (hex) => {
        const bigint = parseInt(hex.slice(1), 16);
        const r = (bigint >> 16) & 255;
        const g = (bigint >> 8) & 255;
        const b = bigint & 255;
        return { r, g, b };
    };

    // Initial setup for color section
    const initialColorInput = document.querySelector(".color");
    const initialColorOutput = document.querySelector(".color-output");
    const initialAddButton = document.querySelector(".add-color");

    initialColorInput.addEventListener("input", () => {
        const rgb = hexToRgb(initialColorInput.value);
        initialColorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;
    });

    initialAddButton.addEventListener("click", addColorSection);
});
