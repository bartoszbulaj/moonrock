package pl.bartoszbulaj.moonrock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.mapper.ApiKeyMapper;
import pl.bartoszbulaj.moonrock.repository.ApiKeyRepository;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.CryptographicService;

@Service
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService {

	private ApiKeyRepository apiKeyRepository;
	private ApiKeyMapper apiKeyMapper;
	private CryptographicService cryptographicService;

	@Autowired
	public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository, ApiKeyMapper apiKeyMapper,
			CryptographicService cryptographicService) {
		this.apiKeyRepository = apiKeyRepository;
		this.apiKeyMapper = apiKeyMapper;
		this.cryptographicService = cryptographicService;
	}

	@Override
	public ApiKeyDto getOneByOwner(String owner) {
		ApiKeyEntity apiKey = apiKeyRepository.getFirstByOwnerEquals(owner)
				.orElseThrow(() -> new BusinessException("Cannot find apiKey with given owner: \"" + owner + "\""));
		return apiKeyMapper.mapToApiKeyDto(apiKey);
	}

	@Override
	public ApiKeyEntity saveApiKey(String owner, String apiPublicKey, byte[] apiSecretKey) {
		if (StringUtils.isBlank(owner) || StringUtils.isBlank(apiPublicKey) || apiSecretKey.length == 0) {
			throw new IllegalArgumentException("Illegal arguments");
		}
		try {
			ApiKeyEntity apiKeyEntity = new ApiKeyEntity(owner, apiPublicKey,
					cryptographicService.encryptPassword(apiSecretKey));
			return apiKeyRepository.save(apiKeyEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
