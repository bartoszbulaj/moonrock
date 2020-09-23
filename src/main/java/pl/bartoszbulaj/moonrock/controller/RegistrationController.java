package pl.bartoszbulaj.moonrock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.bartoszbulaj.moonrock.service.RegistrationService;
import pl.bartoszbulaj.moonrock.service.impl.RegistrationServiceImpl;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

	private final RegistrationService registrationService;

	@Autowired
	public RegistrationController(RegistrationServiceImpl registrationService) {
		this.registrationService = registrationService;
	}

	@GetMapping
	public String getRegistrationPage() {
		return "registration.html";
	}

	@PostMapping
	public String registerUser(String username, String password) {
		registrationService.registerUser(username, password);
		return "redirect:/";
	}
}
