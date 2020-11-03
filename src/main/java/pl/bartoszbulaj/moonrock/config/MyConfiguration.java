package pl.bartoszbulaj.moonrock.config;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.bartoszbulaj.moonrock.service.EmailSenderService;

@Configuration
public class MyConfiguration {

	private static final Logger LOG = LogManager.getLogger(MyConfiguration.class);
	// private static boolean emailCredentialLoadingStatus = false;

	@Autowired
	private EmailSenderService emailSenderService;

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
		if (emailSenderService.isEmailSender()) {
			javaMailSender.setUsername(getEmailAddressFromDb());
			javaMailSender.setPassword(getEmailPasswordFromDb());
		}
		javaMailSender.setJavaMailProperties(getMailProperties());
		// TODO create controller where is checking for any EmailSender.
		// when POST new EmailSender then update JavaMailSender
		return javaMailSender;

	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");

		return properties;
	}

	private String getEmailAddressFromDb() {
		return emailSenderService.getEmailSender().getEmailAddress();
	}

	private String getEmailPasswordFromDb() {
		return emailSenderService.getEmailSender().getEmailPassword();
	}

//	private String getMailUsername() {
//		if (emailCredentialLoadingStatus) {
//			try {
//				File file = new ClassPathResource("/emailUsername.txt").getFile();
//				return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
//			} catch (IOException e) {
//				LOG.error("Cant find emailUsername.txt");
//				e.printStackTrace();
//				return "";
//			}
//		}
//		return "";
//	}
//
//	private String getMailPassword() {
//		if (emailCredentialLoadingStatus) {
//			try {
//				File file = new ClassPathResource("/emailPassword.txt").getFile();
//				return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
//			} catch (IOException e) {
//				LOG.error("Cant find emailPassword.txt");
//				e.printStackTrace();
//				return "";
//			}
//		}
//		return "";
//	}

}
