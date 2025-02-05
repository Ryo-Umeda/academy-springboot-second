// プルダウンで該当月をリロード
document.addEventListener("DOMContentLoaded", function() {

    const monthSelect = document.getElementById("month-select");

    monthSelect.addEventListener("change", function() {
        const selectedMonth = this.value;
        window.location.href = `/skills?month=${selectedMonth}`;
    });

    // URLパラメータからupdateSuccess or deleteSuccessを取得してモーダル表示
    const urlParams = new URLSearchParams(window.location.search);
    const updateSuccess = urlParams.get("updateSuccess");
    const deleteSuccess = urlParams.get("deleteSuccess");
    const skillName = urlParams.get("skillName");

    if (updateSuccess && skillName) {
        showModal(skillName);
    }

    if (deleteSuccess) {
        const decodedSkillName = decodeURIComponent(deleteSuccess);
        showDeleteModal(deleteSuccess);
    }
});

// 項目追加ページへ遷移
function navigateToSkillNew(element) {
    const selectedMonth = document.getElementById("month-select").value;
    const categoryId = element.getAttribute("data-category-id");
    const categoryName = element.getAttribute("data-category-name");

    const url = `/skills-new?month=${selectedMonth}&categoryId=${categoryId}&categoryName=${encodeURIComponent(categoryName)}`;
    window.location.href = url;
}

// 学習時間保存用モーダル
function showModal(skillName) {
    const modal = document.getElementById("successModal");
    const skillNameElement = document.getElementById("addedSkillName");

    skillNameElement.textContent = skillName;  // 学習スキル名を表示
    modal.style.display = "flex";  // モーダル表示

    // 「編集ページに戻る」ボタンの処理
    document.getElementById("returnToList").addEventListener("click", function () {
        modal.style.display = "none";
        window.location.href = `/skills?month=${month}&categoryId=${categoryId}`;
    });
}

// 削除完了モーダル
function showDeleteModal(skillName) {
    const modal = document.getElementById("deleteModal");
    document.getElementById("deletedSkillName").textContent = skillName;
    modal.style.display = "flex";

    document.getElementById("returnToListAfterDelete").addEventListener("click", function () {
        modal.style.display = "none";
        window.location.href = `/skills?month=${month}&categoryId=${categoryId}`;
    });
}


