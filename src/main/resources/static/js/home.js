document.addEventListener("DOMContentLoaded", function () {
    // APIエンドポイント
    const apiUrl = "/api/skills/chart-data";

    // グラフを描画する関数
    function drawChart(chartData) {
        const ctx = document.getElementById("learningChart").getContext("2d");

        let months = Object.keys(chartData).sort(); // 昇順ソート

        // 左から "先々月", "先月", "今月"に順番に変換
        months = months.slice(-3); // 直近3ヶ月のみ使用

        // ラベル変換（"先々月", "先月", "今月"）
        const monthLabels = ["先々月", "先月", "今月"];

        // カテゴリー別に配色
        const categoryColors = {
            "バックエンド": "rgba(255, 120, 150, 0.6)",
            "フロントエンド": "rgba(255, 200, 100, 0.5)", 
            "インフラ": "rgba(255, 240, 160, 0.7)" 
        };

        // データセットの準備
        const datasets = [];

        Object.keys(categoryColors).forEach(category => {
            datasets.push({
                label: category,
                data: months.map(month => chartData[month]?.[category] || 0), 
                backgroundColor: categoryColors[category],
                borderColor: categoryColors[category].replace("0.7", "1"),
                borderWidth: 1,
            });
        });

        // 最大学習時間を計算（最小100、最大データ値の上限に設定）
        const maxHours = Math.max(100, ...datasets.flatMap(dataset => dataset.data));

        // Chart.jsの設定
        new Chart(ctx, {
            type: "bar",
            data: {
                labels: monthLabels, // 先々月・先月・今月
                datasets: datasets
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        min: 0,
                        max: maxHours,
                        ticks: {
                            font: {
                                size: 14
                            }
                        },
                        grid: {
                            drawBorder: false
                        }
                    },
                    x: {
                        ticks: {
                            font: {
                                size: 14
                            }
                        },
                        grid: {
                            drawBorder: false
                        },
                    }
                },
                plugins: {
                    legend: {
                        position: "top",
                        labels: {
                            font: {
                                size: 14
                            }
                        }
                    },
                    tooltip: {
                        mode: "index",
                        intersect: false
                    }
                }
            }
        });
    }

    // APIからデータを取得してグラフを描画
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            console.log("Chart Data:", data); // デバッグ用
            drawChart(data);
        })
        .catch(error => console.error("Error loading chart data:", error));
});
