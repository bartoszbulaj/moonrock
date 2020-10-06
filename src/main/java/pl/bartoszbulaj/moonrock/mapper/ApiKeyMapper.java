package pl.bartoszbulaj.moonrock.mapper;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;

public interface ApiKeyMapper {

	ApiKeyDto mapToApiKeyDto(ApiKeyEntity apiKeyEntity);

	ApiKeyEntity mapToApiKeyEntity(ApiKeyDto apiKeyDto);
}
