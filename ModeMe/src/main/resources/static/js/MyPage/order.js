const selectoption = document.getElementsByName('searchselectoption')[0];
const selectinputhidden = document.getElementsByName('searchselect')[0];

selectoption.addEventListener('change',()=>{
	selectinputhidden.value = selectoption.value;
});

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
    window.location.href = `/order?page=${page}`;  // 페이지 이동
}

function goToPage(newPage) {
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('page', newPage); // 기존 URL의 page 값만 변경

    window.location.href = window.location.pathname + "?" + urlParams.toString(); // 전체 URL로 이동
}

document.addEventListener("DOMContentLoaded", function () {
    const startDateInput = document.getElementById("startdate");
    const endDateInput = document.getElementById("enddate");

    function getFormattedDate(date) { //newDate로 가져오면 UTC(협정 세계시) 기준이여서 yyyy-mm-dd 포맷 직접 생성
        const yyyy = date.getFullYear();
        const mm = String(date.getMonth() + 1).padStart(2, "0"); // 월(0부터 시작) + 1
        const dd = String(date.getDate()).padStart(2, "0");
        return `${yyyy}-${mm}-${dd}`;
    }

    function setDateRange(months) {
        const today = new Date();
        const startDate = new Date();
        startDate.setMonth(startDate.getMonth() - months); // N개월 전으로 설정

        startDateInput.value = getFormattedDate(startDate);
        endDateInput.value = getFormattedDate(today);
    }

    document.querySelectorAll(".daybutton").forEach(button => {
        button.addEventListener("click", function () {
            const text = this.textContent.trim(); // 버튼 텍스트 읽기
            const today = new Date();

            switch (text) {
                case "오늘":
                    startDateInput.value = getFormattedDate(today);
                    endDateInput.value = getFormattedDate(today);
                    break;
                case "1주일":
                    const oneWeekAgo = new Date();
                    oneWeekAgo.setDate(today.getDate() - 7);
                    startDateInput.value = getFormattedDate(oneWeekAgo);
                    endDateInput.value = getFormattedDate(today);
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
});


if (window.performance && window.performance.navigation.type === window.performance.navigation.TYPE_RELOAD) {
    window.location.href = "/order";  // 새로고침 후 /order로 리디렉션
}