function previewMainImage(event) {
          const file = event.target.files[0];
          const preview = document.getElementById('mainimage-preview');
          preview.innerHTML = ''; // 기존 미리보기 제거
          if (file) {
              const reader = new FileReader();
              reader.onload = function (e) {
                  const img = document.createElement('img');
                  img.src = e.target.result;
                  img.style.width = '300px';  // 미리보기 이미지 크기 설정
                  img.style.height = 'auto';
                  preview.appendChild(img);
              };
              reader.readAsDataURL(file);
          }
      }


function addColor() {
    const colorList = document.getElementById('color-list');
    const div = document.createElement('div');
    div.className = 'dynamic-list-item';
    div.innerHTML = `
        <input type="text" placeholder="색상을 입력하세요">
        <button onclick="removeItem(this)">삭제</button>
    `;
    colorList.appendChild(div);
}

function addSize() {
    const sizeList = document.getElementById('size-list');
    const div = document.createElement('div');
    div.className = 'dynamic-list-item';
    div.innerHTML = `
        <input type="text" placeholder="사이즈를 입력하세요">
        <button onclick="removeItem(this)">삭제</button>
    `;
    sizeList.appendChild(div);
}

function addImage() {
    const imageList = document.getElementById('image-list');
    const div = document.createElement('div');
    div.className = 'dynamic-list-item';
    div.innerHTML = `
        <input type="file">
        <button onclick="removeItem(this)">삭제</button>
    `;
    imageList.appendChild(div);
}

function previewSubImage(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('image-preview');
    const previewDiv = document.createElement('div');
    
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.style.width = '100px';  // 서브 이미지 크기 설정
            img.style.height = 'auto';
            previewDiv.appendChild(img);
        };
        reader.readAsDataURL(file);
        
        preview.appendChild(previewDiv);
    }
}

function removeItem(button) {
    button.parentElement.remove();
}

