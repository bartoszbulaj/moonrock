package pl.bartoszbulaj.moonrock.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.EmailService;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

	private static final Logger LOG = LogManager.getLogger(EmailServiceImpl.class);
	private final JavaMailSender javaMailSender;

	public EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmail(String mailText) {
		String mailSubject = "New Signal. " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd.MM"));

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(getEmailAddressReceiver());
		simpleMailMessage.setSubject(mailSubject);
		simpleMailMessage.setText(mailText);
		javaMailSender.send(simpleMailMessage);
		LOG.info("[Mail sender] Mail sent to receiver.");
	}

	@Override
	public String createEmailText(String instrumentSymbol, String signalDirection) {
		return String.format("Signal on %s for %s. %n", instrumentSymbol, signalDirection);

	}

	private String getEmailAddressReceiver() {
		try {
			File file = new ClassPathResource("/emailAddressReceiver.txt").getFile();
			return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOG.error("Cant find emailAddressReceiver.txt");
			e.printStackTrace();
			return "";
		}
	}

}