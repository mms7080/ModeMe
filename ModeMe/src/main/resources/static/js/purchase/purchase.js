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

// 선택된 라디오 버튼 가져오기(기본배송지, 다른배송지)
function getSelectedPostcode() {
    // ✅ 선택된 라디오 버튼 가져오기
    let selectedAddress = document.querySelector('input[name="addressType"]:checked').id;
    
    // ✅ 선택된 주소 유형에 따라 우편번호 가져오기
    let postcode;
    if (selectedAddress === "defaultAddress") {
        postcode = document.getElementById("original-postcode").value; // 기본 배송지 우편번호
    } else if (selectedAddress === "newAddress") {
        postcode = document.getElementById("sample6_postcode").value; // 다른 배송지 우편번호
    }

    return postcode;
}



// 배송지 버튼
document.addEventListener("DOMContentLoaded", function () {
    const defaultAddressRadio = document.getElementById("defaultAddress");
    const newAddressRadio = document.getElementById("newAddress");
    const defaultAddressFields = document.getElementById("defaultAddressFields");
    const newAddressFields = document.getElementById("newAddressFields");

    // ✅ 기본 배송지 선택 시
    defaultAddressRadio.addEventListener("change", function () {
        if (this.checked) {
            defaultAddressFields.style.display = "block";
            newAddressFields.style.display = "none";
        }
    });

    // ✅ 다른 배송지 선택 시
    newAddressRadio.addEventListener("change", function () {
        if (this.checked) {
            defaultAddressFields.style.display = "none";
            newAddressFields.style.display = "block";
        }
    });
});



// 결제 금액 변경
document.addEventListener("DOMContentLoaded", function () {
    function formatCurrency(amount) {
        return "₩" + new Intl.NumberFormat("ko-KR").format(amount);
    }

    function updatePaymentSummary() {
        let totalAmount = 0;

        // ✅ 상품 가격 합산
        document.querySelectorAll(".product-item").forEach(item => {
            const priceText = item.querySelector(".product-total").innerText.replace("₩", "").replace(/,/g, "");
            const price = parseInt(priceText, 10) || 0;
            totalAmount += price;
        });

        // ✅ 사용자가 입력한 마일리지 값 가져오기
        const usedMileageInput = document.getElementById("use-points");
        let usedMileage = parseInt(usedMileageInput.value.replace(/[^0-9]/g, ""), 10) || 0;

        // ✅ 마일리지가 상품 가격을 초과할 경우 제한
        if (usedMileage > totalAmount) {
            usedMileage = totalAmount;
            usedMileageInput.value = totalAmount; // 입력값 조정
        }

        // ✅ 주문 상품 금액 업데이트
        document.getElementById("orderAmount").innerText = formatCurrency(totalAmount);

        // ✅ 할인 적용 (마일리지 할인)
        document.getElementById("discountAmount").innerText = formatCurrency(-usedMileage);

        // ✅ 최종 결제 금액 업데이트
        let finalPrice = totalAmount - usedMileage;
        document.getElementById("finalAmount").innerText = formatCurrency(finalPrice);
        document.getElementById("payButton").innerText = formatCurrency(finalPrice) + " 결제하기";

        // ✅ 최종 가격이 0원이면 경고 메시지 출력
        if (finalPrice === 0) {
            alert("무통장입금으로 부탁드립니다.");
        }
    }

    // ✅ 마일리지 입력 시 제한 체크
    document.getElementById("use-points").addEventListener("input", function () {
        let usedMileage = parseInt(this.value.replace(/[^0-9]/g, ""), 10) || 0;

        let totalAmount = 0;
        document.querySelectorAll(".product-item").forEach(item => {
            const priceText = item.querySelector(".product-total").innerText.replace("₩", "").replace(/,/g, "");
            totalAmount += parseInt(priceText, 10) || 0;
        });

        // ✅ 사용자가 가진 마일리지보다 크면 제한
        if (usedMileage > availableMileage) {
            alert("사용 가능한 마일리지를 초과했습니다!");
            this.value = availableMileage;
        }

        // ✅ 사용 가능한 마일리지가 상품 총 가격을 초과할 경우 제한
        if (usedMileage > totalAmount) {
            alert("상품 가격을 초과하는 마일리지는 사용할 수 없습니다!");
            this.value = totalAmount;
        }

        updatePaymentSummary();
    });

    // ✅ "전액사용" 버튼 클릭 시 최대한 사용 가능한 마일리지 설정
    document.getElementById("use-all-points").addEventListener("click", function () {
        let totalAmount = 0;
        document.querySelectorAll(".product-item").forEach(item => {
            const priceText = item.querySelector(".product-total").innerText.replace("₩", "").replace(/,/g, "");
            totalAmount += parseInt(priceText, 10) || 0;
        });

        // ✅ 사용 가능한 마일리지와 상품 가격 중 작은 값 사용
        let maxUsableMileage = Math.min(availableMileage, totalAmount);

        document.getElementById("use-points").value = maxUsableMileage;
        updatePaymentSummary();
    });

    // ✅ 마일리지 입력 후 포커스 아웃 시 최종 업데이트
    document.getElementById("use-points").addEventListener("focusout", function () {
        updatePaymentSummary();
    });

    // ✅ 페이지 로드 시 초기 업데이트
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
	let address;
	let addressDetail;
    const paymentMethod = document.getElementById("paymentMethod").value; // 선택된 결제수단
    const finalPriceText = document.getElementById("finalAmount").innerText;
    const finalPrice = parseInt(finalPriceText.replace(/₩|,/g, ""), 10);
	const discount = document.getElementById("use-points").value;
	const selectedAddressType = document.querySelector('input[name="addressType"]:checked').id
	if(selectedAddressType === "newAddress"){
	    address = document.getElementById("sample6_address").value;
	    addressDetail = document.getElementById("sample6_detailAddress").value;
	} else if(selectedAddressType === "defaultAddress"){
	    address = document.getElementById("original-address").value;
	    addressDetail = document.getElementById("original-addressDetail").value;
	}
	
	
	
    const char = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let merchantUid = 'TD';
    for (let i = 0; i < 8; i++) {
        const random = Math.floor(Math.random() * char.length);
        merchantUid += char[random];
    }

    let aIdList = items.map(i => i.productId).join(",");
    let itemNameList = items.map(i => i.productName.trim()).join(",");

    if (paymentMethod === "bank-transfer") {
		
		let postcode = getSelectedPostcode();
		
	    let aIdList = items.map(i => i.productId).join(",");
	    let itemNameList = items.map(i => i.productName.trim()).join(",");
	    let colorIdList = items.map(i => i.colorId).join(",");   // ✅ 색상 ID 추가
	    let colorNameList = items.map(i => i.colorName).join(","); // ✅ 색상명 추가
	    let sizeIdList = items.map(i => i.sizeId).join(",");   // ✅ 사이즈 ID 추가
	    let sizeNameList = items.map(i => i.sizeName).join(","); // ✅ 사이즈명 추가
		
	    $.ajax({
	        type: "get",
	        url: "/insertPurchase",
	        data: {
	            aId: aIdList,
	            userId: user.id,
	            prices: items.map(i => i.price).join(","),
	            address: address,
	            addressDetail: addressDetail,
	            impUid: null,
	            merchantUid: merchantUid,
	            itemname: itemNameList,
	            quantities: items.map(i => i.quantity).join(","),
	            colorIds: colorIdList,   // ✅ 추가
	            colorNames: colorNameList, // ✅ 추가
	            sizeIds: sizeIdList,   // ✅ 추가
	            sizeNames: sizeNameList,  // ✅ 추가
				imageUrls: items.map(i => i.imageUrl).join(",")
				, discount: discount
				, postcode: postcode
	        },
	        success: function (response) {
	            if (response === 'success') {
	                alert('주문이 완료되었습니다.');
	                location.href = "/bankTransfer?merchantUid=" + merchantUid;
	                // 결제 성공 시 /order 페이지로 merchantUid를 전달
//					location.href = "/order?merchantUid=" + merchantUid;
	            }
	        },
	        error: (rsp) => {
	            console.log("AJAX 오류:", rsp);
	        }
	    });
	} else{
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
				let postcode = getSelectedPostcode();
	            $.ajax({
	                type: "get",
	                url: "insertPurchase",
	                data: {
						aId: aIdList,
			            userId: user.id,
			            prices: items.map(i => i.price).join(","),
			            address: address,
			            addressDetail: addressDetail,
			            impUid: null,
			            merchantUid: merchantUid,
			            itemname: itemNameList,
			            quantities: items.map(i => i.quantity).join(","),
			            colorIds: colorIdList,   // ✅ 추가
			            colorNames: colorNameList, // ✅ 추가
			            sizeIds: sizeIdList,   // ✅ 추가
			            sizeNames: sizeNameList,  // ✅ 추가
						imageUrls: items.map(i => i.imageUrl).join(","),
						discount: discount,
						postcode: postcode
	                },
	                success: (rsp) => {
	                    if (rsp === 'success') {
	                        alert('결제가 완료되었습니다');
	                        location.href = "/order";
	                        // 결제 성공 시 /order 페이지로 merchantUid를 전달
	   						 location.href = "/order?merchantUid=" + merchantUid;
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
	}
});



// 카카오페이
document.getElementById("kakaopay").addEventListener("click", function () {
	let address;
	let addressDetail;
    const finalPriceText = document.getElementById("finalAmount").innerText;
    const finalPrice = parseInt(finalPriceText.replace(/₩|,/g, ""), 10);
	const discount = document.getElementById("use-points").value;
	const selectedAddressType = document.querySelector('input[name="addressType"]:checked').id
	if(selectedAddressType === "newAddress"){
	    address = document.getElementById("sample6_address").value;
	    addressDetail = document.getElementById("sample6_detailAddress").value;
	} else if(selectedAddressType === "defaultAddress"){
	    address = document.getElementById("original-address").value;
	    addressDetail = document.getElementById("original-addressDetail").value;
	}

	console.log(address)
    const char = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let merchantUid = 'TD';
    for (let i = 0; i < 8; i++) {
        const random = Math.floor(Math.random() * char.length);
        merchantUid += char[random];
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
			let postcode = getSelectedPostcode();

            let aIdList = items.map(i => i.productId).join(",");
            let itemNameList = items.map(i => i.productName.trim()).join(",");
            let colorIdList = items.map(i => i.colorId).join(",");   // ✅ 색상 ID 추가
            let colorNameList = items.map(i => i.colorName).join(","); // ✅ 색상명 추가
            let sizeIdList = items.map(i => i.sizeId).join(",");   // ✅ 사이즈 ID 추가
            let sizeNameList = items.map(i => i.sizeName).join(","); // ✅ 사이즈명 추가

            $.ajax({
                type: "get",
                url: "/insertPurchase",
                data: {
                    aId: aIdList,
                    userId: user.id,
                    prices: items.map(i => i.price).join(","),
                    address: address,
                    addressDetail: addressDetail,
                    impUid: rsp.imp_uid,
                    merchantUid: rsp.merchant_uid,
                    itemname: itemNameList,
                    quantities: items.map(i => i.quantity).join(","),
                    colorIds: colorIdList,   // ✅ 추가
                    colorNames: colorNameList, // ✅ 추가
                    sizeIds: sizeIdList,   // ✅ 추가
                    sizeNames: sizeNameList,  // ✅ 추가
					imageUrls: items.map(i => i.imageUrl).join(","),
					discount: discount,
					postcode: postcode
                },
                success: (rsp) => {
                    if (rsp === 'success') {
                        alert('결제가 완료되었습니다');
                        location.href = "/order";
                         // 결제 성공 시 /order 페이지로 merchantUid를 전달
   						 location.href = "/order?merchantUid=" + merchantUid;
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
	let address;
    let addressDetail;
    const finalPriceText = document.getElementById("finalAmount").innerText;
    const finalPrice = parseInt(finalPriceText.replace(/₩|,/g, ""), 10);
	const discount = document.getElementById("use-points").value;
	const selectedAddressType = document.querySelector('input[name="addressType"]:checked').id
	if(selectedAddressType === "newAddress"){
	    address = document.getElementById("sample6_address").value;
	    addressDetail = document.getElementById("sample6_detailAddress").value;
	} else if(selectedAddressType === "defaultAddress"){
	    address = document.getElementById("original-address").value;
	    addressDetail = document.getElementById("original-addressDetail").value;
	}
    
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
			let postcode = getSelectedPostcode();

	        let aIdList = items.map(i => i.productId).join(",");
	        let itemNameList = items.map(i => i.productName.trim()).join(",");
	        let colorIdList = items.map(i => i.colorId).join(",");   // ✅ 색상 ID 추가
	        let colorNameList = items.map(i => i.colorName).join(","); // ✅ 색상명 추가
	        let sizeIdList = items.map(i => i.sizeId).join(",");   // ✅ 사이즈 ID 추가
	        let sizeNameList = items.map(i => i.sizeName).join(","); // ✅ 사이즈명 추가

			
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
                    quantities: items.map(i => i.quantity).join(","),
                    colorIds: colorIdList,   // ✅ 추가
                    colorNames: colorNameList, // ✅ 추가
                    sizeIds: sizeIdList,   // ✅ 추가
                    sizeNames: sizeNameList,  // ✅ 추가
					imageUrls: items.map(i => i.imageUrl).join(","),
					discount: discount,
					postcode: postcode
                },
                success: (rsp) => {
                    if (rsp === 'success') {
                        alert('결제가 완료되었습니다');
                        location.href = "/order";
                         // 결제 성공 시 /order 페이지로 merchantUid를 전달
						location.href = "/order?merchantUid=" + merchantUid;
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


// 배송지 입력
document.addEventListener("DOMContentLoaded", function () {
    const defaultAddressRadio = document.getElementById("defaultAddress");
    const newAddressRadio = document.getElementById("newAddress");
    const defaultAddressFields = document.getElementById("defaultAddressFields");
    const newAddressFields = document.getElementById("newAddressFields");
    const addressSelect = document.getElementById("addressSelect"); // 추가 배송지 선택 드롭다운

    // 페이지 로드 시 기본 배송지 자동 채우기
    defaultAddressFields.style.display = "block";
    newAddressFields.style.display = "none";

    // 기본 배송지 선택 시
    defaultAddressRadio.addEventListener("change", function () {
        if (this.checked) {
            defaultAddressFields.style.display = "block";
            newAddressFields.style.display = "none";
        }
    });

    // 다른 배송지 선택 시
    newAddressRadio.addEventListener("change", function () {
        if (this.checked) {
            defaultAddressFields.style.display = "none";
            newAddressFields.style.display = "block";
            fetchAddresses(); // 배송지 목록 가져오기
        }
    });

    // 추가 배송지 목록 불러오기 (AJAX 요청)
	function fetchAddresses() {
	    fetch("/getAdditionalAddresses") 
	        .then(response => response.json())
	        .then(data => {
	            addressSelect.innerHTML = '<option value="">배송지를 선택하세요</option>';
	            data.forEach(address => {
	                let fullAddress = address.address; // ✅ 전체 주소 (우편번호 포함)

	                // ✅ 첫 번째 공백 이후 문자열 가져오기 (우편번호 제거)
	                let formattedAddress = fullAddress.substring(fullAddress.indexOf(" ") + 1);

	                let option = document.createElement("option");
	                option.value = address.addressid;
	                option.textContent = formattedAddress; // ✅ '기본주소 (엑스트라주소)' 형태로 표시
	                addressSelect.appendChild(option);
	            });
	        })
	        .catch(error => console.error("추가 배송지 불러오기 오류:", error));
	}


    // 추가 배송지 선택 시 자동 입력
    addressSelect.addEventListener("change", function () {
		let selectedId = this.value;
	    if (!selectedId) return;

	    fetch(`/getAddressById?id=${selectedId}`)
	        .then(response => response.json())
	        .then(address => {
	            let fullAddress = address.address;
				
	            // ✅ 우편번호 추출 (첫 번째 공백 전까지)
	            let postcode = fullAddress.split(" ")[0];

	            // ✅ 기본주소 + 엑스트라 주소 추출 (첫 번째 공백 이후부터)
	            let mainAddress = (fullAddress.substring(fullAddress.indexOf(" ") + 1)).split(")")[0];

	            document.getElementById("sample6_postcode").value = postcode;
	            document.getElementById("sample6_address").value = mainAddress;
	            document.getElementById("sample6_detailAddress").value = (fullAddress.substring(fullAddress.indexOf(" ") + 1)).split(")")[1]; // 상세주소 초기화
	        })
	        .catch(error => console.error("주소 불러오기 오류:", error));
    });
});


