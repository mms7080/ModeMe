document.addEventListener("DOMContentLoaded", () => {
    const selectionContainer = document.querySelector(".selection");
    const addButton = document.querySelector(".add-to-selection");
    const totalPriceElement = document.getElementById("total-price");

    const colorButtons = document.querySelectorAll(".color-button");
    const sizeButtons = document.querySelectorAll(".size-button");
	
	const thumbnails = document.querySelectorAll(".thumbnail-image");
	const mainPreview = document.getElementById("main-preview");

    let selectedColor = null;
    let selectedSizes = []; // 선택한 사이즈를 리스트로 관리
	
	//썸네일 클릭 이벤트
	thumbnails.forEach((thumbnail) => {
	       thumbnail.addEventListener("click", function () {
	           // 썸네일 이미지를 메인 미리보기 이미지로 설정
	           mainPreview.src = thumbnail.src;
	       });
	   });

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
            const size = button.textContent.trim();

            if (selectedSizes.includes(size)) {
                // 이미 선택된 사이즈면 제거
                selectedSizes = selectedSizes.filter((s) => s !== size);
                button.classList.remove("selected-size");
            } else {
                // 새로운 사이즈 추가
                selectedSizes.push(size);
                button.classList.add("selected-size");
            }
        });
    });

    // Add selection items
    const addSelectionItems = () => {
        if (!selectedColor || selectedSizes.length === 0) {
            alert("색상과 사이즈를 모두 선택해주세요.");
            return;
        }

        // 상품 가격 가져오기
        const priceElement = document.querySelector(".price");
        const productPrice = parseInt(priceElement.getAttribute("data-price"), 10);

        selectedSizes.forEach((size) => {
            const existingItem = Array.from(selectionContainer.children).find(
                (item) =>
                    item.querySelector(".color").textContent === selectedColor &&
                    item.querySelector(".size").textContent === size
            );

            if (existingItem) {
                alert(`이미 선택된 옵션입니다: ${size}`);
                return;
            }

            const itemElement = document.createElement("div");
            itemElement.className = "selection-item";
            itemElement.innerHTML = `
                <b>선택한 상품</b>
                <p class="color">${selectedColor}</p>
                <span class="size">${size}</span>
                <input type="number" class="quantity-input" value="1" min="1">
                <span class="price" data-price="${productPrice}">${productPrice.toLocaleString()}원</span>
                <button class="delete-item" aria-label="삭제">&times;</button>
            `;

            itemElement.querySelector(".quantity-input").addEventListener("input", updateTotalPrice);
            itemElement.querySelector(".delete-item").addEventListener("click", () => {
                itemElement.remove();
                updateTotalPrice();
            });

            selectionContainer.appendChild(itemElement);
        });

        resetSelections();
        updateTotalPrice();
    };

    const resetSelections = () => {
        colorButtons.forEach((button) => button.classList.remove("selected-color"));
        sizeButtons.forEach((button) => button.classList.remove("selected-size"));
        selectedColor = null;
        selectedSizes = [];
    };

    const updateTotalPrice = () => {
        const totalPrice = Array.from(selectionContainer.children).reduce((acc, item) => {
            const quantity = parseInt(item.querySelector(".quantity-input").value, 10);
            const price = parseInt(item.querySelector(".price").getAttribute("data-price"), 10);
            return acc + quantity * price;
        }, 0);

        totalPriceElement.textContent = `${totalPrice.toLocaleString()} 원`;
    };

    if (addButton) {
        addButton.addEventListener("click", addSelectionItems);
    }

});
