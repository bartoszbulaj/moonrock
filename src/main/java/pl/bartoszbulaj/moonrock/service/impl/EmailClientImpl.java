package pl.bartoszbulaj.moonrock.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.service.EmailClient;

@Service
@Transactional
public class EmailClientImpl implements EmailClient {
	private static final Logger LOG = LogManager.getLogger(EmailClientImpl.class);
	private JavaMailSender javaMailSender;

	@Autowired
	public EmailClientImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmail(InstrumentHistoryDto instrumentHistoryDto, String signalDirection) throws IOException {
		String mailSubject = String.format("Signal on %s for %s", instrumentHistoryDto.getSymbol(), signalDirection);
		String mailText = "Open: " + instrumentHistoryDto.getOpen() + "\n"//
				+ "High: " + instrumentHistoryDto.getHigh() + "\n" //
				+ "Low: " + instrumentHistoryDto.getLow() + "\n" //
				+ "Close: " + instrumentHistoryDto.getClose();

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(getEmailAddressReceiver());
		simpleMailMessage.setSubject(mailSubject);
		simpleMailMessage.setText(mailText);
		javaMailSender.send(simpleMailMessage);
		LOG.info("[Mail sender] Mail sent to receiver.");
	}

	private String getEmailAddressReceiver() throws IOException {
		File file = new ClassPathResource("/emailAddressReceiver.txt").getFile();
		return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
	}

}