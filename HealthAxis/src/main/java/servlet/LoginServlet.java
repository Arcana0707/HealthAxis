package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Employee;
import model.LoginLogic;



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String employeeId = request.getParameter("employeeId");
		String password = request.getParameter("password");
		
		Employee employee = new Employee();
		employee.setId(employeeId);
		employee.setInitialPassword(password);
		
		String csvPath= this.getServletContext().getRealPath("WEB-INF/master_data/employee_master.csv");
		
		LoginLogic loginlogic = new LoginLogic();
		boolean isLogin = loginlogic.execute(employee,csvPath);
		
		if (isLogin) {
			HttpSession session = request.getSession();
			
			session.setAttribute("loginUser",employee);
		
			response.sendRedirect(request.getContextPath()+"/ResultServlet");
		}else {
			request.setAttribute("errorMsg", "従業員番号またはパスワードが間違っています");
			RequestDispatcher dispatcher= request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}
		
	}

}
