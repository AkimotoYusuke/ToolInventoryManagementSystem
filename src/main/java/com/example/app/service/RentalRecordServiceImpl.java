package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.RentalRecord;
import com.example.app.domain.Tool;
import com.example.app.mapper.RentalRecordMapper;
import com.example.app.mapper.ShippingRecordMapper;
import com.example.app.mapper.ToolMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalRecordServiceImpl implements RentalRecordService {

	private final ShippingRecordMapper shippingRecordMapper;
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
	public void borrowRequestTool(int shippingRecordId, int employeeId) throws Exception {
		// 発送履歴テーブル更新
		shippingRecordMapper.addShippingRequest(shippingRecordId);
		//予約済工具リストを取得
		List<Tool> reservedToolList = rentalMapper.selectReservedByEmployeeId(employeeId);
		// 予約済工具リスト分、入出庫履歴テーブルの新規追加と工具テーブルへの更新
		reservedToolList.forEach(tool -> {
			try {
				RentalRecord rentalRecord = new RentalRecord();
				rentalRecord.setShippingId(shippingRecordId);
				rentalRecord.setEmployeeId(employeeId);
				rentalRecord.setToolId(tool.getId());
				rentalRecordMapper.addBorrowingRequestRecord(rentalRecord);
				rentalMapper.addBorrowingRequestRecord(tool.getId(), employeeId, shippingRecordId, rentalRecord.getId());
			} catch (Exception e) {
				// エラー発生
				System.out.println("出庫依頼処理でエラー");
				e.printStackTrace();
			}
		});
	}
		
	@Override
	public void borrowTool(int shippingRecordId) throws Exception {
		shippingRecordMapper.addShipped(shippingRecordId);
		rentalRecordMapper.addBorrowedRecord(shippingRecordId);
	}

	@Override
	public void returnTool(int shippingRecordId) throws Exception {
		shippingRecordMapper.addReturned(shippingRecordId);
		rentalRecordMapper.addReturnedRecord(shippingRecordId);
		rentalMapper.addReturnedRecord(shippingRecordId);
	}
	
	@Override
	public boolean onlyOneReturnTool(int toolId, int shippingRecordId) throws Exception {
		rentalRecordMapper.addOnlyOneReturnedRecord(toolId);
		rentalMapper.addOnlyOneReturnedRecord(toolId);
		
		// ある発送番号の発送工具が全て入庫済の場合は、発送情報の入庫日を記載する
		if(rentalRecordMapper.countBorrowingByShippingId(shippingRecordId) == 0) {
			shippingRecordMapper.addReturned(shippingRecordId);
			return true;
		}
		
		return false;
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

}
