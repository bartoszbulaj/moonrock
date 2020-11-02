package pl.bartoszbulaj.moonrock.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MyConfiguration {

	private static final Logger LOG = LogManager.getLogger(MyConfiguration.class);
	private static boolean emailCredentialLoadingStatus = false;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public JavaMailSender javaMailSender() {

		String host = "smtp.gmail.com";
		String port = "587";
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host);
		javaMailSender.setPort(Integer.valueOf(port));
		javaMailSender.setUsername(getMailUsername());
		javaMailSender.setPassword(getMailPassword());

		javaMailSender.setJavaMailProperties(getMailProperties());

		return javaMailSender;

	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");

		return properties;
	}

	private String getMailUsername() {
		if (emailCredentialLoadingStatus) {
			try {
				File file = new ClassPathResource("/emailUsername.txt").getFile();
				return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
			} catch (IOException e) {
				LOG.error("Cant find emailUsername.txt");
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}

	private String getMailPassword() {
		if (emailCredentialLoadingStatus) {
			try {
				File file = new ClassPathResource("/emailPassword.txt").getFile();
				return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
			} catch (IOException e) {
				LOG.error("Cant find emailPassword.txt");
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}

}
