package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.ShippingRecord;
import com.example.app.mapper.ShippingRecordMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ShippingRecordServiceImpl implements ShippingRecordService {

	private final ShippingRecordMapper shippingRecordMapper;

	@Override
	public List<ShippingRecord> getShippingRecordListByEmployeeId(Integer employeeId) throws Exception {
		return shippingRecordMapper.selectAllByEmployeeId(employeeId);
	}
	
	@Override
	public List<ShippingRecord> getLimitedShippingRecordListByEmployeeId(int page, int numPerPage, Integer employeeId) throws Exception {
		int offset = numPerPage * (page - 1);
		return shippingRecordMapper.selectLimitedByEmployeeId(offset, numPerPage, employeeId);
	}
	
	@Override
	public int getShippingTotalPages(int numPerPage,  Integer employeeId) throws Exception {
		long count = shippingRecordMapper.countShippingActive(employeeId);
		int totalPages = (int) Math.ceil((double) count / numPerPage);
		return totalPages > 0 ? totalPages : 1; // totalPagesが0ページ以下だったら、1ページにする
	}
	
	@Override
	public List<ShippingRecord> getShippingRecordListIsShippingRequest() throws Exception {
		return shippingRecordMapper.selectAllIsShippingRequest();
	}
	
	@Override
	public List<ShippingRecord> getShippingRecordListIsShipped() throws Exception {
		return shippingRecordMapper.selectAllIsShipped();
	}

	@Override
	public ShippingRecord getShippingRecordById(Integer id) throws Exception {
		return shippingRecordMapper.selectById(id);
	}
	
	@Override
	public ShippingRecord getShippingRecordByEmployeeId(Integer employeeId) throws Exception {
		return shippingRecordMapper.selectByEmployeeId(employeeId);
	}

	@Override
	public void deleteShippingRecordById(Integer id) throws Exception {
		shippingRecordMapper.setDeleteById(id);
	}

	@Override
	public void addShippingRecord(ShippingRecord shippingRecord) throws Exception {
		shippingRecordMapper.insert(shippingRecord);
	}

	@Override
	public void editShippingRecord(ShippingRecord shippingRecord) throws Exception {
		shippingRecordMapper.update(shippingRecord);
	}
	
	@Override
	public int getTotalPages(int numPerPage) throws Exception {
		long count = shippingRecordMapper.countActive();
		int totalPages = (int) Math.ceil((double) count / numPerPage);
		return totalPages > 0 ? totalPages : 1; // totalPagesが0ページ以下だったら、1ページにする
	}
	
	@Override
	public List<ShippingRecord> getShippingRecordListIsShippedPerPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return shippingRecordMapper.selectLimitedIsShipped(offset, numPerPage);
	}

}
