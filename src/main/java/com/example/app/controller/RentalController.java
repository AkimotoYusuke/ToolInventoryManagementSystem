package com.example.app.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.ShippingRecord;
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
			Model model) throws Exception {
		// セッション(依頼者IDを含んでいる)を取得
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		
		// ページ番号の保持
		session.setAttribute("page", page);

		// 全体のページ数
		int totalPages = toolService.getTotalBorrowableToolPages(NUM_PER_PAGE);
		model.addAttribute("totalPages", totalPages);
		// 現在のページ番号
		model.addAttribute("currentPage", page);
		
		// 依頼者の発送情報
		model.addAttribute("shippingRecord", shippingRecordService.getShippingRecordByEmployeeId(loginStatus.getId()));
		
		// 現在、予約済工具のリスト
		List<Tool> reservedToolList = toolService.getReservedToolList(loginStatus.getId());
		model.addAttribute("reservedList", reservedToolList);
//		if (reservedToolList != null) {
//			ShippingRecord shippingRecord = new ShippingRecord();
////			if(model.getAttribute("shippingRecord") != null) {
////				shippingRecord = (ShippingRecord) model.getAttribute("shippingRecord");
////			}
//			shippingRecord.setSenderAddress("東京本社 工具管理センター");
////			model.addAttribute("errors", model.getAttribute("errors"));
//			model.addAttribute("shippingRecord", shippingRecord);
//		}
			
		// 現在、借りている工具のリスト
		model.addAttribute("borrowingList", toolService.getBorrowingToolList(loginStatus.getId()));
		// 貸し出し可能な工具のリスト
		model.addAttribute("toolList", toolService.getBorrowableToolListPerPage(page, NUM_PER_PAGE));
		
		return "list-rental";
	}

	// 「予約」ボタン
	@GetMapping("/rental/reserve/{toolId}")
	public String borrowMaterial(
			@PathVariable("toolId") Integer toolId,
			RedirectAttributes redirectAttributes) throws Exception {
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");

		int previousPage = (int) session.getAttribute("page");

//		// 借りている工具数が最大値を超えていないか確認
//		if (!rentalRecordService.isAbleToBorrow(loginStatus.getId(), BORROWABLE_LIMIT)) {
//			redirectAttributes.addFlashAttribute("message", "貸し出し可能な工具数は" + BORROWABLE_LIMIT + "個までです");
//			return "redirect:/rental?page=" + previousPage;
//		}

//		// 借りようとしている工具が貸し出されていないか確認
//		if (!toolService.isBorrowable(toolId)) {
//			redirectAttributes.addFlashAttribute("message", "工具は貸し出し中、または削除済みです");
//			return "redirect:/rental?page=" + previousPage;
//		}

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
	public String returnMaterial(
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
@PostMapping("/rental/shippingRequest")
public String borrowMaterial(
		@Valid ShippingRecord shippingRecord,
		Errors errors,
		Model model,
		RedirectAttributes redirectAttributes) throws Exception {
	System.out.println("出庫依頼ボタン：POST内");
	if(errors.hasErrors()) {
		System.out.println("エラー検知");
		System.out.println(errors);
//		model.addAttribute("shippingRecord",shippingRecord);
		//redirectAttributes.addFlashAttribute("shippingRecord", shippingRecord);
		//redirectAttributes.addFlashAttribute("errors", errors);
		int page = (int) session.getAttribute("page");
	  return "redirect:/rental?page=" + page;
		// return "list-rental";
	}
	
	LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
	
	// 問題がなければ、「出庫依頼」処理を実行
//	rentalRecordService.borrowTool(loginStatus.getId(), toolId);
	
		//出庫依頼後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/rental?page=" + previousPage;
}
	
////「出庫依頼」ボタン
//	@GetMapping("/rental/borrow/{toolId}")
//	public String borrowMaterial(
//			@PathVariable("toolId") Integer toolId,
//			RedirectAttributes redirectAttributes) throws Exception {
//		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
//
//		int previousPage = (int) session.getAttribute("page");
//
//		// 借りている工具数が最大値を超えていないか確認
//		if (!rentalRecordService.isAbleToBorrow(loginStatus.getId(), BORROWABLE_LIMIT)) {
//			redirectAttributes.addFlashAttribute("message", "貸し出し可能な工具数は" + BORROWABLE_LIMIT + "個までです");
//			return "redirect:/rental?page=" + previousPage;
//		}
//
//		// 借りようとしている工具が貸し出されていないか確認
//		if (!toolService.isBorrowable(toolId)) {
//			redirectAttributes.addFlashAttribute("message", "工具は貸し出し中、または削除済みです");
//			return "redirect:/rental?page=" + previousPage;
//		}
//
//		// 問題がなければ、「出庫依頼」処理を実行
//		rentalRecordService.borrowTool(loginStatus.getId(), toolId);
//		
//		// 出庫依頼後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
//		int totalPages = toolService.getTotalBorrowableToolPages(NUM_PER_PAGE);
//		int page = previousPage <= totalPages ? previousPage : totalPages;
//		return "redirect:/rental?page=" + page;
//	}
	

//	// 「入庫依頼」ボタン
//	@GetMapping("/rental/return/{toolId}")
//	public String returnMaterial(
//			@PathVariable("toolId") Integer toolId) throws Exception {
//		// 本人による返却か確認
//		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
//		if (!rentalRecordService.byAuthenticatedEmployee(loginStatus.getId(), toolId)) {
//			System.out.println("他の従業員による返却");
//		} else {
//			rentalRecordService.returnTool(toolId);
//		}
//
//		// 返却後に戻るページ(元のページ)
//		int previousPage = (int) session.getAttribute("page");
//		return "redirect:/rental?page=" + previousPage;
//	}

}
