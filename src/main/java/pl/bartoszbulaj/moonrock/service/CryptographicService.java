package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface CryptographicService {

	String encryptPassword(byte[] password) throws GeneralSecurityException, IOException;

	byte[] decryptPassword(byte[] encryptedPassword) throws GeneralSecurityException, IOException;
}
