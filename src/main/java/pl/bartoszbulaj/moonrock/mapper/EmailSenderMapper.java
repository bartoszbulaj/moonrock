package pl.bartoszbulaj.moonrock.mapper;

import pl.bartoszbulaj.moonrock.dto.EmailSenderDto;
import pl.bartoszbulaj.moonrock.entity.EmailSenderEntity;

public interface EmailSenderMapper {

	EmailSenderDto mapToEmailSenderDto(EmailSenderEntity emailSenderEntity);

	EmailSenderEntity mapToEmailSenderEntity(EmailSenderDto emailSenderDto);
}
