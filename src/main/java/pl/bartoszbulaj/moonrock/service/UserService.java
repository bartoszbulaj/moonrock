package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;

import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;

public interface UserService {

	WalletDto getWallet(String owner) throws IOException;

	WalletEntity saveWallet(WalletDto walletDto);

}
