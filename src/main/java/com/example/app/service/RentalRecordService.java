package com.example.app.service;

import java.util.List;

import com.example.app.domain.RentalRecord;
import com.example.app.domain.Tool;

public interface RentalRecordService {

	// ある工具の最新の出庫履歴
	List<RentalRecord> getLatestRentalRecordListByToolId(int toolId, int num) throws Exception;
  //工具を予約する
	void reserveTool(int employeeId, int toolId) throws Exception;
	//工具をキャンセルする
	void cancelTool(int toolId) throws Exception;
	//工具の出庫依頼をする
	void borrowRequestTool(int shippingRecordId, int employeeId, List<Tool> reservedToolList) throws Exception;
	// 工具を出庫する
	void borrowTool(int shippingRecordId) throws Exception;
	// 工具を入庫する
	void returnTool(int toolId) throws Exception;
	// 依頼者が工具を借りられる状態か判別する
	boolean isAbleToBorrow(int employeeId, int limitation) throws Exception;
	//本人によるキャンセルか確認する
	boolean cancelByAuthenticatedEmployee(int employeeId, int toolId) throws Exception;
	// 本人による返却か確認する
	boolean byAuthenticatedEmployee(int employeeId, int toolId) throws Exception;

}
