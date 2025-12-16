package com.example.app.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.AuthorityType;
import com.example.app.domain.Employee;
import com.example.app.mapper.AuthorityTypeMapper;
import com.example.app.mapper.EmployeeMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeMapper employeeMapper;
	private final AuthorityTypeMapper authorityTypeMapper;

	@Override
	public Employee getEmployeeById(Integer id) throws Exception {
		return employeeMapper.selectById(id);
	}
	
	@Override
	public Employee getEmployeeByLoginId(String logingId) throws Exception {
		return employeeMapper.selectByLoginId(logingId);
	}
	
	@Override
	public Employee getEmployeeByLoginIdAndStatusAct(String logingId) throws Exception {
		return employeeMapper.selectByLoginIdAndStatusAct(logingId);
	}

	@Override
	public void deleteEmployeeById(Integer id) throws Exception {
		employeeMapper.setDeleteById(id);
	}

	@Override
	public void addEmployee(Employee employee) throws Exception {
		String password = employee.getLoginPass();
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		employee.setLoginPass(hashedPassword);
		employeeMapper.insert(employee);
	}

	@Override
	public void editEmployee(Employee employee) throws Exception {
		String password = employee.getLoginPass();
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		employee.setLoginPass(hashedPassword);
		employeeMapper.update(employee);
	}

	@Override
	public List<Employee> getEmployeeListPerPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return employeeMapper.selectLimited(offset, numPerPage);
	}

	@Override
	public int getTotalPages(int numPerPage) throws Exception {
		long count = employeeMapper.countActive();
		return (int) Math.ceil((double) count / numPerPage);
	}

	@Override
	public List<AuthorityType> getAuthorityTypeList() throws Exception {
		return authorityTypeMapper.selectAll();
	}

}
