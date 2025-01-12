
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

function removeItem(button) {
    button.parentElement.remove();
}

