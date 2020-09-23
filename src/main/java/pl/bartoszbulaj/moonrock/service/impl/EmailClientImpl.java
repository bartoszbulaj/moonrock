package pl.bartoszbulaj.moonrock.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.EmailClient;

@Service
@Transactional
public class EmailClientImpl implements EmailClient {
	private static final Logger LOG = LogManager.getLogger(EmailClientImpl.class);
	private JavaMailSender javaMailSender;

	@Value("${cryptoSignal.message}")
	private String signalString;

	@Autowired
	public EmailClientImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmail(String symbol, String signalDirection) throws IOException {
		String mailSubject = String.format("Signal for %s for %s", symbol, signalDirection);
		String mailText = "test test";

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(getEmailAddressReceiver());
		simpleMailMessage.setSubject(mailSubject);
		simpleMailMessage.setText(mailText);
		javaMailSender.send(simpleMailMessage);
		LOG.info("[Mail sender] Mail sent to receiver: {}", signalString);
	}

	private String getEmailAddressReceiver() throws IOException {
		File file = new ClassPathResource("/emailAddressReceiver.txt").getFile();
		return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
	}

}