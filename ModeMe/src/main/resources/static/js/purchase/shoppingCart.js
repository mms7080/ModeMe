document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".checkout-btn").addEventListener("click", function () {
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

        $.ajax({
            type: "POST",
            url: "/purchase",
            contentType: "application/json",
            data: JSON.stringify({ items: selectedItems }),
            success: function (response) {
                window.location.href = response; // 결제 페이지 이동
            },
            error: function (error) {
                console.error("Error:", error);
                alert("오류가 발생했습니다.");
            }
        });
    });
});
