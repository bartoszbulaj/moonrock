package pl.bartoszbulaj.moonrock.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.entity.EmailSenderEntity;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.mapper.EmailSenderMapper;
import pl.bartoszbulaj.moonrock.repository.EmailSenderRepository;
import pl.bartoszbulaj.moonrock.service.EmailSenderService;

@Service
@Transactional
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {

	private EmailSenderRepository emailSenderRepository;
	private EmailSenderMapper emailSenderMapper;

	@Autowired
	public EmailSenderServiceImpl(EmailSenderRepository emailSenderRepository, EmailSenderMapper emailSenderMapper) {
		this.emailSenderRepository = emailSenderRepository;
		this.emailSenderMapper = emailSenderMapper;
	}

	@Override
	public EmailSenderDto getEmailSender() {
		List<EmailSenderEntity> emailSenderEntityList = emailSenderRepository.findAll();
		if (emailSenderEntityList.isEmpty()) {
			throw new BusinessException("Cannot find any EmailSender");
		}
		return emailSenderMapper.mapToEmailSenderDto(emailSenderEntityList.get(0));
	}

	@Override
	public EmailSenderEntity saveEmailSender(EmailSenderDto emailSenderDto) {
		return emailSenderMapper.mapToEmailSenderEntity(emailSenderDto);
	}

	@Override
	public boolean isEmailSender() {
		List<EmailSenderEntity> emailSenderEntityList = emailSenderRepository.findAll();
		return !emailSenderEntityList.isEmpty();
	}

}
