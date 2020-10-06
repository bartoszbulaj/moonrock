package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;

public interface ApiKeyService {

	ApiKeyDto getOneByOwner(String owner);

	ApiKeyEntity saveApiKey(String owner, String apiPublicKey, byte[] apiSecretKey);
}
