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
    
    // ある生徒が現在借りている教材の取得
    List<Tool> getBorrowingToolList(int employeeId) throws Exception;
    // 貸し出し可能な教材のリスト(ページごと)
    List<Tool> getBorrowableToolListPerPage(int page, int numPerPage) throws Exception;
    // 教材が貸し出し可能な教材のページ数
    int getTotalBorrowableToolPages(int numPerPage) throws Exception;
    // 教材が貸し出し可能か否か判別
    boolean isBorrowable(Integer toolId) throws Exception;

	List<MakerType> getMakerTypeList() throws Exception;

}
