package pl.bartoszbulaj.moonrock.config;

import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;

@Configuration
@Slf4j
public class AppConfiguration {

	@Autowired
	private AppConfigurationService emailSenderService;

	private boolean historyAnalyzerEnabled;
	private boolean emailSenderEnabled;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean(name = "javaMailSender")
	public JavaMailSender javaMailSender() {
		String host = "smtp.gmail.com";
		String port = "587";
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host);
		javaMailSender.setPort(Integer.valueOf(port));
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
		return emailSenderService.getEmailSenderCredentials().getEmailAddress();
	}

	private String getEmailPasswordFromDb() {
		return emailSenderService.getEmailSenderCredentials().getEmailPassword();
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
}
