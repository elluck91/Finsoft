package org.elluck91;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EmployeeAPI
 */
@WebServlet("/EmployeeAPI")
public class EmployeeAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeAPI() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DbManager db = new DbManager();
		Employee employee = db.getEmployeeDetails("10001");
		request.setAttribute("employee", employee);
		RequestDispatcher requestDispatcher; 
		requestDispatcher = request.getRequestDispatcher("/user.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Here");

		int employee_number = Integer.parseInt(request.getParameter("employee_number"));
		System.out.println("Employee nubmer: " + employee_number);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date birth_date = null;
		Date hire_date = null;
		java.util.Date tempDate = null;
		try {
			tempDate = format.parse(request.getParameter("birth_date"));
			birth_date = new java.sql.Date(tempDate.getTime());
			tempDate = format.parse(request.getParameter("hire_date"));
			hire_date = new java.sql.Date(tempDate.getTime());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String first_name = request.getParameter("first_name");
		String last_name = request.getParameter("last_name");
		String department = request.getParameter("department");
		double latest_salary = Double.parseDouble(request.getParameter("latest_salary"));
		String title = request.getParameter("title");
		
		Employee employee = new Employee(employee_number, birth_date,
				first_name, last_name, hire_date, department, latest_salary,
				title);
		DbManager db = new DbManager();
		db.updateEmployee(employee);
	}

}
