package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Tool;

@Mapper
public interface ToolMapper {

	List<Tool> selectAll() throws Exception;
	Tool selectById(Integer id) throws Exception;
	Tool selectByMgmtId(String mgmtId) throws Exception;
	void setDeleteById(Integer id) throws Exception;
	void insert(Tool tool) throws Exception;
	void update(Tool tool) throws Exception;
	List<Tool> selectLimited(@Param("offset") int offset, @Param("num") int num) throws Exception;
    long countActive() throws Exception;
    
    // ある依頼者の予約済のリストを取得
    List<Tool> selectReservedByEmployeeId(int employeeId) throws Exception;
    // ある依頼者の出庫依頼済・出庫済の工具リストを取得
    List<Tool> selectBorrowingByShippingId(int shippingId) throws Exception;
    // 出庫可能な工具のリストを取得(LIMIT句あり)
    List<Tool> selectBorrowableWithOffset(@Param("offset") int offset, @Param("num") int num) throws Exception;
    // 出庫可能な工具の数を取得(ページ番号用)
    long countBorrowable() throws Exception;
    // 工具に「予約済」と「予約者」を記録
    void editReserved(@Param("id") int toolId, @Param("employeeId") int employeeId) throws Exception;
    // 工具に「キャンセル」の為、「予約済」と「予約者」を削除
    void editCanceled(int toolId) throws Exception;
    // 工具に「出庫依頼」を記録
    void addBorrowingRequestRecord(@Param("id") int toolId, @Param("employeeId") int employeeId,
    											@Param("shippingId") int shippingId, @Param("rentalId") int rentalId) throws Exception;
    // 工具に「出庫済」を記録
    void addBorrowedRecord(int shippingId) throws Exception;
    // 工具に「入庫済」を記録
    void addReturnedRecord(int shippingId) throws Exception;

}
