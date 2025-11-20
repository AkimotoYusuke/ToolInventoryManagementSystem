package com.example.app.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Employee;
import com.example.app.domain.Login;
import com.example.app.login.LoginAuthority;
import com.example.app.login.LoginStatus;
import com.example.app.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EmployeeLoginController {

	private final EmployeeService employeeService;
	private final HttpSession session;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute(new Login());
		return "login-employee";
	}

	@PostMapping("/login")
	public String login(
			@Valid Login login,
			Errors errors) throws Exception {
		if(errors.hasErrors()) {
			return "login-employee";
		}

		// 正しいIDとパスワードの組み合わせか確認
		Employee employee = employeeService.getEmployeeByLoginIdAndStatusAct(login.getLoginId());
		if(employee == null || !login.isCorrectPassword(employee.getLoginPass()) || employee.getAuthorityType().getId() != LoginAuthority.EMPLOYEE) {
			errors.rejectValue("loginId", "error.incorrect_id_or_password");
			return "login-employee";
		}

		// セッションに認証情報を格納
		LoginStatus loginStatus = LoginStatus.builder()
				.id(employee.getId())
				.name(employee.getName())
				.loginId(employee.getLoginId())
				.authority(employee.getAuthorityType().getId())
				.build();
		session.setAttribute("loginStatus", loginStatus);
		return "redirect:/rental";
	}

	@GetMapping("/logout")
	public String logout(
			RedirectAttributes redirectAttributes) {
		session.removeAttribute("loginStatus");
		redirectAttributes.addFlashAttribute("message", "ログアウトしました。");
		return "redirect:/login";
	}

}
