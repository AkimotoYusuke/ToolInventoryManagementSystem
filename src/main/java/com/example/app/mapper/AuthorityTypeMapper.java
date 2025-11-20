package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.AuthorityType;

@Mapper
public interface AuthorityTypeMapper {

	List<AuthorityType> selectAll() throws Exception;

}
