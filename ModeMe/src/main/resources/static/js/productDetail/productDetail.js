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
		thumbnail.addEventListener("click", function() {
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
document.addEventListener("DOMContentLoaded", function() {
	const reviewListContainer = document.querySelector(".review-list");
	const paginationContainer = document.querySelector(".pagination");

	if (!paginationContainer || !reviewListContainer) {
		console.error("🚨 페이지네이션 또는 리뷰 리스트 컨테이너를 찾을 수 없습니다.");
		return;
	}

	let totalPages = parseInt(paginationContainer.dataset.totalPages, 10) || 1;
	let currentPage = parseInt(paginationContainer.dataset.currentPage, 10) || 0;
	let productId = paginationContainer.dataset.productId;

	function fetchReviews(page) {
		fetch(`/productDetail/${productId}/reviews?page=${page}`)
			.then(response => {
				if (!response.ok) {
					throw new Error(`서버 응답 오류: ${response.status}`);
				}
				return response.json();
			})
			.then(data => {
				// 기존 리뷰 리스트 초기화
				reviewListContainer.innerHTML = "";

				data.reviews.forEach(review => {
					const reviewItem = document.createElement("div");
					reviewItem.classList.add("review-item");
					reviewItem.innerHTML = `
                   <p>${review.content}</p>
                   <small>${review.commentedTime}</small>

                   <!-- 좋아요 버튼 -->
                   <div class="like-section">
                       <button type="button" class="like-button ${review.liked ? 'liked' : ''}" data-review-id="${review.id}">
                           <span class="like-text">좋아요</span>
                       </button>
                       <span class="like-count" id="like-count-${review.id}">${review.likeCount}</span>
                   </div>
               `;
					reviewListContainer.appendChild(reviewItem);
				});

				// 페이지네이션 업데이트
				updatePagination(data.currentPage);

				// 새로운 리뷰에 대해 좋아요 버튼 이벤트 리스너 다시 설정
				setupLikeButtons();
			})
			.catch(error => console.error("🚨 리뷰 데이터 로드 실패:", error));
	}

	function updatePagination(page) {
		paginationContainer.innerHTML = ""; // 기존 버튼 초기화
		let startPage = Math.floor(page / 10) * 10;
		let endPage = Math.min(startPage + 10, totalPages);

		// 이전 페이지 버튼
		if (startPage > 0) {
			let prevButton = document.createElement("button");
			prevButton.textContent = " < ";
			prevButton.addEventListener("click", () => fetchReviews(startPage - 1));
			paginationContainer.appendChild(prevButton);
		}

		// 페이지 버튼 생성
		for (let i = startPage; i < endPage; i++) {
			let pageButton = document.createElement("button");
			pageButton.textContent = i + 1;
			pageButton.dataset.page = i;

			if (i === page) {
				pageButton.disabled = true; // 현재 페이지 버튼 비활성화
			}

			pageButton.addEventListener("click", function() {
				fetchReviews(parseInt(this.dataset.page, 10));
			});

			paginationContainer.appendChild(pageButton);
		}

		// 다음 페이지 버튼
		if (endPage < totalPages) {
			let nextButton = document.createElement("button");
			nextButton.textContent = " > ";
			nextButton.addEventListener("click", () => fetchReviews(endPage));
			paginationContainer.appendChild(nextButton);
		}
	}

	// 좋아요 로직
	document.addEventListener("DOMContentLoaded", function() {
		document.querySelectorAll(".like-button").forEach(button => {
			button.addEventListener("click", function() {
				toggleLike(this);
			});
		});
	});

	function toggleLike(button) {
		const reviewId = button.getAttribute("data-review-id");
		console.log("좋아요 클릭됨! reviewId:", reviewId); // 로그 추가 (디버깅용)
		if (!reviewId) {
			console.error("리뷰 ID를 찾을 수 없습니다.");
			return;
		}

		const csrfToken = document.querySelector("meta[name='_csrf']").content;
		const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;

		fetch(`/productDetail/review/${reviewId}/like`, {
			method: "POST",
			credentials: "include",
			headers: {
				"Content-Type": "application/json",
				[csrfHeader]: csrfToken
			}
		})
			.then(response => {
				if (!response.ok) {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				return response.json();
			})
			.then(data => {
				if (data.liked) {
					button.classList.add("liked");
				} else {
					button.classList.remove("liked");
				}

				// 좋아요 개수 업데이트
				const likeCountElem = document.getElementById(`like-count-${reviewId}`);
				if (likeCountElem) {
					likeCountElem.textContent = data.likeCount;
				}
			})
			.catch(error => console.error("좋아요 토글 에러:", error));
	}

	updatePagination(currentPage);
});

// 좋아요 로직

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".like-button").forEach(button => {
        button.addEventListener("click", function () {
            let reviewId = this.dataset.reviewId;
            
            // 🔹 버튼이 클릭되었을 때 reviewId 확인
            console.log(`👍 좋아요 요청 - 리뷰 ID: ${reviewId}`);

            fetch(`/productDetail/review/${reviewId}/like`, {
                method: "POST",
                credentials: "include"
            })
            .then(response => {
                console.log(`📌 서버 응답 상태 코드: ${response.status}`);
                return response.json();
            })
            .then(data => {
                console.log("🔥 좋아요 응답 데이터:", data);

                if (data.liked) {
                    this.classList.add("liked");
                } else {
                    this.classList.remove("liked");
                }

                // 🔹 좋아요 개수 업데이트
                let likeCountElem = document.querySelector(`#like-count-${reviewId}`);
                
                if (!likeCountElem) {
                    console.warn(`🚨 like-count-${reviewId} 요소를 찾을 수 없습니다. 요소를 다시 로드합니다.`);
                    
                    // 🔹 동적으로 다시 요소를 찾음
                    setTimeout(() => {
						console.log("🔥 모든 like-count 요소:", document.querySelectorAll("[id^='like-count-']"));

                        likeCountElem = document.querySelector(`#like-count-${reviewId}`);
                        if (likeCountElem) {
                            likeCountElem.textContent = data.likeCount;
                        } else {
                            console.error(`⛔ 여전히 like-count-${reviewId} 요소를 찾을 수 없습니다.`);
                        }
                    }, 500);
                } else {
                    likeCountElem.textContent = data.likeCount;
                }
            })
            .catch(error => console.error("🚨 좋아요 토글 에러:", error));
        });
    });
});
    document.addEventListener("DOMContentLoaded", () => {
    const thumbnails = document.querySelectorAll(".thumbnail-image");
    const mainPreview = document.getElementById("main-preview");

    // 최초 메인 이미지 저장
    const initialMainImageSrc = mainPreview.src;
    let currentMainImageSrc = initialMainImageSrc; // 현재 메인 이미지 추적

    thumbnails.forEach((thumbnail) => {
        thumbnail.addEventListener("click", function () {
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
    mainPreview.addEventListener("mousemove", function (e) {
        const rect = mainPreview.getBoundingClientRect();
        const x = (e.clientX - rect.left) / rect.width * 100;
        const y = (e.clientY - rect.top) / rect.height * 100;

        mainPreview.style.transformOrigin = `${x}% ${y}%`;
        mainPreview.style.transform = "scale(2)"; // 2배 확대
    });

    mainPreview.addEventListener("mouseleave", function () {
        mainPreview.style.transform = "scale(1)"; // 원래 크기로 복구
    });
});



/*document.addEventListener("DOMContentLoaded", function () {
    // 좋아요 버튼에 대한 이벤트 리스너 추가
    document.querySelectorAll(".like-button").forEach(button => {
		console.log(`버튼 ID: ${button.dataset.reviewId}`);
        button.addEventListener("click", function () {
            let reviewId = this.dataset.reviewId;
            
            // 🔹 클릭한 리뷰 ID를 확인
            console.log(`👍 좋아요 요청 - 리뷰 ID: ${reviewId}`);

            fetch(`/productDetail/review/${reviewId}/like`, {
                method: "POST",
                credentials: "include"
            })
                .then(response => {
                    console.log(`📌 서버 응답 상태 코드: ${response.status}`);
                    return response.json();
                })
                .then(data => {
                    console.log("🔥 좋아요 응답 데이터:", data);

                    if (data.liked) {
                        this.classList.add("liked");
                    } else {
                        this.classList.remove("liked");
                    }

                    // 🔹 좋아요 개수 요소 찾기
                    let likeCountElem = document.querySelector(`#like-count-${reviewId}`);
                    
                    if (!likeCountElem) {
                        console.warn(`🚨 like-count-${reviewId} 요소를 찾을 수 없습니다.`);
                    } else {
                        likeCountElem.textContent = data.likeCount;
                    }
                })
                .catch(error => console.error("🚨 좋아요 토글 에러:", error));
        });
    });
});*/










