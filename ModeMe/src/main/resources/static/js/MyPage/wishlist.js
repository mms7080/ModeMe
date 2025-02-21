document.addEventListener("DOMContentLoaded", function () {
    // 부모 요소에서 이벤트를 델리게이션 방식으로 처리
    document.querySelector(".wishlist_form").addEventListener("click", function(event) {
        // 클릭된 요소가 .wishlist_cart 클래스가 있는 버튼일 경우
        if (event.target && event.target.matches(".wishlist_cart")) {
            alert("장바구니로 이동하였습니다.");
        }
    });
});

function setAction(actionValue) {
        // 'action' hidden input에 클릭된 버튼에 맞는 값 설정
        document.getElementById("action").value = actionValue;
        
        // 폼을 제출
        document.querySelector(".wishlist_form").submit();
    }
	
