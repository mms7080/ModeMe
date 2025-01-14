document.addEventListener("DOMContentLoaded", () => {
    const colorInfoContainer = document.querySelector(".product-color-information");

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

        const colorInput = colorSection.querySelector(".color");
        const colorOutput = colorSection.querySelector(".color-output");
        const addButton = colorSection.querySelector(".add-color");

        // Get the hidden input where colors will be stored
        const colorsHiddenInput = document.getElementById("colors");

        // Update RGB values when the color input changes
        colorInput.addEventListener("input", () => {
            const rgb = hexToRgb(colorInput.value);
            colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;

            // Update the hidden input with the selected color value
            const currentColors = colorsHiddenInput.value ? colorsHiddenInput.value.split(",") : [];
            currentColors.push(colorInput.value);
            colorsHiddenInput.value = currentColors.join(",");
        });

        // When + button is clicked, add a new color section
        addButton.addEventListener("click", addColorSection);
    };

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
