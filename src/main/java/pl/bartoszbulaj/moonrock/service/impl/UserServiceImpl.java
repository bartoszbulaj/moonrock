package pl.bartoszbulaj.moonrock.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;
import pl.bartoszbulaj.moonrock.mapper.PositionMapper;
import pl.bartoszbulaj.moonrock.mapper.WalletMapper;
import pl.bartoszbulaj.moonrock.repository.WalletRepository;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.CryptographicService;
import pl.bartoszbulaj.moonrock.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	private final WalletRepository walletRepository;
	private ApiKeyService apiKeyService;
	private WalletMapper walletMapper;
	private PositionMapper positionMapper;
	private CryptographicService cryptographicService;

	@Autowired
	public UserServiceImpl(WalletRepository walletRepository, ApiKeyService apiKeyService, WalletMapper walletMapper,
			PositionMapper positionMapper, CryptographicService cryptographicService) {
		this.walletRepository = walletRepository;
		this.apiKeyService = apiKeyService;
		this.walletMapper = walletMapper;
		this.positionMapper = positionMapper;
		this.cryptographicService = cryptographicService;
	}

	@Override
	public WalletDto getWallet(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {
			String requestMethod = "GET";
			String urlEndPoint = "/user/wallet";

			HttpURLConnection connection = (HttpURLConnection) getCompleteUrl(urlEndPoint).openConnection();
			String httpRequestResult = getStringResultFromHttpRequest(
					setConnectionHeaders(apiKeyService.getOneByOwner(owner), requestMethod, urlEndPoint, connection));
			connection.disconnect();

			String walletResultString = removeWithdrawalLockFieldFromResultString(httpRequestResult);
			return walletMapper.mapToWalletDto(walletResultString);
		}
	}

	@Override
	public WalletEntity saveWallet(WalletDto walletDto) {
		return walletRepository.save(walletMapper.mapToWalletEntity(walletDto));
	}

	@Override
	public List<PositionDto> getPositions(String owner) throws IOException {
		if (StringUtils.isBlank(owner) || apiKeyService.getOneByOwner(owner) == null) {
			throw new IllegalArgumentException("Cant find owner");
		} else {
			String requestMethod = "GET";
			String urlEndPoint = "/position";

			HttpURLConnection connection = (HttpURLConnection) getCompleteUrl(urlEndPoint).openConnection();
			String httpRequestResult = getStringResultFromHttpRequest(
					setConnectionHeaders(apiKeyService.getOneByOwner(owner), requestMethod, urlEndPoint, connection));
			connection.disconnect();
			return positionMapper.mapToPositionDtoList(httpRequestResult);
		}
	}

	private URL getCompleteUrl(String urlEndPoint) throws MalformedURLException {
		if (StringUtils.isBlank(urlEndPoint)) {
			throw new IllegalArgumentException("urlEndPoint is blank");
		}
		return new URL(BitmexClientConfig.getBitmexApiUrl() + urlEndPoint);
	}

	private HttpURLConnection setConnectionHeaders(ApiKeyDto apiKeyDto, String requestMethod, String urlEndPoint,
			HttpURLConnection connection) throws ProtocolException {
		connection.setRequestMethod(requestMethod);
		connection.setRequestProperty("api-expires", getTimeToRequestExpiration());
		connection.setRequestProperty("api-key", apiKeyDto.getApiPublicKey());
		connection.setRequestProperty("api-signature", getRequestSignature(apiKeyDto.getApiSecretKey().getBytes(UTF_8),
				getTimeToRequestExpiration(), urlEndPoint, requestMethod));
		return connection;
	}

	private String getRequestSignature(byte[] encryptedApiSecret, String expirationTime, String urlEndPoint,
			String verb) {
		if (encryptedApiSecret.length == 0 || StringUtils.isBlank(expirationTime) || StringUtils.isBlank(urlEndPoint)
				|| StringUtils.isBlank(verb)) {
			throw new IllegalArgumentException();
		}
		try {
			String algo = "HMACSHA256";
			Mac mac = Mac.getInstance(algo);
			SecretKeySpec secretKey = new SecretKeySpec(cryptographicService.decryptPassword(encryptedApiSecret), algo);
			mac.init(secretKey);
			String path = "/api/v1" + urlEndPoint;
			String dataString = verb + path + expirationTime;
			return new String(Hex.encodeHex(mac.doFinal(dataString.getBytes(UTF_8))));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String getTimeToRequestExpiration() {
		Long time = (System.currentTimeMillis() + 10000L) / 1000L;
		return time.toString();
	}

	private String getStringResultFromHttpRequest(HttpURLConnection connection) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		return content.toString();
	}

	private String removeWithdrawalLockFieldFromResultString(String httpRequestResult) {
		String stringToDeleteFromJSON = ",\"withdrawalLock\":(..)";
		return httpRequestResult.replaceAll(stringToDeleteFromJSON, "");
	}

}
