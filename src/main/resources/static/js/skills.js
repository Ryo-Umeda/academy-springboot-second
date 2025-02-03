// プルダウンで該当月をリロード
document.addEventListener("DOMContentLoaded", function() {

    const monthSelect = document.getElementById("month-select");

    monthSelect.addEventListener("change", function() {
        const selectedMonth = this.value;
        window.location.href = `/skills?month=${selectedMonth}`;
    });
});

// 項目追加ページへ遷移
function navigateToSkillNew(element) {
    const selectedMonth = document.getElementById("month-select").value;
    const categoryId = element.getAttribute("data-category-id");
    const categoryName = element.getAttribute("data-category-name");

    const url = `/skills-new?month=${selectedMonth}&categoryId=${categoryId}&categoryName=${encodeURIComponent(categoryName)}`;
    window.location.href = url;
}
