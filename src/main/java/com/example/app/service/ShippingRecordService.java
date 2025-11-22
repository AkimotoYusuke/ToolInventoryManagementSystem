package com.example.app.service;

import com.example.app.domain.ShippingRecord;

public interface ShippingRecordService {

//	List<Employee> getEmployeeList() throws Exception;
	ShippingRecord getShippingRecordById(Integer id) throws Exception;
//	Employee getEmployeeByLoginId(String logingId) throws Exception;
//	Employee getEmployeeByLoginIdAndStatusAct(String logingId) throws Exception;
//	void deleteEmployeeById(Integer id) throws Exception;
	void addShippingRecord(ShippingRecord shippingRecord) throws Exception;
//	void editEmployee(Employee employee) throws Exception;
//	boolean isExsitingEmployee(String loginId) throws Exception;
//	List<Employee> getEmployeeListPerPage(int page, int numPerPage) throws Exception;
//    int getTotalPages(int numPerPage) throws Exception;
//    
//  List<AuthorityType> getAuthorityTypeList() throws Exception;

}
