package pl.bartoszbulaj.moonrock.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.entity.EmailSenderEntity;
import pl.bartoszbulaj.moonrock.mapper.EmailSenderMapper;

@Component
public class EmailSenderMapperImpl implements EmailSenderMapper {

	private ModelMapper modelMapper;

	@Autowired
	public EmailSenderMapperImpl(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public EmailSenderDto mapToEmailSenderDto(EmailSenderEntity emailSenderEntity) {
		return modelMapper.map(emailSenderEntity, EmailSenderDto.class);
	}

	@Override
	public EmailSenderEntity mapToEmailSenderEntity(EmailSenderDto emailSenderDto) {
		return modelMapper.map(emailSenderDto, EmailSenderEntity.class);
	}

}
