package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.PositionDto;

public interface PositionManagerService {

	List<PositionDto> getPositionsList(String owner) throws IOException;

	String closePositionWithMarketOrder(PositionDto positionDto, String owner) throws IOException;

	void updateTrailingStop(PositionDto positionDto);

	void removePositionFromPositionsList(String owner, PositionDto positionDto);

	String buyMarket(String owner, String symbol, BigDecimal quantity) throws IOException;

	String sellMarket(String owner, String symbol, BigDecimal quantity) throws IOException;

}
