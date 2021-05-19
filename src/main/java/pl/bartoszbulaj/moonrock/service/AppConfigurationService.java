package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

public interface AppConfigurationService {

	EmailSenderDto getEmailSenderCredentials();

	EmailSenderDto saveEmailSenderCredentials(EmailSenderDto emailSenderDto);

	boolean isAnyEmailSender();

	boolean setEmailSenderEnabled(boolean status);

	boolean isHistoryAnalyzerEnabled();

	boolean setHistoryAnalyzerEnabled(boolean status);
}
