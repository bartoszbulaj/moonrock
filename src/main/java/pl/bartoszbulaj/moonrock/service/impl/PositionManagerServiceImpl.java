package pl.bartoszbulaj.moonrock.service.impl;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.mapper.PositionMapper;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.AuthService;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.OrderService;
import pl.bartoszbulaj.moonrock.service.PositionManagerService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PositionManagerServiceImpl implements PositionManagerService {

	private static final String POST_METHOD = "POST";
	private static final String GET_METHOD = "GET";
	private static final String BITMEX_ENDPOINT_ORDER = "/order";
	private static final String BITMEX_ENDPOINT_POSITION = "/position";
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
	public List<PositionDto> getPositionsList(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {
			Map<String, String> filters = new HashMap<>();
			filters.put("isOpen", "true");

			String urlString = authService.createConnectionUrlStringWithFilters(BITMEX_ENDPOINT_POSITION, filters);
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
		if (!positionDto.getIsOpen()) {
			throw new BusinessException("Position is closed");
		}

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("symbol", positionDto.getSymbol());
		paramsMap.put("orderQty", negativeQty(positionDto.getCurrentQty()));
		paramsMap.put("ordType", "Market");

		String urlStringWithParams = authService.createUrlWithParams(BITMEX_ENDPOINT_ORDER, paramsMap);
		HttpURLConnection connection = (HttpURLConnection) new URL(urlStringWithParams).openConnection();
		authService.addAuthRequestHeaders(owner, POST_METHOD, authService.removeUrlPrefix(urlStringWithParams),
				connection);
		String resultString = connectionService.getResultFromHttpRequest(connection);
		connection.disconnect();

		orderService.cancelAllActiveOrders(owner, positionDto.getSymbol());

		removePositionFromPositionsList(owner, positionDto);

		return resultString;
	}

	@Override
	public void updateTrailingStop(PositionDto positionDto) {
		// TODO updateTrailingStop
	}

	@Override
	public void removePositionFromPositionsList(String owner, PositionDto positionDto) {
		this.positionsList = this.positionsList.stream()
				.filter(p -> !p.getSymbol().equalsIgnoreCase(positionDto.getSymbol())).collect(Collectors.toList());
	}

	@Override
	public String buyMarket(String owner, String symbol, BigDecimal quantity) throws IOException {
		String symbolXTB = "XTBUSD"; // TODO remove and replace with param variable
		String side = "Buy";

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("symbol", symbolXTB);
		paramsMap.put("orderQty", quantity.toString());
		paramsMap.put("ordType", "Market");
		paramsMap.put("side", side);

		String urlStringWithParams = authService.createUrlWithParams(BITMEX_ENDPOINT_ORDER, paramsMap);
		HttpURLConnection connection = (HttpURLConnection) new URL(urlStringWithParams).openConnection();
		authService.addAuthRequestHeaders(owner, POST_METHOD, authService.removeUrlPrefix(urlStringWithParams),
				connection);
		String resultString = connectionService.getResultFromHttpRequest(connection);
		connection.disconnect();

		return resultString;
	}

	@Override
	public String sellMarket(String owner, String symbol, BigDecimal quantity) throws IOException {
		String symbolXTB = "XTBUSD"; // TODO remove and replace with param variable
		String side = "Sell";

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("symbol", symbolXTB);
		paramsMap.put("orderQty", quantity.toString());
		paramsMap.put("ordType", "Market");
		paramsMap.put("side", side);

		String urlStringWithParams = authService.createUrlWithParams(BITMEX_ENDPOINT_ORDER, paramsMap);
		HttpURLConnection connection = (HttpURLConnection) new URL(urlStringWithParams).openConnection();
		authService.addAuthRequestHeaders(owner, POST_METHOD, authService.removeUrlPrefix(urlStringWithParams),
				connection);
		String resultString = connectionService.getResultFromHttpRequest(connection);
		connection.disconnect();

		return resultString;
	}

	private String negativeQty(String orderQty) {
		return orderQty.startsWith("-") ? orderQty.substring(1) : "-" + orderQty;
	}
}
