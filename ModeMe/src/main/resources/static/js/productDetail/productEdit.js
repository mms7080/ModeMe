document.addEventListener("DOMContentLoaded", () => {

	const stockInput = document.getElementById("stock");
	const soldOutButton = document.getElementById("sold-out-btn");

	soldOutButton.addEventListener("click", () => {
		stockInput.value = 0;
	});
});

document.addEventListener("DOMContentLoaded", () => {
	const mainCategoryInputs = document.querySelectorAll('input[name="category"]');
	const subcategoryContainer = document.getElementById("subcategory");

	const subcategories = {
		outer: ["점퍼", "가디건", "자켓", "코트"],
		top: ["티셔츠", "민소매", "셔츠/블라우스", "니트", "후드/맨투맨"],
		bottom: ["데님", "팬츠", "슬랙스", "스커트", "트레이닝"]
	};

	const currentSubcategory = subcategoryContainer.getAttribute("data-selected-subcategory");

	const currentCategory = document.querySelector('input[name="category"]:checked')?.value;
	updateSubcategories(currentCategory, currentSubcategory);

	mainCategoryInputs.forEach((input) => {
		input.addEventListener("change", () => {
			updateSubcategories(input.value);
		});
	});

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

			if (item === selectedSubcategory) {
				subItemInput.checked = true;
			}
			subItemDiv.appendChild(subItemInput);
			subItemDiv.appendChild(subItemLabel);
			subcategoryContainer.appendChild(subItemDiv);
		});
	}
});

function updateImageUrlsHiddenInput() {
	const previewContainer = document.getElementById("previewImagesContainer");
	if (!previewContainer) {
		console.error("🚨 Preview images container not found.");
		return;
	}
	const previewImages = previewContainer.querySelectorAll("img.preview-image");
	const imageUrls = [];
	previewImages.forEach(img => {

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

function uploadImage(imageUploadName, previewImageName) {
	const fileInput = document.getElementById(imageUploadName);
	if (!fileInput.files || fileInput.files.length === 0) {
		return;
	}

	const uuid = crypto.randomUUID();
	let file = fileInput.files[0];
	let formData = new FormData();
	let newFileName = file.name + "_" + uuid;
	let renamedFile = new File([file], newFileName, { type: file.type });
	formData.append("file", renamedFile);

	fetch("/api/gcs/upload", { method: "POST", body: formData })
		.then(response => {
			return response.text();
		})
		.then(imageUrl => {
			if (imageUrl && imageUrl.trim() !== "") {

				let imageUrlsInput = document.getElementById("imageUrls");
				if (!imageUrlsInput) {
					return;
				}

				let existingUrls = imageUrlsInput.value ? imageUrlsInput.value.split(",") : [];
				existingUrls.push(imageUrl);
				imageUrlsInput.value = existingUrls.join(",");

				let previewImage = document.getElementById(previewImageName);
				if (previewImage) {
					previewImage.src = imageUrl;
					previewImage.style.display = "block";
					previewImage.width = 300; // 이미지 크기 조절
				} else {
					console.error(`🚨 Preview image not found for id: ${previewImageName}`);
				}

				updateImageUrlsHiddenInput();
			} else { }
		}).catch(error => {
			console.error("🚨 이미지 업로드 실패:", error);
		});
}

document.addEventListener("DOMContentLoaded", () => {
	const imageInputs = document.querySelectorAll(".image-inputs div");

	imageInputs.forEach((box) => {
		const fileInput = box.querySelector("input[type='file']");
		const fileNameSpan = box.querySelector(".file-name");

		box.addEventListener("click", (e) => {

			const tagName = e.target.tagName.toLowerCase();
			if (tagName === "input" || tagName === "button" || tagName === "span") return;
			fileInput.click();
		});

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

	const editorElement = document.getElementById("rich-text-editor");
	const hiddenInput = document.getElementById("productDescription");

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

	const initialContent = hiddenInput.value;
	if (initialContent) {
		editor.clipboard.dangerouslyPasteHTML(initialContent);
	}

	editor.on("text-change", () => {
		hiddenInput.value = editor.root.innerHTML.trim();
	});
});

document.addEventListener("DOMContentLoaded", () => {
	const buttons = document.querySelectorAll(".buttons button");
	buttons.forEach((button) => {
		button.addEventListener("click", () => {
			const url = button.getAttribute("data-url");
			if (url) {
				window.location.href = url;
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

	previewImages.forEach((previewImage, index) => {
		if (currentImageUrls[index]) {
			previewImage.src = currentImageUrls[index];
		}
	});

	fileInputs.forEach((fileInput, index) => {
		fileInput.addEventListener("change", function(event) {
			const file = event.target.files[0];
			if (file) {
				const reader = new FileReader();
				reader.onload = function(e) {
					previewImages[index].src = e.target.result;
					currentImageUrls[index] = e.target.result;
					updateHiddenInput();
				};
				reader.readAsDataURL(file);
			}
		});
	});

	function updateHiddenInput() {
		imageUrlsInput.value = currentImageUrls.join(",");
	}
});