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

        // 임시 UI 업데이트
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

    function updateReviewDates() {
        document.querySelectorAll('.review-date').forEach(el => {
            var ts = el.getAttribute('data-timestamp');
            if (ts) {
                el.textContent = getRelativeTimeIntl(ts);
            }
        });
    }

    document.getElementById('reviewSort').addEventListener('change', function() {
        fetchReviews(0);
    });

    // 리뷰 항목 클릭 시 모달을 열기 위한 이벤트 등록
    function setupReviewItemClick() {
        document.querySelectorAll(".review-item").forEach(item => {
            item.removeEventListener("click", reviewItemClickHandler);
            item.addEventListener("click", reviewItemClickHandler);
        });
    }

    function reviewItemClickHandler(e) {
        // 좋아요 버튼이나 리뷰 액션 영역 클릭 시 이벤트 무시
        if (e.target.closest(".like-button") || e.target.closest(".review-actions")) {
            return;
        }
        const reviewItem = e.currentTarget;
        const reviewId = reviewItem.getAttribute("data-review-id");
        if (reviewId) {
            openReviewModal(reviewId);
        }
    }

    // 리뷰 fetch 및 리뷰 목록, 페이지네이션 업데이트
    function fetchReviews(page) {
        const sortType = document.getElementById('reviewSort').value;
        fetch(`/productDetail/${productId}/reviews?page=${page}&sortType=${sortType}`)
            .then(response => {
                if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
                return response.json();
            })
            .then(data => {
                reviewListContainer.innerHTML = "";
                data.reviews.forEach(review => {
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
                    reviewItem.setAttribute("data-review-id", review.id);
                    reviewItem.innerHTML = `
                        <div class="review-header">
                            <span class="review-writer">${review.username}</span>
                            <small class="review-date" data-timestamp="${review.commentedTime}">${review.commentedTime}</small>
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
                updateReviewDates();
                setupReviewItemClick();
            })
            .catch(error => console.error("🚨 리뷰 데이터 로드 실패:", error));
    }

    function updatePagination(page, totalPages) {
        paginationContainer.innerHTML = "";
        totalPages = totalPages || 1;
        let startPage = Math.floor(page / 10) * 10;
        let endPage = Math.min(startPage + 10, totalPages);

        if (totalPages > 1 && startPage > 0) {
            const prevButton = document.createElement("button");
            prevButton.textContent = " < ";
            prevButton.addEventListener("click", () => fetchReviews(startPage - 1));
            paginationContainer.appendChild(prevButton);
        }

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

// 모달 함수: 리뷰 상세 팝업 열기/닫기
function openReviewModal(reviewId) {
    fetch(`/productDetail/reviewDetails/${reviewId}`)
        .then(response => {
            if (!response.ok) throw new Error("리뷰 상세 데이터 로드 실패");
            return response.json();
        })
        .then(data => {
            document.getElementById("modalUsername").textContent = data.username;
            document.getElementById("modalCommentedTime").textContent = data.commentedTime;
            document.getElementById("modalContent").textContent = data.content;
            
            const modalImagesContainer = document.getElementById("modalReviewImages");
            modalImagesContainer.innerHTML = "";
            if (data.imageUrls && data.imageUrls.length > 0) {
                data.imageUrls.forEach(url => {
                    const img = document.createElement("img");
                    img.src = url;
                    modalImagesContainer.appendChild(img);
                });
            } else {
                modalImagesContainer.innerHTML = "<p>등록된 이미지가 없습니다.</p>";
            }
            
            document.getElementById("reviewModal").style.display = "block";
        })
        .catch(error => {
            console.error("리뷰 상세 데이터 로드 중 오류:", error);
            alert("리뷰 상세 정보를 불러오는데 실패했습니다.");
        });
}

function closeReviewModal() {
    document.getElementById("reviewModal").style.display = "none";
}

document.addEventListener("DOMContentLoaded", function() {
    const modalClose = document.getElementById("modalClose");
    modalClose.addEventListener("click", closeReviewModal);

    window.addEventListener("click", function(event) {
        const modal = document.getElementById("reviewModal");
        if (event.target === modal) {
            closeReviewModal();
        }
    });
});

const rtf = new Intl.RelativeTimeFormat('ko', { numeric: 'auto' });
function getRelativeTimeIntl(timestamp) {
  const now = new Date();
  const reviewDate = new Date(timestamp);
  const diffInSeconds = Math.floor((reviewDate - now) / 1000);

  if (Math.abs(diffInSeconds) < 60) {
    return rtf.format(diffInSeconds, 'second');
  }
  const diffInMinutes = Math.floor(diffInSeconds / 60);
  if (Math.abs(diffInMinutes) < 60) {
    return rtf.format(diffInMinutes, 'minute');
  }
  const diffInHours = Math.floor(diffInMinutes / 60);
  if (Math.abs(diffInHours) < 24) {
    return rtf.format(diffInHours, 'hour');
  }
  const diffInDays = Math.floor(diffInHours / 24);
  if (Math.abs(diffInDays) < 7) {
    return rtf.format(diffInDays, 'day');
  }
  const diffInWeeks = Math.floor(diffInDays / 7);
  if (Math.abs(diffInWeeks) < 4) {
    return rtf.format(diffInWeeks, 'week');
  }
  const diffInMonths = Math.floor(diffInDays / 30);
  if (Math.abs(diffInMonths) < 12) {
    return rtf.format(diffInMonths, 'month');
  }
  const diffInYears = Math.floor(diffInDays / 365);
  return rtf.format(diffInYears, 'year');
}
