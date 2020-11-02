package pl.bartoszbulaj.moonrock.service.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.service.CryptographicService;

@Service
@Transactional
public class CryptographicServiceImpl implements CryptographicService {

	private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
	private static final int TAG_LENGTH_BIT = 128;
	private static final int IV_LENGTH_BYTE = 12;
	private static final int SALT_LENGTH_BYTE = 16;
	private static final int ITERATIONS = 65536;

	@Override
	public String encryptPassword(byte[] password) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, InvalidKeySpecException, IOException,
			IllegalBlockSizeException, BadPaddingException {

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
	public byte[] decryptPassword(byte[] encryptedPassword) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, InvalidKeySpecException, IOException,
			IllegalBlockSizeException, BadPaddingException {
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
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String secretKeyString = System.getenv("SUPER_SECRET_KEY");
		if (StringUtils.isBlank(secretKeyString)) {
			throw new IOException("Cant find ENV VARIABLE");
		} else {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secretKeyString.toCharArray(), salt, ITERATIONS, 256);
			return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		}
	}

}
