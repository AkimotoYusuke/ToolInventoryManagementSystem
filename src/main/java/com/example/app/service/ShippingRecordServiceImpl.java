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
	public List<ShippingRecord> getShippingRecordListIsShippingRequest() throws Exception {
		return shippingRecordMapper.selectAllIsShippingRequest();
	}
	
	@Override
	public List<ShippingRecord> getShippingRecordListByEmployeeId(Integer employeeId) throws Exception {
		return shippingRecordMapper.selectAllByEmployeeId(employeeId);
	}

	@Override
	public ShippingRecord getShippingRecordById(Integer id) throws Exception {
		return shippingRecordMapper.selectById(id);
	}
	
	@Override
	public ShippingRecord getShippingRecordByEmployeeId(Integer employeeId) throws Exception {
		return shippingRecordMapper.selectByEmployeeId(employeeId);
	}
//	
//	@Override
//	public Employee getEmployeeByLoginId(String logingId) throws Exception {
//		return employeeMapper.selectByLoginId(logingId);
//	}
//	
//	@Override
//	public Employee getEmployeeByLoginIdAndStatusAct(String logingId) throws Exception {
//		return employeeMapper.selectByLoginIdAndStatusAct(logingId);
//	}

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

//	@Override
//	public boolean isExsitingEmployee(String loginId) throws Exception {
//		Employee employee = employeeMapper.selectByLoginId(loginId);
//		if(employee != null) {
//			return true;
//		}
//
//		return false;
//	}
//
//	@Override
//	public List<Employee> getEmployeeListPerPage(int page, int numPerPage) throws Exception {
//		int offset = numPerPage * (page - 1);
//		return employeeMapper.selectLimited(offset, numPerPage);
//	}
//
//	@Override
//	public int getTotalPages(int numPerPage) throws Exception {
//		long count = employeeMapper.countActive();
//		return (int) Math.ceil((double) count / numPerPage);
//	}
//
//	@Override
//	public List<AuthorityType> getAuthorityTypeList() throws Exception {
//		return authorityTypeMapper.selectAll();
//	}

}
