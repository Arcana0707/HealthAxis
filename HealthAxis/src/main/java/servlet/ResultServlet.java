package servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Employee;
import model.HealthCheckLogic;
import model.HealthCheckResult;
import model.HealthCheckResultReader;

/*
import model.IdMapping;
import model.IdMappingReader;
*/

@WebServlet("/ResultServlet")
public class ResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Employee emp = (Employee)session.getAttribute("loginUser");
		
		if(emp==null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		
		String empId = emp.getId();
		
		/*
		
		IdMappingReader mappingReader = new IdMappingReader();
		String mappingCsvPath = this.getServletContext().getRealPath("WEB-INF/master_data/id_mapping.csv");
		
		
		Map<String, IdMapping> mappingMap = mappingReader.readIdMapping(mappingCsvPath);
		// ② ログイン中の従業員IDを鍵にして、本人の「IdMapping」の箱をここでガチッと定義する！
		IdMapping mapping = mappingMap.get(empId);
		*/
		
		
		
		
		ServletContext context = this.getServletContext();
		
		 String folderPath = context.getRealPath("/WEB-INF/medical_secure_data");
		 File folder = new File(folderPath);
		 
		 File[] files = folder.listFiles();
		 
		 List<HealthCheckResult> userResultList = new ArrayList<>();
	        
		if(files != null) {
			
			java.util.Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
			
			HealthCheckResultReader reader = new HealthCheckResultReader();
			for(File file : files) {
				if(file.isFile() && file.getName().endsWith(".csv")) {
					String filePath = file.getAbsolutePath();
					
					String timeTag = convertFileNameToPeriod(filePath);
					
					/*
					String targetKensinId = null;
	            if (mapping != null) {
	                if (filePath.contains("2023_second"))  targetKensinId = mapping.getId2023Second();
	                else if (filePath.contains("2024_first"))   targetKensinId = mapping.getId2024First();
	                else if (filePath.contains("2024_second"))  targetKensinId = mapping.getId2024Second();
	                else if (filePath.contains("2025_first"))   targetKensinId = mapping.getId2025First();
	                else if (filePath.contains("2025_second"))  targetKensinId = mapping.getId2025Second();
	                else if (filePath.contains("2026_first"))   targetKensinId = mapping.getId2026First();
	            }
				*/
	             HealthCheckResult result = reader.readHealthCheckResults(filePath, empId, timeTag);
            
            if (result != null) {
                // 💡Readerの内部でsetTimeTag(timeTag)をして返しているので、ここではaddするだけでOKです！
                userResultList.add(result);
            }
				}
			}
			
		}
		
		HealthCheckLogic logic = new HealthCheckLogic();
		HealthCheckLogic.AssessmentResult assessment = logic.calculateAssessment(userResultList);
		
		request.setAttribute("employee", emp);
		request.setAttribute("currentResult", assessment.currentResult);
		request.setAttribute("bmi", assessment.bmi);
		request.setAttribute("bmiStatus", assessment.bmiStatus);
		request.setAttribute("weightTrend", assessment.weightTrend);
		request.setAttribute("totalEval", assessment.totalEval);
		request.setAttribute("chartLabels", assessment.chartLabels);
		request.setAttribute("chartWeightData", assessment.chartWeightData);
		request.setAttribute("chartBmiData", assessment.chartBmiData);
		request.setAttribute("weightMin", assessment.weightMin);
		request.setAttribute("weightMax", assessment.weightMax);
		request.setAttribute("bmiMin", assessment.bmiMin);
		request.setAttribute("bmiMax", assessment.bmiMax);
		
		
		reportRequestAttribute(request, assessment);
        

        // 🎯 最後にJSPへのアクセス案内（フォワード）
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/healthCheckResult.jsp");
        dispatcher.forward(request, response);
    }
	
	private void reportRequestAttribute(HttpServletRequest request, HealthCheckLogic.AssessmentResult assessment) {
		
	}

	
	
	
	private String convertFileNameToPeriod(String filePath) {
        String fileName = new File(filePath).getName();
        String year = "不明な年度";
        String period = "不明な時期";

        for (int i = 0; i <= fileName.length() - 4; i++) {
            String sub = fileName.substring(i, i + 4);
            if (sub.matches("\\d{4}")) {
                year = sub + "年";
                break;
            }
        }

        if (fileName.contains("前期")) {
            period = "前期";
        } else if (fileName.contains("後期")) {
            period = "後期";
        }

        if (year.equals("不明な年度") && period.equals("不明な時期")) {
            return "不明な時期";
        }
        
        return year + period;
    }

	

}
