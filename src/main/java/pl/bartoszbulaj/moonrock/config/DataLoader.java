package pl.bartoszbulaj.moonrock.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.service.ApiKeyService;

@Component
public class DataLoader {

	private static boolean apiKeysLoading = true;

	private ApiKeyService apiKeyService;

	@Autowired
	public DataLoader(ApiKeyService apiKeyService) {
		this.apiKeyService = apiKeyService;
		insertApiKeysToDB();
	}

	private void insertApiKeysToDB() {
		if (apiKeysLoading) {
			try {
				File apiKeyPublic = new ClassPathResource("/apiKeyPublic.txt").getFile();
				File apiKeySecret = new ClassPathResource("/apiKeySecret.txt").getFile();

				apiKeyService.saveApiKey("admin",
						new String(Files.readAllBytes(apiKeyPublic.toPath()), StandardCharsets.UTF_8),
						Files.readAllBytes(apiKeySecret.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
