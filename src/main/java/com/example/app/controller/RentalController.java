package com.example.app.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Tool;
import com.example.app.login.LoginStatus;
import com.example.app.service.RentalRecordService;
import com.example.app.service.ShippingRecordService;
import com.example.app.service.ToolService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RentalController {

	private static final int NUM_PER_PAGE = 5;
	private static final int BORROWABLE_LIMIT = 3;

	private final ToolService toolService;
	private final RentalRecordService rentalRecordService;
	private final ShippingRecordService shippingRecordService;
	private final HttpSession session;

	@GetMapping({ "/", "/rental" })
	public String rental(
			@RequestParam(name = "page", defaultValue = "1") Integer page,
//			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			Model model) throws Exception {
		// セッション(依頼者IDを含んでいる)を取得
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		
		// ページ番号の保持
		session.setAttribute("page", page);
		
		String keyword;
		
		if(session.getAttribute("keyword") == null) {
			keyword = "";
		} else {
			keyword = (String) session.getAttribute("keyword");
		}
		
		// 全体のページ数
//		int totalPages = toolService.getTotalBorrowableToolPages(NUM_PER_PAGE);
		int totalPages = toolService.getKeywordTotalBorrowableToolPages(NUM_PER_PAGE, keyword);
		model.addAttribute("totalPages", totalPages);
		// 現在のページ番号
		model.addAttribute("currentPage", page);
		
		// 依頼者の発送情報
		model.addAttribute("shippingRecord", shippingRecordService.getShippingRecordByEmployeeId(loginStatus.getId()));
		
		// 現在、予約済工具のリスト
		List<Tool> reservedToolList = toolService.getReservedToolList(loginStatus.getId());
		model.addAttribute("reservedList", reservedToolList);
		// 現在の発送リスト	
		model.addAttribute("shippingList", shippingRecordService.getShippingRecordListByEmployeeId(loginStatus.getId()));
		// 貸し出し可能な工具のリスト
//		model.addAttribute("toolList", toolService.getBorrowableToolListPerPage(page, NUM_PER_PAGE));
		model.addAttribute("toolList", toolService.getKeywordBorrowableToolListPerPage(page, NUM_PER_PAGE, keyword));
		
		// 検索ワード
		model.addAttribute("keyword", keyword);
		
		return "list-rental";
	}
	
	//出庫依頼済・出庫済工具リスト
	@GetMapping("/rental/toolList/{shippingId}")
	public String rentalToolList(
			@PathVariable("shippingId") Integer shippingId,
			Model model) throws Exception {

		model.addAttribute("shippingId", shippingId);
		// 現在、出庫依頼・出庫済工具のリスト
		model.addAttribute("borrowingList", toolService.getBorrowingToolList(shippingId));
		
		return "show-tool-list";
	}

	// 「予約」ボタン
	@GetMapping("/rental/reserve/{toolId}")
	public String reserveTool(
			@PathVariable("toolId") Integer toolId,
			RedirectAttributes redirectAttributes) throws Exception {
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");

		int previousPage = (int) session.getAttribute("page");

//		// 借りている工具数が最大値を超えていないか確認
//		if (!rentalRecordService.isAbleToBorrow(loginStatus.getId(), BORROWABLE_LIMIT)) {
//			redirectAttributes.addFlashAttribute("message", "貸し出し可能な工具数は" + BORROWABLE_LIMIT + "個までです");
//			return "redirect:/rental?page=" + previousPage;
//		}

		// 予約しようとしている工具が予約・出庫されていないか確認
		if (!toolService.hasReservation(toolId)) {
			redirectAttributes.addFlashAttribute("message", "工具は予約済か出庫済、または削除済みです");
			return "redirect:/rental?page=" + previousPage;
		}

		// 問題がなければ、「予約」処理を実行
		rentalRecordService.reserveTool(loginStatus.getId(), toolId);
		redirectAttributes.addFlashAttribute("message", "予約しました。");
		
		// 予約後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int totalPages = toolService.getTotalBorrowableToolPages(NUM_PER_PAGE);
		int page = previousPage <= totalPages ? previousPage : totalPages;
		return "redirect:/rental?page=" + page;
	}
	
	//「キャンセル」ボタン
	@GetMapping("/rental/cancel/{toolId}")
	public String returnTool(
			@PathVariable("toolId") Integer toolId,
			RedirectAttributes redirectAttributes) throws Exception {
		// 本人によるキャンセルか確認
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		if (!rentalRecordService.cancelByAuthenticatedEmployee(loginStatus.getId(), toolId)) {
			System.out.println("他の従業員によるキャンセル");
		} else {
			rentalRecordService.cancelTool(toolId);
		}
		
		redirectAttributes.addFlashAttribute("message", "キャンセルしました。");

		// キャンセル後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/rental?page=" + previousPage;
	}
	
	//「出庫依頼」ボタン
	@GetMapping("/rental/request/{shippingId}")
	public String borrowTool(
			@PathVariable Integer shippingId,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		
		// 現在、予約済工具のリストを取得
		List<Tool> reservedToolList = toolService.getReservedToolList(loginStatus.getId());
		
		//予約済工具のリストの分、「出庫依頼」処理を実行
		rentalRecordService.borrowRequestTool(shippingId,loginStatus.getId(), reservedToolList);
		
		redirectAttributes.addFlashAttribute("message", "出庫依頼しました。");
		
		//出庫依頼後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/rental?page=" + previousPage;
	}
	
	//キーワード検索
	@GetMapping("/rental/selectKeyword")
	public String selectKeyword(
			@RequestParam String keyword,
			RedirectAttributes attrs) throws Exception {
		
		// キーワードの保持
		session.setAttribute("keyword", keyword);

		// 1ページ目を表示
		Integer previousPage = 1;
		return "redirect:/rental?page=" + previousPage;
	}

}
