document.addEventListener("DOMContentLoaded", function () {
    // 모든 수정 버튼에 이벤트 리스너 추가
    const updateButtons = document.querySelectorAll(".update-process-btn");

    updateButtons.forEach(button => {
        button.addEventListener("click", function () {
            // 클릭된 버튼의 data-sale-id 속성에서 saleId 가져오기
            const saleId = this.getAttribute("data-sale-id");

            console.log("선택된 saleId:", saleId); // 디버깅 로그

            if (!saleId) {
                console.error("saleId가 정의되지 않았습니다.");
                alert("오류: 주문 ID가 없습니다.");
                return;
            }

            // 해당 버튼이 속한 <tr> 내부의 select 요소 찾기
            const row = this.closest("tr");
            const processElement = row ? row.querySelector(".process-select") : null;

            if (!processElement) {
                console.error(`process-select 요소를 찾을 수 없습니다 (saleId: ${saleId}).`);
                alert(`오류: 주문 상태 입력 요소가 없습니다.`);
                return;
            }

            const newProcess = processElement.value;
            console.log("선택된 주문 상태:", newProcess); // 디버깅 로그

            if (!newProcess) {
                alert("변경할 주문 상태를 선택하세요.");
                return;
            }

            // ✅ Fetch 요청 보내기
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
                location.reload(); // 변경 사항 반영을 위해 새로고침
            })
            .catch(error => {
                console.error("오류 발생:", error);
                alert("주문 상태 업데이트 중 오류가 발생했습니다.");
            });
        });
    });
});

// 모든 삭제 버튼에 이벤트 리스너 추가
    const deleteButtons = document.querySelectorAll(".delete-process-btn");
    deleteButtons.forEach(button => {
        button.addEventListener("click", function () {
            const saleId = this.getAttribute("data-sale-id");

            console.log("삭제할 saleId:", saleId); // 디버깅 로그

            if (!saleId) {
                alert("오류: 주문 ID가 없습니다.");
                return;
            }

            if (!confirm("정말 이 주문을 삭제하시겠습니까?")) {
                return; // 사용자가 취소하면 삭제 중단
            }

            // ✅ Fetch 요청 (주문 삭제)
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