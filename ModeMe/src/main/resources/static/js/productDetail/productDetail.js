let firstMainImage = document.getElementById("main-preview")

document.addEventListener("DOMContentLoaded", function() {
	const selectionContainer = document.querySelector(".selection");
	const addButton = document.querySelector(".add-to-selection");
	const totalPriceElement = document.getElementById("total-price");
	const colorButtons = document.querySelectorAll(".color-button");
	const sizeButtons = document.querySelectorAll(".size-button");

	let selectedColor = null;
	let selectedColorId = null;
	let selectedSizes = [];
	let selectedSizeIds = [];

	colorButtons.forEach(button => {
		button.addEventListener("click", () => {
			colorButtons.forEach(btn => btn.classList.remove("selected-color"));
			button.classList.add("selected-color");
			selectedColor = button.textContent.trim();
			selectedColorId = button.getAttribute("data-color-id");
		});
	});

	sizeButtons.forEach(button => {
		button.addEventListener("click", () => {
			const size = button.textContent.trim();
			const sizeId = button.getAttribute("data-size-id");

			if (selectedSizes.includes(size)) {
				selectedSizes = selectedSizes.filter(s => s !== size);
				selectedSizeIds = selectedSizeIds.filter(id => id !== sizeId);
				button.classList.remove("selected-size");
			} else {
				selectedSizes.push(size);
				selectedSizeIds.push(sizeId);
				button.classList.add("selected-size");
			}
		});
	});

	const addSelectionItems = () => {
		if (!selectedColorId || selectedSizes.length === 0) {
			alert("색상과 사이즈를 모두 선택해주세요.");
			return;
		}

		const priceElement = document.querySelector(".price");
		const productPrice = parseInt(priceElement.getAttribute("data-price"), 10);

		selectedSizes.forEach((size, index) => {
			const sizeId = selectedSizeIds[index];

			const existingItem = Array.from(selectionContainer.children).find(
				item => item.querySelector(".color").getAttribute("data-color-id") === selectedColorId &&
					item.querySelector(".size").getAttribute("data-size-id") === sizeId
			);

			if (existingItem) {
				alert(`이미 선택된 옵션입니다: ${size}`);
				return;
			}

			const itemElement = document.createElement("div");
			itemElement.className = "selection-item";
			itemElement.innerHTML = `
                <b>선택한 상품</b>
                <p class="color" data-color-id="${selectedColorId}">${selectedColor}</p>
                <span class="size" data-size-id="${sizeId}">${size}</span>
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
		colorButtons.forEach(button => button.classList.remove("selected-color"));
		sizeButtons.forEach(button => button.classList.remove("selected-size"));
		selectedColor = null;
		selectedColorId = null;
		selectedSizes = [];
		selectedSizeIds = [];
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


document.addEventListener('DOMContentLoaded', () => {
	const optionButtons = document.querySelectorAll('.option-buttons button');
	const selectionSummary = document.querySelector('.selection-summary');
	const totalPriceElement = document.getElementById("total-price");
	const priceElement = document.querySelector(".price");

	let productPrice = parseInt(priceElement.getAttribute("data-price"), 10);

	// 포맷팅된 가격으로 표시
	const formatPrice = (price) => {
		return price.toLocaleString('en').replace(/,/g, ',');
	};

	// 초기 가격 설정
	priceElement.textContent = `${formatPrice(productPrice)}원`;

	optionButtons.forEach(button => {
		button.addEventListener('click', () => {
			const group = button.parentElement;
			const groupType = group.previousElementSibling.textContent.trim(); // COLOR or SIZE

			// If the button is already selected, unselect it and clear related selection
			if (button.classList.contains('selected')) {
				button.classList.remove('selected');
				updateSummary(groupType, '');
			} else {
				// Deselect all buttons in the same group
				group.querySelectorAll('.selected').forEach(selectedButton => {
					selectedButton.classList.remove('selected');
				});

				// Select the clicked button
				button.classList.add('selected');
				updateSummary(groupType, button.textContent.trim());
			}
		});
	});

	function updateSummary(type, value) {
		const summaryText = document.querySelector(`#summary-${type.toLowerCase()}`);
		if (summaryText) {
			if (value) {
				summaryText.textContent = `${type}: ${value}`;
			} else {
				summaryText.textContent = '';
			}
		}
	}

	const updateTotalPrice = () => {
		const totalPrice = Array.from(document.querySelectorAll('.selection-item')).reduce((total, item) => {
			const quantity = parseInt(item.querySelector('.quantity-input').value, 10);
			const itemPrice = parseInt(item.querySelector('.price').getAttribute('data-price'), 10);
			return total + quantity * itemPrice;
		}, 0);

		totalPriceElement.textContent = `${formatPrice(totalPrice)} 원`;
	};
});

// 페이지네이션 로직
document.addEventListener("DOMContentLoaded", () => {
	const paginationContainer = document.querySelector(".pagination");
	const reviewListContainer = document.querySelector(".review-list");
	if (!paginationContainer || !reviewListContainer) {
		console.error("🚨 페이지네이션 또는 리뷰 리스트 컨테이너를 찾을 수 없습니다.");
		return;
	}

	let currentPage = parseInt(paginationContainer.dataset.currentPage, 10) || 0;
	let totalPages = parseInt(paginationContainer.dataset.totalPages, 10) || 1;
	const productId = paginationContainer.dataset.productId;

	// 좋아요 버튼 이벤트 등록
	function setupLikeButtons() {
		document.querySelectorAll(".like-button").forEach(button => {
			button.removeEventListener("click", toggleLike);
			button.addEventListener("click", toggleLike);
		});
	}

	function toggleLike(event) {
		const button = event.currentTarget;
		const reviewId = button.getAttribute("data-review-id");
		if (!reviewId) {
			console.error("❌ 리뷰 ID를 찾을 수 없습니다.");
			return;
		}
		const csrfToken = document.querySelector("meta[name='_csrf']").content;
		const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;
		const likeIcon = button.querySelector(".like-icon");
		const likeCountElem = document.getElementById(`like-count-${reviewId}`);
		const isLiked = button.classList.contains("liked");

		// UI 임시 업데이트
		if (isLiked) {
			button.classList.remove("liked");
			likeIcon.textContent = "🤍";
			if (likeCountElem) {
				likeCountElem.textContent = Math.max(0, parseInt(likeCountElem.textContent) - 1);
			}
		} else {
			button.classList.add("liked");
			likeIcon.textContent = "❤️";
			if (likeCountElem) {
				likeCountElem.textContent = parseInt(likeCountElem.textContent) + 1;
			}
		}

		// 서버 요청
		fetch(`/productDetail/review/${reviewId}/like`, {
			method: "POST",
			credentials: "include",
			headers: {
				"Content-Type": "application/json",
				[csrfHeader]: csrfToken
			}
		})
			.then(response => {
				if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
				return response.json();
			})
			.then(data => {
				// 서버 응답 기반 UI 업데이트
				if (data.liked) {
					button.classList.add("liked");
					likeIcon.textContent = "❤️";
				} else {
					button.classList.remove("liked");
					likeIcon.textContent = "🤍";
				}
				if (likeCountElem) {
					likeCountElem.textContent = data.likeCount;
				}
			})
			.catch(error => {
				console.error("🚨 좋아요 처리 중 오류:", error);
			});
	}

	// 리뷰 fetch 및 리뷰 목록, 페이지네이션 업데이트
	function fetchReviews(page) {
	    fetch(`/productDetail/${productId}/reviews?page=${page}`)
	        .then(response => {
	            if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
	            return response.json();
	        })
	        .then(data => {
	            reviewListContainer.innerHTML = "";
	            data.reviews.forEach(review => {
	                // 현재 사용자가 리뷰 작성자인 경우 수정/삭제 버튼 생성
					console.log("currentUser:", currentUser, "review.username:", review.username);
				    let reviewActions = "";
				    if (currentUser && review.username === currentUser) {
				        reviewActions = `
				            <div class="review-actions">
				                <a href="/productDetail/review/${review.id}/edit">
				                    <button type="button">수정</button>
				                </a>
				                <form action="/productDetail/review/${review.id}/delete" method="post">
				                    <button type="submit">삭제</button>
				                </form>
				            </div>
				        `;
				    }
	                const reviewItem = document.createElement("div");
	                reviewItem.classList.add("review-item");
	                reviewItem.innerHTML = `
	                    <div class="review-header">
	                        <span class="review-writer">${review.username}</span>
	                        <small class="review-date">${review.commentedTime}</small>
	                    </div>
	                    <p>${review.content}</p>
	                    <div class="review-footer">
	                        <div class="like-section">
	                            <button type="button" class="like-button ${review.liked ? 'liked' : ''}" data-review-id="${review.id}">
	                                <span class="like-icon">${review.liked ? '❤️' : '🤍'}</span>
	                            </button>
	                            <span class="like-count" id="like-count-${review.id}">${review.likeCount}</span>
	                        </div>
	                        ${reviewActions}
	                    </div>
	                `;
	                reviewListContainer.appendChild(reviewItem);
	            });
	            totalPages = data.totalPages || 1;
	            updatePagination(data.currentPage, totalPages);
	            setupLikeButtons();
	        })
	        .catch(error => console.error("🚨 리뷰 데이터 로드 실패:", error));
	}

	/*function fetchReviews(page) {
		fetch(`/productDetail/${productId}/reviews?page=${page}`)
			.then(response => {
				if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
				return response.json();
			})
			.then(data => {
				reviewListContainer.innerHTML = "";
				data.reviews.forEach(review => {
					const reviewItem = document.createElement("div");
					reviewItem.classList.add("review-item");
					reviewItem.innerHTML = `
                        <div class="review-header">
                            <span class="review-writer">${review.username}</span>
                            <small class="review-date">${review.commentedTime}</small>
                        </div>
                        <p>${review.content}</p>
                        <div class="review-footer">
                            <div class="like-section">
                                <button type="button" class="like-button ${review.liked ? 'liked' : ''}" data-review-id="${review.id}">
                                    <span class="like-icon">${review.liked ? '❤️' : '🤍'}</span>
                                </button>
                                <span class="like-count" id="like-count-${review.id}">${review.likeCount}</span>
                            </div>
                        </div>
                    `;
					reviewListContainer.appendChild(reviewItem);
				});
				// 업데이트된 totalPages 값도 함께 전달되었다고 가정 (없으면 기존 totalPages 사용)
				totalPages = data.totalPages || 1;
				updatePagination(data.currentPage, totalPages);
				setupLikeButtons();
			})
			.catch(error => console.error("🚨 리뷰 데이터 로드 실패:", error));
	}*/

	function updatePagination(page, totalPages) {
		paginationContainer.innerHTML = "";
		totalPages = totalPages || 1;
		let startPage = Math.floor(page / 10) * 10;
		let endPage = Math.min(startPage + 10, totalPages);

		// 이전 페이지 버튼 (전체 페이지가 1 이상일 때만 추가)
		if (totalPages > 1 && startPage > 0) {
			const prevButton = document.createElement("button");
			prevButton.textContent = " < ";
			prevButton.addEventListener("click", () => fetchReviews(startPage - 1));
			paginationContainer.appendChild(prevButton);
		}

		// 페이지 번호 버튼 생성: 항상 최소 1페이지 버튼 생성
		for (let i = startPage; i < endPage; i++) {
			const pageButton = document.createElement("button");
			pageButton.textContent = i + 1;
			pageButton.dataset.page = i;
			if (i === page) {
				pageButton.disabled = true;
			}
			pageButton.addEventListener("click", function() {
				fetchReviews(parseInt(this.dataset.page, 10));
			});
			paginationContainer.appendChild(pageButton);
		}

		// 다음 페이지 버튼 (전체 페이지가 1 이상일 때만 추가)
		if (totalPages > 1 && endPage < totalPages) {
			const nextButton = document.createElement("button");
			nextButton.textContent = " > ";
			nextButton.addEventListener("click", () => fetchReviews(endPage));
			paginationContainer.appendChild(nextButton);
		}
	}

	// 초기 리뷰 로드
	fetchReviews(currentPage);
});

document.addEventListener("DOMContentLoaded", () => {
	const thumbnails = document.querySelectorAll(".thumbnail-image");
	const mainPreview = document.getElementById("main-preview");

	// 최초 메인 이미지 저장
	const initialMainImageSrc = mainPreview.src;
	let currentMainImageSrc = initialMainImageSrc; // 현재 메인 이미지 추적

	thumbnails.forEach((thumbnail) => {
		thumbnail.addEventListener("click", function() {
			let clickedThumbnailSrc = this.src;

			// 클릭한 썸네일이 현재 메인 이미지인 경우, 초기 이미지로 되돌림
			if (clickedThumbnailSrc === currentMainImageSrc) {
				mainPreview.src = initialMainImageSrc;
				currentMainImageSrc = initialMainImageSrc;
			} else {
				// 메인 이미지와 썸네일 이미지를 교체
				mainPreview.src = clickedThumbnailSrc;
				this.src = currentMainImageSrc;
				currentMainImageSrc = clickedThumbnailSrc;
			}
		});
	});

	// 메인 이미지 확대 기능 (돋보기 효과)
	mainPreview.addEventListener("mousemove", function(e) {
		const rect = mainPreview.getBoundingClientRect();
		const x = (e.clientX - rect.left) / rect.width * 100;
		const y = (e.clientY - rect.top) / rect.height * 100;

		mainPreview.style.transformOrigin = `${x}% ${y}%`;
		mainPreview.style.transform = "scale(2)"; // 2배 확대
	});

	mainPreview.addEventListener("mouseleave", function() {
		mainPreview.style.transform = "scale(1)"; // 원래 크기로 복구
	});
});

// 정민 : 테스트중, 상품 상세조회에서 사이즈, 색상 선택 후 구매 버튼 클릭 시 구매화면으로 이동
document.addEventListener("DOMContentLoaded", function() {
	document.querySelector("#buyNowBtn").addEventListener("click", function() {
		let form = document.createElement("form");
		form.method = "POST";
		form.action = "/purchase";

		let selectedItems = [];
		let isValid = true;

		document.querySelectorAll(".selection-item").forEach(item => {
			let colorElement = item.querySelector(".color");
			let sizeElement = item.querySelector(".size");
			let colorId = colorElement ? colorElement.getAttribute("data-color-id") : null;
			let colorName = colorElement ? colorElement.textContent.trim() : "";
			let sizeId = sizeElement ? sizeElement.getAttribute("data-size-id") : null;
			let sizeName = sizeElement ? sizeElement.textContent.trim() : "";
			let priceElement = item.querySelector(".price");
			let quantityElement = item.querySelector(".quantity-input");
			let price = priceElement ? parseInt(priceElement.getAttribute("data-price"), 10) : NaN;
			let quantity = quantityElement ? parseInt(quantityElement.value, 10) : NaN;
			let productDetails = document.querySelector(".product-details");
			let productId = productDetails ? productDetails.getAttribute("data-product-id") : null;
			let productName = productDetails ? productDetails.querySelector("h2").textContent.trim() : "";
			let imageElement = document.querySelector(".product-images .image-preview img");
			let imageSrc = imageElement ? imageElement.getAttribute("src") : "";


			selectedItems.push({
				productId: productId,
				productName: productName,
				price: price,
				quantity: quantity,
				imageSrc: imageSrc,
				colorId: colorId,
				colorName: colorName,
				sizeId: sizeId,
				sizeName: sizeName
			});

			if (!colorId || !colorName || !sizeId || !sizeName || isNaN(price) || isNaN(quantity) || quantity < 1) {
				isValid = false;
				return;
			}
		});

		if (!isValid) {
			alert("색상, 사이즈 및 수량을 정확히 선택해주세요.");
			return;
		}

		if (selectedItems.length === 0) {
			alert("결제할 상품을 선택해주세요.");
			return;
		}

		selectedItems.forEach(item => {
			form.appendChild(createHiddenInput("productId[]", item.productId));
			form.appendChild(createHiddenInput("productName[]", item.productName));
			form.appendChild(createHiddenInput("price[]", item.price));
			form.appendChild(createHiddenInput("quantity[]", item.quantity));
			form.appendChild(createHiddenInput("imageSrc[]", item.imageSrc));
			form.appendChild(createHiddenInput("colorId[]", item.colorId));
			form.appendChild(createHiddenInput("colorName[]", item.colorName));
			form.appendChild(createHiddenInput("sizeId[]", item.sizeId));
			form.appendChild(createHiddenInput("sizeName[]", item.sizeName));
		});

		document.body.appendChild(form);
		form.submit();
	});

	function createHiddenInput(name, value) {
		let input = document.createElement("input");
		input.type = "hidden";
		input.name = name;
		input.value = value;
		return input;
	}

	document.querySelectorAll(".color-button").forEach(button => {
		button.addEventListener("click", () => {
			document.querySelectorAll(".color-button").forEach(btn => btn.classList.remove("selected-color"));
			button.classList.add("selected-color");
		});
	});
});

// 정민 : 장바구니 추가(옵션선택 불가능)
document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("cartAdd").addEventListener("click", function() {
		const productId = document.querySelector(".product-details").getAttribute("data-product-id");
		const productName = document.querySelector(".product-details").children[0].innerHTML;

		$.ajax({
			type: "POST",
			url: "/cart/add",
			contentType: "application/json",
			data: JSON.stringify({
				productId: productId,
				productName: productName,
				quantity: 1
			}),
			success: function(response) {
				if (response === "success") {
					alert("장바구니에 추가되었습니다!");
				} else if (response === "exists") {
					alert("이미 장바구니에 있는 상품입니다!");
				}
			},
			error: function(error) {
				console.error("장바구니 추가 오류:", error);
			}
		});
	});
});

// 정민 : 관심상품 등록
document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("wishAdd").addEventListener("click", function() {
		const productId = document.querySelector(".product-details").getAttribute("data-product-id");
		const productName = document.querySelector(".product-details").children[0].innerHTML;

		$.ajax({
			type: "POST",
			url: "/wishlist/add",
			contentType: "application/json",
			data: JSON.stringify({
				itemNumber: productId,
				itemName: productName,
				itemImage: firstMainImage.src
			}),
			success: function(response) {
				if (response === "success") {
					alert("위시리스트에 추가되었습니다!");
				} else if (response === "exists") {
					alert("이미 위시리스트에 있는 상품입니다!");
				}
			},
			error: function(error) {
				console.error("위시리스트 추가 오류:", error);
			}
		});
	})
})
