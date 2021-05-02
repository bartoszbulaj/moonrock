package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

public interface AppConfigurationService {

	EmailSenderDto getEmailSenderCredentials();

	EmailSenderDto saveEmailSenderCredentials(EmailSenderDto emailSenderDto);

	boolean isAnyEmailSender();

	boolean setHistoryAnalyzerEnabled(boolean status);

	boolean setEmailSenderEnabled(boolean status);
}
