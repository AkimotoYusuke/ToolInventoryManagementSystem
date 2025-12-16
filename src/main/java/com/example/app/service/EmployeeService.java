package com.example.app.service;

import java.util.List;

import com.example.app.domain.AuthorityType;
import com.example.app.domain.Employee;

public interface EmployeeService {

	Employee getEmployeeById(Integer id) throws Exception;
	Employee getEmployeeByLoginId(String logingId) throws Exception;
	Employee getEmployeeByLoginIdAndStatusAct(String logingId) throws Exception;
	void deleteEmployeeById(Integer id) throws Exception;
	void addEmployee(Employee employee) throws Exception;
	void editEmployee(Employee employee) throws Exception;
	List<Employee> getEmployeeListPerPage(int page, int numPerPage) throws Exception;
    int getTotalPages(int numPerPage) throws Exception;
    
  List<AuthorityType> getAuthorityTypeList() throws Exception;

}
