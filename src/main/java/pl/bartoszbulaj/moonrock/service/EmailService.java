package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;

public interface EmailService {

	void sendEmail(String mailText) throws IOException;

	String createEmailText(String instrumentSymbol, String signalDirection);
}
