package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.RentalRecord;
import com.example.app.domain.Tool;
import com.example.app.mapper.RentalRecordMapper;
import com.example.app.mapper.ToolMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalRecordServiceImpl implements RentalRecordService {

	private final RentalRecordMapper rentalRecordMapper;
	private final ToolMapper rentalMapper;

	@Override
	public List<RentalRecord> getLatestRentalRecordListByToolId(int toolId, int num) throws Exception {
		return rentalRecordMapper.selectLatestByToolId(toolId, num);
	}
	
	@Override
	public void reserveTool(int employeeId, int toolId) throws Exception {
		rentalMapper.editReserved(toolId, employeeId);
	}
	
	@Override
	public void cancelTool(int toolId) throws Exception {
		rentalMapper.editCanceled(toolId);
	}
		
	@Override
	public void borrowTool(int employeeId, int toolId) throws Exception {
		RentalRecord rentalRecord = new RentalRecord();
		rentalRecord.setEmployeeId(employeeId);
		rentalRecord.setToolId(toolId);
		rentalRecordMapper.addBorrowedRecord(rentalRecord);
		rentalMapper.addBorrowedRecord(toolId, rentalRecord.getId());
	}

	@Override
	public void returnTool(int toolId) throws Exception {
		rentalRecordMapper.addReturnedRecord(toolId);
		rentalMapper.addReturnedRecord(toolId);
	}

	@Override
	public boolean isAbleToBorrow(int employeeId, int limitation) throws Exception {
		return rentalRecordMapper.countBorrowingByEmployeeId(employeeId) < limitation;
	}
	
	@Override
	public boolean cancelByAuthenticatedEmployee(int employeeId, int toolId) throws Exception {
		Tool tool = rentalMapper.selectById(toolId);
		if(tool == null) {
			return false;
		}

		if(tool.getReservedEmployeeId() != (employeeId)) {
			return false;
		}

		return true;
	}

	@Override
	public boolean byAuthenticatedEmployee(int employeeId, int toolId) throws Exception {
		RentalRecord record = rentalRecordMapper.selectBorrowingRecordByToolId(toolId);
		if(record == null) {
			return false;
		}

		if(!record.getEmployeeId().equals(employeeId)) {
			return false;
		}

		return true;
	}

}
