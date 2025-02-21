document.addEventListener('DOMContentLoaded', function() {
    const reviewFileInputs = document.querySelectorAll('.file-upload input[type="file"]');
    
    reviewFileInputs.forEach(input => {
        input.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const formData = new FormData();
                formData.append("file", file);
                
                fetch('/api/gcs/upload', {
                    method: 'POST',
                    body: formData
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`서버 오류: ${response.status}`);
                    }
                    return response.text();
                })
                .then(url => {
                    // 예: input id "reviewFile1" -> index "1"
                    const inputId = event.target.id;
                    const index = inputId.replace('reviewFile', '');
                    
                    const hiddenInput = document.getElementById('reviewImageUrl' + index);
                    if (hiddenInput) {
                        hiddenInput.value = url;
                    }
                    
                    const previewImg = document.getElementById('reviewPreview' + index);
                    if (previewImg) {
                        previewImg.src = url;
                        previewImg.style.display = 'block';
                    }
                })
                .catch(error => {
                    console.error('파일 업로드 오류:', error);
                });
            }
        });
    });
});
