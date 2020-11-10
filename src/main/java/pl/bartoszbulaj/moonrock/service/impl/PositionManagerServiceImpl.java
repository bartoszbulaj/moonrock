package pl.bartoszbulaj.moonrock.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.mapper.PositionMapper;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.AuthService;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.PositionManagerService;

@Component
public class PositionManagerServiceImpl implements PositionManagerService {

	private ApiKeyService apiKeyService;
	private AuthService authService;
	private PositionMapper positionMapper;
	private ConnectionService connectionService;

	public PositionManagerServiceImpl(AuthService authService, ApiKeyService apiKeyService,
			PositionMapper positionMapper, ConnectionService connectionService) {
		this.connectionService = connectionService;
		this.apiKeyService = apiKeyService;
		this.authService = authService;
		this.positionMapper = positionMapper;
	}

	@Override
	public List<PositionDto> getPositions(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {
			String requestMethod = "GET";
			String urlEndPoint = "/position";

			HttpURLConnection connection = (HttpURLConnection) authService.createConnectionUrl(urlEndPoint)
					.openConnection();
			authService.addAuthRequestHeaders(owner, requestMethod, urlEndPoint, connection);
			String resultString = connectionService.getHttpRequestResult(connection);
			connection.disconnect();

			return positionMapper.mapToPositionDtoList(resultString);
		}
	}

	@Override
	public void closeWithMarket(PositionDto positionDto) {
		if (positionDto == null) {
			throw new IllegalArgumentException();
		}
		if (!positionDto.getIsOpen().booleanValue()) {
			throw new BusinessException("Position is closed");
		}
		// TODO create close market order and execute
	}

	@Override
	public void updateTrailingStop(PositionDto positionDto) {
		// TODO Auto-generated method stub

	}

}
