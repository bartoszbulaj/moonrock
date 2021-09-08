package pl.bartoszbulaj.moonrock.validator.impl;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.validator.ApiKeyValidator;

@Component
public class ApiKeyValidatorImpl implements ApiKeyValidator {

	@Override
	public boolean isValid(ApiKeyDto apiKeyDto) {
		if (apiKeyDto != null) {
			return isOwnerValid(apiKeyDto)//
					&& isPublicValid(apiKeyDto)//
					&& isSecretValid(apiKeyDto);
		}
		return false;
	}

	@Override
	public boolean isPublicValid(String publicKey) {
		// TODO implement validation length etc
		return false;
	}

	@Override
	public boolean isSecretValid(byte[] secretKey) {
		// TODO implement validation length etc
		return false;
	}

	private boolean isPublicValid(ApiKeyDto apiKeyDto) {
		return !StringUtils.isBlank(apiKeyDto.getApiPublicKey())//
				&& apiKeyDto.getApiPublicKey().length() == 24;
	}

	private boolean isSecretValid(ApiKeyDto apiKeyDto) {
		return !StringUtils.isBlank(apiKeyDto.getApiSecretKey())//
				&& apiKeyDto.getApiSecretKey().length() == 48;
	}

	private boolean isOwnerValid(ApiKeyDto apiKeyDto) {
		return !StringUtils.isBlank(apiKeyDto.getOwner());
	}
}
