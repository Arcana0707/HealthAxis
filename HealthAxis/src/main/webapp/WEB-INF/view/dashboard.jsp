<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>マイページ | 健診結果Web確認サービス</title>
    <!-- 💡世界のプロが使うグラフライブラリ「Chart.js」をインターネット経由で読み込みます -->
    <script src="https://jsdelivr.net"></script>
    <style>
        body { 
            font-family: sans-serif; 
            background-color: #f5f7fa; 
            padding: 40px; 
            margin: 0;
        }
        .container { 
            max-width: 650px; 
            margin: 0 auto; 
            background: white; 
            padding: 30px; 
            border-radius: 8px; 
            box-shadow: 0 4px 12px rgba(0,0,0,0.1); 
        }
        h2 { 
            color: #333; 
            margin-top: 0; 
            font-size: 22px;
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
            margin-bottom: 25px;
        }
        .info-text { 
            font-size: 13px; 
            color: #666; 
            margin-top: 25px; 
            line-height: 1.6; 
            background-color: #e9ecef;
            padding: 15px;
            border-radius: 4px;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2>健康診断結果（経年推移グラフ）</h2>
        
        <!-- 💡ブラウザ上に折れ線グラフを描くためのキャンバス（額縁）です -->
        <canvas id="pastChart" width="400" height="220"></canvas>

        <!-- 💡あなたが設計した「4年前は別の業者だった」という最高にリアルな背景ストーリーの注記です -->
        <p class="info-text">
            ℹ️ <strong>【表示期間に関するお知らせ】</strong><br>
            本システムでは、現在の健診システム（2024年度一新）とのデータ互換性が保たれている、直近3年間（2023後期〜2026前期：計6回分）の健診結果を時系列推移として表示しています。4年以上前の旧業者のデータは表示対象外となります。
        </p>
    </div>

    <!-- 💡今はテスト開発の骨組み段階なので、JavaScript側にテスト用のダミー数値を直接置いてグラフを描きます -->
    <script>
        const ctx = document.getElementById('pastChart').getContext('2d');
        new Chart(ctx, {
            type: 'line', // 折れ線グラフを指定！
            data: {
                // 横軸：あなたがCSVデータで用意してくれた、完璧な6期分のタイムライン！
                labels: ['2023後期', '2024前期', '2024後期', '2025前期', '2025後期', '2026前期'],
                datasets: [
                    {
                        label: '体重の推移 (kg)',
                        data: [74.5, 75.2, 76.0, 75.8, 76.3, 76.7], // 鈴木大介さんのテストデータをイメージしたダミー数値
                        borderColor: '#007bff',
                        backgroundColor: 'rgba(0, 123, 255, 0.1)',
                        borderWidth: 3,
                        tension: 0.2 // 線の少したわんだ柔らかさ
                    },
                    {
                        label: '身長の推移 (cm)',
                        data: [172.5, 172.5, 172.5, 172.5, 172.5, 172.5], // 身長は変わらないリアルな推移
                        borderColor: '#28a745',
                        backgroundColor: 'rgba(40, 167, 69, 0.05)',
                        borderWidth: 2,
                        tension: 0
                    }
                ]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: false // グラフの縦軸の最小値を0にせず、数値の周辺を拡大して見やすくするプロの技！
                    }
                }
            }
        });
    </script>
</body>
</html>
