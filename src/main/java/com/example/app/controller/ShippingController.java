package com.example.app.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
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

	private static final int NUM_PER_PAGE = 5;

	private final ShippingRecordService service;
	private final HttpSession session;

	@GetMapping("/add")
	public String add(Model model) throws Exception {
		ShippingRecord shippingRecord = new ShippingRecord();
		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		shippingRecord.setSenderName(loginStatus.getName());
		shippingRecord.setSenderPostCode("143-0006");
		shippingRecord.setSenderAddress("東京都大田区平和島9876 株式会社 ABCD 工具管理センター");
		model.addAttribute("shippingRecord", shippingRecord);
		model.addAttribute("heading", "発送情報の入力");
		return "save-shipping";
	}

	@PostMapping("/add")
	public String add(
			@Valid ShippingRecord shippingRecord,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		if(errors.hasErrors()) {
			model.addAttribute("heading", "発送情報の入力");
			model.addAttribute("shippingRecord", shippingRecord);
			System.out.println("POST：エラー");
			return "save-shipping";
		}

		LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
		shippingRecord.setEmployeeId(loginStatus.getId());
//		System.out.println(shippingRecord);
		service.addShippingRecord(shippingRecord);
		redirectAttributes.addFlashAttribute("message", "発送情報を登録しました。");
		
		// 登録後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/rental?page=" + previousPage;
	}

//	@GetMapping("/edit/{id}")
//	public String edit(
//			@PathVariable Integer id,
//			Model model) throws Exception {
//		model.addAttribute("employee", service.getEmployeeById(id));
//		model.addAttribute("authorityTypeList", service.getAuthorityTypeList());
//		model.addAttribute("heading", "従業員の編集");
//		return "admin/save-employee";
//	}
//
//	@PostMapping("/edit/{id}")
//	public String edit(
//			@PathVariable Integer id,
//			@Valid Employee employee,
//			Errors errors,
//			Model model,
//			RedirectAttributes redirectAttributes) throws Exception {
//
//		String originalLoginId = service.getEmployeeById(id).getLoginId();
//		Employee comparisonEmployee = service.getEmployeeByLoginId(employee.getLoginId());
//
//		if(!employee.getLoginId().isBlank()) {
//			if(!originalLoginId.equals(employee.getLoginId()) && comparisonEmployee != null) {
//				if(comparisonEmployee.getStatus().equals("DEL")) {
//					errors.rejectValue("loginId", "error.existing_deletedemployee_loginId");
//				} else {
//					errors.rejectValue("loginId", "error.existing_employee_loginId");
//				}
//			}
//		}
//
//		if(errors.hasErrors()) {
//			model.addAttribute("authorityTypeList", service.getAuthorityTypeList());
//			model.addAttribute("heading", "従業員の編集");
//			return "admin/save-employee";
//		}
//
//		service.editEmployee(employee);
//		
//		// 自身の権限が、「依頼者」に編集された場合は、ログアウト
//		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");
//		Employee originalEmployee = service.getEmployeeById(id);
//		if(status.getId() == originalEmployee.getId()) {
//			if(originalEmployee.getAuthorityType().getId() == LoginAuthority.EMPLOYEE) {
//				session.removeAttribute("loginStatus");
//				redirectAttributes.addFlashAttribute("message", "自身の権限が「依頼者」に編集された為、ログアウトしました。");
//				return "redirect:/admin/login";
//			}
//		}
//		
//		redirectAttributes.addFlashAttribute("message", "従業員を編集しました。");
//		
//		// 編集後に戻るページ(元のページ)
//		int previousPage = (int) session.getAttribute("page");
//		return "redirect:/admin/employee/list?page=" + previousPage;
//	}

}
