package pl.bartoszbulaj.moonrock.mapper;

import java.io.IOException;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.entity.PositionEntity;

public interface PositionMapper {

	PositionDto mapToPositionDto(PositionEntity position);

	List<PositionDto> mapToPositionDtoList(List<PositionEntity> positionList);

	List<PositionDto> mapToPositionDtoList(String jsonString) throws IOException;
}
