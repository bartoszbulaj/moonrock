
package pl.bartoszbulaj.moonrock.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.PositionManagerService;
import pl.bartoszbulaj.moonrock.service.UserService;
import pl.bartoszbulaj.moonrock.validator.ApiKeyValidator;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final String USER_PANEL = "user-panel.html";
	private UserService userService;
	private ApiKeyService apiKeyService;
	private ApiKeyValidator apiKeyValidator;
	private PositionManagerService positionManagerService;

	@Autowired
	public UserController(UserService userService, ApiKeyService apiKeyService, ApiKeyValidator apiKeyValidator,
			PositionManagerService positionManagerService) {
		this.userService = userService;
		this.apiKeyService = apiKeyService;
		this.apiKeyValidator = apiKeyValidator;
		this.positionManagerService = positionManagerService;
	}

	@GetMapping("/wallet")
	@ResponseBody
	public ResponseEntity<WalletDto> getWallet(@RequestParam String owner) {
		if (StringUtils.isBlank(owner)) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			WalletDto walletDto = userService.getWallet(owner);
			return new ResponseEntity<>(walletDto, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/position")
	@ResponseBody
	public ResponseEntity<List<PositionDto>> getOpenPositions(@RequestParam String owner) throws IOException {
		if (StringUtils.isBlank(owner)) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			List<PositionDto> positions = positionManagerService.getPositions(owner);
			return new ResponseEntity<>(positions, HttpStatus.OK);
		} catch (BusinessException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	// TODO delete or refactor to ResponseEntity -> JSON? other data?
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

	@PostMapping("/apikey")
	public ResponseEntity<String> addApiKeys(@RequestBody ApiKeyDto apiKeyDto) {
		if (apiKeyValidator.isValid(apiKeyDto)) {
			apiKeyService.saveApiKey(apiKeyDto.getOwner(), apiKeyDto.getApiPublicKey(),
					apiKeyDto.getApiSecretKey().getBytes());
			return new ResponseEntity<>("ApiKey saved", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Wrong request body", HttpStatus.NOT_ACCEPTABLE);
	}
}
