package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

public interface EmailSenderService {

	EmailSenderDto getEmailSender();

	EmailSenderDto saveEmailSender(EmailSenderDto emailSenderDto);

	boolean isAnyEmailSender();
}
