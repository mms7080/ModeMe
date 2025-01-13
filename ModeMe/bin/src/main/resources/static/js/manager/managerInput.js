// Add new size input
function addSize() {
    const sizeList = document.getElementById("size-list");
    const div = document.createElement("div");
    div.className = "dynamic-list-item";
    div.innerHTML = `
        <input type="text" placeholder="사이즈를 입력하세요">
        <button onclick="removeItem(this)">삭제</button>
    `;
    sizeList.appendChild(div);
}

// Add new image input
function addImage() {
    const imageList = document.getElementById("image-list");
    const div = document.createElement("div");
    div.className = "dynamic-list-item";
    div.innerHTML = `
        <input type="file" accept="image/*">
        <button onclick="removeItem(this)">삭제</button>
    `;
    imageList.appendChild(div);
}

// Remove any item from the dynamic list
function removeItem(button) {
    button.parentElement.remove();
}
