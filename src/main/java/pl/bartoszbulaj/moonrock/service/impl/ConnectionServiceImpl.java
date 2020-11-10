package pl.bartoszbulaj.moonrock.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.service.ConnectionService;

@Component
public class ConnectionServiceImpl implements ConnectionService {

	@Override
	public String getHttpRequestResult(HttpURLConnection connection) {
		if (connection == null) {
			throw new IllegalArgumentException();
		}
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			return content.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
