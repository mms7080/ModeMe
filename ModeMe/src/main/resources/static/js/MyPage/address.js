// '우편번호 찾기' 버튼을 클릭하면 호출되는 함수
function openDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 우편번호와 주소를 입력란에 채워넣기
            document.getElementById('zip').value = data.zonecode; // 우편번호
            document.getElementById('address').value = data.address; // 주소
        }
    }).open();
}

// 폼 제출 처리 함수 (폼을 서버로 보내기)
function submitAddressForm(event) {
    event.preventDefault();  // 폼 제출 시 새로 고침 방지

    // 폼 데이터 가져오기
    const recipient = document.getElementById("recipient").value;
    const address = document.getElementById("address").value;
    const phone = document.getElementById("phone").value;
    const zip = document.getElementById("zip").value;

    // 콘솔에 출력하여 데이터 확인
    console.log("받는 사람:", recipient);
    console.log("주소:", address);
    console.log("전화번호:", phone);
    console.log("우편번호:", zip);

    // 데이터 전송 코드 추가 가능 (여기서는 알림을 사용)
    alert("배송지가 등록되었습니다.");

    // 팝업 닫기 (필요한 경우)
    closeAddressPopup();
}

// 팝업 열기
function openAddressPopup() {
    const popup = document.getElementById("addressPopup");
    popup.style.display = "flex";  // 팝업을 화면에 표시
}

// 팝업 닫기
function closeAddressPopup() {
    const popup = document.getElementById("addressPopup");
    popup.style.display = "none";  // 팝업을 숨기기
}


// 폼 제출 이벤트 리스너 추가
document.getElementById("addressForm").addEventListener("submit", submitAddressForm);
