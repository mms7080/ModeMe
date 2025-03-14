
	
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

// setAction: 버튼 클릭 시 action 값을 설정하고 폼 제출
function setAction(actionValue) {
    var form = event.target.closest("form");  // 클릭된 버튼의 폼을 찾음
    form.querySelector("input[name='action']").value = actionValue;  // 'action' 값을 설정
    
    // 폼을 제출
    form.submit();
}

// 알림을 띄우는 함수
function showAlert(message) {
    if (message) {
        alert(message);  // 메시지가 존재하면 알림창 띄우기
    }
}

// 페이지 로드 시 메시지 표시
window.onload = function() {
    // 서버에서 전달된 message 값을 가져옴
    var message = document.body.getAttribute("data-message"); 

    // 메시지가 존재하면 알림창으로 표시
    if (message && message.trim() !== '') {
        showAlert(message); // 메시지가 존재하면 alert로 띄우기
    }
};

