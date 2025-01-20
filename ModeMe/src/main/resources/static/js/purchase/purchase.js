// 주소 검색&입력
function openDaumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

			// 각 주소의 노출 규칙에 따라 주소를 조합한다.
			// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
			var addr = ''; // 주소 변수
			var extraAddr = ''; // 참고항목 변수

			//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
			if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
				addr = data.roadAddress;
			} else { // 사용자가 지번 주소를 선택했을 경우(J)
				addr = data.jibunAddress;
			}

			// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
			if (data.userSelectedType === 'R') {
				// 법정동명이 있을 경우 추가한다. (법정리는 제외)
				// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
				if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
					extraAddr += data.bname;
				}
				// 건물명이 있고, 공동주택일 경우 추가한다.
				if (data.buildingName !== '' && data.apartment === 'Y') {
					extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
				}
				// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
				if (extraAddr !== '') {
					extraAddr = ' (' + extraAddr + ')';
				}
				// 조합된 참고항목을 해당 필드에 넣는다.
				document.getElementById("extraaddress").value = extraAddr;

			} else {
				document.getElementById("extraaddress").value = '';
			}

			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			document.getElementById('sample6_postcode').value = data.zonecode;
			document.getElementById("sample6_address").value = addr;
			// 커서를 상세주소 필드로 이동한다.
			document.getElementById("sample6_detailAddress").focus();
		}
	}).open();
}


// 이메일 입력 변경
function handleDomainChange() {
    const emailDomainSelect = document.getElementById("email-domain");
    const customDomainInput = document.getElementById("custom-domain");

    if (emailDomainSelect.value === "custom") {
        customDomainInput.style.display = "block";
        customDomainInput.focus();
    } else {
        customDomainInput.style.display = "none";
    }
}

// 카카오페이 API호출
document.getElementById("payButton").addEventListener("click", function () {
	console.log("ㅎㅇ")
    // Kakao SDK 초기화 (JavaScript 키 사용)
    Kakao.init('DEV581382E3D0577F398E33A1EF7D71AF90A241F'); // 여기에 본인의 JavaScript 키 입력

    // 결제 요청
    Kakao.Pay.request({
        merchant_id: 'YOUR_MERCHANT_ID', // 카카오에서 발급된 가맹점 ID
        product_name: 'Cashmere Half-neck Knitwear', // 상품 이름
        total_amount: 34900, // 결제 금액 (KRW 단위)
        tax_free_amount: 0, // 비과세 금액
        success_url: 'localhost:9999/main', // 성공 시 이동할 URL
        fail_url: 'localhost:9999/error', // 실패 시 이동할 URL
    }).then(function (response) {
        console.log("결제 성공:", response);
        alert("결제가 성공적으로 처리되었습니다!");
    }).catch(function (error) {
        console.error("결제 실패:", error);
        alert("결제가 실패했습니다. 다시 시도해주세요.");
    });
});
