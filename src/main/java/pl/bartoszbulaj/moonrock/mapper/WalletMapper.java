package pl.bartoszbulaj.moonrock.mapper;

import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;

public interface WalletMapper {

	WalletDto walletToDto(WalletEntity wallet);
}
