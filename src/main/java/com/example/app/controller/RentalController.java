package com.example.app.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	private final ToolService toolService;
	private final RentalRecordService rentalRecordService;
	private final ShippingRecordService shippingRecordService;
	private final HttpSession session;

	@GetMapping({ "/", "/rental" })
	public String rental(
			@RequestParam(name = "pageTool", defaultValue = "1") Integer pageTool,
			@RequestParam(name = "pageShipped", defaultValue = "1") Integer pageShipped,
			@RequestParam(name = "pageReserved", defaultValue = "1") Integer pageReserved,
			Model model) throws Exception {
		// セッション(依頼者IDを含んでいる)を取得
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		
		// ページ番号の保持
		session.setAttribute("pageTool", pageTool);
		session.setAttribute("pageShipped", pageShipped);
		session.setAttribute("pageReserved", pageReserved);
		
		String keyword;
		if(session.getAttribute("keyword") == null) {
			keyword = "";
		} else {
			keyword = (String) session.getAttribute("keyword");
		}
		
		// 貸し出し可能な工具全体のページ数
		int totalPagesTool = toolService.getKeywordTotalBorrowableToolPages(NUM_PER_PAGE, keyword);
		model.addAttribute("totalPagesTool", totalPagesTool);
		// 発送依頼済・発送済全体のページ数
		int totalPagesShipping = shippingRecordService.getShippingTotalPages(NUM_PER_PAGE, loginStatus.getId());
		model.addAttribute("totalPagesShipped", totalPagesShipping);
		// 予約済全体のページ数
		int totalPagesReserved = toolService.getTotalReservedToolPages(NUM_PER_PAGE, loginStatus.getId());
		model.addAttribute("totalPagesReserved", totalPagesReserved);
		// 現在の貸し出し可能な工具ページ番号
		model.addAttribute("currentPageTool", pageTool);
		// 現在の発送依頼済・発送済ページ番号
		model.addAttribute("currentPageShipped", pageShipped);
		// 現在の予約済ページ番号
		model.addAttribute("currentPageReserved", pageReserved);
		
		// 依頼者の発送情報
		model.addAttribute("shippingRecord", shippingRecordService.getShippingRecordByEmployeeId(loginStatus.getId()));
		
		// 現在の予約済工具のリスト
		List<Tool> reservedToolList = toolService.getLimitedReservedToolList(pageReserved, NUM_PER_PAGE, loginStatus.getId());
		
		model.addAttribute("reservedList", reservedToolList);
		// 現在の発送リスト
		model.addAttribute("shippingList", shippingRecordService.getLimitedShippingRecordListByEmployeeId(pageShipped, NUM_PER_PAGE, loginStatus.getId()));
		// 検索キーワード込みの貸し出し可能な工具のリスト
		model.addAttribute("toolList", toolService.getKeywordBorrowableToolListPerPage(pageTool, NUM_PER_PAGE, keyword));
		
		// 検索キーワード
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

		int previousPage = (int) session.getAttribute("pageTool");

		// 予約しようとしている工具が予約・出庫・削除されていないか確認
		if (!toolService.hasReservation(toolId)) {
			redirectAttributes.addFlashAttribute("errorMessage", "工具は予約済か出庫済、または削除済みです");
			
			// 戻るページ(元のページ)
			int pageTool = (int)session.getAttribute("pageTool");
			int pageShipped = (int)session.getAttribute("pageShipped");
			int pageReserved = (int)session.getAttribute("pageReserved");
			return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
		}

		// 問題がなければ、「予約」処理を実行
		rentalRecordService.reserveTool(loginStatus.getId(), toolId);
		redirectAttributes.addFlashAttribute("message", "予約しました。");
		
		// 予約後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		String keyword;
		keyword = (String) session.getAttribute("keyword");
		int totalPages = toolService.getKeywordTotalBorrowableToolPages(NUM_PER_PAGE, keyword);
		int pageTool = previousPage <= totalPages ? previousPage : totalPages;
		// 出庫依頼済・出庫済ページは元のページ
		int pageShipped = (int)session.getAttribute("pageShipped");
		// 予約済ページは元のページ
		int pageReserved = (int)session.getAttribute("pageReserved");
		return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
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
		int pageTool = (int)session.getAttribute("pageTool");
		int pageShipped = (int)session.getAttribute("pageShipped");
		// キャンセル後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int totalPages = toolService.getTotalReservedToolPages(NUM_PER_PAGE, loginStatus.getId());
		int previousPage = (int) session.getAttribute("pageReserved");
		int pageReserved = previousPage <= totalPages ? previousPage : totalPages;
		return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
	}
	
	//「出庫依頼」ボタン
	@GetMapping("/rental/request/{shippingId}")
	public String borrowTool(
			@PathVariable Integer shippingId,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		
		ShippingRecord shippingRecord = shippingRecordService.getShippingRecordByEmployeeId(loginStatus.getId());
		LocalDate shipDate = shippingRecord.getShipDate();  // 発送希望日
		LocalDate arrivalDate = shippingRecord.getArrivalDate();  // 到着希望日
		LocalDate today = LocalDate.now(); // 本日
		
		// 日付チェック
    if (shipDate.isBefore(today) || arrivalDate.isBefore(today)) {
        redirectAttributes.addFlashAttribute("errorMessage",
            "発送希望日または到着希望日が前の日付です。修正してください。");
      // 日付チェック後に戻るページ(元のページ)
	    int pageTool = (int) session.getAttribute("pageTool");
			int pageShipped = (int)session.getAttribute("pageShipped");
			int pageReserved = (int)session.getAttribute("pageReserved");
	    return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
    }
		
		//予約済工具リストの「出庫依頼」処理を実行
		rentalRecordService.borrowRequestTool(shippingId,loginStatus.getId());
		
		redirectAttributes.addFlashAttribute("message", "出庫依頼しました。");
		
		//出庫依頼後に戻るページ(元のページ)
		int pageTool = (int) session.getAttribute("pageTool");
		//出庫依頼後、出庫依頼済・出庫済ページは1ページ目を表示
		Integer pageShipped = 1;
		//予約済ページは1ページ目に戻す
		Integer pageReserved = 1;
		return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
	}
	
	//キーワード検索
	@GetMapping("/rental/selectKeyword")
	public String selectKeyword(
			@RequestParam String keyword,
			RedirectAttributes attrs) throws Exception {
		
		// キーワードの保持
		session.setAttribute("keyword", keyword);

		// 検索後に1ページ目を表示
		Integer pageTool = 1;
		// 検索後に戻るページ(元のページ)
		int pageShipped = (int)session.getAttribute("pageShipped");
		int pageReserved = (int)session.getAttribute("pageReserved");
		return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
	}

}
