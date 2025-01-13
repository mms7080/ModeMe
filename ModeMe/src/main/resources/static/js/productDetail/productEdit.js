document.addEventListener("DOMContentLoaded", () => {
    const mainCategoryInputs = document.querySelectorAll('input[name="category"]');
    const mainCategoryLabels = document.querySelectorAll('label[for]');
    const subcategoryContainer = document.getElementById("subcategory");

    // 서브 카테고리 데이터
    const subcategories = {
        outer: ["점퍼", "가디건", "자켓", "코트"],
        top: ["티셔츠", "민소매", "셔츠/블라우스", "니트", "후드/맨투맨"],
        bottom: ["데님", "팬츠", "슬랙스", "스커트", "트레이닝"]
    };

    // 카테고리 선택 시 서브 카테고리 표시 및 라벨 색상 변경
    mainCategoryInputs.forEach((input) => {
        input.addEventListener("change", () => {
            const selectedCategory = input.value;

            // 모든 라벨을 회색으로 초기화
            mainCategoryLabels.forEach((label) => {
                label.style.color = "#aaa";
                label.style.fontWeight = "normal";
            });

            // 선택된 라벨만 검정색으로 강조
            const selectedLabel = document.querySelector(`label[for="${input.id}"]`);
            selectedLabel.style.color = "#000";
            selectedLabel.style.fontWeight = "bold";

            // 서브 카테고리 생성
            const subItems = subcategories[selectedCategory] || [];
            subcategoryContainer.innerHTML = ""; // 기존 서브 카테고리 초기화

            subItems.forEach((item) => {
                const subItemDiv = document.createElement("div");
                const subItemLabel = document.createElement("label");
                const subItemInput = document.createElement("input");

                subItemLabel.textContent = item;
                subItemInput.type = "radio";
                subItemInput.name = "subcategory";
                subItemInput.value = item;

                subItemDiv.appendChild(subItemInput);
                subItemDiv.appendChild(subItemLabel);

                subcategoryContainer.appendChild(subItemDiv);
            });
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const imageInputs = document.querySelectorAll(".image-inputs div");

    imageInputs.forEach((box, index) => {
        const fileInput = box.querySelector("input[type='file']");

        // 박스를 클릭하면 연결된 파일 입력 필드 활성화
        box.addEventListener("click", () => {
            fileInput.click();
        });

        // 파일 선택 후 파일 이름 표시 (선택적으로 추가)
        fileInput.addEventListener("change", () => {
            if (fileInput.files.length > 0) {
                box.innerHTML = `<span>${fileInput.files[0].name}</span>`;
            }
        });
    });
});

// rich text editer
document.addEventListener("DOMContentLoaded", () => {
    // Rich Text Editor 초기화
    const editor = new Quill("#rich-text-editor", {
        theme: "snow",
        placeholder: "상품 상세 정보를 입력하세요...",
        modules: {
            toolbar: [
                ["bold", "italic", "underline", "strike"],
                ["blockquote", "code-block"],
                [{ header: 1 }, { header: 2 }],
                [{ list: "ordered" }, { list: "bullet" }],
                [{ script: "sub" }, { script: "super" }],
                [{ indent: "-1" }, { indent: "+1" }],
                [{ direction: "rtl" }],
                [{ size: ["small", false, "large", "huge"] }],
                [{ header: [1, 2, 3, 4, 5, 6, false] }],
                [{ color: [] }, { background: [] }],
                [{ font: [] }],
                [{ align: [] }],
                ["clean"],
            ],
        },
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".buttons button");

    buttons.forEach((button) => {
        button.addEventListener("click", () => {
            const url = button.getAttribute("data-url"); // 버튼의 data-url 속성 값 가져오기
            if (url) {
                window.location.href = url; // 해당 URL로 이동
            } else {
                console.error("URL이 설정되지 않았습니다.");
            }
        });
    });
});



