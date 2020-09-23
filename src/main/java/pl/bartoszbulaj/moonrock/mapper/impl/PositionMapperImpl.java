package pl.bartoszbulaj.moonrock.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.entity.PositionEntity;
import pl.bartoszbulaj.moonrock.mapper.PositionMapper;

@Component
public class PositionMapperImpl implements PositionMapper {

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PositionDto mapToPositionDto(PositionEntity positionEntity) {
		PositionDto positionDto = modelMapper.map(positionEntity, PositionDto.class);
		return positionDto;
	}

	@Override
	public List<PositionDto> mapToPositionDtoList(List<PositionEntity> positionList) {
		return positionList.stream().map(this::mapToPositionDto).collect(Collectors.toList());
	}

}
