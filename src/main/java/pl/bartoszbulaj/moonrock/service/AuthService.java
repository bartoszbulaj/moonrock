package pl.bartoszbulaj.moonrock.service;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public interface AuthService {

	boolean authenticateUser();

	URL createConnectionUrl(String urlEndPoint) throws MalformedURLException;

	HttpURLConnection addAuthRequestHeaders(String owner, String requestMethod, String urlEndPoint,
			HttpURLConnection connection) throws ProtocolException;

}
