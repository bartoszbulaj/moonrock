package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.PositionDto;

public interface PositionManagerService {

	List<PositionDto> getPositions(String owner) throws IOException;

	String closePositionWithMarketOrder(PositionDto positionDto, String owner) throws IOException;

	void updateTrailingStop(PositionDto positionDto);

	void removePositionFromPositionsList(String owner, PositionDto positionDto);

	boolean buyMarket(String owner, String symbol);

	boolean sellMarket(String owner, String symbol);

}
