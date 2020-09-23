package pl.bartoszbulaj.moonrock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String showLoginPage() {
        return "login.html";
    }

    @RequestMapping("/login-error")
    public String showLoginPageWithError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }
}
