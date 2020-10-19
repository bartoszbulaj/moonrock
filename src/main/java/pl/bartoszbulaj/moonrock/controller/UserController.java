
package pl.bartoszbulaj.moonrock.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserService userService;
	private ApiKeyService apiKeyService;

	@Autowired
	public UserController(UserService userService, ApiKeyService apiKeyService) {
		this.userService = userService;
		this.apiKeyService = apiKeyService;
	}

	@GetMapping("/wallet")
	@ResponseBody
	public String getWallet() {
		return "here should be wallet data";
	}

	@GetMapping("/position")
	@ResponseBody
	public String getOpenPositions() throws IOException {
		return "here should be positions data";
	}

	@GetMapping("/panel")
	public String showUserPanel(Model model, Principal principal) throws IOException {
		try {
			ApiKeyDto apiKeyDto = apiKeyService.getOneByOwner(principal.getName());
			WalletDto walletDto = userService.getWallet(principal.getName());

			model.addAttribute("apiKeyDto", apiKeyDto);
			model.addAttribute("walletDto", walletDto);
			return "user-panel.html";
		} catch (Exception e) {
			e.printStackTrace();
			return "user-panel.html";
		}

	}
}
