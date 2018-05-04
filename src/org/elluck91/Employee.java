package org.elluck91;

import java.sql.Date;

public class Employee {
	private int employee_number;
	private Date birth_date;
	private String first_name;
	private String last_name;
	private Date hire_date;
	private String department;
	private String dept_no;
	private double latest_salary;
	private String title;
	
	public String getDept_no() {
		return dept_no;
	}

	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}

	public int getEmployee_number() {
		return employee_number;
	}

	public void setEmployee_number(int employee_number) {
		this.employee_number = employee_number;
	}

	public Date getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public Date getHire_date() {
		return hire_date;
	}

	public void setHire_date(Date hire_date) {
		this.hire_date = hire_date;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public double getLatest_salary() {
		return latest_salary;
	}

	public void setLatest_salary(double latest_salary) {
		this.latest_salary = latest_salary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Employee(int employee_number, Date birth_date, String first_name, String last_name,
			Date hire_date, String department, double latest_salary, String title) {
		super();
		this.employee_number = employee_number;
		this.birth_date = birth_date;
		this.first_name = first_name;
		this.last_name = last_name;
		this.hire_date = hire_date;
		this.department = department;
		this.latest_salary = latest_salary;
		this.title = title;
	}
}
