package model;

import java.util.ArrayList;
import java.util.List;

public class HealthCheckLogic {

    /**
     * 判定結果やグラフ用データを一括でサーブレットに返却するためのデータ保持用クラス
     */
    public static class AssessmentResult {
        public HealthCheckResult currentResult;
        public double bmi;
        public String bmiStatus;
        public String weightTrend;
        public String totalEval;
        public String chartLabels;
        public String chartWeightData;
        public String chartBmiData;
        public double weightMin;
        public double weightMax;
        public double bmiMin;
        public double bmiMax;
    }

    /**
     * 過去の全データリストから、最新の判定およびグラフ用データを生成する職人メソッド
     */
    public AssessmentResult calculateAssessment(List<HealthCheckResult> userResultList) {
        AssessmentResult report = new AssessmentResult();

        // データが空の場合はデフォルト値を詰めて返す（安全ガード）
        if (userResultList == null || userResultList.isEmpty()) {
            report.bmiStatus = "データなし";
            report.weightTrend = "データなし";
            report.totalEval = "-";
            report.chartLabels = "";
            report.chartWeightData = "";
            report.chartBmiData = "";
            return report;
        }

        // 1. 今期（最新）と前期のデータを特定
        HealthCheckResult currentResult = userResultList.get(userResultList.size() - 1);
        HealthCheckResult previousResult = (userResultList.size() >= 2) ? userResultList.get(userResultList.size() - 2) : null;
        report.currentResult = currentResult;

        // 2. BMIの計算
        double height = currentResult.getHeight();
        double weight = currentResult.getWeight();
        double heightM = height / 100.0;
        double bmi = weight / (heightM * heightM);
        report.bmi = bmi;

        // 3. BMI判定
        String bmiStatus;
        if (bmi < 18.5) {
            bmiStatus = "やせ";
        } else if (bmi < 25.0) {
            bmiStatus = "普通体重";
        } else if (bmi < 30.0) {
            bmiStatus = "軽度肥満";
        } else {
            bmiStatus = "重度肥満";
        }
        report.bmiStatus = bmiStatus;

        // 4. 前期からの推移判定
        String weightTrend = "データなし";
        boolean hasTrend = false;
        boolean isWeightDecreased = false;
        boolean isWeightIncreased = false;

        if (previousResult != null) {
            double prevWeight = previousResult.getWeight();
            double changeRate = (weight - prevWeight) / prevWeight;

            if (Math.abs(changeRate) >= 0.10) {
                hasTrend = true;
                if (changeRate < 0) {
                    weightTrend = "減少傾向あり";
                    isWeightDecreased = true;
                } else {
                    weightTrend = "増加傾向あり";
                    isWeightIncreased = true;
                }
            } else {
                weightTrend = "維持されています";
            }

            // 外れ値チェック（30%以上の急激な変化）
            if (Math.abs(changeRate) >= 0.30) {
                hasTrend = false;
            }

            // 直前の期かどうかの判定
            String currentTag = currentResult.getTimeTag();
            String prevTag = previousResult.getTimeTag();
            boolean isJustBefore = false;
            if (currentTag.contains("後期") && prevTag.contains(currentTag.substring(0, 4) + "前期")) {
                isJustBefore = true;
            } else if (currentTag.contains("前期")) {
                try {
                    int prevYear = Integer.parseInt(currentTag.substring(0, 4)) - 1;
                    if (prevTag.contains(prevYear + "年後期")) {
                        isJustBefore = true;
                    }
                } catch (NumberFormatException e) {
                    // パース失敗時は安全のため false のまま
                }
            }

            if (!isJustBefore) {
                weightTrend = weightTrend + "（※" + prevTag + "のデータと比較）";
            }
        } else {
            weightTrend = "データなし";
        }
        report.weightTrend = weightTrend;

        // 5. 外れ値のチェック
        boolean isOutlier = false;
        if (height < 100.0 || height > 250.0 || weight < 30.0 || weight > 200.0) {
            isOutlier = true;
        }
        if (previousResult != null) {
            double prevWeight = previousResult.getWeight();
            double changeRate = (weight - prevWeight) / prevWeight;
            if (Math.abs(changeRate) > 0.30) {
                isOutlier = true;
            }
        }

        // 6. 総合評価の判定
        String totalEval = "A(異常なし)";
        if (isOutlier) {
            totalEval = "X(再検査)";
        } else if ((bmiStatus.equals("やせ") && isWeightDecreased) ||
                   ((bmiStatus.equals("軽度肥満") || bmiStatus.equals("重度肥満")) && isWeightIncreased)) {
            totalEval = "C(要受診)";
        } else if (!bmiStatus.equals("普通体重") || hasTrend) {
            totalEval = "B(経過観察)";
        }
        report.totalEval = totalEval;

        // 7. 📊 Chart.js用のグラフデータ動的生成
        List<String> labels = new ArrayList<>();
        List<String> weights = new ArrayList<>();
        List<String> bmis = new ArrayList<>();

        // 💡 一人一人の最高値・最低値を記録するための臨時の箱
        double minW = Double.MAX_VALUE;
        double maxW = -Double.MAX_VALUE;
        double minB = Double.MAX_VALUE;
        double maxB = -Double.MAX_VALUE;

        for (HealthCheckResult res : userResultList) {
            labels.add("'" + res.getTimeTag() + "'");
            
            double w = res.getWeight();
            weights.add(String.valueOf(w));
            
            // その場で身長と体重からBMIを正しく計算
            double hM = res.getHeight() / 100.0;
            double b = w / (hM * hM);
            bmis.add(String.format(java.util.Locale.US, "%.2f", b));

            // 💡【追加】ループの中で、この人の「過去最高」と「過去最低」を自動で特定していく
            if (w < minW) minW = w;
            if (w > maxW) maxW = w;
            if (b < minB) minB = b;
            if (b > maxB) maxB = b;
        }

        // 紐にガチャンと結合して、サーブレットへ返す箱に入れる
        report.chartLabels = String.join(", ", labels);
        report.chartWeightData = String.join(", ", weights);
        report.chartBmiData = String.join(", ", bmis);

        // 🎯【追加】本人のデータから「体重が上寄り」「BMIが下寄り」になる目盛り幅をオーダーメイドで自動計算する
        // 1. 体重（上半分に配置）：下側の目盛りを大きく広げて、グラフ全体を上へ押し上げる
        report.weightMin = Math.floor(minW - (maxW - minW) - 15); // 下の余白を特大にする
        report.weightMax = Math.ceil(maxW + 3);                  // 上の余白は少しだけ
        
        // 2. BMI（下半分に配置）：目標線(22.0)も考慮しつつ、上側の目盛りを大きく広げて下に沈める
        double targetMinB = Math.min(minB, 22.0);                 // 標準線22.0が隠れないように安全ガード
        report.bmiMin = Math.floor(targetMinB - 1);               // 下の余白は少しだけ
        report.bmiMax = Math.ceil(maxB + (maxB - targetMinB) + 8); // 上の余白を特大にして下に沈める


        return report;
    }
}
