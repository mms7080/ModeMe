document.addEventListener("DOMContentLoaded", function () {
    // ✅ 주문 상태 클릭 시 해당 상태의 주문 목록 검색
    const statusFilters = document.querySelectorAll(".infoBox");

    statusFilters.forEach(filter => {
        filter.addEventListener("click", function () {
            const statusText = this.querySelector("h3").innerText.trim(); // 클릭한 상태 텍스트 가져오기
            let processStatus = "";

            // 한국어 주문 상태를 내부 상태 값으로 변환
            switch (statusText) {
                case "⏰ 입금 전":
                    processStatus = "입금전";
                    break;
                case "📦 배송준비 중":
                    processStatus = "배송준비중";
                    break;
                case "🚛 배송 중":
                    processStatus = "배송중";
                    break;
                case "✅ 배송완료":
                    processStatus = "배송완료";
                    break;
                default:
                    return; // 알 수 없는 상태면 실행 X
            }

            console.log("필터링할 주문 상태:", processStatus);

            // 페이지 이동 (주문 상태 검색 적용)
            window.location.href = `/manager/ManagerSale?searchOption=process&keyword=${processStatus}`;
        });
    });

    // ✅ 주문 상태 변경 버튼 이벤트
    const updateButtons = document.querySelectorAll(".update-process-btn");

    updateButtons.forEach(button => {
        button.addEventListener("click", function () {
            const saleId = this.getAttribute("data-sale-id");

            if (!saleId) {
                alert("오류: 주문 ID가 없습니다.");
                return;
            }

            // 상태 선택값 가져오기
            const row = this.closest("tr");
            const processElement = row ? row.querySelector(".process-select") : null;

            if (!processElement) {
                alert("오류: 주문 상태를 선택하세요.");
                return;
            }

            const newProcess = processElement.value;

            if (!newProcess) {
                alert("변경할 주문 상태를 선택하세요.");
                return;
            }

            // ✅ 주문 상태 변경 요청
            fetch(`/manager/ManagerSale/${saleId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ newProcess: newProcess })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error("서버 요청 실패: " + response.status);
                }
                return response.text();
            })
            .then(data => {
                alert(data); // 성공 메시지 출력
                location.reload(); // 변경 사항 반영
            })
            .catch(error => {
                console.error("오류 발생:", error);
                alert("주문 상태 업데이트 중 오류가 발생했습니다.");
            });
        });
    });

    // ✅ 주문 삭제 버튼 이벤트
    const deleteButtons = document.querySelectorAll(".delete-process-btn");

    deleteButtons.forEach(button => {
        button.addEventListener("click", function () {
            const saleId = this.getAttribute("data-sale-id");

            if (!saleId) {
                alert("오류: 주문 ID가 없습니다.");
                return;
            }

            if (!confirm("정말 이 주문을 삭제하시겠습니까?")) {
                return; // 사용자가 취소하면 삭제 중단
            }

            // ✅ 주문 삭제 요청
            fetch(`/manager/ManagerSale/${saleId}`, {
                method: "DELETE"
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error("서버 요청 실패: " + response.status);
                }
                return response.text();
            })
            .then(data => {
                alert(data);
                location.reload();
            })
            .catch(error => {
                console.error("오류 발생:", error);
                alert("주문 삭제 중 오류가 발생했습니다.");
            });
        });
    });
});
