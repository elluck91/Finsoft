/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.elluck91;

import java.util.ArrayList;

/**
 *
 * @author elluck91
 */
public interface IRepo {
    
     int Login(String username,String password);
     void Logout();
     void updateEmployee(Employee employee);
     ArrayList<Employee> getAllEmployees();
     ArrayList<Employee> getDepartmentEmployees();
     Employee getMyDetails();
     ArrayList<Employee> getEmployeesByFilter();
     Employee getEmployeeDetails(String employee_id);
     
}
