package pl.bartoszbulaj.moonrock.service.impl;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class CryptographicServiceImpl implements CryptographicService {

	private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
	private static final int TAG_LENGTH_BIT = 128;
	private static final int IV_LENGTH_BYTE = 12;
	private static final int SALT_LENGTH_BYTE = 16;
	private static final int ITERATIONS = 65536;
	private static final String MOONROCK_SECRET_KEY = "MOONROCK_SECRET_KEY";

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
		// TODO test api endpoint with getting wallet balance,
		// maybe env creation should be at app start?
		String moonrockSecretKey = createSuperSecretEnvironmentVariable();
		if (StringUtils.isBlank(moonrockSecretKey)) {
			throw new IOException("Cant find ENV VARIABLE");
		} else {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(moonrockSecretKey.toCharArray(), salt, ITERATIONS, 256);
			return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		}
	}

	private String createSuperSecretEnvironmentVariable() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		Map<String, String> env = processBuilder.environment();
		return env.put(MOONROCK_SECRET_KEY, UUID.randomUUID().toString());
	}

}
