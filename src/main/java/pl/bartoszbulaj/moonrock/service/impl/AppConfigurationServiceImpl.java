package pl.bartoszbulaj.moonrock.service.impl;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.config.AppConfiguration;
import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.entity.EmailSenderEntity;
import pl.bartoszbulaj.moonrock.exception.BusinessException;
import pl.bartoszbulaj.moonrock.mapper.EmailSenderMapper;
import pl.bartoszbulaj.moonrock.repository.EmailSenderRepository;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;

@Service
@Transactional
@Slf4j
public class AppConfigurationServiceImpl implements AppConfigurationService {

	private EmailSenderRepository emailSenderRepository;
	private EmailSenderMapper emailSenderMapper;
	private ApplicationContext context;

	@Autowired
	public AppConfigurationServiceImpl(EmailSenderRepository emailSenderRepository, EmailSenderMapper emailSenderMapper,
			ApplicationContext context) {
		this.emailSenderRepository = emailSenderRepository;
		this.emailSenderMapper = emailSenderMapper;
		this.context = context;
	}

	@Override
	public EmailSenderDto getEmailSenderCredentials() {
		List<EmailSenderEntity> emailSenderEntityList = emailSenderRepository.findAll();
		if (emailSenderEntityList.isEmpty()) {
			throw new BusinessException("Cannot find any emailSender");
		}
		return emailSenderMapper.mapToEmailSenderDto(emailSenderEntityList.get(0));
	}

	@Override
	public EmailSenderDto saveEmailSenderCredentials(EmailSenderDto emailSenderDto) throws MessagingException {
		if (emailSenderDto == null) {
			throw new IllegalArgumentException("emailSenderDto is null");
		}
		EmailSenderEntity emailSenderEntity = emailSenderRepository
				.save(emailSenderMapper.mapToEmailSenderEntity(emailSenderDto));

		updateJavaMailSender(emailSenderDto);

		return emailSenderMapper.mapToEmailSenderDto(emailSenderEntity);
	}

	@Override
	public boolean isAnyEmailSender() {
		List<EmailSenderEntity> emailSenderEntityList = emailSenderRepository.findAll();
		return !emailSenderEntityList.isEmpty();
	}

	private void updateJavaMailSender(EmailSenderDto emailSenderDto) throws MessagingException {
		if (emailSenderDto == null) {
			throw new IllegalArgumentException("emailSenderDto is null");
		}
		JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) context.getBean("javaMailSender");
		javaMailSender.setUsername(emailSenderDto.getEmailAddress());
		javaMailSender.setPassword(emailSenderDto.getEmailPassword());
		javaMailSender.testConnection();
		setEmailSenderEnabled(true);
	}
	@Override
	public void setHistoryAnalyzerEnabled(boolean status) {
		AppConfiguration appConfiguration = getAppConfigurationBean();
		appConfiguration.setHistoryAnalyzerEnabled(status);
	}

	@Override
	public void setEmailSenderEnabled(boolean status) {
		AppConfiguration appConfiguration = getAppConfigurationBean();
		appConfiguration.setEmailSenderEnabled(status);
	}

	@Override
	public boolean isHistoryAnalyzerEnabled() {
		AppConfiguration appConfiguration = getAppConfigurationBean();
		return appConfiguration.isHistoryAnalyzerEnabled();
	}

	private AppConfiguration getAppConfigurationBean() {
		return context.getBean(AppConfiguration.class);
	}
}
