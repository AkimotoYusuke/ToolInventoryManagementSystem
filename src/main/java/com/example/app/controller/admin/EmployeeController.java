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

import com.example.app.domain.Employee;
import com.example.app.login.LoginAuthority;
import com.example.app.login.LoginStatus;
import com.example.app.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/employee")
@RequiredArgsConstructor
public class EmployeeController {

	private static final int NUM_PER_PAGE = 5;

	private final EmployeeService service;
	private final HttpSession session;

	@GetMapping("/list")
	public String list(
			@RequestParam(name="page", defaultValue="1") Integer page,
			Model model) throws Exception {
		// 詳細・追加・編集ページから戻る際に利用
		session.setAttribute("page", page);
				
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);
		model.addAttribute("employeeList", service.getEmployeeListPerPage(page, NUM_PER_PAGE));
		return "admin/list-employee";
	}

	@GetMapping("/add")
	public String add(Model model) throws Exception {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		model.addAttribute("authorityTypeList", service.getAuthorityTypeList());
		model.addAttribute("heading", "従業員の追加");
		return "admin/save-employee";
	}

	@PostMapping("/add")
	public String add(
			@Valid Employee employee,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		if(!employee.getLoginId().isBlank()) {
			Employee comparisonEmployee = service.getEmployeeByLoginId(employee.getLoginId());
			if(comparisonEmployee != null) {
				if(comparisonEmployee.getStatus().equals("DEL")) {
					errors.rejectValue("loginId", "error.existing_deletedemployee_loginId");
				} else {
					errors.rejectValue("loginId", "error.existing_employee_loginId");
				}
			}
		}

		if(errors.hasErrors()) {
			model.addAttribute("heading", "従業員の追加");
			model.addAttribute("authorityTypeList", service.getAuthorityTypeList());
			return "admin/save-employee";
		}

		service.addEmployee(employee);
		redirectAttributes.addFlashAttribute("message", "従業員を追加しました。");
		
		// 追加後に戻るページ(⇒最終ページ)
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		return "redirect:/admin/employee/list?page=" + totalPages;
	}

	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("employee", service.getEmployeeById(id));
		model.addAttribute("authorityTypeList", service.getAuthorityTypeList());
		model.addAttribute("heading", "従業員の編集");
		return "admin/save-employee";
	}

	@PostMapping("/edit/{id}")
	public String edit(
			@PathVariable Integer id,
			@Valid Employee employee,
			Errors errors,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {

		String originalLoginId = service.getEmployeeById(id).getLoginId();
		Employee comparisonEmployee = service.getEmployeeByLoginId(employee.getLoginId());

		if(!employee.getLoginId().isBlank()) {
			if(!originalLoginId.equals(employee.getLoginId()) && comparisonEmployee != null) {
				if(comparisonEmployee.getStatus().equals("DEL")) {
					errors.rejectValue("loginId", "error.existing_deletedemployee_loginId");
				} else {
					errors.rejectValue("loginId", "error.existing_employee_loginId");
				}
			}
		}

		if(errors.hasErrors()) {
			model.addAttribute("authorityTypeList", service.getAuthorityTypeList());
			model.addAttribute("heading", "従業員の編集");
			return "admin/save-employee";
		}

		service.editEmployee(employee);
		
		// 自身の権限が、「依頼者」に編集された場合は、ログアウト
		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");
		Employee originalEmployee = service.getEmployeeById(id);
		if(status.getId() == originalEmployee.getId()) {
			if(originalEmployee.getAuthorityType().getId() == LoginAuthority.EMPLOYEE) {
				session.removeAttribute("loginStatus");
				redirectAttributes.addFlashAttribute("message", "自身の権限が「依頼者」に編集された為、ログアウトしました。");
				return "redirect:/admin/login";
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "従業員を編集しました。");
		
		// 編集後に戻るページ(元のページ)
		int previousPage = (int) session.getAttribute("page");
		return "redirect:/admin/employee/list?page=" + previousPage;
	}

	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Integer id,
			RedirectAttributes redirectAttributes) throws Exception {
		service.deleteEmployeeById(id);
		
		// 自身を削除した場合は、ログアウト
		LoginStatus status = (LoginStatus) session.getAttribute("loginStatus");
		Employee originalEmployee = service.getEmployeeById(id);
		if(status.getId() == originalEmployee.getId()) {
			if (originalEmployee.getStatus().equals("DEL")) {
				session.removeAttribute("loginStatus");
				redirectAttributes.addFlashAttribute("message", "自身を削除した為、ログアウトしました。");
				return "redirect:/admin/login";
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "従業員を削除しました。");
		
		// 削除後に戻るページ(⇒ページ数が減って、元のページが無くなった場合は最終ページ)
		int previousPage = (int) session.getAttribute("page");
		int totalPages = service.getTotalPages(NUM_PER_PAGE);
		int page = previousPage <= service.getTotalPages(NUM_PER_PAGE) ? previousPage : totalPages;
		return "redirect:/admin/employee/list?page=" + page;
	}

}
