// 구매할 상품 체크 후 결제버튼 클릭 시
document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".checkout-btn").addEventListener("click", function () {
		var form = document.createElement("form");
		form.method = 'POST';
		form.action = "/purchase";
        let selectedItems = [];

        document.querySelectorAll(".product-table tbody tr").forEach(row => {
            let checkbox = row.querySelector("input[type='checkbox']");
            if (checkbox.checked) {
                let productId = row.getAttribute("data-product-id");
                let productName = row.querySelector("td:nth-child(3)").innerText;
                let price = parseInt(row.querySelector("td:nth-child(4)").innerText.replace("KRW ", "").replace(",", ""));
                let quantity = parseInt(row.querySelector("td:nth-child(5) input").value);

                selectedItems.push({
                    productId: productId,
                    productName: productName,
                    price: price,
                    quantity: quantity
                });
            }
        });
        if (selectedItems.length === 0) {
            alert("결제할 상품을 선택해주세요.");
            return;
        }
		
		selectedItems.forEach(function(item, index) {
	        // 각각의 데이터에 대해 hidden input 필드를 생성
	        form.appendChild(createHiddenInput("productId", item.productId));
	        form.appendChild(createHiddenInput("productName", item.productName));
	        form.appendChild(createHiddenInput("price", item.price));
	        form.appendChild(createHiddenInput("quantity", item.quantity));
	    });

	    // 폼을 body에 추가하고, 폼을 제출하여 서버로 전송
	    document.body.appendChild(form);
	    form.submit();
    });
});

function createHiddenInput(name, value) {
    var input = document.createElement("input");
    input.type = "hidden";
    input.name = name;
    input.value = value;
    return input;
}


// 수량 변경시 금액들 변경
document.addEventListener("DOMContentLoaded", function () {
    function formatCurrency(amount) {
        return new Intl.NumberFormat("ko-KR").format(amount) + "원";
    }

    function updateTotal() {
        let totalAmount = 0;
        document.querySelectorAll(".product-table tbody tr").forEach(row => {
            const quantityInput = row.querySelector("input[type='number']");
            const priceText = row.querySelector("td:nth-child(4)").innerText.replace("KRW ", "").replace(",", "");
            const totalCell = row.querySelector("td:nth-child(8)");

            const quantity = parseInt(quantityInput.value, 10);
            const price = parseInt(priceText, 10);
            const total = quantity * price;
            
            totalCell.innerText = formatCurrency(total);
            totalAmount += total;
        });

        // 총 금액 업데이트
        document.querySelector(".summary div:nth-child(1) strong").innerText = formatCurrency(totalAmount);
        document.querySelector(".summary div:nth-child(3) strong").innerText = formatCurrency(totalAmount);
        document.querySelector(".total strong").innerText = formatCurrency(totalAmount);
    }

    // 수량 변경 이벤트
    document.querySelectorAll(".product-table tbody input[type='number']").forEach(input => {
        input.addEventListener("input", function () {
            if (this.value < 1) this.value = 1; // 최소 수량 1개 보장
            updateTotal();
        });
    });

    updateTotal(); // 초기 실행
});


// 전체 선택, 해제
document.addEventListener("DOMContentLoaded", function () {
    const selectAllCheckbox = document.getElementById("select-all");
    const itemCheckboxes = document.querySelectorAll(".item-checkbox");

    // ✅ "전체 선택" 체크박스 클릭 시 모든 체크박스 상태 변경
    selectAllCheckbox.addEventListener("change", function () {
        itemCheckboxes.forEach(checkbox => {
            checkbox.checked = selectAllCheckbox.checked;
        });
    });

    // ✅ 개별 체크박스 클릭 시 "전체 선택" 체크박스 상태 업데이트
    function updateSelectAllState() {
        selectAllCheckbox.checked = [...itemCheckboxes].every(chk => chk.checked);
    }

    itemCheckboxes.forEach(checkbox => {
        checkbox.addEventListener("change", updateSelectAllState);
    });

    // ✅ 체크박스가 있는 셀(td)을 클릭하면 체크박스 선택/해제
    document.querySelectorAll(".select-cell").forEach(selectCell => {
        selectCell.addEventListener("click", function (event) {
            const checkbox = selectCell.querySelector(".item-checkbox");

            // 체크박스가 없으면 종료
            if (!checkbox) return;

            // 체크박스를 직접 클릭한 경우 중복 실행 방지
            if (event.target.tagName === "INPUT") return;

            // 체크 상태 반전
            checkbox.checked = !checkbox.checked;

            // "전체 선택" 체크박스 상태 업데이트
            updateSelectAllState();
        });
    });
});

// 상품 삭제 이벤트
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function () {
            const row = this.closest("tr"); // 현재 행 가져오기
            const productId = row.getAttribute("data-product-id"); // 상품 ID 가져오기
            
            if (!productId) {
                console.error("상품 ID를 찾을 수 없음");
                return;
            }

            if (!confirm("정말로 삭제하시겠습니까?")) return; // 삭제 확인 팝업

            $.ajax({
                type: "POST",
                url: "/cart/delete",
                data: { productId: productId },
                success: function (response) {
                    if (response === "deleted") {
                        alert("상품이 삭제되었습니다.");
                        location.reload(); // 페이지 새로고침
                    } else {
                        alert("상품을 삭제할 수 없습니다.");
                    }
                },
                error: function () {
                    alert("삭제 요청 중 오류가 발생했습니다.");
                }
            });
        });
    });
});

