package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

import javax.mail.MessagingException;

public interface AppConfigurationService {

	EmailSenderDto getEmailSenderCredentials();

	EmailSenderDto saveEmailSenderCredentials(EmailSenderDto emailSenderDto) throws MessagingException;

	boolean isAnyEmailSender();

	void setEmailSenderEnabled(boolean status);

	boolean isHistoryAnalyzerEnabled();

	void setHistoryAnalyzerEnabled(boolean status);

	void setHistoryAnalyzerInterval(String interval);

	String getHistoryAnalyzerInterval();

}
