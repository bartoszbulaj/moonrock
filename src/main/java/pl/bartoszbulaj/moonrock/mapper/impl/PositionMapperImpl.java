package pl.bartoszbulaj.moonrock.mapper.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.entity.PositionEntity;
import pl.bartoszbulaj.moonrock.mapper.PositionMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	public List<PositionDto> mapToPositionDtoList(String jsonString) {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return objectMapper.readValue(jsonString, new TypeReference<List<PositionDto>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

}
