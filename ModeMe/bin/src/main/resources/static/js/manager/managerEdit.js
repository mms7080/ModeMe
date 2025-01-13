document.addEventListener("DOMContentLoaded", () => {
    // Initialize the Quill Rich Text Editor
    const editor = new Quill("#rich-text-editor", {
        theme: "snow", // Snow theme for a simple toolbar
        placeholder: "상품 상세 정보를 입력하세요...",
        modules: {
            toolbar: [
                ["bold", "italic", "underline", "strike"], // Text styling options
                ["blockquote", "code-block"], // Quote and code block options
                [{ header: 1 }, { header: 2 }], // Header levels
                [{ list: "ordered" }, { list: "bullet" }], // Ordered and unordered lists
                [{ script: "sub" }, { script: "super" }], // Subscript and superscript
                [{ indent: "-1" }, { indent: "+1" }], // Indentation
                [{ direction: "rtl" }], // Text direction
                [{ size: ["small", false, "large", "huge"] }], // Font size
                [{ header: [1, 2, 3, 4, 5, 6, false] }], // Header levels
                [{ color: [] }, { background: [] }], // Text and background color
                [{ font: [] }], // Font family
                [{ align: [] }], // Text alignment
                ["clean"], // Remove formatting
            ],
        },
    });

    // Function to get editor content in HTML format
    const getEditorContent = () => {
        return editor.root.innerHTML;
    };

    // Function to set editor content (useful for editing existing content)
    const setEditorContent = (htmlContent) => {
        editor.root.innerHTML = htmlContent;
    };

    // Example usage: Save content when a button is clicked
    const saveButton = document.createElement("button");
    saveButton.textContent = "옵션 저장";
    saveButton.addEventListener("click", () => {
        const content = getEditorContent();
        console.log("Saved Content:", content);
        // You can now send `content` to a server or use it as needed
    });

    // Append the save button below the editor (for demonstration)
    const editorContainer = document.querySelector(".RTE-container");
    editorContainer.appendChild(saveButton);
});
