package pl.bartoszbulaj.moonrock.service.impl;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.service.CryptographicService;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
@Transactional
public class CryptographicServiceImpl implements CryptographicService {

	private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
	private static final int TAG_LENGTH_BIT = 128;
	private static final int IV_LENGTH_BYTE = 12;
	private static final int SALT_LENGTH_BYTE = 16;
	private static final int ITERATIONS = 65536;


	private final AppConfigurationService appConfigurationService;

	@Autowired
	public CryptographicServiceImpl(AppConfigurationService appConfigurationService) {
		this.appConfigurationService = appConfigurationService;
	}

	@Override
	public String encryptPassword(byte[] password) throws GeneralSecurityException, IOException {

		byte[] salt = getRandomBytesArray(SALT_LENGTH_BYTE);
		byte[] iv = getRandomBytesArray(IV_LENGTH_BYTE);

		Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
		cipher.init(Cipher.ENCRYPT_MODE, getAESSuperSecretKey(salt), new GCMParameterSpec(TAG_LENGTH_BIT, iv));

		byte[] passwordBytes = cipher.doFinal(password);
		byte[] passwordBytesWithIvAndSalt = ByteBuffer.allocate(iv.length + salt.length + passwordBytes.length).put(iv)
				.put(salt).put(passwordBytes).array();

		return Base64.getEncoder().encodeToString(passwordBytesWithIvAndSalt);
	}

	@Override
	public byte[] decryptPassword(byte[] encryptedPassword) throws GeneralSecurityException, IOException {
		ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(encryptedPassword));

		byte[] iv = new byte[IV_LENGTH_BYTE];
		byteBuffer.get(iv);
		byte[] salt = new byte[SALT_LENGTH_BYTE];
		byteBuffer.get(salt);
		byte[] passwordBytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(passwordBytes);

		Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
		cipher.init(Cipher.DECRYPT_MODE, getAESSuperSecretKey(salt), new GCMParameterSpec(TAG_LENGTH_BIT, iv));

		return cipher.doFinal(passwordBytes);
	}

	private byte[] getRandomBytesArray(int numberOfBytes) {
		byte[] bytes = new byte[numberOfBytes];
		new SecureRandom().nextBytes(bytes);
		return bytes;
	}

	private SecretKey getAESSuperSecretKey(byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		String moonrockEnvKey = appConfigurationService.getEnvKey();
		if (StringUtils.isBlank(moonrockEnvKey)) {
			throw new IOException("Can not find ENV_KEY");
		} else {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(moonrockEnvKey.toCharArray(), salt, ITERATIONS, 256);
			return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		}
	}

}
