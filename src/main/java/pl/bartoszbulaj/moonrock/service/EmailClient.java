package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;

public interface EmailClient {

	void sendEmail(String symbol, String signalDirection) throws IOException;
}
