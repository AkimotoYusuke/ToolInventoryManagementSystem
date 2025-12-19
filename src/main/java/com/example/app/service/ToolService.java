package com.example.app.service;

import java.util.List;

import com.example.app.domain.MakerType;
import com.example.app.domain.Tool;

public interface ToolService {

	Tool getToolById(Integer id) throws Exception;
	Tool getToolMgmtId(String mgmtId) throws Exception;
	void deleteToolById(Integer id) throws Exception;
	void addTool(Tool tool) throws Exception;
	void editTool(Tool tool) throws Exception;
	List<Tool> getKeywordToolListPerPage(int page, int numPerPage, String keyword) throws Exception;
    int getKeywordTotalPages(int numPerPage, String keyword) throws Exception;
    int getKeywordTargetIdPage(int numPerPage, int toolId, String keyword) throws Exception;
    
    // 依頼者の現在予約済工具の取得
    List<Tool> getLimitedReservedToolList(int page, int numPerPage, int employeeId) throws Exception;
    // 予約済工具のページ数
    int getTotalReservedToolPages(int numPerPage, int employeeId) throws Exception;
    // 依頼者が現在出庫依頼済または出庫済工具リストの取得
    List<Tool> getBorrowingToolList(int shippingId) throws Exception;
    // キーワード検索した際の出庫可能な工具のリスト(ページごと)
    List<Tool> getKeywordBorrowableToolListPerPage(int page, int numPerPage, String keyword) throws Exception;
    // キーワード検索した際の工具が出庫可能な工具のページ数
    int getKeywordTotalBorrowableToolPages(int numPerPage, String keyword) throws Exception;
    // 工具が予約可能か否か判別
    boolean hasReservation(Integer toolId) throws Exception;
    // 工具の削除可能か否か判別
    String hasDelete(Integer toolId) throws Exception;

	List<MakerType> getMakerTypeList() throws Exception;

}
