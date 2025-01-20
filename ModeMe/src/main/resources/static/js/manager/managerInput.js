document.addEventListener("DOMContentLoaded", () => {
    // **카테고리 및 서브 카테고리 로직**
    const mainCategoryInputs = document.querySelectorAll('input[name="category"]');
    const mainCategoryLabels = document.querySelectorAll('label[for]');
    const subcategoryContainer = document.getElementById("subcategory");
//    const subcategoryHiddenInput = document.getElementById("subcategory-hidden");
/*	const subcategoryHiddenInput = document.getElementById("subcategory");*/
	const subcategoryHiddenInput = document.querySelector('input[name="subcategory"]');

    const subcategories = {
        outer: ["점퍼", "가디건", "자켓", "코트"],
        top: ["티셔츠", "민소매", "셔츠/블라우스", "니트", "후드/맨투맨"],
        bottom: ["데님", "팬츠", "슬랙스", "스커트", "트레이닝"],
    };

    mainCategoryInputs.forEach((input) => {
        input.addEventListener("change", () => {
            const selectedCategory = input.value;

            // 모든 라벨 초기화
            mainCategoryLabels.forEach((label) => {
                label.style.color = "#aaa";
                label.style.fontWeight = "normal";
            });

            // 선택된 라벨 강조
            const selectedLabel = document.querySelector(`label[for="${input.id}"]`);
            selectedLabel.style.color = "#000";
            selectedLabel.style.fontWeight = "bold";

            // 서브 카테고리 표시
            const subItems = subcategories[selectedCategory] || [];
            subcategoryContainer.innerHTML = "";

            subItems.forEach((item) => {
                const subItemDiv = document.createElement("div");
                const subItemLabel = document.createElement("label");
                const subItemInput = document.createElement("input");

                subItemLabel.textContent = item;
                subItemInput.type = "radio";
                subItemInput.name = "subcategory";
                subItemInput.value = item;

                // 서브 카테고리 선택 시 hidden input 업데이트
                subItemInput.addEventListener("change", () => {
                    subcategoryHiddenInput.value = item;
                });

                subItemDiv.appendChild(subItemInput);
                subItemDiv.appendChild(subItemLabel);
                subcategoryContainer.appendChild(subItemDiv);
            });

            // 초기값 설정
            if (subItems.length > 0) {
                subcategoryHiddenInput.value = subItems[0];
            } else {
                subcategoryHiddenInput.value = "";
            }
        });
    });

    // **Rich Text Editor 초기화**
    const editor = new Quill("#rich-text-editor", {
        theme: "snow",
        placeholder: "상품 상세 정보를 입력하세요...",
        modules: {
            toolbar: [
                ["bold", "italic", "underline", "strike"],
                ["blockquote", "code-block"],
                [{ header: 1 }, { header: 2 }],
                [{ list: "ordered" }, { list: "bullet" }],
                [{ size: ["small", false, "large", "huge"] }],
                [{ color: [] }, { background: [] }],
                ["clean"],
            ],
        },
    });

    const productDescriptionInput = document.getElementById("productDescription");

    // Rich Text Editor 내용 변경 시 hidden input 업데이트
    editor.on("text-change", () => {
        productDescriptionInput.value = editor.root.innerHTML.trim(); // HTML 내용 저장
    });

    // **폼 제출 시 유효성 검사**
    const form = document.querySelector("form");
    form.addEventListener("submit", (event) => {
        // 서브 카테고리 유효성 검사
        if (!subcategoryHiddenInput.value) {
            event.preventDefault();
            alert("서브 카테고리를 선택해주세요.");
            return;
        }

        // 상품 상세 정보 유효성 검사
        if (!productDescriptionInput.value || productDescriptionInput.value.trim() === "") {
            event.preventDefault();
            alert("상품 상세 정보를 입력해주세요.");
        }
    });
	//사이즈 체크박스
	document.querySelector('form').addEventListener('submit', function (e) {
	    e.preventDefault(); // 기본 제출 동작을 막음 (테스트용)
	    const selectedSizes = Array.from(document.querySelectorAll('input[name="productSize[]"]:checked'))
	                               .map(input => input.value);
	    console.log('선택된 사이즈:', selectedSizes);
	});
	//사이즈 제약조건
	form.addEventListener("submit", (event) => {
	    const selectedSizes = Array.from(
	        document.querySelectorAll('input[name="productSize[]"]:checked')
	    );
	    if (!subcategoryHiddenInput.value || subcategoryHiddenInput.value.trim() === "") {
	        event.preventDefault();
	        alert("서브 카테고리를 선택해주세요.");
	        return;
	    }
	    if (!productDescriptionInput.value || productDescriptionInput.value.trim() === "") {
	        event.preventDefault();
	        alert("상품 상세 정보를 입력해주세요.");
	        return;
	    }
	    if (selectedSizes.length === 0) {
	        event.preventDefault();
	        alert("적어도 하나 이상의 사이즈를 선택해주세요.");
	        return;
	    }
	});



    // **이미지 파일 입력 로직**
    const imageInputs = document.querySelectorAll(".image-inputs div");

    imageInputs.forEach((box) => {
        const fileInput = box.querySelector("input[type='file']");

        box.addEventListener("click", () => {
            fileInput.click();
        });

        fileInput.addEventListener("change", () => {
            if (fileInput.files.length > 0) {
                box.innerHTML = `<span>${fileInput.files[0].name}</span>`;
            }
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const sizeCheckboxes = document.querySelectorAll(".size-checkbox");
    const selectedSizesList = document.getElementById("selected-sizes-list");

    // 사이즈 추가 함수
    const addSelectedSize = (size) => {
        // 이미 추가된 사이즈인지 확인
        if (Array.from(selectedSizesList.children).some((item) => item.textContent === size)) {
            alert(`${size} 사이즈는 이미 선택되었습니다.`);
            return;
        }

        // 리스트 항목 생성
        const listItem = document.createElement("li");
        listItem.textContent = size;

        // 삭제 버튼 추가
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "삭제";
        deleteButton.style.marginLeft = "10px";
        deleteButton.addEventListener("click", () => {
            listItem.remove(); // 항목 삭제
            // 체크박스 상태 해제
            document.querySelector(`.size-checkbox[value="${size}"]`).checked = false;
        });

        listItem.appendChild(deleteButton);
        selectedSizesList.appendChild(listItem);
    };

    // 체크박스 클릭 이벤트 처리
    sizeCheckboxes.forEach((checkbox) => {
        checkbox.addEventListener("change", () => {
            if (checkbox.checked) {
                addSelectedSize(checkbox.value);
            } else {
                // 체크 해제 시 해당 항목 삭제
                Array.from(selectedSizesList.children).forEach((item) => {
                    if (item.textContent.includes(checkbox.value)) {
                        item.remove();
                    }
                });
            }
        });
    });
});

