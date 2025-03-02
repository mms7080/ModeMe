document.addEventListener("DOMContentLoaded", () => {
	const mainCategoryLabels = document.querySelectorAll('label[for]');
	const colorInputs = document.querySelectorAll(".color");

	// HEX -> RGB 변환 함수
	const hexToRgb = (hex) => {
		const bigint = parseInt(hex.slice(1), 16);
		const r = (bigint >> 16) & 255;
		const g = (bigint >> 8) & 255;
		const b = bigint & 255;
		return { r, g, b };
	}

	// 색상 변경 시 RGB 업데이트
	colorInputs.forEach((colorInput) => {
		const colorOutput = colorInput.closest(".color-section").querySelector(".color-output");

		// 초기값으로 RGB 표시
		const initialHex = colorInput.value;
		const initialRgb = hexToRgb(initialHex);
		colorOutput.textContent = `R: ${initialRgb.r}, G: ${initialRgb.g}, B: ${initialRgb.b}`;

		// 색상 변경 이벤트
		colorInput.addEventListener("input", () => {
			const hexValue = colorInput.value;
			const rgb = hexToRgb(hexValue);
			colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B:${rgb.b}`;
		});
	});
});

// 색상 삭제 로직
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

// 색상 추가 로직
document.addEventListener("DOMContentLoaded", () => {
	const colorContainer = document.getElementById("color-container");
	const addColorButton = document.querySelector(".add-color");

	addColorButton.addEventListener("click", () => {

		// 중복 추가 방지: 현재 추가된 색상 개수를 기준으로 인덱스 설정
		const newIndex = document.querySelectorAll(".color-section").length;

		// 새로운 색상 입력 필드 생성
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

		// 새로운 색상 섹션을 + 버튼 위에만 추가되도록 수정
		colorContainer.insertBefore(newColorSection, addColorButton);

		// 삭제 버튼 이벤트 추가
		newColorSection.querySelector(".remove-color").addEventListener("click", () => {
			newColorSection.remove();
		});

		// 색상 변경 이벤트 추가 (RGB 업데이트)
		const colorInput = newColorSection.querySelector(".color");
		const colorOutput = newColorSection.querySelector(".color-output");

		colorInput.addEventListener("input", () => {
			const hexValue = colorInput.value;
			const rgb = hexToRgb(hexValue);
			colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;
		});
	});

	// HEX → RGB 변환 함수
	function hexToRgb(hex) {
		const bigint = parseInt(hex.slice(1), 16);
		return {
			r: (bigint >> 16) & 255,
			g: (bigint >> 8) & 255,
			b: bigint & 255
		};
	}
});