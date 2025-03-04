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
    window.location.href = `/mileage?page=${page}`;  // 페이지 이동
}