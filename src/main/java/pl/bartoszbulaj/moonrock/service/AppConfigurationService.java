package pl.bartoszbulaj.moonrock.service;

import javax.mail.MessagingException;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

public interface AppConfigurationService {

	EmailSenderDto getEmailSenderCredentials();

	EmailSenderDto saveEmailSenderCredentials(EmailSenderDto emailSenderDto) throws MessagingException;

	boolean isAnyEmailSender();

	void setEmailSenderEnabled(boolean status);

	boolean isHistoryAnalyzerEnabled();

	void setHistoryAnalyzerEnabled(boolean status);
}
