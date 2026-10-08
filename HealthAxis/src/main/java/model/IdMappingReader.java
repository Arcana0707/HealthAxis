package model; // ★確定したパッケージの住所

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 💡【新設】id_mapping.csv（暗号辞書）を一行ずつ読み込み、
 * 従業員番号をキーにしたIdMapping（箱）のMapを生成して返す専門の読込職人です。
 */
public class IdMappingReader {

    /**
     * 暗号辞書CSVファイルを読み込み、従業員番号をキーにしたIdMappingオブジェクトのMapを返します。
     * @param filePath CSVファイルのパス（例: WEB-INF/master_data/id_mapping.csv）
     * @return 従業員番号がキー、IdMappingオブジェクトが値のMap
     */
    public Map<String, IdMapping> readIdMapping(String filePath) {
        // 🔑 従業員番号（String）を鍵にして、その人の6期分の健診ID（IdMapping）を一発で取り出せるマップ
        Map<String, IdMapping> mappingMap = new HashMap<>();

        // お馴染みの「一文字（FileReader）と一行（BufferedReader）のガッチャンコ」
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // 1行目がヘッダー（従業員番号, 2023後期健診ID...）の場合はスキップ
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // カンマ「,」で分割して、0番、1番、2番…の配列にする
                String[] data = line.split(",");

                // 💡ウェブのルール：新設した空箱を作って、セッターで1個ずつ綺麗に詰める！
                IdMapping mapping = new IdMapping();
                
                // カッの中の背番号を指定して、それぞれの期のポケットに流し込みます
                mapping.setEmployeeId(data[0]);    // 0番目：従業員番号（QA22など）
                mapping.setId2023Second(data[1]);  // 1番目：2023後期健診ID
                mapping.setId2024First(data[2]);   // 2番目：2024前期健診ID
                mapping.setId2024Second(data[3]);  // 3番目：2024後期健診ID
                mapping.setId2025First(data[4]);   // 4番目：2025前期健診ID
                mapping.setId2025Second(data[5]);  // 5番目：2025後期健診ID
                mapping.setId2026First(data[6]);   // 6番目：2026前期健診ID
                
                // 🔑 完成した箱を、従業員番号（mapping.getEmployeeId()）を鍵にしてMapに保存する
                mappingMap.put(mapping.getEmployeeId(), mapping);
            }
        } catch (IOException e) {
            System.err.println("マッピングファイルの読み込みエラー: " + e.getMessage());
        }

        // 30人分の暗号辞書データが鍵付きでぎっしり詰まったMapを返す
        return mappingMap;
    }
}

