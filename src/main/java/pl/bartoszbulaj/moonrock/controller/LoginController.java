package pl.bartoszbulaj.moonrock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String showLoginPage() {
		return "login.html";
	}

	@GetMapping("/login-error")
	public String showLoginPageWithError(Model model) {
		model.addAttribute("loginError", true);
		return "login.html";
	}
}
