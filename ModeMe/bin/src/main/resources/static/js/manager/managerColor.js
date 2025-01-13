// Function to add a new color section
const addColorSection = () => {
    const colorList = document.querySelector(".product-color-information");

    const colorSection = document.createElement("div");
    colorSection.className = "color-section";
    colorSection.innerHTML = `
        <span class="color-info">
            <label for="color">상품 색상</label>
            <input class="color" type="color" value="#000000">
        </span>
        <div class="color-output">R: 0, G: 0, B: 0</div>
        <input class="color-name" type="text" placeholder="색상 이름 입력">
        <button onclick="removeItem(this)">삭제</button>
    `;

    colorList.appendChild(colorSection);

    const colorInput = colorSection.querySelector(".color");
    const colorOutput = colorSection.querySelector(".color-output");
    const addButton = colorSection.querySelector(".add-color");

    // Update RGB values dynamically
    colorInput.addEventListener("input", () => {
        const rgb = hexToRgb(colorInput.value);
        colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;
    });

    // Add event listener to "+" button for adding new color sections
    addButton.addEventListener("click", addColorSection);
};

// Convert HEX color to RGB
const hexToRgb = (hex) => {
    const bigint = parseInt(hex.slice(1), 16);
    const r = (bigint >> 16) & 255;
    const g = (bigint >> 8) & 255;
    const b = bigint & 255;
    return { r, g, b };
};

// Initialize the first color section
document.addEventListener("DOMContentLoaded", () => {
    const initialColorInput = document.querySelector(".color");
    const initialColorOutput = document.querySelector(".color-output");
    const initialAddButton = document.querySelector(".add-color");

    initialColorInput.addEventListener("input", () => {
        const rgb = hexToRgb(initialColorInput.value);
        initialColorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;
    });

    initialAddButton.addEventListener("click", addColorSection);
});
