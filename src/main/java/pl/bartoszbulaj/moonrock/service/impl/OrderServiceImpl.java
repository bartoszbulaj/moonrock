package pl.bartoszbulaj.moonrock.service.impl;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.OrderDto;
import pl.bartoszbulaj.moonrock.mapper.OrderMapper;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.AuthService;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.OrderService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

	private static final String BITMEX_END_POINT = "/order";
	private static final String GET_METHOD = "GET";
	private static final String DELETE_METHOD = "DELETE";

	private ApiKeyService apiKeyService;
	private AuthService authService;
	private ConnectionService connectionService;
	private OrderMapper orderMapper;

	@Autowired
	public OrderServiceImpl(ApiKeyService apiKeyService, AuthService authService, ConnectionService connectionService,
			OrderMapper orderMapper) {
		this.apiKeyService = apiKeyService;
		this.authService = authService;
		this.connectionService = connectionService;
		this.orderMapper = orderMapper;
	}

	@Override
	public List<OrderDto> getAllOrders(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {

			String urlString = authService.createConnectionUrlStringWithFilters(BITMEX_END_POINT, null);
			HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
			authService.addAuthRequestHeaders(owner, GET_METHOD, urlString, connection);
			String resultString = connectionService.getResultFromHttpRequest(connection);
			connection.disconnect();

			return orderMapper.mapToOrderDtoList(resultString);
		}
	}

	@Override
	public List<OrderDto> getOpenOrders(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {
			Map<String, String> filters = new HashMap<>();
			filters.put("open", "true");
			String urlString = authService.createConnectionUrlStringWithFilters(BITMEX_END_POINT, filters);
			HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
			authService.addAuthRequestHeaders(owner, GET_METHOD, authService.removeUrlPrefix(urlString), connection);
			String resultString = connectionService.getResultFromHttpRequest(connection);
			connection.disconnect();

			return orderMapper.mapToOrderDtoList(resultString);
		}
	}

	@Override
	public List<OrderDto> getOpenOrders(String owner, String symbol) throws IOException {
		List<OrderDto> openOrdersList = getOpenOrders(owner);
		return openOrdersList.stream().filter(p -> p.getSymbol().equalsIgnoreCase(symbol)).collect(Collectors.toList());
	}

	@Override
	public OrderDto createOrderCloseWithMarket(String symbol, String orderQty) {
		if (StringUtils.isBlank(symbol) && !BitmexClientConfig.getActiveInstruments().contains(symbol.toUpperCase())) {
			throw new IllegalArgumentException("wrong argument");
		}
		return new OrderDto(symbol, "Market", orderQty);
	}

	@Override
	public String cancelAllActiveOrders(String owner, String symbol) throws IOException {
		if (StringUtils.isBlank(symbol) || StringUtils.isBlank(owner)) {
			throw new IllegalArgumentException();
		}
		String urlString = authService.createConnectionUrlStringWithFilters(BITMEX_END_POINT + "/all", null);
		HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
		authService.addAuthRequestHeaders(owner, DELETE_METHOD, authService.removeUrlPrefix(urlString), connection);
		String resultString = connectionService.getResultFromHttpRequest(connection);
		connection.disconnect();

		return resultString;

	}

}
