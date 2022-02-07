package ci.bhci.bhevaluationpro.exception;

import ci.bhci.bhevaluationpro.util.Response;

@SuppressWarnings("serial")
public class CustomDataNotFoundException extends RuntimeException {

	@SuppressWarnings("unused")
	private String message;
	@SuppressWarnings("unused")
	private Response response;

	public CustomDataNotFoundException() {
		super("Aucun donnée trouvée!");
	}

	public CustomDataNotFoundException(String message) {
		super(message);
		this.message = message;
		this.response = new Response();
	}

	public CustomDataNotFoundException(String message, Response response) {
		super(message);
		this.message = message;
		this.response = response;
	}

}
