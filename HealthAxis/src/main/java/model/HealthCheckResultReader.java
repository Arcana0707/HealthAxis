package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HealthCheckResultReader {
    
    // 💡健診会社のCSVには従業員マップは不要なので、引数はシンプルに「filePath」だけにします
	public HealthCheckResult readHealthCheckResults(String filePath, String empId ,String timeTag) {
        
		if (empId == null || empId.isEmpty() || timeTag == null || timeTag.isEmpty()) {
            return null;
        }
		

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // 1行目がヘッダー（従業員番号/健診ID,体重,身長）の場合はスキップ
                if (isHeader) {
                    isHeader = false; 
                    continue;
                }

                // カンマで分割
                String[] data = line.split(",");

                
                
                if (data.length < 4) {
                    continue;
                }
                
                /*
                // 🎯 ここを書き足してください！
                String csvKensinId = data[0].trim(); 
                
                if (csvKensinId.equals(targetKensinId)) {
                    // (Beanにデータを詰める処理...)
                 */
                
                String csvEmpId = data[0].trim(); 
                if (csvEmpId.equals(empId)) {
                
                // 💡【バグの修正】：空の箱を作って、セッターで1個ずつ丁寧に詰める！（ウェブのルール）
                HealthCheckResult result = new HealthCheckResult();
                
                
                // 1番目：氏名（※今はテスト開発なので、ビーンズに名前ポケットがなければスルーでOK）
                // 2番目、3番目：体重と身長を数値に変換（パース）してセット
                result.setWeight(Double.parseDouble(data[2]));
                result.setHeight(Double.parseDouble(data[3]));
                
                result.setTimeTag(timeTag);
                
                return result;
                }
            }
        } catch (IOException e) {
            System.err.println("健康診断結果の読み込みエラー: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("数値のパースエラー(CSVの列順を確認してください): " + e.getMessage());
        }

        return null;
    }

    /**
     * ファイル名から自動判定して「〇〇〇〇年〇期」を作るメソッド（元のままで完璧です！）
     */
    
}



	