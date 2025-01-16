// '우편번호 찾기' 버튼을 클릭하면 호출되는 함수
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
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("extraaddress").value = extraAddr;
            
            } else {
                document.getElementById("extraaddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('zip').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("addressdetail").focus();
        }
    }).open();
}


let isAlertShown = false; // 알림이 표시되었는지 여부를 기록하는 변수

// 폼 제출 처리 함수 (폼을 서버로 보내기)
function submitAddressForm(event) {
    event.preventDefault();  // 폼 제출 시 새로 고침 방지

    // 폼 데이터 가져오기
    const name = document.getElementById("recipient").value;
    const address = document.getElementById("address").value;
    const phone = document.getElementById("phone").value;
    const zip = document.getElementById("zip").value;

    // 콘솔에 출력하여 데이터 확인
    console.log("받는 사람:", recipient);
    console.log("주소:", address);
    console.log("전화번호:", phone);
    console.log("우편번호:", zip);

    // 알림이 이미 울렸는지 확인
    if (!isAlertShown) {
        alert("배송지가 등록되었습니다.");
        isAlertShown = true;  // 알림을 울렸다는 표시
    }

    // 팝업 닫기 (필요한 경우)
    closeAddressPopup();

    // 폼을 실제로 제출
    document.getElementById("addressForm").submit();
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

// 기본 배송지 설정 버튼 클릭 시
    function setDefaultAddress(addressId) {
        $.ajax({
            url: '/address_default',
            method: 'POST',
            data: { addressid: addressId },
            success: function(response) {
                // 기본 배송지 영역만 갱신
                $('#default-address-list').html(response);
            }
        });
    }

	function setDefaultAddress(addressId, name, phone, address) {
	    // 기본 배송지 영역을 찾아서 내용 업데이트
	    document.getElementById("defaultName").innerText = name;
	    document.getElementById("defaultPhone").innerText = phone;
	    document.getElementById("defaultAddress").innerText = address;
	}
