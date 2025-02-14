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
document.addEventListener('DOMContentLoaded', () => {
    function addClickEventToProducts(selector) {
        const products = document.querySelectorAll(selector);
        products.forEach(p => {
            p.addEventListener('click', (e) => {
                if (e.target.tagName === 'IMG' || e.target.tagName === 'P') {
                    let pNum = e.target.parentElement.children[0].value;
                    location.href = "/productDetail/productDetail/" + pNum;
                }
            });
        });
    }
    
    addClickEventToProducts('.swiper-slide');
    addClickEventToProducts('.product-card');
});

// 장바구니에 추가
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".plus-icon").forEach(icon => {
        icon.addEventListener("click", function () {
            const productCard = this.closest(".product-card");
            const productId = productCard.querySelector("input[type='hidden']").value;
            const productName = productCard.querySelector("p:first-of-type").innerText;

            $.ajax({
                type: "POST",
                url: "/cart/add",
                contentType: "application/json",
                data: JSON.stringify({
                    productId: productId,
                    productName: productName,
                    quantity: 1
                }),
                success: function (response) {
                    if (response === "success") {
                        alert("장바구니에 추가되었습니다!");
                    } else if (response === "exists") {
                        alert("이미 장바구니에 있는 상품입니다!");
                    }
                },
                error: function (error) {
                    console.error("장바구니 추가 오류:", error);
                }
            });
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".heart-icon").forEach(icon => {
        icon.addEventListener("click", function () {
            const productCard = this.closest(".product-card"); // 상품 카드 요소 찾기
            const itemNumber = productCard.querySelector("input[type='hidden']").value; // 상품 ID
            const itemName = productCard.querySelector("p:first-of-type").innerText; // 상품 이름
            const itemImage = productCard.querySelector(".product-image").src; // 상품 이미지

            $.ajax({
                type: "POST",
                url: "/wishlist/add",
                contentType: "application/json",
                data: JSON.stringify({
                    itemNumber: itemNumber,
                    itemName: itemName,
                    itemImage: itemImage
                }),
                success: function (response) {
                    if (response === "success") {
                        alert("위시리스트에 추가되었습니다!");
                    } else if (response === "exists") {
                        alert("이미 위시리스트에 있는 상품입니다!");
                    }
                },
                error: function (error) {
                    console.error("위시리스트 추가 오류:", error);
                }
            });
        });
    });
});
