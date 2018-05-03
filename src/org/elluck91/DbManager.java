package org.elluck91;

import java.sql.Connection;
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
			ResultSetMetaData rsmd = res.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (res.next()) {
			    for (int i = 1; i <= columnsNumber; i++) {
			        if (i > 1) System.out.print(",  ");
			        String columnValue = res.getString(i);
			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
			    }
			    System.out.println("");
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
		return null;
	}
    
	public void updateEmployee() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost/employees","root","admin");
			PreparedStatement sql = con.prepareStatement("SELECT transactions FROM users where username=?");

			//sql.setString(1, username);
			String transactionList = "";
			ResultSet res = sql.executeQuery();
			
			while (res.next()) {
			    transactionList += res.getString("transactions");
				
			}
			//transactionList += transaction_id + ",";
			sql = con.prepareStatement("UPDATE users SET transactions=? WHERE username=?");
			sql.setString(1, transactionList);
			//sql.setString(2, username);
			
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
