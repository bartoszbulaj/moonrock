package pl.bartoszbulaj.moonrock.service;

import java.util.List;
import java.util.Optional;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;

public interface ApiKeyService {

	public Optional<ApiKeyDto> getOneById(Long id, String owner);

	public String encryptSecretKey(String secretKey, String owner);

	public ApiKeyDto getOneByOwner(String owner);

	public List<ApiKeyEntity> getAllApiKeys();

	public void saveApiKey(ApiKeyEntity apiKey);
}
