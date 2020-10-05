package pl.bartoszbulaj.moonrock.service;

public interface CryptographicService {

	String encryptPassword(byte[] password) throws Exception;

	String decryptPassword(byte[] encryptedPassword) throws Exception;
}
