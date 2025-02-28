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


document.addEventListener("DOMContentLoaded", () => {
	const mainCategoryInputs = document.querySelectorAll('input[name="category"]');
	const subcategoryContainer = document.getElementById("subcategory");

	// 서브카테고리 데이터
	const subcategories = {
		outer: ["점퍼", "가디건", "자켓", "코트"],
		top: ["티셔츠", "민소매", "셔츠/블라우스", "니트", "후드/맨투맨"],
		bottom: ["데님", "팬츠", "슬랙스", "스커트", "트레이닝"]
	};

	// 저장된 서브카테고리 값 가져오기
	const currentSubcategory = subcategoryContainer.getAttribute("data-selected-subcategory");

	// 초기 서브카테고리 설정
	const currentCategory = document.querySelector('input[name="category"]:checked')?.value;
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

			// 저장된 서브카테고리를 초기 선택
			if (item === selectedSubcategory) {
				subItemInput.checked = true; // 선택된 서브카테고리 체크
			}
			subItemDiv.appendChild(subItemInput);
			subItemDiv.appendChild(subItemLabel);
			subcategoryContainer.appendChild(subItemDiv);
		});
	}
});

// 미리보기 영역에 있는 이미지들의 src를 hidden input에 업데이트하는 함수 (미리보기 컨테이너를 명확히 지정)
function updateImageUrlsHiddenInput() {
	const previewContainer = document.getElementById("previewImagesContainer");
	if (!previewContainer) {
		console.error("🚨 Preview images container not found.");
		return;
	}
	const previewImages = previewContainer.querySelectorAll("img.preview-image");
	const imageUrls = [];
	previewImages.forEach(img => {
		// placeholder 이미지가 아닌 경우에만 URL 수집
		if (img && img.src && !img.src.includes("placeholder.png")) {
			imageUrls.push(img.src);
		}
	});
	const hiddenInput = document.getElementById("imageUrls");
	if (hiddenInput) {
		hiddenInput.value = imageUrls.join(",");
	} else {
		console.error("Hidden input with id 'imageUrls' not found.");
	}
}

// 수정된 이미지 업로드 함수 (Promise 반환 없이 바로 실행)
function uploadImage(imageUploadName, previewImageName) {
	const fileInput = document.getElementById(imageUploadName);
	if (!fileInput.files || fileInput.files.length === 0) {
		return;
	}

	const uuid = crypto.randomUUID();
	let file = fileInput.files[0];
	let formData = new FormData();
	let newFileName = file.name + "_" + uuid; // 파일명 변경
	let renamedFile = new File([file], newFileName, { type: file.type });
	console.log(`Renamed file: ${renamedFile.name}`);
	formData.append("file", renamedFile);

	fetch("/api/gcs/upload", { method: "POST", body: formData })
		.then(response => {
			return response.text(); // 이미지 URL 반환
		})
		.then(imageUrl => {
			if (imageUrl && imageUrl.trim() !== "") {
				// hidden input 업데이트
				let imageUrlsInput = document.getElementById("imageUrls");
				if (!imageUrlsInput) {
					return;
				}
				// 기존 값 가져오기 (hidden input은 수정페이지에서 등록 페이지와 달리 초기값이 있을 수 있음)
				let existingUrls = imageUrlsInput.value ? imageUrlsInput.value.split(",") : [];
				existingUrls.push(imageUrl);
				imageUrlsInput.value = existingUrls.join(",");

				// 미리보기 이미지 업데이트
				let previewImage = document.getElementById(previewImageName);
				if (previewImage) {
					previewImage.src = imageUrl;
					previewImage.style.display = "block";
					previewImage.width = 300; // 이미지 크기 조절
				} else {
					console.error(`🚨 Preview image not found for id: ${previewImageName}`);
				}
				// 업데이트 후 hidden input 재갱신 (전체 미리보기 이미지 기준)
				updateImageUrlsHiddenInput();
			} else {}
		}).catch(error => {
			console.error("🚨 이미지 업로드 실패:", error);
		});
}

document.addEventListener("DOMContentLoaded", () => {
	const imageInputs = document.querySelectorAll(".image-inputs div");

	imageInputs.forEach((box) => {
		const fileInput = box.querySelector("input[type='file']");
		const fileNameSpan = box.querySelector(".file-name");

		// 박스를 클릭하면 파일 입력 필드 활성화
		box.addEventListener("click", (e) => {
			// 클릭 대상이 input, button, span이 아닌 경우에만 파일 입력 클릭
			const tagName = e.target.tagName.toLowerCase();
			if (tagName === "input" || tagName === "button" || tagName === "span") return;
			fileInput.click();
		});

		// 파일 선택 후, 파일 이름 표시와 업로드 함수 호출
		fileInput.addEventListener("change", () => {
			if (fileInput.files.length > 0) {
				fileNameSpan.textContent = fileInput.files[0].name;
				const previewId = fileInput.getAttribute("data-preview-id");
				uploadImage(fileInput.id, previewId);
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
					["clean"],
				],
				handlers: {
					image: function() {
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

document.addEventListener("DOMContentLoaded", function() {
	const fileInputs = document.querySelectorAll(".image-input");
	const previewImages = document.querySelectorAll(".preview-image");
	const imageUrlsInput = document.getElementById("imageUrls");

	let currentImageUrls = imageUrlsInput.value ? imageUrlsInput.value.split(",") : [];

	// 기존 이미지 유지 (기존 이미지 URL이 hidden input에 저장됨)
	previewImages.forEach((previewImage, index) => {
		if (currentImageUrls[index]) {
			previewImage.src = currentImageUrls[index];
		}
	});

	// 파일 입력이 변경될 때 이벤트 리스너 추가
	fileInputs.forEach((fileInput, index) => {
		fileInput.addEventListener("change", function(event) {
			const file = event.target.files[0];
			if (file) {
				const reader = new FileReader();
				reader.onload = function(e) {
					previewImages[index].src = e.target.result;
					currentImageUrls[index] = e.target.result; // 새로운 이미지 URL 업데이트
					updateHiddenInput();
				};
				reader.readAsDataURL(file);
			}
		});
	});

	// Hidden input 업데이트 함수
	function updateHiddenInput() {
		imageUrlsInput.value = currentImageUrls.join(",");
	}
});