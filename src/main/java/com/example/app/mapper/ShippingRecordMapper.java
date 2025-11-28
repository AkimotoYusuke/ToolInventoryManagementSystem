package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.ShippingRecord;

@Mapper
public interface ShippingRecordMapper {

	List<ShippingRecord> selectAllByEmployeeId(Integer employeeId) throws Exception;
	List<ShippingRecord> selectAllIsShippingRequest() throws Exception;
	List<ShippingRecord> selectAllIsShipped() throws Exception;
	ShippingRecord selectById(Integer id) throws Exception;
	ShippingRecord selectByEmployeeId(Integer employeeId) throws Exception;
	void setDeleteById(Integer id) throws Exception;
	void insert(ShippingRecord shippingRecord) throws Exception;
	void update(ShippingRecord shippingRecord) throws Exception;
	void addShippingRequest(Integer id) throws Exception;
	void addShipped(Integer id) throws Exception;
	void addReturned(Integer id) throws Exception;
	
	long countActive() throws Exception;
	List<ShippingRecord> selectLimitedIsShipped(@Param("offset") int offset, @Param("num") int num) throws Exception;
	
}
