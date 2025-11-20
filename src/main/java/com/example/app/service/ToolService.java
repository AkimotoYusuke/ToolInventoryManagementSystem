package com.example.app.service;

import java.util.List;

import com.example.app.domain.MakerType;
import com.example.app.domain.Tool;

public interface ToolService {

	List<Tool> getToolList() throws Exception;
	Tool getToolById(Integer id) throws Exception;
	Tool getToolMgmtId(String mgmtId) throws Exception;
	void deleteToolById(Integer id) throws Exception;
	void addTool(Tool tool) throws Exception;
	void editTool(Tool tool) throws Exception;
	boolean isExsitingTool(String mgmtId) throws Exception;
	List<Tool> getToolListPerPage(int page, int numPerPage) throws Exception;
    int getTotalPages(int numPerPage) throws Exception;
    
    // ある依頼者が現在予約済工具の取得
    List<Tool> getReservedToolList(int employeeId) throws Exception;
    // ある依頼者が現在出庫済工具の取得
    List<Tool> getBorrowingToolList(int employeeId) throws Exception;
    // 出庫可能な工具のリスト(ページごと)
    List<Tool> getBorrowableToolListPerPage(int page, int numPerPage) throws Exception;
    // 工具が出庫可能な工具のページ数
    int getTotalBorrowableToolPages(int numPerPage) throws Exception;
    // 工具が出庫可能か否か判別
    boolean isBorrowable(Integer toolId) throws Exception;

	List<MakerType> getMakerTypeList() throws Exception;

}
