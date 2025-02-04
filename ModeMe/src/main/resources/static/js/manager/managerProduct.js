document.addEventListener("DOMContentLoaded", () => {
    // 삭제 버튼 클릭 시 이벤트 처리
    const deleteButtons = document.querySelectorAll('.deleteButton');
    
    deleteButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            // 삭제할 상품의 ID를 가져옵니다.
            const productId = event.target.getAttribute('data-id');
            
            // 사용자에게 삭제 여부 확인
            const confirmDelete = confirm("정말로 이 상품을 삭제하시겠습니까?");
            
            if (confirmDelete) {
                // Ajax를 사용하여 DELETE 요청을 보냅니다.
                fetch(`/manager/deleteProduct/${productId}`, {
                    method: 'DELETE',  // HTTP DELETE 요청
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    if (response.ok) {
                        alert("상품이 삭제되었습니다.");
                        // 삭제 후 페이지를 새로 고침하여 목록을 갱신합니다.
                        location.reload();
                    } else {
                        alert("삭제에 실패했습니다. 다시 시도해주세요.");
                    }
                })
                .catch(error => {
                    console.error('삭제 오류:', error);
                    alert("서버와의 연결에 문제가 발생했습니다.");
                });
            }
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const modifyButton = document.getElementById("modifyButton");

    if (modifyButton) {
        modifyButton.addEventListener("click", function() {
            const productId = this.getAttribute("data-product-id"); // 버튼에 id 속성 추가 필요
            if (productId) {
                window.location.href = `/productDetail/productEdit/${productId}`;
            } else {
                alert("상품 ID를 찾을 수 없습니다.");
            }
        });
    }
});

 