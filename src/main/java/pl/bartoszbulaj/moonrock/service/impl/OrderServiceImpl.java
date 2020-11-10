package pl.bartoszbulaj.moonrock.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

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

	private ApiKeyService apiKeyService;
	private AuthService authService;
	private ConnectionService connectionService;
	private OrderMapper orderMapper;

	@Override
	public List<OrderDto> getAllOrders(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {
			String requestMethod = "GET";
			String urlEndPoint = "/order";

			HttpURLConnection connection = (HttpURLConnection) authService.createConnectionUrl(urlEndPoint)
					.openConnection();
			authService.addAuthRequestHeaders(owner, requestMethod, urlEndPoint, connection);
			String resultString = connectionService.getHttpRequestResult(connection);
			connection.disconnect();

			return orderMapper.mapToOrderDtoList(resultString);
		}
	}

}
