package pl.bartoszbulaj.moonrock.mapper.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.entity.PositionEntity;
import pl.bartoszbulaj.moonrock.mapper.PositionMapper;

@Component
public class PositionMapperImpl implements PositionMapper {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public PositionDto mapToPositionDto(PositionEntity positionEntity) {
		return modelMapper.map(positionEntity, PositionDto.class);
	}

	@Override
	public List<PositionDto> mapToPositionDtoList(List<PositionEntity> positionList) {
		return positionList.stream().map(this::mapToPositionDto).collect(Collectors.toList());
	}

	@Override
	public List<PositionDto> mapToPositionDtoList(String jsonString) throws IOException {
		return objectMapper.readValue(jsonString, new TypeReference<List<PositionDto>>() {
		});
	}

}
