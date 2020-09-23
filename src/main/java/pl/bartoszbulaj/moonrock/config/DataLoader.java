package pl.bartoszbulaj.moonrock.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.service.ApiKeyService;

@Component
public class DataLoader {

	private ApiKeyService apiKeyService;

	@Autowired
	public DataLoader(ApiKeyService apiKeyService) {
		this.apiKeyService = apiKeyService;
	}
}
