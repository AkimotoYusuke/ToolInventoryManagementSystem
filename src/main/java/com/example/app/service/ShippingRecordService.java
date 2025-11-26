package com.example.app.service;

import java.util.List;

import com.example.app.domain.ShippingRecord;

public interface ShippingRecordService {

	List<ShippingRecord> getShippingRecordListByEmployeeId(Integer employeeId) throws Exception;
	List<ShippingRecord> getShippingRecordListIsShippingRequest() throws Exception;
	List<ShippingRecord> getShippingRecordListIsShipped() throws Exception;
	ShippingRecord getShippingRecordById(Integer id) throws Exception;
	ShippingRecord getShippingRecordByEmployeeId(Integer employeeId) throws Exception;
//	Employee getEmployeeByLoginId(String logingId) throws Exception;
//	Employee getEmployeeByLoginIdAndStatusAct(String logingId) throws Exception;
	void deleteShippingRecordById(Integer id) throws Exception;
	void addShippingRecord(ShippingRecord shippingRecord) throws Exception;
	void editShippingRecord(ShippingRecord shippingRecord) throws Exception;
//	boolean isExsitingEmployee(String loginId) throws Exception;
//	List<Employee> getEmployeeListPerPage(int page, int numPerPage) throws Exception;
//    int getTotalPages(int numPerPage) throws Exception;
//    
//  List<AuthorityType> getAuthorityTypeList() throws Exception;

}
