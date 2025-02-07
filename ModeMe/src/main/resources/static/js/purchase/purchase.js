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


// 결제 금액 변경
document.addEventListener("DOMContentLoaded", function () {
    function formatCurrency(amount) {
        return "₩" + new Intl.NumberFormat("ko-KR").format(amount);
    }

    function updatePaymentSummary() {
        let totalAmount = 0;
        let discount = 2000; // 기본 할인 금액 (예제)

        // 상품 가격 합산
        document.querySelectorAll(".product-item").forEach(item => {
            const priceText = item.querySelector(".product-total").innerText.replace("₩", "").replace(/,/g, "");
            const price = parseInt(priceText, 10) || 0;
            totalAmount += price;
        });
		
        // 주문상품 금액 업데이트
        document.getElementById("orderAmount").innerText = formatCurrency(totalAmount);
        
        // 할인 적용
        document.getElementById("discountAmount").innerText = formatCurrency(-discount);
        
        // 최종 결제 금액 업데이트
        let finalPrice = totalAmount - discount;
        document.getElementById("finalAmount").innerText = formatCurrency(finalPrice);
		document.getElementById("payButton").innerText = formatCurrency(finalPrice) + ' 결제하기';
    }

    // 페이지 로드 시 초기 업데이트
    updatePaymentSummary();
});



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



// 네이버는 불가능
// iamport 결제 호출(nice)
document.getElementById("payButton").addEventListener("click", function () {
    const paymentMethod = document.getElementById("paymentMethod").value; // 선택된 결제수단
    const address = document.getElementById("sample6_address").value;
    const addressDetail = document.getElementById("sample6_detailAddress").value;
    const finalPriceText = document.getElementById("finalAmount").innerText;
    const finalPrice = parseInt(finalPriceText.replace(/₩|,/g, ""), 10);

    const char = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let merchantUid = 'TD';
    for (let i = 0; i < 8; i++) {
        const random = Math.floor(Math.random() * char.length);
        merchantUid += char[random];
    }

    let aIdList = items.map(i => i.productId).join(",");
    let itemNameList = items.map(i => i.productName.trim()).join(",");

    if (paymentMethod === "bank-transfer") {
        // ✅ 무통장입금 처리 (결제 요청 없이 바로 AJAX 실행)
        $.ajax({
            type: "get",
            url: "insertPurchase",
            data: {
                aId: aIdList,
                userId: user.id,
                prices: items.map(i => i.price).join(","),
                address: address,
                addressDetail: addressDetail,
                impUid: null, // 무통장입금이므로 impUid 없음
                merchantUid: merchantUid,
                itemname: itemNameList,
                quantities: items.map(i => i.quantity).join(",")
            },
            success: (rsp) => {
                if (rsp === 'success') {
                    alert('주문이 접수되었습니다.');
                    location.href = "/bankTransfer"; // ✅ 무통장입금 성공 시 이동 경로 변경
                }
            },
            error: (rsp) => {
                console.log("AJAX 오류:", rsp);
            }
        });
        return;
    }

    // ✅ 신용카드 등 결제 프로세스 실행
    IMP.init('imp00488067');
    IMP.request_pay({
        pg: 'nice',
        merchant_uid: merchantUid,
        name: '결제',
        amount: finalPrice,
        buyer_email: user.email,
        buyer_name: user.name,
        buyer_tel: user.phone,
    }, function (rsp) {
        if (rsp.success) {
            alert("결제 성공");
            $.ajax({
                type: "get",
                url: "insertPurchase",
                data: {
                    aId: aIdList,
                    userId: user.id,
                    prices: items.map(i => i.price).join(","),
                    address: address,
                    addressDetail: addressDetail,
                    impUid: rsp.imp_uid,
                    merchantUid: rsp.merchant_uid,
                    itemname: itemNameList,
                    quantities: items.map(i => i.quantity).join(",")
                },
                success: (rsp) => {
                    if (rsp === 'success') {
                        alert('결제가 완료되었습니다');
                        location.href = "/order";
                    }
                },
                error: (rsp) => {
                    console.log("AJAX 오류:", rsp);
                }
            });
        } else {
            alert("결제 실패: " + rsp.error_msg);
        }
    });
});



// 카카오페이
document.getElementById("kakaopay").addEventListener("click", function () {
    const address = document.getElementById("sample6_address").value;
    const addressDetail = document.getElementById("sample6_detailAddress").value;
    const finalPriceText = document.getElementById("finalAmount").innerText;
    const finalPrice = parseInt(finalPriceText.replace(/₩|,/g, ""), 10);
    const productElements = document.querySelectorAll(".product-names");
    

	const char = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	let merchantUid = 'TD';
	for (let i = 0; i < 8; i++) {
		const random = Math.floor(Math.random() * char.length);
		merchantUid += char[random]
	}
	
    IMP.init('imp00488067');

    IMP.request_pay({
        pg: 'kakaopay',
        merchant_uid: merchantUid,
        name: '결제',
        amount: finalPrice,
        buyer_email: user.email,
        buyer_name: user.name,
        buyer_tel: user.phone,
    }, function (rsp) {
        if (rsp.success) {
            alert("결제 성공");

            let aIdList = items.map(i => i.productId).join(","); // ✅ 공백 제거
            let itemNameList = items.map(i => i.productName.trim()).join(","); // ✅ trim 추가

			
            $.ajax({
                type: "get",
                url: "insertPurchase",
                data: {
                    aId: aIdList,
                    userId: user.id,
                    prices: items.map(i => i.price).join(","),
                    address: address,
                    addressDetail: addressDetail,
                    impUid: rsp.imp_uid,
                    merchantUid: rsp.merchant_uid,
                    itemname: itemNameList,
					quantities: items.map(i => i.quantity).join(",")
                },
                success: (rsp) => {
                    if (rsp === 'success') {
                        alert('결제가 완료되었습니다');
                        location.href = "/order";
                    }
                },
                error: (rsp) => {
                    console.log("AJAX 오류:", rsp);
                }
            });
        } else {
            alert("결제 실패: " + rsp.error_msg);
        }
    });
});



document.getElementById("tosspay").addEventListener("click", function() {
	const address = document.getElementById("sample6_address").value;
	    const addressDetail = document.getElementById("sample6_detailAddress").value;
	    const finalPriceText = document.getElementById("finalAmount").innerText;
	    const finalPrice = parseInt(finalPriceText.replace(/₩|,/g, ""), 10);
	    const productElements = document.querySelectorAll(".product-names");
	    
		const char = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
		let merchantUid = 'TD';
		for (let i = 0; i < 8; i++) {
			const random = Math.floor(Math.random() * char.length);
			merchantUid += char[random]
		}
		
	    IMP.init('imp00488067');

	    IMP.request_pay({
	        pg: 'uplus',
	        merchant_uid: merchantUid,
	        name: '결제',
	        amount: finalPrice,
	        buyer_email: user.email,
	        buyer_name: user.name,
	        buyer_tel: user.phone,
	    }, function (rsp) {
	        if (rsp.success) {
	            alert("결제 성공");

	            let aIdList = items.map(i => i.productId).join(","); // ✅ 공백 제거
	            let itemNameList = items.map(i => i.productName.trim()).join(","); // ✅ trim 추가

				
	            $.ajax({
	                type: "get",
	                url: "insertPurchase",
	                data: {
	                    aId: aIdList,
	                    userId: user.id,
	                    prices: items.map(i => i.price).join(","),
	                    address: address,
	                    addressDetail: addressDetail,
	                    impUid: rsp.imp_uid,
	                    merchantUid: rsp.merchant_uid,
	                    itemname: itemNameList,
						quantities: items.map(i => i.quantity).join(",")
	                },
	                success: (rsp) => {
	                    if (rsp === 'success') {
	                        alert('결제가 완료되었습니다');
	                        location.href = "/order";
	                    }
	                },
	                error: (rsp) => {
	                    console.log("AJAX 오류:", rsp);
	                }
	            });
	        } else {
	            alert("결제 실패: " + rsp.error_msg);
	        }
	    });
	});

document.getElementById("use-points").addEventListener("input", function() {
    const usedMileage = document.getElementById("use-points").value; // 사용자가 입력한 마일리지 값
    console.log("사용할 마일리지: " + usedMileage); // 콘솔에서 확인

    // 입력값이 비어있지 않으면 요청을 보냄
    if (usedMileage !== "") {
        $.ajax({
            type: "get",  // HTTP 메서드
            url: "/mileage",  // 요청 URL
            data: { usedMileage: usedMileage },  // 전송할 데이터
            success: (rsp) => {
                if (rsp == 'success') {
                    alert('적립 사용 됨');
                    location.href = "/mileage";
                }
            },
            error: (rsp) => {
                console.log(rsp);
            }
        });
    }
});



