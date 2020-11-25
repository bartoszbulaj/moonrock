package pl.bartoszbulaj.moonrock.service;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Map;

public interface AuthService {

	boolean authenticateUser();

	String createConnectionUrlStringWithFilters(String bitmexEndPoint, Map<String, String> filters)
			throws MalformedURLException;

	HttpURLConnection addAuthRequestHeaders(String owner, String requestMethod, String bitmexEndPoint,
			HttpURLConnection connection) throws ProtocolException;

	String removeUrlPrefix(String bitmexEndpoint);

	String createUrlWithParams(String bitmexEndPoint, Map<String, String> paramsMap);

}
