package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.ApiKeyDto;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.dto.WalletDto;

public interface UserService {

	String calculateTimeToRequestExpiration();

	WalletDto getWallet(String owner) throws IOException;

	// return String signature = HEX(HMAC_SHA256(apiSecret, text))
	String calculateRequestSignature(String apiSecret, String expirationTime, String bitmexUrlEndPoint, String verb);

	URL getCompleteUrl(String bitmexUrlEndPoint) throws MalformedURLException;

	String getStringResultFromHttpRequest(HttpURLConnection connection) throws IOException;

	HttpURLConnection setConnectionHeaders(ApiKeyDto apiKeyDto, String requestMethod, String bitmexUrlEndPoint,
			HttpURLConnection connection) throws ProtocolException;

	List<PositionDto> getPositions(String owner) throws IOException;
}
