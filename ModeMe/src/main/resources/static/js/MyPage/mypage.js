function updateMileage() {
    fetch('/mileage/get')  // 마일리지 최신 데이터 가져오기
        .then(response => response.json())
        .then(data => {
            document.getElementById("mileage-display").innerText = data.total_mileage;
        })
        .catch(error => console.error('Error fetching mileage:', error));
}