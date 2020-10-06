package pl.bartoszbulaj.moonrock.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;
import pl.bartoszbulaj.moonrock.mapper.ApiKeyMapper;

public class ApiKeyMapperImpl implements ApiKeyMapper {

	private ModelMapper modelMapper;

	@Autowired
	public ApiKeyMapperImpl(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public ApiKeyDto mapToApiKeyDto(ApiKeyEntity apiKeyEntity) {

		return modelMapper.map(apiKeyEntity, ApiKeyDto.class);
	}

	@Override
	public ApiKeyEntity mapToApiKeyEntity(ApiKeyDto apiKeyDto) {

		return modelMapper.map(apiKeyDto, ApiKeyEntity.class);
	}

}
