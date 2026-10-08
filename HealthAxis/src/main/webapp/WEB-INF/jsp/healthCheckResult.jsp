<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 💡 サーブレットから渡されたJavaオブジェクトをインポートします --%>
<%@ page import="model.Employee" %>
<%@ page import="model.HealthCheckResult" %>
<%
    // サーブレットから属性（Attribute）を安全に引き出します
	
    Employee emp = (Employee) request.getAttribute("employee");
    HealthCheckResult cur = (HealthCheckResult) request.getAttribute("currentResult");
    Double bmi = (Double) request.getAttribute("bmi");
    String bmiStatus = (String) request.getAttribute("bmiStatus");
    String weightTrend = (String) request.getAttribute("weightTrend");
    String totalEval = (String) request.getAttribute("totalEval");

    // Chart.js用のカンマ区切り文字列を取得
    String chartLabels = (String) request.getAttribute("chartLabels");
    String chartWeightData = (String) request.getAttribute("chartWeightData");
    String chartBmiData = (String) request.getAttribute("chartBmiData");
	Double weightMin = (Double) request.getAttribute("weightMin");
    Double weightMax = (Double) request.getAttribute("weightMax");
    Double bmiMin = (Double) request.getAttribute("bmiMin");
    Double bmiMax = (Double) request.getAttribute("bmiMax");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= cur.getTimeTag() %> 健診結果 | 健診結果Web確認サービス</title>
    <!-- 💡世界のプロが使うグラフライブラリ「Chart.js」をCDN経由で読み込みます -->
    <script src=https://cdn.jsdelivr.net/npm/chart.js></script>
    <style>
        body { 
            font-family: sans-serif; 
            background-color: #f5f7fa; 
            padding: 40px; 
            margin: 0;
        }
        .container { 
            max-width: 700px; 
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
            margin-bottom: 20px;
        }
        .profile-box {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 20px;
            font-size: 14px;
            border-left: 5px solid #6c757d;
        }
        .result-grid {
            display: flex;
            justify-content: space-between;
            margin-bottom: 25px;
        }
        .result-card {
            width: 48%;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            padding: 15px;
            background: #fff;
            box-sizing: border-box;
        }
        .result-card h3 {
            margin-top: 0;
            font-size: 16px;
            color: #495057;
            border-bottom: 1px solid #eee;
            padding-bottom: 5px;
        }
        .result-card p { margin: 10px 0; font-size: 14px; }
        .eval-highlight {
            font-size: 20px;
            font-weight: bold;
            color: #007bff;
            text-align: center;
            margin-top: 15px;
            padding: 10px;
            background: #e7f1ff;
            border-radius: 4px;
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
        <!-- 💡1番上に「いつのデータか」を見出しとして配置して引き締めます -->
        <h2><%= cur.getTimeTag() %>　健康診断結果</h2>
		
		<form action="${pageContext.request.contextPath}/LogoutServlet" method="POST" style="text-align: right; margin-bottom: 15px;">
            <button type="submit" style="background-color: #dc3545; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; font-weight: bold;">
                ログアウト
            </button>
        </form>
        
        <!-- 従業員マスター情報 -->
        <div class="profile-box">
            <strong>従業員番号：</strong> <%= emp.getId() %> &nbsp;|&nbsp;
            <strong>所属部課：</strong> <%= emp.getDept() %> &nbsp;|&nbsp;
            <strong>氏名：</strong> <%= emp.getName() %> 様
        </div>

        <!-- 測定数値とアセスメントの2枚カード配置 -->
        <div class="result-grid">
			<div class="result-card">
                <h3>［身体測定数値］</h3>
                <p>・身長： <strong><%= cur.getHeight() %></strong> cm</p>
                <p>・体重： <strong><%= cur.getWeight() %></strong> kg</p>
                <p>・BMI ： <strong><%= String.format("%.2f", bmi) %></strong> [<%= bmiStatus %>]</p>
            </div>
			
			
            <div class="result-card">
                <h3>［総合判定］</h3>
                <p>・前期からの推移：<br><strong><%= weightTrend %></strong></p>
				
				<%
                    String cleanEval = (totalEval != null) ? totalEval.toString().trim() : "";
                    String tooltipText = "判定の解説がありません。";
                    String evalStyle = "background: #e7f1ff; color: #007bff;";
                
                    if (cleanEval.contains("A")) {
                        tooltipText = "【A: 異常なし】 現在の良好な健康状態を維持してください。このままの生活習慣を続けましょう。";
                    } else if (cleanEval.contains("C")) {
                        tooltipText = "【C: 要受診】 生活習慣の改善、または医療機関での再検査をお勧めします。担当の産業医への相談も可能です。";
                        evalStyle = "background: #fff3cd; color: #856404; border: 1px solid #ffeeba;";
                    } else if (cleanEval.contains("X")) {
                        tooltipText = "【X: 要再検査】 測定値又は入力値に一般的にはあり得ない異常数値（テスト限界値）を検知しました。正しい数値での再測定、またはかかりつけ医、担当の産業医へご相談ください。";
                        evalStyle = "background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; font-weight: bold;";
                    }
                %>
				
				<div class="eval-highlight" style="<%= evalStyle %>" title="<%= tooltipText %>">
                    総合評価：<%= totalEval %>
                </div>
            </div>
        </div>

        <h2>経年推移グラフ（直近3年間）</h2>
        <!-- 💡ブラウザ上に折れ線グラフを描くためのキャンバス（額縁）です -->
        <canvas id="pastChart" width="400" height="220"></canvas>

        <p class="info-text">
            ℹ️ <strong>【表示期間に関するお知らせ】</strong><br>
            本システムでは、現在の健診システム（2024年度一新）とのデータ互換性が保たれている、直近3年間（2023後期〜2026前期：計6回分）の健診結果を時系列推移として表示しています。4年以上前の旧業者のデータは表示対象外となります。
        </p>
    </div>

	<script src=https://cdn.jsdelivr.net/npm/chart.js></script>
	<script>
	    const ctx = document.getElementById('pastChart').getContext('2d');
	    new Chart(ctx, {
	        type: 'line', // 折れ線グラフを指定
	        data: {
	            // 横轴：Java側から渡された完璧な時系列のタイムラインを配列化！
	            labels: [<%= chartLabels %>],
	            datasets: [
	                {
	                    label: '体重の推移 (kg)',
	                    // 💡 サーブレットがCSVから計算した本物の体重推移配列
	                    data: [<%= chartWeightData %>], 
	                    borderColor: '#007bff',
	                    backgroundColor: 'rgba(0, 123, 255, 0.1)',
	                    borderWidth: 3,
	                    tension: 0.2,
	                    yAxisID: 'yWeight' // ⭕️ 大文字「A」に修正し、左目盛りを指定
	                },
	                {
	                    label: 'BMIの推移',
	                    // 💡 サーブレットがCSVから計算した本物のBMI推移配列
	                    data: [<%= chartBmiData %>], 
	                    borderColor: '#28a745',
	                    backgroundColor: 'rgba(40, 167, 69, 0.05)',
	                    borderWidth: 2,
	                    tension: 0,
	                    yAxisID: 'yBmi' // ⭕️ 大文字「A」に修正し、右目盛りの「yBmi」を指定
	                },
	                {
	                    label: '目標・標準BMI (22.0)',
	                    // データの件数分だけ「22.0」を並べた配列を自動で作る
	                    data: new Array([<%= chartBmiData %>].length).fill(22.0), 
	                    borderColor: '#dc3545',       // 目立つ赤色
	                    borderDash:[5,5],           // 点線にする設定
	                    borderWidth: 1.5,
	                    pointRadius: 0,               // 線だけの表示にするため点を消す
	                    fill: false,
	                    yAxisID: 'yBmi'               // BMIの右目盛りを使用
	                }
	            ]
	        },
			options: {
			                responsive: true,
			                scales: {
			                    yWeight: {
			                        type: 'linear',
			                        position: 'left',
			                        title: { display: true, text: '体重 (kg)' },
			                        // 💡【変更】Javaが本人のデータから自動計算した数値をそのまま流し込む！
			                        min: <%= weightMin %>,
			                        max: <%= weightMax %>,
			                        beginAtZero: false
			                    },
			                    yBmi: {
			                        type: 'linear',
			                        position: 'right',
			                        title: { display: true, text: 'BMI' },
			                        // 💡【変更】Javaが本人のデータから自動計算した数値をそのまま流し込む！
			                        min: <%= bmiMin %>,
			                        max: <%= bmiMax %>,
			                        beginAtZero: false,
			                        grid: { drawOnChartArea: false } 
			                    }
			                }
			            }// 👈 options を閉じる
	    });
	</script>

</body>
</html>
