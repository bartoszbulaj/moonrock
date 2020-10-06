package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;

public interface UserService {

	WalletDto getWallet(String owner) throws IOException;

	WalletEntity saveWallet(WalletDto walletDto);

	List<PositionDto> getPositions(String owner) throws IOException;

}
