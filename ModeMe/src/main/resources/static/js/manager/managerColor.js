document.addEventListener("DOMContentLoaded", () => {
    const colorInfoContainer = document.querySelector(".product-color-information");
    const colorsHiddenInput = document.getElementById("colors");
    const colorNamesHiddenInput = document.getElementById("colorNames");

    const hexToRgb = (hex) => {
        const bigint = parseInt(hex.slice(1), 16);
        const r = (bigint >> 16) & 255;
        const g = (bigint >> 8) & 255;
        const b = bigint & 255;
        return { r, g, b };
    };

    const updateHiddenInputs = () => {
        const colorSections = colorInfoContainer.querySelectorAll(".color-section");
        const colors = [];
        const colorNames = [];

        colorSections.forEach((section) => {
            const colorValue = section.querySelector(".color").value;
            const colorName = section.querySelector(".color-name").value;
            colors.push(colorValue);
            colorNames.push(colorName);
        });

        colorsHiddenInput.value = colors.join(",");
        colorNamesHiddenInput.value = colorNames.join(",");
    };

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
        attachColorSectionEvents(colorSection);
        updateHiddenInputs();
    };

    const attachColorSectionEvents = (section) => {
        const colorInput = section.querySelector(".color");
        const colorOutput = section.querySelector(".color-output");
        const addButton = section.querySelector(".add-color");

        colorInput.addEventListener("input", () => {
            const rgb = hexToRgb(colorInput.value);
            colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;
            updateHiddenInputs();
        });

        section.querySelector(".color-name").addEventListener("input", updateHiddenInputs);

        addButton.addEventListener("click", (event) => {
            event.preventDefault();
            addColorSection();
        });
    };

    // 초기 색상 섹션 이벤트 설정
    const initialColorSection = document.querySelector(".color-section");
    attachColorSectionEvents(initialColorSection);
});
