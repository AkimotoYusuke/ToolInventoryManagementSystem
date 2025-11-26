package com.example.app.controller.admin;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Tool;
import com.example.app.service.RentalRecordService;
import com.example.app.service.ShippingRecordService;
import com.example.app.service.ToolService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/tool")
@RequiredArgsConstructor
public class ToolController {

	private static final int NUM_PER_PAGE = 5;
	private static final int RECORD_NUM = 3;

	private final ToolService service;
	private final RentalRecordService rentalRecordService;
	private final ShippingRecordService shippingRecordService;
	private final HttpSession session;

	@GetMapping("/list")
	public String list(
			@RequestParam(name="page", defaultValue="1") Integer page,
			Model model) throws Exception {
		// 詳細・追加・編集ページから戻る際に利用
		session.setAttribute("page", page);
		
		// 工具依頼リスト
		model.addAttribute("shippingRequestList", shippingRecordService.getShippingRecordListIsShippingRequest());
		
		// 出庫済の工具リスト
		model.addAttribute("shippedAtList", shippingRecordService.getShippingRecordListIsShipped());
		
	  int totalPages = service.getTotalPages(NUM_PER_PAGE);
	  model.addAttribute("totalPages", totalPages);
	  model.addAttribute("currentPage", page);
		model.addAttribute("toolList", service.getToolListPerPage(page, NUM_PER_PAGE));
		return "admin/list-tool";
	}

	@GetMapping("/show/{id}")
	public String show(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("tool", service.getToolById(id));
		model.addAttribute("recordList", rentalRecordService.getLatestRentalRecordListByToolId(id, RECORD_NUM));
		return "admin/show-tool";
	}

	@GetMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("tool", new Tool());
		model.addAttribute("makerTypeList", service.getMakerTypeList());
		model.addAttribute("heading", "工具の追加");
		return "admin/save-tool";
	}

	@PostMapping("/add")
	public String add(
			@Valid Tool tool,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		if(!tool.getMgmtId().isBlank()) {
			Tool comparisonTool = service.getToolMgmtId(tool.getMgmtId());
			if(comparisonTool != null) {
				if(comparisonTool.getStatus().equals("DEL")) {
					errors.rejectValue("mgmtId", "error.existing_employee_loginId");
				} else {
					errors.rejectValue("mgmtId", "error.existing_mgmt_id");
				}
			}
		}

		if(errors.hasErrors()) {
			model.addAttribute("makerTypeList", service.getMakerTypeList());
			model.addAttribute("heading", "工具の追加");
			return "admin/save-tool";
		}

		service.addTool(tool);
		redirectAttributes.addFlashAttribute("message", "工具を追加しました。");
		
		// 追加後に戻るページ(⇒最終ページ)
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		return "redirect:/admin/tool/list?page=" + totalPages;
	}

	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("tool", service.getToolById(id));
		model.addAttribute("makerTypeList", service.getMakerTypeList());
		model.addAttribute("heading", "工具の編集");
		return "admin/save-tool";
	}

	@PostMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			@Valid Tool tool,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		String originalToolMgmtId = service.getToolById(id).getMgmtId();
		Tool comparisonTool = service.getToolMgmtId(tool.getMgmtId());

		if(!tool.getMgmtId().isBlank()) {
			if(!originalToolMgmtId.equals(tool.getMgmtId()) && comparisonTool != null) {
				if(comparisonTool.getStatus().equals("DEL")) {
					errors.rejectValue("mgmtId", "error.existing_employee_loginId");
				} else {
					errors.rejectValue("mgmtId", "error.existing_mgmt_id");
				}
			}
		}

		if(errors.hasErrors()) {
			model.addAttribute("makerTypeList", service.getMakerTypeList());
			model.addAttribute("heading", "工具の編集");
			return "admin/save-tool";
		}

		service.editTool(tool);
		redirectAttributes.addFlashAttribute("message", "工具を編集しました。");
		
		// 編集後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/admin/tool/list?page=" + previousPage;
	}

	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Integer id,
			RedirectAttributes redirectAttributes) throws Exception {
		service.deleteToolById(id);
		redirectAttributes.addFlashAttribute("message", "工具を削除しました。");
		
		// 削除後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int previousPage = (int) session.getAttribute("page");
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		int page = previousPage <= totalPages ? previousPage : totalPages;
		return "redirect:/admin/tool/list?page=" + page;
	}
	
	@GetMapping("/showShipping/{id}")
	public String showShipping(
			@PathVariable Integer id,
			Model model) throws Exception {
		
		// 依頼者の発送情報
		model.addAttribute("shippingRecord", shippingRecordService.getShippingRecordById(id));
			return "show-shipping";
	}
	
	//出庫依頼済・出庫済工具リスト
	@GetMapping("/toolList/{shippingId}")
	public String rentalToolList(
			@PathVariable("shippingId") Integer shippingId,
			Model model) throws Exception {

		model.addAttribute("shippingId", shippingId);
	  // 依頼者の発送情報で発送日のデータを取得
		model.addAttribute("shippingRecord", shippingRecordService.getShippingRecordById(shippingId));
		// 現在、出庫依頼・出庫済工具のリスト
		model.addAttribute("borrowingList", service.getBorrowingToolList(shippingId));
		
		return "show-tool-list";
	}
	
//「出庫」ボタン
@GetMapping("/borrow/{shippingId}")
public String borrowTool(
		@PathVariable("shippingId") Integer shippingId,
		RedirectAttributes redirectAttributes) throws Exception {

	//「出庫」処理を実行
	rentalRecordService.borrowTool(shippingId);
	redirectAttributes.addFlashAttribute("message", "出庫処理をしました。");
	
	//出庫後に戻るページ(⇒最終ページ)
	int totalPages = service.getTotalPages(NUM_PER_PAGE);
	return "redirect:/admin/tool/list?page=" + totalPages;
}

	//「入庫」ボタン
	@GetMapping("/return/{shippingId}")
	public String returnTool(
			@PathVariable("shippingId") Integer shippingId,
			RedirectAttributes redirectAttributes) throws Exception {
	
		//「入庫」処理を実行
		rentalRecordService.returnTool(shippingId);
		redirectAttributes.addFlashAttribute("message", "入庫処理をしました。");
		
		// 返却後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/admin/tool/list?page=" + previousPage;
	}

}
