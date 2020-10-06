package pl.bartoszbulaj.moonrock.mapper;

import java.io.IOException;

import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;

public interface WalletMapper {

	WalletDto mapToWalletDto(String walletString) throws IOException;

	WalletDto mapToWalletDto(WalletEntity walletEntity);

	WalletEntity mapToWalletEntity(WalletDto walletDto);

	WalletEntity mapToWalletEntity(String walletString) throws IOException;
}
