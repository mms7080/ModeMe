// 검색 옵션 선택 시 숨겨진 입력 필드 값 업데이트
const selectoption = document.getElementsByName('searchselectoption')[0];
const selectinputhidden = document.getElementsByName('searchselect')[0];

selectoption.addEventListener('change', () => {
    selectinputhidden.value = selectoption.value; // 선택된 옵션 값을 숨겨진 필드에 저장
});

let currentPage = 1;
let totalPages = 1; // 서버에서 가져오는 총 페이지 수
let startPage = 1;
let endPage = 5;

// 페이지네이션 업데이트 함수
function updatePagination() {
    startPage = Math.floor((currentPage - 1) / 5) * 5 + 1;
    endPage = Math.min(startPage + 4, totalPages); // 최대 totalPages를 넘지 않게 설정
}

// 특정 페이지 로드 함수
function loadPage(page) {
    if (page < 1 || page > totalPages) return;

    currentPage = page;
    updatePagination();

    window.location.href = `/order?page=${page}`; // 페이지 이동
}

// 그룹 단위 페이지네이션 이동 (이전/다음 그룹 버튼)
function changePageGroup(direction) {
    if (direction === "prev" && startPage > 1) {
        loadPage(startPage - 1);
    } else if (direction === "next" && endPage < totalPages) {
        loadPage(endPage + 1);
    }
}

// 개별 페이지 이동 함수
function goToPage(newPage) {
    if (newPage < 1 || newPage > totalPages) return;

    currentPage = newPage;
    updatePagination();

    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set("page", newPage);

    window.location.href = window.location.pathname + "?" + urlParams.toString();
}

// DOM이 로드된 후 실행
document.addEventListener("DOMContentLoaded", function () {
    totalPages = Number(document.getElementById("totalPages").value); // 서버에서 가져온 총 페이지 수
    currentPage = Number(document.getElementById("currentPage").value); // 현재 페이지
    updatePagination();
});

// 날짜 포맷 변환 함수
function getFormattedDate(date) { // new Date()를 yyyy-mm-dd 포맷으로 변환
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, "0"); // 월(0부터 시작) + 1
    const dd = String(date.getDate()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd}`;
}

// 날짜 범위를 설정하는 함수 (N개월 전 ~ 오늘)
function setDateRange(months) {
    const today = new Date();
    const startDate = new Date();
    startDate.setMonth(startDate.getMonth() - months); // N개월 전으로 설정

    document.getElementById("startdate").value = getFormattedDate(startDate); // 시작 날짜 설정
    document.getElementById("enddate").value = getFormattedDate(today); // 종료 날짜 설정
}

// 날짜 선택 버튼 이벤트 리스너 추가
document.querySelectorAll(".daybutton").forEach(button => {
    button.addEventListener("click", function () {
        const text = this.textContent.trim(); // 버튼 텍스트 가져오기
        const today = new Date();

        switch (text) {
            case "오늘":
                document.getElementById("startdate").value = getFormattedDate(today);
                document.getElementById("enddate").value = getFormattedDate(today);
                break;
            case "1주일":
                const oneWeekAgo = new Date();
                oneWeekAgo.setDate(today.getDate() - 7);
                document.getElementById("startdate").value = getFormattedDate(oneWeekAgo);
                document.getElementById("enddate").value = getFormattedDate(today);
                break;
            case "1개월":
                setDateRange(1);
                break;
            case "3개월":
                setDateRange(3);
                break;
            case "6개월":
                setDateRange(6);
                break;
        }
    });
});

// 페이지 새로고침 시 /order 페이지로 리디렉션
if (window.performance && window.performance.navigation.type === window.performance.navigation.TYPE_RELOAD) {
    window.location.href = "/order";  // 새로고침 후 /order로 리디렉션
}
