package pl.bartoszbulaj.moonrock.service;

import java.net.HttpURLConnection;

public interface ConnectionService {

	String getResultFromHttpRequest(HttpURLConnection connection);
}
