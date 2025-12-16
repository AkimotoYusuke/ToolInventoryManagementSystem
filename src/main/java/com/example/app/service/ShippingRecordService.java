package com.example.app.service;

import java.util.List;

import com.example.app.domain.ShippingRecord;

public interface ShippingRecordService {

	List<ShippingRecord> getLimitedShippingRecordListByEmployeeId(int page, int numPerPage, Integer employeeId) throws Exception;
	int getShippingTotalPages(int numPerPage,  Integer employeeId) throws Exception;
	List<ShippingRecord> getLimitedShippingRecordListIsShippingRequest(int page, int numPerPage) throws Exception;
	int getShippingRequestTotalPages(int numPerPage) throws Exception;
	List<ShippingRecord> getShippingRecordListIsShipped() throws Exception;
	ShippingRecord getShippingRecordById(Integer id) throws Exception;
	ShippingRecord getShippingRecordByEmployeeId(Integer employeeId) throws Exception;
	void deleteShippingRecordById(Integer id) throws Exception;
	void addShippingRecord(ShippingRecord shippingRecord) throws Exception;
	void editShippingRecord(ShippingRecord shippingRecord) throws Exception;
	int getTotalPages(int numPerPage) throws Exception;
	List<ShippingRecord> getShippingRecordListIsShippedPerPage(int page, int numPerPage) throws Exception;

}
