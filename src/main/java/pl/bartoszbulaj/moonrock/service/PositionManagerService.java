package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.PositionDto;

public interface PositionManagerService {

	List<PositionDto> getPositions(String owner) throws IOException;

	void closeWithMarket(PositionDto positionDto);

	void updateTrailingStop(PositionDto positionDto);
}
