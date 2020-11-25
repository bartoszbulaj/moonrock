package pl.bartoszbulaj.moonrock.service.impl;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.service.ApiKeyService;
import pl.bartoszbulaj.moonrock.service.AuthService;
import pl.bartoszbulaj.moonrock.service.CryptographicService;

@Service
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	private CryptographicService cryptographicService;
	private ApiKeyService apiKeyService;

	@Autowired
	public AuthServiceImpl(CryptographicService cryptographicService, ApiKeyService apiKeyService) {
		this.cryptographicService = cryptographicService;
		this.apiKeyService = apiKeyService;
	}

	@Override
	public boolean authenticateUser() {
		return false;
	}

	@Override
	public String removeUrlPrefix(String bitmexEndPoint) {
		if (StringUtils.isBlank(bitmexEndPoint)) {
			throw new IllegalArgumentException();
		}
		return bitmexEndPoint.substring(29);
	}

	@Override
	public String createUrlWithParams(String bitmexEndPoint, Map<String, String> paramsMap) {
		if (StringUtils.isBlank(bitmexEndPoint)) {
			throw new IllegalArgumentException();
		}
		if (paramsMap != null && paramsMap.size() > 0) {
			StringBuilder urlString = new StringBuilder();
			urlString.append(BitmexClientConfig.getBitmexApiUrl());
			urlString.append(bitmexEndPoint);
			urlString.append("?");

			paramsMap.entrySet().stream().forEach(param -> {
				urlString.append(param.getKey());
				urlString.append("=");
				urlString.append(param.getValue());
				urlString.append("&");
			});
			urlString.deleteCharAt(urlString.length() - 1);

			return urlString.toString();
		} else {
			return BitmexClientConfig.getBitmexApiUrl() + bitmexEndPoint;
		}
	}

	@Override
	public String createConnectionUrlStringWithFilters(String bitmexEndPoint, Map<String, String> filters)
			throws MalformedURLException {
		if (StringUtils.isBlank(bitmexEndPoint)) {
			throw new IllegalArgumentException();
		}
		if (filters != null && filters.size() > 0) {
			StringBuilder urlString = new StringBuilder();
			urlString.append(BitmexClientConfig.getBitmexApiUrl());
			urlString.append(bitmexEndPoint);
			urlString.append("?filter={");

			filters.entrySet().stream().forEach(filter -> {
				urlString.append("\"");
				urlString.append(filter.getKey());
				urlString.append("\"");
				urlString.append(":");
				if (!Boolean.parseBoolean(filter.getValue())) {
					urlString.append("\"");
				}
				urlString.append(filter.getValue());
				if (!Boolean.parseBoolean(filter.getValue())) {
					urlString.append("\"");
				}
			});

			urlString.append("}");

			return urlString.toString();
		}
		if (filters == null) {
			return BitmexClientConfig.getBitmexApiUrl() + bitmexEndPoint;
		}
		return "";
	}

	@Override
	public HttpURLConnection addAuthRequestHeaders(String owner, String requestMethod, String bitmexEndPoint,
			HttpURLConnection connection) throws ProtocolException {
		ApiKeyDto apiKeyDto = apiKeyService.getOneByOwner(owner);
		connection.setRequestMethod(requestMethod);
		connection.setRequestProperty("api-expires", getTimeToRequestExpiration());
		connection.setRequestProperty("api-key", apiKeyDto.getApiPublicKey());
		connection.setRequestProperty("api-signature", getRequestSignature(apiKeyDto.getApiSecretKey().getBytes(UTF_8),
				getTimeToRequestExpiration(), bitmexEndPoint, requestMethod));
		return connection;
	}

	private String getTimeToRequestExpiration() {
		Long time = (System.currentTimeMillis() + 10000L) / 1000L;
		return time.toString();
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

}
