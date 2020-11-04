package pl.bartoszbulaj.moonrock.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailSenderDto {

	public EmailSenderDto(String owner, String address, String password) {
		this.owner = owner;
		this.emailAddress = address;
		this.emailPassword = password;
	}

	private Long id;
	private String owner;
	private String emailAddress;
	private String emailPassword;

}
