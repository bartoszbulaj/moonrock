package pl.bartoszbulaj.moonrock.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.mapper.PositionMapper;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.AuthService;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.OrderService;
import pl.bartoszbulaj.moonrock.service.PositionManagerService;

@Component
@Slf4j
public class PositionManagerServiceImpl implements PositionManagerService {

	private static final String POST_METHOD = "POST";
	private static final String GET_METHOD = "GET";
	private ApiKeyService apiKeyService;
	private AuthService authService;
	private PositionMapper positionMapper;
	private ConnectionService connectionService;
	private OrderService orderService;

	private List<PositionDto> positionsList;

	public PositionManagerServiceImpl(AuthService authService, ApiKeyService apiKeyService,
			PositionMapper positionMapper, ConnectionService connectionService, OrderService orderService) {
		this.connectionService = connectionService;
		this.apiKeyService = apiKeyService;
		this.authService = authService;
		this.positionMapper = positionMapper;
		this.orderService = orderService;

	}

	@Override
	public List<PositionDto> getPositions(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {
			String bitmexEndPoint = "/position";
			Map<String, String> filters = new HashMap<>();
			filters.put("isOpen", "true");

			String urlString = authService.createConnectionUrlStringWithFilters(bitmexEndPoint, filters);
			HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
			authService.addAuthRequestHeaders(owner, GET_METHOD, authService.removeUrlPrefix(urlString), connection);
			String resultString = connectionService.getResultFromHttpRequest(connection);
			connection.disconnect();

			this.positionsList = positionMapper.mapToPositionDtoList(resultString);
			return this.positionsList;
		}
	}

	@Override
	public String closePositionWithMarketOrder(PositionDto positionDto, String owner) throws IOException {
		if (positionDto == null) {
			throw new IllegalArgumentException();
		}
		if (!positionDto.getIsOpen().booleanValue()) {
			throw new BusinessException("Position is closed");
		}
		String bitmexEndPoint = "/order";

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("symbol", positionDto.getSymbol());
		paramsMap.put("orderQty", negativeQty(positionDto.getCurrentQty()));
		paramsMap.put("ordType", "Market");

		String urlStringWithParams = authService.createUrlWithParams(bitmexEndPoint, paramsMap);
		HttpURLConnection connection = (HttpURLConnection) new URL(urlStringWithParams).openConnection();
		authService.addAuthRequestHeaders(owner, POST_METHOD, authService.removeUrlPrefix(urlStringWithParams),
				connection);
		String resultString = connectionService.getResultFromHttpRequest(connection);
		connection.disconnect();

		orderService.closeAllOrders(owner, positionDto.getSymbol());

		removePositionFromPositionsList(owner, positionDto);

		return resultString;
	}

	@Override
	public void updateTrailingStop(PositionDto positionDto) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removePositionFromPositionsList(String owner, PositionDto positionDto) {
		this.positionsList = this.positionsList.stream()
				.filter(p -> !p.getSymbol().equalsIgnoreCase(positionDto.getSymbol())).collect(Collectors.toList());
	}

	@Override
	public boolean buyMarket(String owner, String symbol) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sellMarket(String owner, String symbol) {
		// TODO Auto-generated method stub
		return false;
	}

	private String negativeQty(String orderQty) {
		return orderQty.startsWith("-") ? orderQty.substring(1) : "-" + orderQty;
	}
}
