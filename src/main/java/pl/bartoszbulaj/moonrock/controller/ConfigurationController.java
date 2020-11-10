package pl.bartoszbulaj.moonrock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.service.ConfigurationService;
import pl.bartoszbulaj.moonrock.validator.EmailSenderValidator;

@RestController
@RequestMapping("/config")
public class ConfigurationController {

	private ConfigurationService emailSenderService;
	private EmailSenderValidator emailSenderValidator;

	@Autowired
	public ConfigurationController(ConfigurationService emailSenderService, EmailSenderValidator emailSenderValidator) {
		this.emailSenderService = emailSenderService;
		this.emailSenderValidator = emailSenderValidator;
	}

	@PostMapping("/email-sender")
	public ResponseEntity<EmailSenderDto> addEmailSender(@RequestBody EmailSenderDto emailSenderDto) {
		if (emailSenderValidator.isEmailSenderValid(emailSenderDto)) {
			EmailSenderDto emailSenderDtoToSave = emailSenderService.saveEmailSenderCredentials(emailSenderDto);
			return new ResponseEntity<>(emailSenderDtoToSave, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(emailSenderDto, HttpStatus.NOT_ACCEPTABLE);
	}

}
