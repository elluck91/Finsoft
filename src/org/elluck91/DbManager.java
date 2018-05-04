package org.elluck91;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author root
 */
public class DbManager implements IRepo{


	Connection con ;
	
	public DbManager(){

	}

	@Override
	public int Login(String username, String password) {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","admin");

			PreparedStatement sql = con.prepareStatement("SELECT COUNT(*) FROM users WHERE Username=? AND Password=sha2(?, 256)");
			sql.setString(1, username);
			sql.setString(2, password);


			ResultSet res=  sql.executeQuery();

			res.next();

			return res.getInt(1);

		} catch (SQLException ex) {
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, "ex happened !!!", ex);
			return 0;
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
			return 0;
		}catch(Exception ex){
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			return 0;
		}finally{
			try {
				con.close();
			} catch (SQLException ex) {
				Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

    public void Logout() {
    	
    }
    
    @Override
	public Employee getEmployeeDetails(String employee_id) {
		Employee employee = null;
    	try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees","root","blank");
			String statement = "SELECT employees.emp_no, employees.birth_date, employees.first_name, employees.last_name, employees.hire_date, dept_emp.dept_no, salaries.salary, titles.title, departments.dept_name\r\n" + 
					"FROM employees\r\n" + 
					"INNER JOIN dept_emp ON employees.emp_no = dept_emp.emp_no\r\n" + 
					"INNER JOIN salaries ON employees.emp_no=salaries.emp_no\r\n" + 
					"INNER JOIN titles ON employees.emp_no=titles.emp_no\r\n" + 
					"INNER JOIN departments ON departments.dept_no=dept_emp.dept_no\r\n" + 
					"WHERE employees.emp_no=?\r\n" + 
					"ORDER BY salaries.salary DESC\r\n" + 
					"LIMIT 1";
			PreparedStatement sql = con.prepareStatement(statement);
			sql.setString(1, employee_id);

			ResultSet res = sql.executeQuery();
			while (res.next()) {
			    employee = new Employee(res.getInt("emp_no"),
			    		res.getDate("birth_date"),
			    		res.getString("first_name"),
			    		res.getString("last_name"),
			    		res.getDate("hire_date"),
			    		res.getString("dept_name"),
			    		res.getDouble("salary"),
			    		res.getString("title"));
			}

			
		} catch (SQLException ex) {
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, "ex happened !!!", ex);
			return null;
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}catch(Exception ex){
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			return null;
		}finally{
			try {
				con.close();
			} catch (SQLException ex) {
				Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return employee;
	}
    
	public void updateEmployee(Employee employee) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","blank");
			
			// update employees table
			String statementEmployee = "UPDATE employees SET \r\n" + 
					"birth_date=?\r\n" + 
					"first_name=?\r\n" + 
					"last_name=?\r\n" + 
					"hire_date=? WHERE emp_no=?";
			
			// update dept_emp table
			String getDeptId = "SELECT dept_no FROM departments WHERE dept_name=?";
			PreparedStatement sql = con.prepareStatement(getDeptId);
			sql.setString(1, employee.getDepartment());

			ResultSet res = sql.executeQuery();
			String deptId = null;
			while (res.next()) {
			    deptId = res.getString("dept_no");
			}
			
			String dept_emp = "UPDATE dept_emp SET dept_no=? WHERE emp_no=?";
			
			sql = con.prepareStatement(dept_emp);
			sql.setString(1, deptId);
			sql.setInt(2, employee.getEmployee_number());
			
			sql.execute();
			
			// close last salary
			String closeSalary = "UPDATE salaries set to_date=NOW() WHERE emp_no=? AND salary=?";
			sql = con.prepareStatement(closeSalary);
			sql.setInt(1, employee.getEmployee_number());
			sql.setDouble(2, employee.getLatest_salary());
			sql.execute();
			
			// create new salary
			String newSalary = "INSERT INTO salaries VALUES(?,?,NOW(),?)";
			sql = con.prepareStatement(newSalary);
			sql.setInt(1, employee.getEmployee_number());
			sql.setDouble(2, employee.getLatest_salary());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date tempDate = null;
			try {
				tempDate = format.parse("9999-01-01");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Date to_Date = new java.sql.Date(tempDate.getTime());
			sql.setDate(3, to_Date);

			sql.execute();
			
			// close last position
			String closePosition = "UPDATE titles set to_date=NOW() WHERE emp_no=? AND to_date=?";
			sql = con.prepareStatement(closePosition);
			sql.setInt(1, employee.getEmployee_number());
			sql.setDate(2, to_Date);
			sql.execute();
			
			// create new positions
			// BUT ONLY IF IT CHANGES
			String newPosition = "INSERT INTO titles VALUES(?,?,NOW(),?)";
			sql = con.prepareStatement(newPosition);
			sql.setInt(1, employee.getEmployee_number());
			sql.setString(2, employee.getTitle());
			sql.setDate(3, to_Date);
			sql.execute();
			
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
    public ArrayList<Employee> getAllEmployees() {
		return null;
    	
    }
    
    public ArrayList<Employee> getDepartmentEmployees() {
		return null;
    	
    }
    
    public Employee getMyDetails() {
		return null;
    	
    	
    }
    
    public ArrayList<Employee> getEmployeesByFilter() {
		return null;
    	
    }
	
/*
	public String[] getTransactionList(String username) {
		String[] result = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","admin");
			PreparedStatement sql = con.prepareStatement("SELECT * FROM users where username=?");
			sql.setString(1, username);

			ResultSet res = sql.executeQuery();
			
			while (res.next())
			    result = res.getString("transactions").split(",");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}


	public Transaction getTransactionDetails(String transaction) {
		Transaction result = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","admin");
			PreparedStatement sql = con.prepareStatement("SELECT * FROM transaction where transaction_id=?");
			sql.setString(1, transaction);

			ResultSet res = sql.executeQuery();
			String[] productList;
			Product prod;
			while (res.next()) {
				result = new Transaction(res.getInt("transaction_id"), res.getString("transaction_products"),
						res.getDate("transaction_date"), res.getDouble("transaction_total"));
				productList = res.getString("transaction_products").split(",");
				for (String product : productList) {
					prod = getProduct(product);
					if (prod != null) {
						result.addProduct(prod);
					}		
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}


	public Product getProduct(String product_id) {
		Product product = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","admin");
			PreparedStatement sql = con.prepareStatement("SELECT * FROM product where product_id=?");
			sql.setString(1, product_id);

			ResultSet res = sql.executeQuery();
			
			while (res.next()) {
				product = new Product(res.getString("product_uniquename"), res.getString("product_name"),
						res.getDouble("product_price"), res.getString("product_description"),
						res.getString("product_img"), res.getString("product_category"), res.getInt("product_id"));
				
			}
			
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return product;
	}


	public int insertTransaction(Transaction transaction) {
		int transaction_id = 0;
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","admin");
			PreparedStatement sql = con.prepareStatement("INSERT INTO transaction VALUES(NULL,NOW(),?,?)", Statement.RETURN_GENERATED_KEYS);
			sql.setDouble(1, transaction.getTotalSum());
			sql.setString(2, transaction.getProductListString());
			sql.executeUpdate();
			ResultSet res = sql.getGeneratedKeys();
			if (res.next())
				transaction_id = res.getInt(1);
		} catch (SQLException ex) {
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, "ex happened !!!", ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
		}catch(Exception ex){
			Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}finally{
			try {
				con.close();
			} catch (SQLException ex) {
				Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		return transaction_id;
		
	}

	public ArrayList<Product> getCategoryProducts(String category) {
		ArrayList<Product> productList = new ArrayList<Product>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","admin");
			PreparedStatement sql = con.prepareStatement("SELECT * FROM product where product_category=?");
			sql.setString(1, category);

			ResultSet res = sql.executeQuery();
			Product product;
			while (res.next()) {
				product = new Product(res.getString("product_uniquename"), res.getString("product_name"),
						res.getDouble("product_price"), res.getString("product_description"),
						res.getString("product_img"), res.getString("product_category"), res.getInt("product_id"));
				productList.add(product);
				
			}
			
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return productList;
	}
	
	public ArrayList<Product> productSearch(String productName) {
		//word search 
		ArrayList<Product> searchedProducts = new ArrayList<Product>();
		Product product = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","admin");
			PreparedStatement sql = con.prepareStatement("SELECT * FROM product where product_name = ?");
			sql.setString(1, productName);

			ResultSet res = sql.executeQuery();
			
			while (res.next()) {
				product = new Product(res.getString("product_uniquename"), res.getString("product_name"),
						res.getDouble("product_price"), res.getString("product_description"),
						res.getString("product_img"), res.getString("product_category"), res.getInt("product_id"));
				searchedProducts.add(product);
				
			}
			sql = con.prepareStatement("SELECT * FROM product where product_uniquename like ?");
			sql.setString(1, "%" + productName + "%");

			res = sql.executeQuery();
			
			while (res.next()) {
				product = new Product(res.getString("product_uniquename"), res.getString("product_name"),
						res.getDouble("product_price"), res.getString("product_description"),
						res.getString("product_img"), res.getString("product_category"), res.getInt("product_id"));
				searchedProducts.add(product);
			}
			sql = con.prepareStatement("SELECT * FROM product where product_category = ?");
			sql.setString(1, productName);

			res = sql.executeQuery();
			
			while (res.next()) {
				product = new Product(res.getString("product_uniquename"), res.getString("product_name"),
						res.getDouble("product_price"), res.getString("product_description"),
						res.getString("product_img"), res.getString("product_category"), res.getInt("product_id"));
				searchedProducts.add(product);
			}
				
			
			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Product> resultProducts = new ArrayList<Product>();
		Set<String> titles = new HashSet<String>();

		for( Product p : searchedProducts ) {
			if( titles.add( p.getProduct_uniquename())) {
			resultProducts.add(p);
			}
		}
		
		return resultProducts;
	}
	*/

}
