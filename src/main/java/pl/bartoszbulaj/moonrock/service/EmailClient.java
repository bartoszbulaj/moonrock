package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;

public interface EmailClient {

	void sendEmail(String mailText) throws IOException;

	String createEmailText(String instrumentSymbol, String signalDirection);
}
