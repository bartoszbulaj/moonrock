package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

import javax.mail.MessagingException;
import java.util.List;

public interface AppConfigurationService {

	EmailSenderDto getEmailSenderCredentials();

	EmailSenderDto saveEmailSenderCredentials(EmailSenderDto emailSenderDto) throws MessagingException;

	boolean isAnyEmailSender();

	void setEmailSenderEnabled(boolean status);

	boolean isHistoryAnalyzerEnabled();

	void setHistoryAnalyzerEnabled(boolean status);

	void setHistoryAnalyzerInterval(String interval);

	String getHistoryAnalyzerInterval();

	boolean isApiKeysSaved();

	void setApiKeysSaved(boolean value);

	String getEnvKey();

	void initEnvKey();

    String getCryptoPairForSimulator();

	void setCryptoPairForSimulator(String value);

	List<String> getInstrumentsList();

}
