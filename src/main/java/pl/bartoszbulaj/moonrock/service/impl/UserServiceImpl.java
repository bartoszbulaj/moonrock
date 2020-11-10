package pl.bartoszbulaj.moonrock.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.dto.WalletDto;
import pl.bartoszbulaj.moonrock.entity.WalletEntity;
import pl.bartoszbulaj.moonrock.mapper.WalletMapper;
import pl.bartoszbulaj.moonrock.repository.WalletRepository;
import pl.bartoszbulaj.moonrock.service.AuthService;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final WalletRepository walletRepository;
	private WalletMapper walletMapper;
	private AuthService authService;
	private ConnectionService connectionService;

	@Autowired
	public UserServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper, AuthService authService,
			ConnectionService connectionService) {
		this.walletRepository = walletRepository;
		this.authService = authService;
		this.walletMapper = walletMapper;
		this.connectionService = connectionService;
	}

	@Override
	public WalletDto getWallet(String owner) throws IOException {
		if (StringUtils.isBlank(owner)) {
			throw new IllegalArgumentException("Wrong argument");
		} else {
			String requestMethod = "GET";
			String bitmexEndPoint = "/user/wallet";

			String connectionUrlString = authService.createConnectionUrlString(bitmexEndPoint, null);
			HttpURLConnection connection = (HttpURLConnection) new URL(connectionUrlString).openConnection();
			authService.addAuthRequestHeaders(owner, requestMethod, bitmexEndPoint, connection);
			String resultString = connectionService.getHttpRequestResult(connection);
			connection.disconnect();

			String walletResultString = removeWithdrawalLockFieldFromResultString(resultString);
			return walletMapper.mapToWalletDto(walletResultString);
		}
	}

	@Override
	public WalletEntity saveWallet(WalletDto walletDto) {
		return walletRepository.save(walletMapper.mapToWalletEntity(walletDto));
	}

	private String removeWithdrawalLockFieldFromResultString(String httpRequestResult) {
		String stringToDeleteFromJSON = ",\"withdrawalLock\":(..)";
		return httpRequestResult.replaceAll(stringToDeleteFromJSON, "");
	}

}
