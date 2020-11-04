package pl.bartoszbulaj.moonrock.validator;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;

public interface EmailSenderValidator {

	boolean isEmailSenderValid(EmailSenderDto emailSenderDto);
}
