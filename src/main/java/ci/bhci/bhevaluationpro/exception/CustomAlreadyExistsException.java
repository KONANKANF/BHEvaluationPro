package ci.bhci.bhevaluationpro.exception;

@SuppressWarnings("serial")
public class CustomAlreadyExistsException extends RuntimeException {

	@SuppressWarnings("unused")
	private String message;

	public CustomAlreadyExistsException(String message) {
		super(message);
		this.message = message;
	}

	public CustomAlreadyExistsException() {
		super("Record already exists!");
	}
}
