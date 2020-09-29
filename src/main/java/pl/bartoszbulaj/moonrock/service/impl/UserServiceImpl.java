package pl.bartoszbulaj.moonrock.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.PositionEntity;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;
import pl.bartoszbulaj.moonrock.mapper.PositionMapper;
import pl.bartoszbulaj.moonrock.mapper.WalletMapper;
import pl.bartoszbulaj.moonrock.repository.WalletRepository;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final WalletRepository walletRepository;
	private ApiKeyService apiKeyService;
	private ObjectMapper objectMapper;
	private WalletMapper walletMapper;
	private PositionMapper positionMapper;

	@Autowired
	public UserServiceImpl(WalletRepository walletRepository, ObjectMapper objectMapper, ApiKeyService apiKeyService,
			WalletMapper walletMapper, PositionMapper positionMapper) {

		this.walletRepository = walletRepository;
		this.objectMapper = objectMapper;
		this.apiKeyService = apiKeyService;
		this.walletMapper = walletMapper;
		this.positionMapper = positionMapper;

	}

	public String calculateTimeToRequestExpiration() {
		Long time = (System.currentTimeMillis() + 10000L) / 1000L; // add 10 seconds to current time
		return time.toString();
	}

	// return String signature = HEX(HMAC_SHA256(apiSecret, text))
	public String calculateRequestSignature(String apiSecret, String expirationTime, String bitmexUrlEndPoint,
			String verb) {
		String algo = "HMACSHA256";
		String path = "/api/v1" + bitmexUrlEndPoint;
		String dataString = verb + path + expirationTime;

		try {
			Mac mac = Mac.getInstance(algo);
			SecretKeySpec secretKey = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), algo);
			mac.init(secretKey);

			return new String(Hex.encodeHex(mac.doFinal(dataString.getBytes(StandardCharsets.UTF_8))));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
			return "";
		}
	}

	public URL getCompleteUrl(String bitmexUrlEndPoint) throws MalformedURLException {
		return new URL(BitmexClientConfig.getBitmexApiUrl() + bitmexUrlEndPoint);
	}

	public WalletDto getWallet(String owner) throws IOException {
		ApiKeyDto apiKeyDto = apiKeyService.getOneByOwner(owner);
		if (apiKeyDto != null) {
			String requestMethod = "GET";
			String bitmexUrlEndPoint = "/user/wallet";

			HttpURLConnection connection = (HttpURLConnection) getCompleteUrl(bitmexUrlEndPoint).openConnection();

			String httpRequestResult = getStringResultFromHttpRequest(
					setConnectionHeaders(apiKeyDto, requestMethod, bitmexUrlEndPoint, connection));
			connection.disconnect();

			// regex deleted from json
			String stringToDeleteFromJSON = ",\"withdrawalLock\":(..)";
			String walletString = httpRequestResult.replaceAll(stringToDeleteFromJSON, "");

			WalletEntity wallet = objectMapper.readValue(walletString, WalletEntity.class);
			walletRepository.save(wallet);
			return walletMapper.walletToDto(wallet);
		} else {
			return null;
		}

	}

	public List<PositionDto> getPositions(String owner) throws IOException {
		ApiKeyDto apiKeyDto = apiKeyService.getOneByOwner(owner);
		if (apiKeyDto != null) {
			String requestMethod = "GET";
			String bitmexUrlEndPoint = "/position";

			HttpURLConnection connection = (HttpURLConnection) getCompleteUrl(bitmexUrlEndPoint).openConnection();
			String httpRequestResult = getStringResultFromHttpRequest(
					setConnectionHeaders(apiKeyDto, requestMethod, bitmexUrlEndPoint, connection));
			connection.disconnect();
			List<PositionEntity> positionList = Arrays
					.asList(objectMapper.readValue(httpRequestResult, PositionEntity[].class));

			return positionMapper.mapToPositionDtoList(positionList);
		} else {
			return new ArrayList<>();
		}
	}

	public String getStringResultFromHttpRequest(HttpURLConnection connection) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		return content.toString();
	}

	public HttpURLConnection setConnectionHeaders(ApiKeyDto apiKeyDto, String requestMethod, String bitmexUrlEndPoint,
			HttpURLConnection connection) throws ProtocolException {
		connection.setRequestMethod(requestMethod);
		connection.setRequestProperty("api-expires", calculateTimeToRequestExpiration());
		connection.setRequestProperty("api-key", apiKeyDto.getApiPublicKey());
		connection.setRequestProperty("api-signature", calculateRequestSignature(apiKeyDto.getApiSecretKey(),
				calculateTimeToRequestExpiration(), bitmexUrlEndPoint, requestMethod));
		return connection;
	}
}
