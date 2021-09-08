package pl.bartoszbulaj.moonrock.validator;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;

public interface ApiKeyValidator {

	boolean isValid(ApiKeyDto apiKeyDto);

	boolean isPublicValid(String publicKey);

	boolean isSecretValid(byte[] secretKey);
}
