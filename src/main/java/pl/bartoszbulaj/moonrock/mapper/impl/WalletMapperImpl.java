package pl.bartoszbulaj.moonrock.mapper.impl;

import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;
import pl.bartoszbulaj.moonrock.mapper.WalletMapper;

@Component
public class WalletMapperImpl implements WalletMapper {

	@Override
	public WalletDto walletToDto(WalletEntity wallet) {
		WalletDto walletDto = new WalletDto(wallet.getAmount(), wallet.getAddr());
		return walletDto;
	}

}
