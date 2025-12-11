package com.example.app.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.ShippingRecord;
import com.example.app.login.LoginStatus;
import com.example.app.service.ShippingRecordService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingController {

	private static final String SENDER_POST_CODE = "143-0006";
	private static final String SENDER_ADDRESS = "東京都大田区平和島9876 株式会社 ABCD 工具管理センター";

	private final ShippingRecordService service;
	private final HttpSession session;
	
	@GetMapping("/show/{id}")
	public String show(
			@PathVariable Integer id,
			Model model) throws Exception {
		
		// 依頼者の発送情報
		model.addAttribute("shippingRecord", service.getShippingRecordById(id));
			return "show-shipping";
	}

	@GetMapping("/add")
	public String add(Model model) throws Exception {
		ShippingRecord shippingRecord = new ShippingRecord();
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		shippingRecord.setSenderName(loginStatus.getName());
		shippingRecord.setSenderPostCode(SENDER_POST_CODE);
		shippingRecord.setSenderAddress(SENDER_ADDRESS);
		shippingRecord.setSenderPhone(loginStatus.getPhone());
		model.addAttribute("shippingRecord", shippingRecord);
		model.addAttribute("heading", "発送情報の入力");
		return "save-shipping";
	}

	@PostMapping("/add")
	public String add(
			@Valid @ModelAttribute("shippingRecord") ShippingRecord shippingRecord,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		
		if(errors.hasErrors()) {
			model.addAttribute("heading", "発送情報の入力");
			return "save-shipping";
		}
		
		// 到着希望日 < 発送希望日 ならエラー
		if(shippingRecord.getArrivalDate().isBefore(shippingRecord.getShipDate())) {
			errors.rejectValue("arrivalDate", "error.arrivalDate_isBefore_shipDate");
			model.addAttribute("heading", "発送情報の入力");
			return "save-shipping";
		}

		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		shippingRecord.setEmployeeId(loginStatus.getId());
		service.addShippingRecord(shippingRecord);
		redirectAttributes.addFlashAttribute("message", "発送情報を登録しました。");
		
		// 登録後に戻るページ(元のページ)
		int pageTool = (int) session.getAttribute("pageTool");
		int pageShipped = (int)session.getAttribute("pageShipped");
		int pageReserved = (int)session.getAttribute("pageReserved");
		return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
	}

	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("heading", "発送情報の編集");
		// 依頼者の発送情報
		model.addAttribute("shippingRecord", service.getShippingRecordById(id));
		return "save-shipping";
	}

	@PostMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			@Valid @ModelAttribute("shippingRecord") ShippingRecord shippingRecord,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		if(errors.hasErrors()) {
			model.addAttribute("heading", "発送情報の編集");
			return "save-shipping";
		}
		
		// 到着希望日 < 発送希望日 ならエラー
		if(shippingRecord.getArrivalDate().isBefore(shippingRecord.getShipDate())) {
			errors.rejectValue("arrivalDate", "error.arrivalDate_isBefore_shipDate");
			model.addAttribute("heading", "発送情報の入力");
			return "save-shipping";
		}

		service.editShippingRecord(shippingRecord);
		redirectAttributes.addFlashAttribute("message", "発送情報を編集しました。");
		
		// 編集後に戻るページ(元のページ)
		int pageTool = (int) session.getAttribute("pageTool");
		int pageShipped = (int)session.getAttribute("pageShipped");
		int pageReserved = (int)session.getAttribute("pageReserved");
		return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
	}
	
	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Integer id,
			RedirectAttributes redirectAttributes) throws Exception {
		service.deleteShippingRecordById(id);
		redirectAttributes.addFlashAttribute("message", "発送情報を削除しました。");
		
		// 削除後に戻るページ(元のページ)
		int pageTool = (int) session.getAttribute("pageTool");
		int pageShipped = (int)session.getAttribute("pageShipped");
		int pageReserved = (int)session.getAttribute("pageReserved");
		return "redirect:/rental?pageTool=" + pageTool + "&pageShipped=" + pageShipped + "&pageReserved=" + pageReserved;
	}

}
