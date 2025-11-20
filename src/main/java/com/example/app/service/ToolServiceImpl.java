package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.MakerType;
import com.example.app.domain.Tool;
import com.example.app.mapper.MakerTypeMapper;
import com.example.app.mapper.ToolMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService {

	private final ToolMapper toolMapper;
	private final MakerTypeMapper makerTypeMapper;

	@Override
	public List<Tool> getToolList() throws Exception {
		return toolMapper.selectAll();
	}

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
	public void addTool(Tool material) throws Exception {
		toolMapper.insert(material);
	}

	@Override
	public void editTool(Tool material) throws Exception {
		toolMapper.update(material);
	}
	
	@Override
	public boolean isExsitingTool(String mgmtId) throws Exception {
		Tool tool = toolMapper.selectByMgmtId(mgmtId);
		if(tool != null) {
			return true;
		}

		return false;
	}

	@Override
	public List<Tool> getToolListPerPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return toolMapper.selectLimited(offset, numPerPage);
	}

	@Override
	public int getTotalPages(int numPerPage) throws Exception {
		long count = toolMapper.countActive();
		int totalPages = (int) Math.ceil((double) count / numPerPage);
		return totalPages > 0 ? totalPages : 1; // totalPagesが0ページ以下だったら、1ページにする
	}
	
	@Override
	public List<Tool> getBorrowingToolList(int employeeId) throws Exception {
		return toolMapper.selectBorrowingByEmployeeId(employeeId);
	}

	@Override
	public List<Tool> getBorrowableToolListPerPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return toolMapper.selectBorrowableWithOffset(offset, numPerPage);
	}

	@Override
	public int getTotalBorrowableToolPages(int numPerPage) throws Exception {
		long count = toolMapper.countBorrowable();
		int totalPages = (int) Math.ceil((double) count / numPerPage);
		return totalPages > 0 ? totalPages : 1; // totalPagesが0ページ以下だったら、1ページにする
	}

	@Override
	public boolean isBorrowable(Integer toolId) throws Exception {
		Tool tool = toolMapper.selectById(toolId);

		if(tool == null) {
			return false;
		}
		else if(tool.getRentalId() != null) {
			return false;
		}

		return true;
	}

	@Override
	public List<MakerType> getMakerTypeList() throws Exception {
		return makerTypeMapper.selectAll();
	}

}
