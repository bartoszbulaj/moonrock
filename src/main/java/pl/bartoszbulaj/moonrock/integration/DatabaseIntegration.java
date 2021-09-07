package pl.bartoszbulaj.moonrock.integration;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

import javax.mail.MessagingException;

public interface DatabaseIntegration {

	EmailSenderDto saveEmailSenderCredentials(EmailSenderDto emailSenderDto) throws MessagingException;

	boolean saveApiKeys(String owner, String apiPublicKey, byte[] apiSecretKey);
}
