document.addEventListener("DOMContentLoaded", function () {
    const fileInput = document.getElementById('image');
    const fileNameSpan = document.getElementById('file-name');
    const errorSpan = document.createElement("div"); // エラーメッセージ表示用
    errorSpan.classList.add("text-danger");
    fileInput.parentNode.appendChild(errorSpan); 

    fileInput.addEventListener('change', function () {
        errorSpan.textContent = ""; 
        fileNameSpan.textContent = ""; 

        if (this.files.length > 0) {
            const file = this.files[0];

            // 5MB (5 * 1024 * 1024 = 5242880 bytes) を超えていないかチェック
            if (file.size > 5242880) {
                errorSpan.textContent = "ファイルサイズが上限を超えています。5MB以下のファイルを選択してください。";
                fileInput.value = ""; 
                return;
            }

            fileNameSpan.textContent = file.name; // ファイル名を表示
        }
    });
});

