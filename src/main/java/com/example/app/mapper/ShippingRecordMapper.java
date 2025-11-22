package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.ShippingRecord;

@Mapper
public interface ShippingRecordMapper {

//	List<Employee> selectAll() throws Exception;
	ShippingRecord selectById(Integer id) throws Exception;
//	Employee selectByLoginId(String loginId) throws Exception;
//	Employee selectByLoginIdAndStatusAct(String loginId) throws Exception;
//	void setDeleteById(Integer id) throws Exception;
	void insert(ShippingRecord shippingRecord) throws Exception;
//	void update(Employee student) throws Exception;
//	List<Employee> selectLimited(@Param("offset") int offset, @Param("num") int num) throws Exception;
//    long countActive() throws Exception;

}
