package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.RentalRecord;

@Mapper
public interface RentalRecordMapper {

	// ある工具の最新の出庫履歴
	List<RentalRecord> selectLatestByToolId(@Param("toolId") int toolId, @Param("num") int num) throws Exception;
	// 「出庫依頼」に対応する処理：依頼者ID, 工具ID, 発送ID, 出庫依頼日を記録
	void addBorrowingRequestRecord(RentalRecord rentalRecord) throws Exception;
  //「出庫」に対応する処理：出庫日を記録
	void addBorrowedRecord(int shippingId) throws Exception;
	// 「入庫」に対応する処理：入庫日を記録
	void addReturnedRecord(int shippingId) throws Exception;
	// 対象１個のみの「入庫」に対応する処理：入庫日を記録
	void addOnlyOneReturnedRecord(int toolId) throws Exception;
	// ある依頼者の未入庫の工具数
	int countBorrowingByEmployeeId(int employeeId) throws Exception;
	// ある発送番号の未入庫の工具数
	long countBorrowingByShippingId(int shippingId) throws Exception;
	// 工具IDから現在の出庫状況(どの依頼者が出庫しているか)を取得
	RentalRecord selectBorrowingRecordByToolId(int toolId) throws Exception;

}
