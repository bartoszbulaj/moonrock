package pl.bartoszbulaj.moonrock.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -6633585006375533566L;

	public BusinessException(String message) {
		super(message);
	}

}
