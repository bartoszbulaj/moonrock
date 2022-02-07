package pl.bartoszbulaj.moonrock.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;

import java.util.Properties;

@Configuration
@Slf4j
public class AppConfiguration {

	@Autowired
	private AppConfigurationService appConfigurationService;

	private boolean historyAnalyzerEnabled;
	private boolean emailSenderEnabled;
	private boolean apiKeysSaved;
	private String historyAnalyzerInterval = CandleSize.CANDLE_SIZE_5M;
	private String envKey;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
		taskScheduler.setPoolSize(15);
		taskScheduler.initialize();
		return taskScheduler;
	}

	@Bean(name = "javaMailSender")
	public JavaMailSender javaMailSender() {
		String host = "smtp.gmail.com";
		String port = "587";
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host);
		javaMailSender.setPort(Integer.parseInt(port));
		try {
			javaMailSender.setUsername(getEmailAddressFromDb());
			javaMailSender.setPassword(getEmailPasswordFromDb());
		} catch (BusinessException e) {
			log.warn("!!! Cannot find any emailSender !!!");
		}

		javaMailSender.setJavaMailProperties(getMailProperties());
		return javaMailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");

		return properties;
	}

	private String getEmailAddressFromDb() {
		return appConfigurationService.getEmailSenderCredentials().getEmailAddress();
	}

	private String getEmailPasswordFromDb() {
		return appConfigurationService.getEmailSenderCredentials().getEmailPassword();
	}

	public boolean isHistoryAnalyzerEnabled() {
		return historyAnalyzerEnabled;
	}

	public void setHistoryAnalyzerEnabled(boolean historyAnalyzerEnabled) {
		this.historyAnalyzerEnabled = historyAnalyzerEnabled;
	}

	public boolean isEmailSenderEnabled() {
		return emailSenderEnabled;
	}

	public void setEmailSenderEnabled(boolean emailSenderEnabled) {
		this.emailSenderEnabled = emailSenderEnabled;
	}

	public void setHistoryAnalyzerInterval(String interval) {
		this.historyAnalyzerInterval = interval;
	}

	public String getHistoryAnalyzerInterval() {
		return this.historyAnalyzerInterval;
	}

	public boolean isApiKeysSaved() {
		return apiKeysSaved;
	}

	public void setApiKeysSaved(boolean apiKeysSaved) {
		this.apiKeysSaved = apiKeysSaved;
	}

	public String getEnvKey() {
		return envKey;
	}

	public void setEnvKey(String envKey) {
		this.envKey = envKey;
	}
}
