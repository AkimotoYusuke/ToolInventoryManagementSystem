package com.example.app.service;

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
//	private final AuthorityTypeMapper authorityTypeMapper;

//	@Override
//	public List<Employee> getEmployeeList() throws Exception {
//		return employeeMapper.selectAll();
//	}
//
	@Override
	public ShippingRecord getShippingRecordById(Integer id) throws Exception {
		return shippingRecordMapper.selectById(id);
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
//
//	@Override
//	public void deleteEmployeeById(Integer id) throws Exception {
//		employeeMapper.setDeleteById(id);
//	}

	@Override
	public void addShippingRecord(ShippingRecord shippingRecord) throws Exception {
		shippingRecordMapper.insert(shippingRecord);
	}

//	@Override
//	public void editEmployee(Employee employee) throws Exception {
//		String password = employee.getLoginPass();
//		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//		employee.setLoginPass(hashedPassword);
//		employeeMapper.update(employee);
//	}
//
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
