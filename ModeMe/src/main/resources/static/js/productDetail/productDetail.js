document.addEventListener("DOMContentLoaded", () => {
    const selectionContainer = document.querySelector(".selection");
    const addButton = document.querySelector(".add-to-selection");
    const totalPriceElement = document.getElementById("total-price");

    const colorButtons = document.querySelectorAll(".option-label.color + .option-buttons button");
    const sizeButtons = document.querySelectorAll(".option-label.size + .option-buttons button");

    let selectedColor = null;
    let selectedSize = null;

    // Handle color selection
    colorButtons.forEach((button) => {
        button.addEventListener("click", () => {
            // 모든 색상 버튼에서 'selected-color' 클래스 제거
            colorButtons.forEach((btn) => btn.classList.remove("selected-color"));

            // 클릭한 버튼에 'selected-color' 클래스 추가
            button.classList.add("selected-color");

            // 선택한 색상 텍스트 저장
            selectedColor = button.textContent.trim();
        });
    });

    // Handle size selection
    sizeButtons.forEach((button) => {
        button.addEventListener("click", () => {
            // 모든 사이즈 버튼에서 'selected-size' 클래스 제거
            sizeButtons.forEach((btn) => btn.classList.remove("selected-size"));

            // 클릭한 버튼에 'selected-size' 클래스 추가
            button.classList.add("selected-size");

            // 선택한 사이즈 텍스트 저장
            selectedSize = button.textContent.trim();
        });
    });

    // Add selection item
    const addSelectionItem = () => {
        if (!selectedColor || !selectedSize) {
            alert("색상과 사이즈를 모두 선택해주세요.");
            return;
        }

        const existingItem = Array.from(selectionContainer.children).find(
            (item) =>
                item.querySelector("p").textContent === selectedColor &&
                item.querySelector(".size").textContent === selectedSize
        );

        if (existingItem) {
            alert("이미 선택된 옵션입니다.");
            return;
        }

        const itemElement = document.createElement("div");
        itemElement.className = "selection-item";
        itemElement.innerHTML = `
            <b>최고로 따뜻한 발마칸 코오트</b>
            <p>${selectedColor}</p>
            <span class="size">${selectedSize}</span>
            <input type="number" class="quantity-input" value="1" min="1">
            <span class="price">₩81,900</span>
            <button class="delete-item" aria-label="삭제">&times;</button>
        `;

        itemElement.querySelector(".quantity-input").addEventListener("input", updateTotalPrice);
        itemElement.querySelector(".delete-item").addEventListener("click", () => {
            itemElement.remove();
            updateTotalPrice();
        });

        selectionContainer.appendChild(itemElement);

        resetSelections();
        updateTotalPrice();
    };

    const resetSelections = () => {
        colorButtons.forEach((button) => button.classList.remove("selected-color"));
        sizeButtons.forEach((button) => button.classList.remove("selected-size"));
        selectedColor = null;
        selectedSize = null;
    };

    const updateTotalPrice = () => {
        const totalPrice = Array.from(selectionContainer.children).reduce((acc, item) => {
            const quantity = parseInt(item.querySelector(".quantity-input").value, 10);
            const price = 81900;
            return acc + quantity * price;
        }, 0);

        totalPriceElement.textContent = `₩${totalPrice.toLocaleString()}`;
    };

    if (addButton) {
        addButton.addEventListener("click", addSelectionItem);
    }
});
