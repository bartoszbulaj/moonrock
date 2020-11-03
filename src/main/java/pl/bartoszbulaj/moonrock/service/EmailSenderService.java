package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.entity.EmailSenderEntity;

public interface EmailSenderService {

	EmailSenderDto getEmailSender();

	EmailSenderEntity saveEmailSender(EmailSenderDto emailSenderDto);

	boolean isEmailSender();
}
