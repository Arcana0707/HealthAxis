package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeReader {

    /**
     * 従業員マスターCSVを読み込み、IDをキーにしたMapを返します。
     * @param filePath CSVファイルのパス
     * @return 従業員IDがキー、Employeeオブジェクトが値のMap
     */
    public Map<String, Employee> readEmployeeMaster(String filePath) {
        Map<String, Employee> employeeMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // 1行目がヘッダー（従業員番号,氏名,所属,誕生日）の場合はスキップ
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // カンマで分割
                String[] data = line.split(",");
                

                    // あなたが作ってくれたオリジナルのEmployeeコンストラクタにそのままセット！
                    Employee emp = new Employee();
                    emp.setId(data[0]);
                    emp.setName(data[1]);
                    emp.setDept(data[2]);
                    emp.setInitialPassword(data[3]);
                    
                    employeeMap.put(emp.getId(),emp);
                }
            
        } catch (IOException e) {
            System.err.println("従業員マスターの読み込み中にエラーが発生しました: " + e.getMessage());
        }

        return employeeMap;
    }
    
}
