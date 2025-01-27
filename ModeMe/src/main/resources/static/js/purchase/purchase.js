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



// 네이버는 불가능
// iamport 결제 호출(nice)
document.getElementById("payButton").addEventListener("click", function() {
	const address = document.getElementById("sample6_address").value
	const addressDetail = document.getElementById("sample6_detailAddress").value
	const finalPrice = document.getElementById("finalPrice").innerText.slice(1)
	
	//주문번호 생성
	const char = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	let merchantUid = 'TD';
	for (let i = 0; i < 8; i++) {
		const random = Math.floor(Math.random() * char.length);
		merchantUid += char[random]
	}
	IMP.init('imp00488067'); // i'mport 관리자 페이지 -> 내정보 -> 가맹점식별코드                 
	IMP.request_pay({
		pg: 'nice', // 'nice', 'tosspay', 'kakaopay'
		merchant_uid: merchantUid,
		name: '결제',
		amount: finalPrice,
		buyer_email: user.email,
		buyer_name: user.name,
		buyer_tel: user.phone,

	}, function(rsp) {
		if (rsp.success == true) {
			alert("결제 성공")
			$.ajax({
				type: "post",
				url: "insertPurchase",
				data: {aId:aId, userId:user.id, totalPrice:finalPrice,
					address:address, addressDetail:addressDetail,
					 impUid: rsp.imp_uid, merchantUid: rsp.merchant_uid,},
				success: (rsp) => {
					if (rsp == 'success') {
						alert('결제가 완료되었습니다')
						location.href = "/order"
					}
				},
				error: (rsp) => {
					console.log(rsp);
				}
			})
			let msg = '결제가 완료되었습니다.';       
			msg += '고유ID : ' + rsp.imp_uid;
			msg += '상점 거래ID : ' + rsp.merchant_uid;        
			msg += '결제 금액 : ' + rsp.paid_amount;              
		} else {
			let msg = '결제에 실패하였습니다.';
			msg += '에러내용 : ' + rsp.error_msg;
			alert(msg);
		}
	});
});


document.getElementById("kakaopay").addEventListener("click", function() {
	const address = document.getElementById("sample6_address").value
	const addressDetail = document.getElementById("sample6_detailAddress").value
	const finalPrice = document.getElementById("finalPrice").innerText.slice(1)
	const itemName = document.querySelector(".product-details p").innerText;

	//주문번호 생성
	const char = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	let merchantUid = 'TD';
	for (let i = 0; i < 8; i++) {
		const random = Math.floor(Math.random() * char.length);
		merchantUid += char[random]
	}
	IMP.init('imp00488067'); // i'mport 관리자 페이지 -> 내정보 -> 가맹점식별코드                 
	IMP.request_pay({
		pg: 'kakaopay', // 'nice', 'tosspay', 'kakaopay'
		merchant_uid: merchantUid,
		name: '결제',
		amount: finalPrice,
		buyer_email: user.email,
		buyer_name: user.name,
		buyer_tel: user.phone,

	}, function(rsp) {
		if (rsp.success == true) {
			alert("결제 성공")
			$.ajax({
				type: "get",
				url: "insertPurchase",
								data: {
									aId: aId,
									userId: user.id,
									totalPrice: finalPrice,
									address: address,
									addressDetail: addressDetail,
									impUid: rsp.imp_uid,
									merchantUid: rsp.merchant_uid,
									itemname: itemName // itemname 추가
								},
								success: (rsp) => {
									if (rsp == 'success') {
										alert('결제가 완료되었습니다');
										location.href = "/order";
									}
								},
								error: (rsp) => {
									console.log(rsp);
								}
			})
			let msg = '결제가 완료되었습니다.';       
			msg += '고유ID : ' + rsp.imp_uid;
			msg += '상점 거래ID : ' + rsp.merchant_uid;        
			msg += '결제 금액 : ' + rsp.paid_amount;              
		} else {
			let msg = '결제에 실패하였습니다.';
			msg += '에러내용 : ' + rsp.error_msg;
			alert(msg);
		}
	});
});


document.getElementById("tosspay").addEventListener("click", function() {
	const address = document.getElementById("sample6_address").value
	const addressDetail = document.getElementById("sample6_detailAddress").value
	const finalPrice = document.getElementById("finalPrice").innerText.slice(1)
	//주문번호 생성
	const char = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	let merchantUid = 'TD';
	for (let i = 0; i < 8; i++) {
		const random = Math.floor(Math.random() * char.length);
		merchantUid += char[random]
	}
	IMP.init('imp00488067'); // i'mport 관리자 페이지 -> 내정보 -> 가맹점식별코드                 
	IMP.request_pay({
		pg: 'uplus', // 'nice', 'tosspay', 'kakaopay'
		merchant_uid: merchantUid,
		name: '결제',
		amount: finalPrice,
		buyer_email: user.email,
		buyer_name: user.name,
		buyer_tel: user.phone,

	}, function(rsp) {
		if (rsp.success == true) {
			alert("결제 성공")
			$.ajax({
				type: "post",
				url: "insertPurchase",
				data: {aId:aId, userId:user.id, totalPrice:finalPrice,
					address:address, addressDetail:addressDetail,
					 impUid: rsp.imp_uid, merchantUid: rsp.merchant_uid,},
				success: (rsp) => {
					if (rsp == 'success') {
						alert('결제가 완료되었습니다')
						location.href = "/order"
					}
				},
				error: (rsp) => {
					console.log(rsp);
				}
			})
			let msg = '결제가 완료되었습니다.';       
			msg += '고유ID : ' + rsp.imp_uid;
			msg += '상점 거래ID : ' + rsp.merchant_uid;        
			msg += '결제 금액 : ' + rsp.paid_amount;              
		} else {
			let msg = '결제에 실패하였습니다.';
			msg += '에러내용 : ' + rsp.error_msg;
			alert(msg);
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



