package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.MakerType;

@Mapper
public interface MakerTypeMapper {

	List<MakerType> selectAll() throws Exception;

}
