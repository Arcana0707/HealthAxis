package model;

import java.util.Map;

public class LoginLogic {
	
	public boolean execute(Employee employee, String csvPath) {
	    if(employee.getId()==null|| employee.getId().isEmpty()) {return false;}
	    if(employee.getInitialPassword()==null|| employee.getInitialPassword().isEmpty()) {return false;}
		    
	    EmployeeReader reader = new EmployeeReader();
	    Map<String, Employee> employeeMap = reader.readEmployeeMaster(csvPath);
	    
	    // 2. 入力されたIDの社員が、名簿に存在するかチェック
	    String inputId = employee.getId();
	    if (employeeMap.containsKey(inputId)) {
	        
	        // 名簿から、名前や所属が「すべて詰まった完全なEmployeeデータ」を取り出す
	        Employee masterEmp = employeeMap.get(inputId);
	        
	        // 3. パスワードが一致するかチェック
	        if (employee.getInitialPassword().equals(masterEmp.getInitialPassword())) {
	            
	            // 4. ✨【超重要】サーブレット側に名前や所属を引き継ぐため、
	            // 入力用カバン(employee)に、名簿から取った名前と所属をガチャンと合体させる！
	            employee.setName(masterEmp.getName());
	            employee.setDept(masterEmp.getDept());
	            /*
	            IdMappingReader mappingReader = new IdMappingReader();
	            Map<String,IdMapping> mappingMap = mappingReader.readIdMapping(mappingCsvPath);
	            
	            if(mappingMap.containsKey(inputId)) {
	            	employee.setIdMapping(mappingMap.get(inputId));
	            }
	            */
	            return true; // ログイン成功！
	        }
	    }
		return false;
	}
}
