package pl.bartoszbulaj.moonrock.service.impl;

import java.util.List;
import java.util.Optional;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;
import pl.bartoszbulaj.moonrock.repository.ApiKeyRepository;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;

@Service
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService {

	private ApiKeyRepository apiKeyRepository;

	@Autowired
	public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
		this.apiKeyRepository = apiKeyRepository;
	}

	public Optional<ApiKeyDto> getOneById(Long id, String owner) {
		ApiKeyEntity apiKey = apiKeyRepository.getOne(id);
		if (apiKey.getOwner().equals(owner)) {
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword(owner);
			ApiKeyDto apiKeyDTO = new ApiKeyDto(apiKey.getId(), apiKey.getOwner(), apiKey.getApiPublicKey(),
					textEncryptor.decrypt(apiKey.getApiSecretKey()));
			return Optional.of(apiKeyDTO);
		} else {
			return Optional.empty();
		}
	}

	public String encryptSecretKey(String secretKey, String owner) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(owner);
		return textEncryptor.encrypt(secretKey);
	}

	public ApiKeyDto getOneByOwner(String owner) {
		ApiKeyEntity apiKey = apiKeyRepository.getFirstByOwnerEquals(owner);
		if (apiKey != null) {
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword(owner);
			ApiKeyDto apiKeyDTO = new ApiKeyDto(apiKey.getId(), apiKey.getOwner(), apiKey.getApiPublicKey(),
					textEncryptor.decrypt(apiKey.getApiSecretKey()));
			return apiKeyDTO;
		} else {
			return null;
		}
	}

	@Override
	public List<ApiKeyEntity> getAllApiKeys() {
		return apiKeyRepository.findAll();
	}

	@Override
	public void saveApiKey(ApiKeyEntity apiKey) {
		apiKeyRepository.save(apiKey);
	}
}
