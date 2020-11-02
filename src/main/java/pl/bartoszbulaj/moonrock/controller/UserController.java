
package pl.bartoszbulaj.moonrock.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final String USER_PANEL = "user-panel.html";
	private UserService userService;
	private ApiKeyService apiKeyService;

	@Autowired
	public UserController(UserService userService, ApiKeyService apiKeyService) {
		this.userService = userService;
		this.apiKeyService = apiKeyService;
	}

	@GetMapping("/wallet")
	@ResponseBody
	public String getWallet(@RequestParam String owner) {
		if (StringUtils.isBlank(owner)) {
			return "Cannot read wallet data";
		}
		try {
			return userService.getWallet(owner).toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "Cannot read wallet data";
		}
	}

	@GetMapping("/position")
	@ResponseBody
	public String getOpenPositions(@RequestParam String owner) throws IOException {
		if (StringUtils.isBlank(owner)) {
			return "Cannot read positions data";
		}
		return userService.getPositions(owner).toString();
	}

	@GetMapping("/panel")
	public String showUserPanel(Model model, @RequestParam String owner) throws IOException {
		if (StringUtils.isBlank(owner)) {
			return USER_PANEL;
		}
		try {
			ApiKeyDto apiKeyDto = apiKeyService.getOneByOwner(owner);
			WalletDto walletDto = userService.getWallet(owner);

			model.addAttribute("apiKeyDto", apiKeyDto);
			model.addAttribute("walletDto", walletDto);
			return USER_PANEL;
		} catch (Exception e) {
			e.printStackTrace();
			return USER_PANEL;
		}
	}
}
