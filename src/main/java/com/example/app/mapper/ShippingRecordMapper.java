package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.ShippingRecord;

@Mapper
public interface ShippingRecordMapper {

	List<ShippingRecord> selectAllIsShippingRequest() throws Exception;
	List<ShippingRecord> selectAllByEmployeeId(Integer employeeId) throws Exception;
	ShippingRecord selectById(Integer id) throws Exception;
	ShippingRecord selectByEmployeeId(Integer employeeId) throws Exception;
//	Employee selectByLoginId(String loginId) throws Exception;
//	Employee selectByLoginIdAndStatusAct(String loginId) throws Exception;
	void setDeleteById(Integer id) throws Exception;
	void insert(ShippingRecord shippingRecord) throws Exception;
	void update(ShippingRecord shippingRecord) throws Exception;
	void addShippingRequest(Integer id) throws Exception;
	void addShippedAt(Integer id) throws Exception;
//	List<Employee> selectLimited(@Param("offset") int offset, @Param("num") int num) throws Exception;
//    long countActive() throws Exception;

}
