package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Employee;
import com.example.app.domain.MakerType;
import com.example.app.domain.Tool;
import com.example.app.mapper.EmployeeMapper;
import com.example.app.mapper.MakerTypeMapper;
import com.example.app.mapper.ToolMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService {

	private final ToolMapper toolMapper;
	private final MakerTypeMapper makerTypeMapper;
	private final EmployeeMapper employeeMapper;

	@Override
	public Tool getToolById(Integer id) throws Exception {
		return toolMapper.selectById(id);
	}
	
	@Override
	public Tool getToolMgmtId(String mgmtId) throws Exception {
		return toolMapper.selectByMgmtId(mgmtId);
	}

	@Override
	public void deleteToolById(Integer id) throws Exception {
		toolMapper.setDeleteById(id);
	}

	@Override
	public void addTool(Tool tool) throws Exception {
		toolMapper.insert(tool);
	}

	@Override
	public void editTool(Tool tool) throws Exception {
		toolMapper.update(tool);
	}
	
	@Override
	public List<Tool> getKeywordToolListPerPage(int page, int numPerPage, String keyword) throws Exception {
		int offset = numPerPage * (page - 1);
		return toolMapper.selectKeywordLimited(offset, numPerPage, keyword);
	}
	
	@Override
	public int getKeywordTotalPages(int numPerPage, String keyword) throws Exception {
		long count = toolMapper.countKeywordActive(keyword);
		int totalPages = (int) Math.ceil((double) count / numPerPage);
		return totalPages > 0 ? totalPages : 1; // totalPagesが0ページ以下だったら、1ページにする
	}
	
	@Override
	public int getKeywordTargetIdPage(int numPerPage, int toolId, String keyword) throws Exception {
		long count = toolMapper.countKeywordActiveAddedId(toolId, keyword);
		int targetPage = (int) Math.ceil((double) count / numPerPage);
		return targetPage > 0 ? targetPage : 1; // targetPageが0ページ以下だったら、1ページにする
	}
	
	@Override
	public List<Tool> getLimitedReservedToolList(int page, int numPerPage, int employeeId) throws Exception {
		int offset = numPerPage * (page - 1);
		return toolMapper.selectLimitedReservedByEmployeeId(offset, numPerPage, employeeId);
	}
	
	@Override
	public int getTotalReservedToolPages(int numPerPage, int employeeId) throws Exception {
		long count = toolMapper.countReserved(employeeId);
		int totalPages = (int) Math.ceil((double) count / numPerPage);
		return totalPages > 0 ? totalPages : 1; // totalPagesが0ページ以下だったら、1ページにする
	}
	
	@Override
	public List<Tool> getBorrowingToolList(int shippingId) throws Exception {
		return toolMapper.selectBorrowingByShippingId(shippingId);
	}
	
	@Override
	public List<Tool> getKeywordBorrowableToolListPerPage(int page, int numPerPage, String keyword) throws Exception {
		int offset = numPerPage * (page - 1);
		return toolMapper.selectKeywordBorrowableWithOffset(offset, numPerPage, keyword);
	}
	
	@Override
	public int getKeywordTotalBorrowableToolPages(int numPerPage, String keyword) throws Exception {
		long count = toolMapper.countKeywordBorrowable(keyword);
		int totalPages = (int) Math.ceil((double) count / numPerPage);
		return totalPages > 0 ? totalPages : 1; // totalPagesが0ページ以下だったら、1ページにする
	}

	@Override
	public boolean hasReservation(Integer toolId) throws Exception {
		Tool tool = toolMapper.selectById(toolId);

		if(tool.getStatus().equals("DEL")) {
			return false;
		}
		else if(tool.getReservedEmployeeId() != null) {
			return false;
		}
		else if(tool.getRequestedEmployeeId() != null) {
			return false;
		}

		return true;
	}
	
	@Override
	public String hasDelete(Integer toolId) throws Exception {
		Tool tool = toolMapper.selectById(toolId);

		if(tool.getReservedEmployeeId() != null) {
			Integer emplyoeeId = tool.getReservedEmployeeId();
			Employee employee = employeeMapper.selectById(emplyoeeId);
			return employee.getName() + "さんによって予約済の為、削除不可です";
		}
		else if(tool.getRequestedEmployeeId() != null) {
			Integer emplyoeeId = tool.getRequestedEmployeeId();
			Employee employee = employeeMapper.selectById(emplyoeeId);
			return employee.getName() + "さんによって出庫依頼済の為、削除不可です";
		}
		else {
			return "未予約";
		}
	}

	@Override
	public List<MakerType> getMakerTypeList() throws Exception {
		return makerTypeMapper.selectAll();
	}

}
