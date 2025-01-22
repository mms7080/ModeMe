document.addEventListener("DOMContentLoaded", () => {
    const mainCategoryInputs = document.querySelectorAll('input[name="category"]');
    const subcategoryContainer = document.getElementById("subcategory");
    const mainCategoryLabels = document.querySelectorAll('label[for]');
	const colorInputs = document.querySelectorAll(".color");
	    
    // HEX → RGB 변환 함수
    const hexToRgb = (hex) => {
        const bigint = parseInt(hex.slice(1), 16);
        const r = (bigint >> 16) & 255;
        const g = (bigint >> 8) & 255;
        const b = bigint & 255;
        return { r, g, b };
    };

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
            colorOutput.textContent = `R: ${rgb.r}, G: ${rgb.g}, B: ${rgb.b}`;
        });
    });

	// 서브 카테고리 데이터
    const subcategories = {
        outer: ["점퍼", "가디건", "자켓", "코트"],
        top: ["티셔츠", "민소매", "셔츠/블라우스", "니트", "후드/맨투맨"],
        bottom: ["데님", "팬츠", "슬랙스", "스커트", "트레이닝"]
    };

    // 초기 서브카테고리 설정
    const currentCategory = document.querySelector('input[name="category"]:checked')?.value;
    const currentSubcategory = subcategoryContainer.getAttribute("data-selected-subcategory");
    updateSubcategories(currentCategory, currentSubcategory);

    // 메인 카테고리 변경 시 서브카테고리 업데이트
    mainCategoryInputs.forEach((input) => {
        input.addEventListener("change", () => {
            updateSubcategories(input.value);
        });
    });

    // 서브카테고리 업데이트 함수
    function updateSubcategories(category, selectedSubcategory = null) {
        const subItems = subcategories[category] || [];
        subcategoryContainer.innerHTML = ""; // 기존 서브카테고리 초기화

        subItems.forEach((item) => {
            const subItemDiv = document.createElement("div");
            const subItemLabel = document.createElement("label");
            const subItemInput = document.createElement("input");

            subItemLabel.textContent = item;
            subItemInput.type = "radio";
            subItemInput.name = "subcategory";
            subItemInput.value = item;

            // 선택된 서브카테고리 유지
            if (item === selectedSubcategory) {
                subItemInput.checked = true;
            }

            subItemDiv.appendChild(subItemInput);
            subItemDiv.appendChild(subItemLabel);
            subcategoryContainer.appendChild(subItemDiv);
        });
    }
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

document.addEventListener("DOMContentLoaded", () => {
    // Hidden input 필드와 에디터 컨테이너 가져오기
    const editorElement = document.getElementById("rich-text-editor");
    const hiddenInput = document.getElementById("productDescription");

    // Rich Text Editor 초기화
    const editor = new Quill("#rich-text-editor", {
        theme: "snow",
        placeholder: "상품 상세 정보를 입력하세요...",
        modules: {
            toolbar: {
                container: [
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
                    ["image"], // 이미지 삽입 버튼 추가
                    ["clean"],
                ],
                handlers: {
                    image: function () {
                        const input = document.createElement("input");
                        input.setAttribute("type", "file");
                        input.setAttribute("accept", "image/*");
                        input.click();

                        input.onchange = async () => {
                            const file = input.files[0];
                            if (file) {
                                const reader = new FileReader();
                                reader.onload = (e) => {
                                    const range = editor.getSelection();
                                    editor.insertEmbed(range.index, "image", e.target.result);
                                };
                                reader.readAsDataURL(file);

                                // 서버 업로드 (선택적으로 구현)
                                const formData = new FormData();
                                formData.append("image", file);

                                try {
                                    const response = await fetch("/upload-image", {
                                        method: "POST",
                                        body: formData,
                                    });
                                    const data = await response.json();
                                    if (data.url) {
                                        const range = editor.getSelection();
                                        editor.insertEmbed(range.index, "image", data.url);
                                    } else {
                                        console.error("서버 업로드 실패");
                                    }
                                } catch (error) {
                                    console.error("서버 업로드 중 오류 발생:", error);
                                }
                            }
                        };
                    },
                },
            },
        },
    });

    // 초기값 설정
    const initialContent = hiddenInput.value; // Hidden input에 저장된 값 가져오기
    if (initialContent) {
        editor.clipboard.dangerouslyPasteHTML(initialContent);
    }

    // 에디터 내용 변경 시 hidden input 업데이트
    editor.on("text-change", () => {
        hiddenInput.value = editor.root.innerHTML.trim();
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

document.addEventListener("DOMContentLoaded", () => {
    const saveButton = document.getElementById("save-btn");
    const cancelButton = document.getElementById("cancel-btn");
    const form = document.querySelector("form");

    // 수정 완료 버튼 동작
    saveButton.addEventListener("click", async (event) => {
        event.preventDefault(); // 기본 폼 제출 방지
        const actionUrl = form.action; // 폼의 action URL 가져오기
        const formData = new FormData(form); // 폼 데이터 생성

        try {
            const response = await fetch(actionUrl, {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                const id = formData.get("id"); // hidden 필드에서 id 가져오기
                const redirectUrl = `/productDetail/${id}`; // 상세 페이지 URL 생성
                window.location.href = redirectUrl; // 상세 페이지로 이동
            } else {
                console.error("저장 중 오류 발생:", response.statusText);
                alert("수정 중 문제가 발생했습니다.");
            }
        } catch (error) {
            console.error("저장 요청 중 오류 발생:", error);
            alert("저장 요청을 처리할 수 없습니다.");
        }
    });

    // 수정 취소 버튼 동작
    cancelButton.addEventListener("click", (event) => {
        event.preventDefault(); // 기본 동작 방지
        const id = form.querySelector('input[name="id"]').value; // hidden 필드에서 id 가져오기
        const redirectUrl = `/productDetail/${id}`; // 상세 페이지 URL 생성
        window.location.href = redirectUrl; // 상세 페이지로 이동
    });
});


