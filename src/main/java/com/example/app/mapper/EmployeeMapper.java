package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Employee;

@Mapper
public interface EmployeeMapper {

	List<Employee> selectAll() throws Exception;
	Employee selectById(Integer id) throws Exception;
	Employee selectByLoginId(String loginId) throws Exception;
	Employee selectByLoginIdAndStatusAct(String loginId) throws Exception;
	void setDeleteById(Integer id) throws Exception;
	void insert(Employee student) throws Exception;
	void update(Employee student) throws Exception;
	List<Employee> selectLimited(@Param("offset") int offset, @Param("num") int num) throws Exception;
    long countActive() throws Exception;

}
