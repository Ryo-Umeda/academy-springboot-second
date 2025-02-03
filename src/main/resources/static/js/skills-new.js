document.addEventListener("DOMContentLoaded", function() {
    const skillNameInput = document.getElementById("skillName");
    const learningHoursInput = document.getElementById("learningHours");
    const addSkillBtn = document.getElementById("addSkillBtn");
    const skillNameError = document.getElementById("skillNameError");
    const learningHoursError = document.getElementById("learningHoursError");

    // recordedMonth の補完処理
    const recordedMonthInput = document.querySelector("input[name='recordedMonth']");
    const recordedMonthValue = recordedMonthInput.value;

    // "yyyy-MM" 形式なら "-01" を付加して "yyyy-MM-01" に変換
    if (recordedMonthValue && !recordedMonthValue.includes("-01")) {
        recordedMonthInput.value = recordedMonthValue + "-01";
    }

    // フォーム送信時のバリデーション
    addSkillBtn.addEventListener("click", function(event) {
        let valid = true;
        skillNameError.textContent = "";
        learningHoursError.textContent = "";

        if (!skillNameInput.value.trim()) {
            skillNameError.textContent = "項目名は必ず入力してください";
            valid = false;
        } else if (skillNameInput.value.length > 50) {
            skillNameError.textContent = "項目名は50文字以内で入力してください";
            valid = false;
        }

        if (!learningHoursInput.value.trim()) {
            learningHoursError.textContent = "学習時間は必ず入力してください";
            valid = false;
        } else if (parseInt(learningHoursInput.value) < 0) {
            learningHoursError.textContent = "学習時間は0以上の数字で入力してください";
            valid = false;
        }

        if (!valid) {
            event.preventDefault();
        }
    });

    // 登録成功後のモーダル表示
    const successModal = document.getElementById("successModal");
    const returnToList = document.getElementById("returnToList");

    const urlParams = new URLSearchParams(window.location.search);
    const success = urlParams.get("success");
    const addedSkillName = urlParams.get("addedSkillName");  // URLパラメータから取得
    const addedLearningHours = urlParams.get("addedLearningHours");  // URLパラメータから取得

    if (success === "true") {
        successModal.style.display = "flex";
        document.getElementById("addedSkillName").textContent = addedSkillName || ""; // URLから取得したデータを表示
        document.getElementById("addedLearningHours").textContent = addedLearningHours || ""; // URLから取得したデータを表示
    }

    returnToList.addEventListener("click", function() {
        const recordedMonth = document.querySelector("input[name='recordedMonth']").value;
        window.location.href = `/skills?month=${recordedMonth.substring(0, 7)}`; // URL表示用に"yyyy-MM"形式へ戻す
    });
});
