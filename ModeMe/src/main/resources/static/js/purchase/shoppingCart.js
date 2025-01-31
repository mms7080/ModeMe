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

        // 폼 생성 후 POST 요청
        let form = document.createElement("form");
        form.method = "POST";
        form.action = "/purchase";
        form.style.display = "none";

        let input = document.createElement("input");
        input.type = "hidden";
        input.name = "items";
        input.value = JSON.stringify(selectedItems);
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();
    });
});
