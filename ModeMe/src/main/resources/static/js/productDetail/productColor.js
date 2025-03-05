document.addEventListener("DOMContentLoaded", () => {
	const mainCategoryLabels = document.querySelectorAll('label[for]');
	const colorInputs = document.querySelectorAll(".color");

	const hexToRgb = (hex) => {
		const bigint = parseInt(hex.slice(1), 16);
		const r = (bigint >> 16) & 255;
		const g = (bigint >> 8) & 255;
		const b = bigint & 255;
		return { r, g, b };
	}

	colorInputs.forEach((colorInput) => {
		const colorOutput = colorInput.closest(".color-section").querySelector(".color-output");

		const initialHex = colorInput.value;
		const initialRgb = hexToRgb(initialHex);
		colorOutput.textContent = `R: ${initialRgb.r}, G: ${initialRgb.g}, B: ${initialRgb.b}`;

		colorInput.addEventListener("input", () => {
			const hexValue = colorInput.value;
			const rgb = hexToRgb(hexValue);
			colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B:${rgb.b}`;
		});
	});
});

document.addEventListener("DOMContentLoaded", () => {
	const colorContainer = document.getElementById("color-container");
	if (colorContainer) {
		colorContainer.addEventListener("click", (event) => {
			if (event.target.classList.contains("remove-color")) {
				const colorSection = event.target.closest(".color-section");
				if (colorSection) {
					colorSection.remove();
				}
			}
		});
	}
});

document.addEventListener("DOMContentLoaded", () => {
	const colorContainer = document.getElementById("color-container");
	const addColorButton = document.querySelector(".add-color");

	addColorButton.addEventListener("click", () => {

		const newIndex = document.querySelectorAll(".color-section").length;

		const newColorSection = document.createElement("div");
		newColorSection.classList.add("color-section");

		newColorSection.innerHTML = `
            <span class="color-info">
                <label for="color-${newIndex}">상품 색상</label>
                <input class="color" type="color" id="color-${newIndex}" name="colors" value="#000000">
            </span>
            <div class="color-output">R: 0, G: 0, B: 0</div>
            <input class="color-name" type="text" name="colorNames" placeholder="색상 이름 입력">
            <button type="button" class="remove-color">삭제</button>
        `;

		colorContainer.insertBefore(newColorSection, addColorButton);

		newColorSection.querySelector(".remove-color").addEventListener("click", () => {
			newColorSection.remove();
		});

		const colorInput = newColorSection.querySelector(".color");
		const colorOutput = newColorSection.querySelector(".color-output");

		colorInput.addEventListener("input", () => {
			const hexValue = colorInput.value;
			const rgb = hexToRgb(hexValue);
			colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;
		});
	});

	function hexToRgb(hex) {
		const bigint = parseInt(hex.slice(1), 16);
		return {
			r: (bigint >> 16) & 255,
			g: (bigint >> 8) & 255,
			b: bigint & 255
		};
	}
});