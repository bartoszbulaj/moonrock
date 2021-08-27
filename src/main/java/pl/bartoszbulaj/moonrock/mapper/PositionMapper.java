package pl.bartoszbulaj.moonrock.mapper;

import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.entity.PositionEntity;

import java.util.List;

public interface PositionMapper {

	PositionDto mapToPositionDto(PositionEntity position);

	List<PositionDto> mapToPositionDtoList(List<PositionEntity> positionList);

	List<PositionDto> mapToPositionDtoList(String jsonString);
}
