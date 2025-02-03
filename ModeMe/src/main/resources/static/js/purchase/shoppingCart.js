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
