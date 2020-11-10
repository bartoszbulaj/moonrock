package pl.bartoszbulaj.moonrock.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.dto.OrderDto;
import pl.bartoszbulaj.moonrock.mapper.OrderMapper;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.AuthService;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private static final String BITMEX_END_POINT = "/order";

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
			String requestMethod = "GET";

			String urlString = authService.createConnectionUrlString(BITMEX_END_POINT, null);
			HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
			authService.addAuthRequestHeaders(owner, requestMethod, urlString, connection);
			String resultString = connectionService.getHttpRequestResult(connection);
			connection.disconnect();

			return orderMapper.mapToOrderDtoList(resultString);
		}
	}

	@Override
	public List<OrderDto> getOpenOrders(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {
			String requestMethod = "GET";
			Map<String, String> filters = new HashMap<>();
			filters.put("open", "true");
			// TODO line belofe not works with filters map
			String urlString = authService.createConnectionUrlString(BITMEX_END_POINT, null);
			HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
			authService.addAuthRequestHeaders(owner, requestMethod, BITMEX_END_POINT, connection);
			String resultString = connectionService.getHttpRequestResult(connection);
			connection.disconnect();

			return orderMapper.mapToOrderDtoList(resultString);
		}
	}
}
