// Define slide step size
const slideStep = 220; // Image width + gap


// 하트 클릭시 색 변경
document.addEventListener("DOMContentLoaded", () => {
    // 하트 클릭 이벤트
    document.querySelectorAll(".heart-icon").forEach((heart) => {
        heart.addEventListener("click", () => {
            if (heart.classList.contains("filled")) {
                heart.classList.remove("filled");
                heart.classList.add("unfilled");
            } else {
                heart.classList.remove("unfilled");
                heart.classList.add("filled");
            }
        });
    });
});
// 하트 클릭시 색 변경

// 상품 클릭시 상세로 이동
const products = document.getElementsByClassName('product-grid')
for(const p of products){
	p.addEventListener('click', (e)=>{
		if(e.target.tagName == 'IMG' || e.target.tagName == 'P'){
			let pNum = e.target.parentElement.children[0].value
			location.href = "/productDetail/productDetail/" + pNum;
		}
	})
}

// 장바구니에 추가
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".plus-icon").forEach(icon => {
        icon.addEventListener("click", function () {
            const productCard = this.closest(".product-card");
            const productId = productCard.querySelector("input[type='hidden']").value;
			console.log(productId)
            const productName = productCard.querySelector("p:first-of-type").innerText;

            $.ajax({
                type: "GET",
                url: "/cart/add",
                contentType: "application/json",
                data: JSON.stringify({
                    productId: productId,
                    productName: productName,
                    quantity: 1 // 항상 1개 추가
                }),
                success: function (response) {
                    if (response === "success") {
                        alert("장바구니에 추가되었습니다!");
                    }
                },
                error: function (error) {
                    console.error("장바구니 추가 오류:", error);
                }
            });
        });
    });
});
