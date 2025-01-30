document.addEventListener("DOMContentLoaded", function() {
    
    const monthSelect = document.getElementById("month-select");

    monthSelect.addEventListener("change", function() {
        const selectedMonth = this.value;
        window.location.href = `/skills?month=${selectedMonth}`;
    });
});
