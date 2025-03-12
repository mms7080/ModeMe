 // 페이지 로드 시, 적립금 정보 업데이트 요청
    window.onload = function() {
        updateMileageInfo();
    };

    function updateMileageInfo() {
        // Ajax로 서버에 POST 요청
        fetch('/update-mileage', {
            method: 'POST', // 또는 GET으로 변경 가능
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({}) // 필요한 데이터가 있다면 여기에 추가
        })
        .then(response => response.json())
        .then(data => {
            // 서버에서 받아온 데이터를 이용해 마이페이지 UI 업데이트
            document.getElementById('totalMileage').textContent = formatCurrency(data.totalMileage);
            document.getElementById('availableMileage').textContent = formatCurrency(data.availableMileage);
            document.getElementById('usedMileage').textContent = formatCurrency(data.usedMileage);
            document.getElementById('totalPrice').textContent = `KRW ${formatCurrency(data.totalPrice)} (${data.count}회)`;
        })
        .catch(error => console.error('Error:', error));
    }

    // 숫자 포맷 함수
    function formatCurrency(value) {
        return value.toLocaleString('ko-KR') + '원';
    }