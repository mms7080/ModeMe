document.addEventListener("DOMContentLoaded", () => {
    const selectionContainer = document.querySelector(".selection");
    const addButton = document.querySelector(".add-to-selection");
    const totalPriceElement = document.getElementById("total-price");

    const colorButtons = document.querySelectorAll(".color-button");
    const sizeButtons = document.querySelectorAll(".size-button");

    let selectedColor = null;
    let selectedSize = null;

    // Handle color selection
    colorButtons.forEach((button) => {
        button.addEventListener("click", () => {
            colorButtons.forEach((btn) => btn.classList.remove("selected-color"));
            button.classList.add("selected-color");
            selectedColor = button.textContent.trim();
        });
    });

    // Handle size selection
    sizeButtons.forEach((button) => {
        button.addEventListener("click", () => {
            sizeButtons.forEach((btn) => btn.classList.remove("selected-size"));
            button.classList.add("selected-size");
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
            <b>선택한 상품</b>
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

document.addEventListener("DOMContentLoaded", () => {
    const previewImage = document.querySelector(".image-preview img");
    const thumbnails = document.querySelectorAll(".thumbnail img");

    thumbnails.forEach((thumbnail) => {
        thumbnail.addEventListener("click", () => {
            const currentPreviewSrc = previewImage.src;
            const currentPreviewAlt = previewImage.alt;

            previewImage.src = thumbnail.src;
            previewImage.alt = thumbnail.alt;

            thumbnail.src = currentPreviewSrc;
            thumbnail.alt = currentPreviewAlt;
        });
    });

    if (thumbnails.length > 0) {
        previewImage.src = thumbnails[0].src;
        previewImage.alt = thumbnails[0].alt;
    }
});
