package pl.bartoszbulaj.moonrock.validator;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;

public interface ApiKeyValidator {

	boolean isValid(ApiKeyDto apiKeyDto);
}
