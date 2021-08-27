package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.PositionDto;

import java.io.IOException;
import java.util.List;

public interface PositionManagerService {

	List<PositionDto> getPositionsList(String owner) throws IOException;

	String closePositionWithMarketOrder(PositionDto positionDto, String owner) throws IOException;

	void updateTrailingStop(PositionDto positionDto);

	void removePositionFromPositionsList(String owner, PositionDto positionDto);

	boolean buyMarket(String owner, String symbol);

	boolean sellMarket(String owner, String symbol);

}
