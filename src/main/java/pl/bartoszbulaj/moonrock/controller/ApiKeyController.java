package pl.bartoszbulaj.moonrock.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.impl.ApiKeyServiceImpl;

@RestController
@RequestMapping("/apikey")
public class ApiKeyController {

	private ApiKeyService apiKeyService;

	@Autowired
	public ApiKeyController(ApiKeyServiceImpl apiKeyService) {
		this.apiKeyService = apiKeyService;
	}

	@PostMapping
	public void saveApiKey(@RequestBody ApiKeyEntity apiKey, Principal principal) {
		apiKeyService.saveApiKey(new ApiKeyEntity(principal.getName(), apiKey.getApiPublicKey(),
				apiKeyService.encryptSecretKey(apiKey.getApiSecretKey(), principal.getName())));
	}

	@GetMapping
	public List<ApiKeyEntity> getAllApiKeys() {
		return apiKeyService.getAllApiKeys();
	}

	@GetMapping(value = "/{id}")
	public Optional<ApiKeyDto> getApiKeyById(@PathVariable Long id, Principal principal) {
		return apiKeyService.getOneById(id, principal.getName());
	}
}
