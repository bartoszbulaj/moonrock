package pl.bartoszbulaj.moonrock.integration.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;
import pl.bartoszbulaj.moonrock.integration.DatabaseIntegration;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.validator.EmailSenderValidator;

import javax.mail.MessagingException;

@Service
public class DatabaseIntegrationImpl implements DatabaseIntegration {

	private final AppConfigurationService emailSenderService;
	private final EmailSenderValidator emailSenderValidator;
	private final ApiKeyService apiKeyService;

	@Autowired
	public DatabaseIntegrationImpl(AppConfigurationService emailSenderService,
			EmailSenderValidator emailSenderValidator, ApiKeyService apiKeyService) {
		this.emailSenderService = emailSenderService;
		this.emailSenderValidator = emailSenderValidator;
		this.apiKeyService = apiKeyService;
	}
	@Override
	public EmailSenderDto saveEmailSenderCredentials(EmailSenderDto emailSenderDto) throws MessagingException {
		if (emailSenderValidator.isEmailSenderValid(emailSenderDto)) {
			return emailSenderService.saveEmailSenderCredentials(emailSenderDto);
		}
		throw new IllegalArgumentException("Cannot save emailSenderDto");
	}

	@Override
	public boolean saveApiKeys(String owner, String apiPublicKey, byte[] apiSecretKey) {
		ApiKeyEntity apiKeyEntity = apiKeyService.saveApiKey(owner, apiPublicKey, apiSecretKey);
		return apiKeyEntity != null;
	}
}
