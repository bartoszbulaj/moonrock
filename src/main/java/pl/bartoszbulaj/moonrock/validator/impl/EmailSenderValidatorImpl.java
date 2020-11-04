package pl.bartoszbulaj.moonrock.validator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.validator.EmailSenderValidator;

@Component
public class EmailSenderValidatorImpl implements EmailSenderValidator {

	@Override
	public boolean isEmailSenderValid(EmailSenderDto emailSenderDto) {
		if (emailSenderDto != null) {
			return !StringUtils.isBlank(emailSenderDto.getEmailAddress())
					&& isEmailAddressValid(emailSenderDto.getEmailAddress())
					&& !StringUtils.isBlank(emailSenderDto.getEmailPassword())
					&& !StringUtils.isBlank(emailSenderDto.getOwner());
		}
		return false;
	}

	private boolean isEmailAddressValid(String emailAddress) {
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
		Matcher matcher = pattern.matcher(emailAddress);
		return matcher.matches();
	}

}
