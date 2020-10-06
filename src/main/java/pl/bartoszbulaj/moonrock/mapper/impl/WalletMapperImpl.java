package pl.bartoszbulaj.moonrock.mapper.impl;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;
import pl.bartoszbulaj.moonrock.mapper.WalletMapper;

@Component
public class WalletMapperImpl implements WalletMapper {

	private ObjectMapper objectMapper;
	private ModelMapper modelMapper;

	@Autowired
	public WalletMapperImpl(ObjectMapper objectMapper, ModelMapper modelMapper) {
		this.objectMapper = objectMapper;
		this.modelMapper = modelMapper;
	}

	@Override
	public WalletDto mapToWalletDto(WalletEntity walletEntity) {
		return modelMapper.map(walletEntity, WalletDto.class);
	}

	@Override
	public WalletDto mapToWalletDto(String walletResultString) throws IOException {
		return objectMapper.readValue(walletResultString, WalletDto.class);

	}

	@Override
	public WalletEntity mapToWalletEntity(WalletDto walletDto) {
		return modelMapper.map(walletDto, WalletEntity.class);
	}

	@Override
	public WalletEntity mapToWalletEntity(String walletString) throws IOException {
		return objectMapper.readValue(walletString, WalletEntity.class);
	}

}
