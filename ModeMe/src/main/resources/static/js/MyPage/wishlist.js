document.addEventListener("DOMContentLoaded", function () {
    // 부모 요소에서 이벤트를 델리게이션 방식으로 처리
    document.querySelector(".wishlist_form").addEventListener("click", function(event) {
        // 클릭된 요소가 .wishlist_cart 클래스가 있는 버튼일 경우
        if (event.target && event.target.matches(".wishlist_cart")) {
            alert("장바구니로 이동하였습니다.");
        }
    });
});

function setAction(actionValue) {
        // 'action' hidden input에 클릭된 버튼에 맞는 값 설정
        document.getElementById("action").value = actionValue;
        
        // 폼을 제출
        document.querySelector(".wishlist_form").submit();
    }
	
let currentPage = 1;
let totalPages = 1; // 서버에서 가져오는 총 페이지 수
let startPage = 1;
let endPage = 5;

function loadPage(page) {
	if (page < 1 || page > totalPages) return;

	// 페이지가 현재 범위의 끝에 도달했을 경우
	if (page > endPage) {
		startPage += 5;  // 페이지 범위를 5씩 증가
		endPage = Math.min(startPage + 4, totalPages);  // 끝 페이지는 5개씩 표시되므로 startPage + 4
	} else if (page < startPage) {
		startPage = Math.max(1, startPage - 5);  // 페이지 범위를 5씩 감소
		endPage = startPage + 4;  // 끝 페이지 계산
	}

	currentPage = page;
	window.location.href = `/wishlist?page=${page}`;  // 페이지 이동
}

if (window.performance && window.performance.navigation.type === window.performance.navigation.TYPE_RELOAD) {
    window.location.href = "/wishlist";  // 새로고침 후 /order로 리디렉션
}

// 알림을 띄우는 함수
        function showAlert(message) {
            if (message) {
                alert(message);  // 메시지가 존재하면 알림창 띄우기
            }
        }

        window.onload = function() {
            var message = /*[[${message}]]*/ '';  // Thymeleaf에서 전달된 메시지
            showAlert(message);  // showAlert 함수 호출
        };