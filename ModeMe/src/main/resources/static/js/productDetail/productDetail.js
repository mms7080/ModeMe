document.addEventListener("DOMContentLoaded", () => {
    const colorButtons = document.querySelectorAll(".option-label.color + .option-buttons button");
    const sizeButtons = document.querySelectorAll(".option-label.size + .option-buttons button");
    const selectionContainer = document.querySelector(".selection");
    const addButton = document.querySelector(".add-to-selection");
    const totalPriceElement = document.getElementById("total-price");

    let selectedColor = null;
    let selectedSize = null;

    // Handle color selection
    colorButtons.forEach((button) => {
        button.addEventListener("click", () => {
            colorButtons.forEach((btn) => btn.classList.remove("selected-color"));
            button.classList.add("selected-color");
            selectedColor = button.textContent;
        });
    });

    // Handle size selection
    sizeButtons.forEach((button) => {
        button.addEventListener("click", () => {
            sizeButtons.forEach((btn) => btn.classList.remove("selected-size"));
            button.classList.add("selected-size");
            selectedSize = button.textContent;
        });
    });

    // Add selection item
    const addSelectionItem = () => {
        if (!selectedColor || !selectedSize) {
            alert("색상과 사이즈를 모두 선택해주세요.");
            return;
        }

        // Check if the combination already exists
        const existingItem = Array.from(selectionContainer.children).find(
            (item) =>
                item.querySelector("p").textContent === selectedColor &&
                item.querySelector(".size").textContent === selectedSize
        );

        if (existingItem) {
            alert("이미 선택된 옵션입니다.");
            return;
        }

        // Add new selection item
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

        // Add quantity change listener
        itemElement.querySelector(".quantity-input").addEventListener("input", updateTotalPrice);

        // Add delete button functionality
        itemElement.querySelector(".delete-item").addEventListener("click", () => {
            itemElement.remove();
            updateTotalPrice();
        });

        selectionContainer.appendChild(itemElement);

        // Reset selected options
        resetSelections();
        updateTotalPrice();
    };

    // Reset selected options
    const resetSelections = () => {
        colorButtons.forEach((button) => button.classList.remove("selected-color"));
        sizeButtons.forEach((button) => button.classList.remove("selected-size"));
        selectedColor = null;
        selectedSize = null;
    };

    // Calculate total price
    const updateTotalPrice = () => {
        const totalPrice = Array.from(selectionContainer.children).reduce((acc, item) => {
            const quantity = parseInt(item.querySelector(".quantity-input").value, 10);
            const price = 81900; // 가격 설정
            return acc + quantity * price;
        }, 0);

        totalPriceElement.textContent = `₩${totalPrice.toLocaleString()}`;
    };

    // Add event listener to add button
    if (addButton) {
        addButton.addEventListener("click", addSelectionItem);
    }
});
