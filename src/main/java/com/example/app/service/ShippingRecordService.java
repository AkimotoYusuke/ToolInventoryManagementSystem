package com.example.app.service;

import java.util.List;

import com.example.app.domain.ShippingRecord;

public interface ShippingRecordService {

	List<ShippingRecord> getShippingRecordListByEmployeeId(Integer employeeId) throws Exception;
	List<ShippingRecord> getShippingRecordListIsShippingRequest() throws Exception;
	List<ShippingRecord> getShippingRecordListIsShipped() throws Exception;
	ShippingRecord getShippingRecordById(Integer id) throws Exception;
	ShippingRecord getShippingRecordByEmployeeId(Integer employeeId) throws Exception;
	void deleteShippingRecordById(Integer id) throws Exception;
	void addShippingRecord(ShippingRecord shippingRecord) throws Exception;
	void editShippingRecord(ShippingRecord shippingRecord) throws Exception;

}
